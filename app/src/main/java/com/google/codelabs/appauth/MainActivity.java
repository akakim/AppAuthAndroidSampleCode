// Copyright 2016 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codelabs.appauth;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.codelabs.appauth.MainApplication.LOG_TAG;

public class MainActivity extends AppCompatActivity {

  private static final String SHARED_PREFERENCES_NAME = "AuthStatePreference";
  private static final String AUTH_STATE = "AUTH_STATE";
  private static final String USED_INTENT = "USED_INTENT";

  MainApplication mMainApplication;

  // state
  AuthState mAuthState;

  // views
  AppCompatButton mAuthorize;
  AppCompatButton mMakeApiCall;
  AppCompatButton mSignOut;
  AppCompatTextView mGivenName;
  AppCompatTextView mFamilyName;
  AppCompatTextView mFullName;
  ImageView mProfileView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mMainApplication = (MainApplication) getApplication();
    mAuthorize = (AppCompatButton) findViewById(R.id.authorize);
    mMakeApiCall = (AppCompatButton) findViewById(R.id.makeApiCall);
    mSignOut = (AppCompatButton) findViewById(R.id.signOut);
    mGivenName = (AppCompatTextView) findViewById(R.id.givenName);
    mFamilyName = (AppCompatTextView) findViewById(R.id.familyName);
    mFullName = (AppCompatTextView) findViewById(R.id.fullName);
    mProfileView = (ImageView) findViewById(R.id.profileImage);

    enablePostAuthorizationFlows();

    // wire click listeners
    mAuthorize.setOnClickListener(new AuthorizeListener());
  }

  private void enablePostAuthorizationFlows() {
    mAuthState = restoreAuthState();

    if (mAuthState != null && mAuthState.isAuthorized()) {
      if (mMakeApiCall.getVisibility() == View.GONE) {
        mMakeApiCall.setVisibility(View.VISIBLE);
        mMakeApiCall.setOnClickListener(new MakeApiCallListener(this, mAuthState, new AuthorizationService(this)));
      }
      if (mSignOut.getVisibility() == View.GONE) {
        mSignOut.setVisibility(View.VISIBLE);
        mSignOut.setOnClickListener(new SignOutListener(this));
      }
    } else {
      mMakeApiCall.setVisibility(View.GONE);
      mSignOut.setVisibility(View.GONE);
    }
  }

  /**
   * Exchanges the code, for the {@link TokenResponse}.
   * 코드를 교환한다.
   *
   * @param intent 커스텀 탭 혹은 시스템 브라우저로 부터 Intent를 표현한다.
   *
   * intent represents the {@link Intent} from the Custom Tabs or the System Browser.
   */
  private void handleAuthorizationResponse(@NonNull Intent intent) {

    // code from the step 'Handle the Authorization Response' goes here.
    AuthorizationResponse response = AuthorizationResponse.fromIntent( intent );
    AuthorizationException error = AuthorizationException.fromIntent( intent );
    // 사용자 정보가 들어있는 객체
    // 편하게 Oauth Response을 업데이트 할 수 있다.
    final AuthState authState = new AuthState(response,error);


    // 토큰에 접근 그리고 갱신을 위한  인증 코드를 교환 할 것이다.
    // 그리고 Auth State 인스턴스를 업데이트 할 것이다.

    if ( response != null ){
        Log.i(LOG_TAG, String.format("Handled Authorization Response %s ", authState.toJsonString()));
        AuthorizationService service = new AuthorizationService( this );
        service.performTokenRequest(
                response.createTokenExchangeRequest(),
                new AuthorizationService.TokenResponseCallback(){

                  @Override
                  public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException e) {

                    if( e != null  ){
                       Log.w(LOG_TAG, "token Exchange Failed ", e );
                    } else {
                       if( tokenResponse != null ){
                         authState.update( tokenResponse, e );
                         persistAuthState( authState ); // 권한에 대해서 뭔가를 설정하는 거지
                         Log.i(LOG_TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken, tokenResponse.idToken));
                       }
                    }
                  }
                }


        );
    }
  }

  private void persistAuthState(@NonNull AuthState authState) {
    getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        .putString(AUTH_STATE, authState.toJsonString())
        .commit();
    enablePostAuthorizationFlows();
  }

  private void clearAuthState() {
    getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit()
        .remove(AUTH_STATE)
        .apply();
  }

  @Nullable
  private AuthState restoreAuthState() {
    String jsonString = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        .getString(AUTH_STATE, null);

    if (!TextUtils.isEmpty(jsonString)) {
      try {
        return AuthState.fromJson(jsonString);
      } catch (JSONException jsonException) {
        // should never happen
      }
    }
    return null;
  }

  @Override
  protected void onStart() {
    super.onStart();

    MagicLog("onStart()");
    checkIntent( getIntent() );
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

      MagicLog("onNewIntent()");
    checkIntent( intent );
  }

  private void checkIntent(@Nullable Intent intent ){

    if( intent != null ){
      String action = intent.getAction();
      MagicLog( "check Intent : " + action );
      switch (action){
        case "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE":
          if( !intent.hasExtra(USED_INTENT)){
            handleAuthorizationResponse( intent );
            intent.putExtra(USED_INTENT,true);


          }
          break;
        default:
          // doNoting
      }
    }else {
        MagicLog( "check intent() intent is null ");
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * Kicks off the authorization flow.
   */
  public static class AuthorizeListener implements Button.OnClickListener {
    @Override
    public void onClick(View view) {

      // code from the step 'Create the Authorization Request',
      // and the step 'Perform the Authorization Request' goes here.

      // 권한 서비스 설정 객체 생성
      AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(
              Uri.parse( "https://accounts.google.com/o/oauth2/v2/auth")/* auth endpoint */,
              Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
      );


      // 클라이언트 아이디
      String clientId = "511828570984-fuprh0cm7665emlne3rnf9pk34kkn86s.apps.googleusercontent.com";
      Uri redirectUri = Uri.parse("com.google.codelabs.appauth:/oauth2callback");

      AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
              config,
              clientId,
              AuthorizationRequest.RESPONSE_TYPE_CODE,
              redirectUri
      );

      //요청하는 범위를 지정한다.
      builder.setScopes("profile");
      AuthorizationRequest request = builder.build();

      // 권한을 요청하는 기능을 구현한다.

      AuthorizationService authorizationService = new AuthorizationService( view.getContext());

      String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
      Intent postAuthorizationIntent = new Intent(action);

      PendingIntent pendingIntent = PendingIntent.getActivity( view.getContext(), request.hashCode(), postAuthorizationIntent ,0 );
      authorizationService.performAuthorizationRequest( request,pendingIntent );

      // AuthorizationService 는 OauthCode 흐름을 초기화 하는 역할을 가지고 있다.
      // 흐름은 다음고 ㅏ같다.

      /**
       *  +~~~~~~~~~~~~~~~~~~~~~~~~~~~+
       *  |        User Devices       |
       *  |                           |
       *  | +-----------------------+ |  (5) Auth Code            +-----------------------+
       *  | |                       | --------------------------->| Token                 |
       *  | |       Client App      | <-------------------------- | Endpoint              |
       *  | |                       | |   (6) Access Token,       | ( ServiceProvider )   |
       *  | +-----------------------+ |       Refresh Token       +-----------------------+
       *  |    |             ^        |
       *  |    |             |        |
       *  |    | (1)         | (4)    |
       *  |    | Auth        | Authz  |
       *  |    | Request     | Code   |
       *  |    v             |        |
       *  | +-----------------------+ |  (2) Authz Request        +-----------------------+
       *  | |                       | --------------------------->| Authorization         |
       *  | |  In-app Browser Tab   | <-------------------------- | EndPoint              |
       *  | |                       | |   (6) Authz Code          |                       |
       *  | +-----------------------+ |                           +-----------------------+
       */
    }
  }

  public static class SignOutListener implements Button.OnClickListener {

    private final MainActivity mMainActivity;

    public SignOutListener(@NonNull MainActivity mainActivity) {
      mMainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
      mMainActivity.mAuthState = null;
      mMainActivity.clearAuthState();
      mMainActivity.enablePostAuthorizationFlows();
    }
  }

  public static class MakeApiCallListener implements Button.OnClickListener {

    private final MainActivity mMainActivity;
    private AuthState mAuthState;
    private AuthorizationService mAuthorizationService;

    public MakeApiCallListener(@NonNull MainActivity mainActivity, @NonNull AuthState authState, @NonNull AuthorizationService authorizationService) {
      mMainActivity = mainActivity;
      mAuthState = authState;
      mAuthorizationService = authorizationService;
    }

    @Override
    public void onClick(View view) {

      // code from the section 'Making API Calls' goes here

      // 당신이 토큰을 response 에서 받아왔을때 그 토큰은 만료가 된 상태이다 반드시 갱신을 해야한다.
//      mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction() {
//        @Override
//        public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException e) {
//                if ( e != null ){
//                  // negotiation for fresh tokens failed, check error for more details....
//                  // 여기는 토큰을 갱신하더라도 협상의 여지를 남겨놓는다.
//                  // 에러에 대해서 좀 더 세부적으로 체크한다.
//                  return ;
//                }
//
//                // use the access token to do something
//                // Log.i(LOG_TAG, String.format("TODO: make an API call with [Access Token: %s, ID Token: %s]", accessToken, idToken));
//
//                OkHttpClient client = new OkHttpClient();
////                Request requets = new Request.Builder()
////                        .url("https://www.googleapis.com/oauth2/v3/userinfo")
////                        .addHeader("Authorization", String.format("Bearer %s", tokens[0]))
//
//        }
//      });

        mAuthState.performActionWithFreshTokens( mAuthorizationService,new AuthState.AuthStateAction() {

            @Override
            public void execute(@Nullable String s, @Nullable String s1, @Nullable AuthorizationException e) {
                if ( e != null ){
                  // negotiation for fresh tokens failed, check error for more details....

                  return ;
                }
                // use the access token to do something
                // Log.i(LOG_TAG, String.format("TODO: make an API call with [Access Token: %s, ID Token: %s]", accessToken, idToken));

                new AsyncTask<String,Void,JSONObject>(){

                    @Override
                    protected JSONObject doInBackground(String... strings) {

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("https://www.googleapis.com/oauth2/v3/userinfo")
                                .addHeader("Authorization", String.format("Bearer %s", strings[0]))
                                .build();

                        try {
                            Response response = client.newCall(request).execute();
                            String jsonBody = response.body().string();
                            Log.i(LOG_TAG, String.format("User Info Response %s", jsonBody));
                            return new JSONObject(jsonBody);
                        } catch (Exception exception) {
                            Log.w(LOG_TAG, exception);
                        }
                        return null;
                    }


                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        super.onPostExecute(jsonObject);

                        if( jsonObject != null){
                            String fullName = jsonObject.optString("name", null);
                            String givenName = jsonObject.optString("given_name", null);
                            String familyName = jsonObject.optString("family_name", null);
                            String imageUrl = jsonObject.optString("picture", null);

                            if (!TextUtils.isEmpty(imageUrl)) {
                                Picasso.with(mMainActivity)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.ic_account_circle_black_48dp)
                                        .into(mMainActivity.mProfileView);
                            }
                            if (!TextUtils.isEmpty(fullName)) {
                                mMainActivity.mFullName.setText(fullName);
                            }
                            if (!TextUtils.isEmpty(givenName)) {
                                mMainActivity.mGivenName.setText(givenName);
                            }
                            if (!TextUtils.isEmpty(familyName)) {
                                mMainActivity.mFamilyName.setText(familyName);
                            }

                            String message;
                            if (jsonObject.has("error")) {
                                message = String.format("%s [%s]", mMainActivity.getString(R.string.request_failed), jsonObject.optString("error_description", "No description"));
                            } else {
                                message = mMainActivity.getString(R.string.request_complete);
                            }
                            Snackbar.make(mMainActivity.mProfileView, message, Snackbar.LENGTH_SHORT)
                                    .show();

                        }

                    }
                }.execute(s);
            }
        });
    }


  }

    public void MagicLog(String messgage){
        Log.d(getClass().getSimpleName(),messgage);
    }

}

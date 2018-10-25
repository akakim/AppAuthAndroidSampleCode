package com.google.codelabs.appauth;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.VisibleForTesting;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationException.AuthorizationRequestErrors;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationResponse.Builder;

import net.openid.appauth.AuthorizationRequest;


public class RedirectCustomReceiverActivity extends Activity {
    private static final String KEY_STATE = "state";
    private CustomClock mClock;

    public RedirectCustomReceiverActivity() {
        this.mClock = CurrentSystemClock.INSTANCE;
    }

    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Intent intent = this.getIntent();
        Uri data = intent.getData();
        String state = data.getQueryParameter("state");
        AuthorizationRequest request = PendingIntentStore.getInstance().getOriginalRequest(state);
        PendingIntent target = PendingIntentStore.getInstance().getPendingIntent(state);
        if (request == null) {
//            Logger.error("Response received for unknown request with state %s", new Object[]{state});
            this.finish();
        } else {
            Intent responseData;
            if (data.getQueryParameterNames().contains("error")) {
                String error = data.getQueryParameter("error");
                AuthorizationException ex =
                        AuthorizationException.fromOAuthTemplate(
                                AuthorizationRequestErrors.byString(error),
                                error,
                                data.getQueryParameter("error_description"),
                                UriUtil.parseUriIfAvailable(data.getQueryParameter("error_uri")));
                responseData = ex.toIntent();
            } else {
                AuthorizationResponse response = (new Builder(request)).fromUri(data, this.mClock).build();
                responseData = response.toIntent();
            }

//            Logger.debug("Forwarding redirect", new Object[0]);

            try {
                target.send(this, 0, responseData);
            } catch (CanceledException var10) {
//                Logger.errorWithStack(var10, "Unable to send pending intent", new Object[0]);

            }

            this.finish();
        }
    }

    @VisibleForTesting
    void setClock(CurrentSystemClock clock) {
        this.mClock = clock;
    }
}

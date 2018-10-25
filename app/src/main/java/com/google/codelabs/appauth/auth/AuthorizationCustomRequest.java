package com.google.codelabs.appauth.auth;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.Map;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class AuthorizationCustomRequest {

    public static final String RESPONSE_MODE_QUERY = "query";
    public static final String RESPONSE_MODE_FRAGMENT = "fragment";
    public static final String RESPONSE_TYPE_CODE = "code";
    public static final String RESPONSE_TYPE_TOKEN = "token";
    public static final String SCOPE_OPENID = "openid";
    public static final String SCOPE_PROFILE = "profile";
    public static final String SCOPE_EMAIL = "email";
    public static final String SCOPE_ADDRESS = "address";
    public static final String SCOPE_PHONE = "phone";
    public static final String CODE_CHALLENGE_METHOD_S256 = "S256";
    public static final String CODE_CHALLENGE_METHOD_PLAIN = "plain";
    @VisibleForTesting
    static final String PARAM_CLIENT_ID = "client_id";
    @VisibleForTesting
    static final String PARAM_CODE_CHALLENGE = "code_challenge";
    @VisibleForTesting
    static final String PARAM_CODE_CHALLENGE_METHOD = "code_challenge_method";
    @VisibleForTesting
    static final String PARAM_DISPLAY = "display";
    @VisibleForTesting
    static final String PARAM_PROMPT = "prompt";
    @VisibleForTesting
    static final String PARAM_REDIRECT_URI = "redirect_uri";
    @VisibleForTesting
    static final String PARAM_RESPONSE_MODE = "response_mode";
    @VisibleForTesting
    static final String PARAM_RESPONSE_TYPE = "response_type";
    @VisibleForTesting
    static final String PARAM_SCOPE = "scope";
    @VisibleForTesting
    static final String PARAM_STATE = "state";


    private static final String KEY_CONFIGURATION = "configuration";
    private static final String KEY_CLIENT_ID = "clientId";
    private static final String KEY_DISPLAY = "display";
    private static final String KEY_PROMPT = "prompt";
    private static final String KEY_RESPONSE_TYPE = "responseType";
    private static final String KEY_REDIRECT_URI = "redirectUri";
    private static final String KEY_SCOPE = "scope";
    private static final String KEY_STATE = "state";
    private static final String KEY_CODE_VERIFIER = "codeVerifier";
    private static final String KEY_CODE_VERIFIER_CHALLENGE = "codeVerifierChallenge";
    private static final String KEY_CODE_VERIFIER_CHALLENGE_METHOD = "codeVerifierChallengeMethod";
    private static final String KEY_RESPONSE_MODE = "responseMode";
    private static final String KEY_ADDITIONAL_PARAMETERS = "additionalParameters";
    private static final int STATE_LENGTH = 16;
    @NonNull
    public final AuthorizationServiceConfiguration configuration;
    @NonNull
    public final String clientId;
    @Nullable
    public final String display;
    @Nullable
    public final String prompt;
    @NonNull
    public final String responseType;
    @NonNull
    public final Uri redirectUri;
    @Nullable
    public final String scope;
    @Nullable
    public final String state;
    @Nullable
    public final String codeVerifier;
    @Nullable
    public final String codeVerifierChallenge;
    @Nullable
    public final String codeVerifierChallengeMethod;
    @Nullable
    public final String responseMode;
    @NonNull
    public final Map<String, String> additionalParameters;

}

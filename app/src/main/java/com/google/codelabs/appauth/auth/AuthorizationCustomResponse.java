package com.google.codelabs.appauth.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import net.openid.appauth.AuthorizationRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class AuthorizationCustomResponse {

    public static final String EXTRA_RESPONSE = "net.openid.appauth.AuthorizationResponse";
    public static final String TOKEN_TYPE_BEARER = "bearer";
    @VisibleForTesting
    static final String KEY_REQUEST = "request";
    @VisibleForTesting
    static final String KEY_ADDITIONAL_PARAMETERS = "additional_parameters";
    @VisibleForTesting
    static final String KEY_EXPIRES_AT = "expires_at";
    @VisibleForTesting
    static final String KEY_STATE = "state";
    @VisibleForTesting
    static final String KEY_TOKEN_TYPE = "token_type";
    @VisibleForTesting
    static final String KEY_AUTHORIZATION_CODE = "code";
    @VisibleForTesting
    static final String KEY_ACCESS_TOKEN = "access_token";
    @VisibleForTesting
    static final String KEY_EXPIRES_IN = "expires_in";
    @VisibleForTesting
    static final String KEY_ID_TOKEN = "id_token";
    @VisibleForTesting
    static final String KEY_SCOPE = "scope";
    private static final Set<String> BUILT_IN_PARAMS = Collections.unmodifiableSet(new HashSet(Arrays.asList("token_type", "state", "code", "access_token", "expires_in", "id_token", "scope")));
    @NonNull
    public final AuthorizationRequest request;
    @Nullable
    public final String state;
    @Nullable
    public final String tokenType;
    @Nullable
    public final String authorizationCode;
    @Nullable
    public final String accessToken;
    @Nullable
    public final Long accessTokenExpirationTime;
    @Nullable
    public final String idToken;
    @Nullable
    public final String scope;
    @NonNull
    public final Map<String, String> additionalParameters;

    private AuthorizationCustomResponse(@NonNull )
}

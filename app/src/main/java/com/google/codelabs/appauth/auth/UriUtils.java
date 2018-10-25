package com.google.codelabs.appauth.auth;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class UriUtils {

    private UriUtils(){

    }

    public static Uri parseUriIfAvailable(@Nullable String uri ){
        return uri == null ? null : Uri.parse(uri);

    }

    public static void appendQueryParameterIfNotNull(@NonNull Uri.Builder uriBuilder, @NonNull String paramName, @Nullable Object value) {
        if (value != null) {
            String valueStr = value.toString();
            if (valueStr != null) {
                uriBuilder.appendQueryParameter(paramName, value.toString());
            }
        }
    }

    public static Long getLongQueryParameter(@NonNull Uri uri, @NonNull String param) {
        String valueStr = uri.getQueryParameter(param);
        return valueStr != null ? Long.parseLong(valueStr) : null;
    }

}

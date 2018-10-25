package com.google.codelabs.appauth;

import android.app.PendingIntent;
import android.support.annotation.VisibleForTesting;

import net.openid.appauth.AuthorizationRequest;
//import net.openid.appauth.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class PendingIntentStore {

    private Map<String, AuthorizationRequest> mRequests = new HashMap();
    private Map<String, PendingIntent> mPendingIntents = new HashMap();
    private static PendingIntentStore sInstance;

    private PendingIntentStore() {
    }

    public static synchronized PendingIntentStore getInstance() {
        if (sInstance == null) {
            sInstance = new PendingIntentStore();
        }

        return sInstance;
    }

    public void addPendingIntent(AuthorizationRequest request, PendingIntent intent) {
//        Logger.verbose("Adding pending intent for state %s", new Object[]{request.state});
        this.mRequests.put(request.state, request);
        this.mPendingIntents.put(request.state, intent);
    }

    public AuthorizationRequest getOriginalRequest(String state) {
//        Logger.verbose("Retrieving original request for state %s", new Object[]{state});
        return (AuthorizationRequest)this.mRequests.remove(state);
    }

    public PendingIntent getPendingIntent(String state) {
//        Logger.verbose("Retrieving pending intent for scheme %s", new Object[]{state});
        return (PendingIntent)this.mPendingIntents.remove(state);
    }

    @VisibleForTesting
    void clearPendingIntents() {
        this.mPendingIntents.clear();
    }
}

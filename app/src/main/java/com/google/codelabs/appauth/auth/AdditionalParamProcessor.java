package com.google.codelabs.appauth.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class AdditionalParamProcessor {

    static Set<String> buildInParams( String... params ){
        return  params != null && params.length != 0 ?
                Collections.<String>unmodifiableSet(new HashSet(Arrays.asList(params))) :
                Collections.<String>emptySet();
    }

    static Map<String,String> checkAdditionalParams( @Nullable Map<String,String> params ,
                                                     @NonNull Set<String> buildInParams){
        if( params == null){
            return Collections.emptyMap();
        } else {
            Map<String,String> addtionalParams = new LinkedHashMap<>();
            Iterator iter = addtionalParams.entrySet().iterator();

            while( iter.hasNext() ){
                Entry<String,String> entry = (Entry<String, String>) iter.next();
                String key = entry.getKey();
                String value = entry.getValue();



            }
        }
    }

}

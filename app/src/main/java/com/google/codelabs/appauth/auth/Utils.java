package com.google.codelabs.appauth.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 *
 *
 */
public class Utils {

    private static final int INITIAL_READ_BUFFER_SIZE = 1024;

    private Utils(){
    }

    /**
     * 1024 buffer 사이즈로 String을 만드는 함수.
     * @param in
     * @return
     * @throws IOException
     */
    public static String readInputStream(InputStream in ) throws IOException{

        BufferedReader br = new BufferedReader( new InputStreamReader( in ));
        char[] buffer = new char[ INITIAL_READ_BUFFER_SIZE ];

        StringBuffer sb = new StringBuffer();

        int readCount;
        while( (readCount = br.read(buffer)) != -1 ){
            sb.append(buffer,0,readCount);
        }


        return sb.toString();
    }

    public static void closeQuietly( InputStream in ){
        try{
            if( in != null ){
                in.close();
            }
        }catch ( IOException io){
            io.printStackTrace();
        }
    }
}

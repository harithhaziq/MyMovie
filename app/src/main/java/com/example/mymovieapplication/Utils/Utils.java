package com.example.mymovieapplication.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.http2.Header;

public class Utils {

    public static Map<String, String> getHeadersMap(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer <your_access_token_here>");
      return headers;
    }
}


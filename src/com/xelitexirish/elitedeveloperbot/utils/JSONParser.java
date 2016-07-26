package com.xelitexirish.elitedeveloperbot.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class JSONParser {

    String charset = "UTF-8";
    HttpURLConnection httpURLConnection;
    DataOutputStream outputStream;
    StringBuilder result;
    URL urlObj;
    JSONObject jsonObject = null;
    StringBuilder stringBuilder;
    String paramsString;

    public JSONObject makeHttpRequest(String url, String method, HashMap<String, String> params) {
        stringBuilder = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(key).append("=").append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);

                httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", charset);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);

                httpURLConnection.connect();

                paramsString = stringBuilder.toString();

                outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                outputStream.writeBytes(paramsString);
                outputStream.flush();
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("GET")) {
            // request method is GET

            if (stringBuilder.length() != 0) {
                url += "?" + stringBuilder.toString();
            }

            try {
                urlObj = new URL(url);
                httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept-Charset", charset);
                httpURLConnection.setConnectTimeout(15000);

                httpURLConnection.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        httpURLConnection.disconnect();

        // try parse the string to a JSON object
        try {
            jsonObject = new JSONObject(result.toString());
        } catch (JSONException e) {

        }

        // return JSON Object
        return new JSONObject();
    }
}

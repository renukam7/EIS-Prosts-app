package com.dublin.restaurant.ServerCall;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Shridhar on 2/13/2018.
 */

public class MakeServerCall {

    String url;
    ServerResponse callBack;
    Uri.Builder builder;

    public MakeServerCall(String url, ServerResponse callBack, Uri.Builder builder)
    {
        this.url = url;
        this.callBack = callBack;
        this.builder = builder;
    }

    public void getResposeByServer() {



        new AsyncTask<String,String,String>() {
            @Override
            protected String doInBackground(String... params) {
                URL atUrl = null;
                HttpURLConnection urlConnection = null;
                try {
                    atUrl = new URL(url);
                    urlConnection = (HttpURLConnection) atUrl.openConnection();
                    urlConnection.setRequestMethod("POST");

                    urlConnection.setConnectTimeout(150000);
                    /*OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(jsonObject.toString());

                    wr.flush();
                    wr.close();*/

                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    /*Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("email", "shridhar@gmail.com")
                            .appendQueryParameter("password","1234");*/
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    urlConnection.connect();

                    int statusCode = urlConnection.getResponseCode();
                    InputStream in;
                    if (statusCode >= 200 && statusCode < 400) {
                        // Create an InputStream in order to extract the response object
                        in = urlConnection.getInputStream();
                    } else {
                        in = urlConnection.getErrorStream();
                    }
                    return getStringFromInputStream(in, "dds");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                try {
                    if(callBack != null) {
                        if(url.startsWith("https://maps.googleapis.com/maps/api/directions")) {
                            callBack.serverResponseProcess(s,"DIRECTION");
                        } else if(url.startsWith("https://maps.googleapis.com/maps/api/place/autocomplete")){
                            callBack.serverResponseProcess(s,"AUTOCOMPLETE");
                        } else {
                            callBack.serverResponseProcess(s, url);
                        }
                    }

                    Log.d("serverResponse :: ",url+"\n"+s);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public String getStringFromInputStream(InputStream stream, String charsetName) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream );
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}

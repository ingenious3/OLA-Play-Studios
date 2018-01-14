package com.example.ishant.olaplaystudios.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.ishant.olaplaystudios.model.SongModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpQueryUtils {

    public static ArrayList<SongModel> songList = new ArrayList<>();

    private HttpQueryUtils() {
    }

    private static final String LOG_TAG = HttpQueryUtils.class.getSimpleName();

    public static ArrayList<SongModel> fetchSongList(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem Closing inputStream", e);
        }

        ArrayList<SongModel> SongList = getSongList(jsonResponse);

        return SongList;
    }
    private static URL createUrl(String stringUrl){

        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error Creating Url", e);
        }
        return url;

    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(30000 /*milliseconds*/);
            urlConnection.setConnectTimeout(60000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode()== 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error Response Code : " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem Retrieving the json Response", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null){

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){

                output.append(line);
                line = reader.readLine();
            }
        }
        Log.e(LOG_TAG,output.toString());
        return output.toString();

    }

    public static ArrayList<SongModel> getSongList(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        songList = new Gson().fromJson(jsonResponse, new TypeToken<List<SongModel>>() {}.getType());
        return songList;
    }
}

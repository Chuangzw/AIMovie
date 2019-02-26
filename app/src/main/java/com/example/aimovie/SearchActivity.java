package com.example.aimovie;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private String movieName;
    public List<MovieData> movieList;

    String movieJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Button buttonSearch = findViewById(R.id.search_button);


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mEditText = findViewById(R.id.editText);
                movieName = mEditText.getText().toString();
                final String movieUrl = "https://api.douban.com/v2/movie/search?q="+movieName+"&apikey=0b2bdeda43b5688921839c8ecb20399b&start=0&count=10";

                movieJson = null;
                while (movieJson == null){
                    new DownloadThread(movieUrl).start();

                }

//                Toast.makeText(getApplicationContext(),movieJson,Toast.LENGTH_LONG).show();

//                MovieData movieData = jsonToMovie(movieJson);
//
//                final TextView textView = findViewById(R.id.movie_name);
//                ImageView imageView = findViewById(R.id.movie_image);

//                textView.setText(movieName+" "+"ID"+movieData.getSubjects().get(0).getId());

//                String movieImageUrl = movieData.getSubjects().get(0).getImages().getSmall();
//
//                Bitmap bitmap = getBitmap(movieImageUrl);
//
//                imageView.setImageBitmap(bitmap);

                Intent intent = new Intent(SearchActivity.this,MovieScrollingActivity.class);
                intent.putExtra("data",movieJson);
                startActivity(intent);




            }
        });

    }


    public String download(String urlStr) {

        try {
            URL   url = new URL(urlStr);// 获得连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);//设置超时
            connection.setDoInput(true);
            connection.setUseCaches(false);//不缓存
            connection.connect();

            InputStream is = connection.getInputStream();

            InputStreamReader isr;
            isr = new InputStreamReader(is, "UTF-8");

            BufferedReader buffer = new BufferedReader(isr);

            StringBuffer jsonBuffer = new StringBuffer();

            String line;
            while ((line = buffer.readLine()) != null) {
                jsonBuffer.append(line);
            }
            return jsonBuffer.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    private class DownloadThread extends Thread {

        String url;

        public DownloadThread(String url) {
            this.url = url;
        }
        public void run() {

            while (movieJson == null){
                movieJson = download(url);
            }


        }
    }
    public static Bitmap getBitmap(String path) {

        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MovieData jsonToMovie(String movieJson){


        Gson gson = new Gson();

        MovieData movieData = gson.fromJson(movieJson,MovieData.class);

        return movieData;

    }

}

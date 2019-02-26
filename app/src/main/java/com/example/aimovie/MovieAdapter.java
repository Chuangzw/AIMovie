package com.example.aimovie;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MovieAdapter {
    Bitmap bitmap = null;
    public MovieAdapter(MovieData movieData, Activity activity){

        String movieName = movieData.getTitle();
        String movieImageUrl = movieData.getSubjects().get(0).getImages().getMedium();

        TextView textView = activity.findViewById(R.id.movie_name);

        String movieDetails = MovieDetails(movieData);


        textView.setText(movieName+"\n"+movieDetails);

        ImageView imageView = activity.findViewById(R.id.movie_image);


        bitmap = null;
        while (bitmap == null){
            new ImageThread(movieImageUrl).start();
        }


//            imageView.setImageBitmap(urlToBitmap(movieImageUrl));

        imageView.setImageBitmap(bitmap);


    }

    class ImageThread extends Thread{
        String imageUrl;
        public ImageThread(String imageUrl){
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {
            super.run();


            bitmap = urlToBitmap(imageUrl);


         }
    }
    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    private Bitmap urlToBitmap(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);// 获得连接

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);//设置超时
            connection.setDoInput(true);
            connection.setUseCaches(false);//不缓存
            connection.connect();
            InputStream is = connection.getInputStream();//获得图片的数据流
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    public String MovieDetails(MovieData movieData){
        String movieDetails ="";


        // 评分
        String average = String.valueOf(movieData.getSubjects().get(0).getRating().getAverage());
        movieDetails += "评分: " + average+"\n";

            // 原名
        String originalTitle = movieData.getSubjects().get(0).getOriginal_title();
        movieDetails +="原名: " + originalTitle+"\n";

        // 年代
        String year = movieData.getSubjects().get(0).getYear();
        movieDetails +="年代: " + year+"\n";


// 电影 ID
        String movieId = movieData.getSubjects().get(0).getId();
        movieDetails += "电影 ID: " + movieId+"\n";



// 电影中文名
        String title = movieData.getSubjects().get(0).getTitle();
        movieDetails += "电影中文名: " + title+"\n";



// 影片类型（最多提供3个）
        String genres = movieData.getSubjects().get(0).getGenres().toString();
        String genre = genres.substring(1, genres.length() - 1);
        movieDetails += "影片类型: " + genre+"\n";



// 主演
        String castAvatar = movieData.getSubjects().get(0).getCasts().get(0).getAvatars().getMedium();    // 主演头像
        String castName = movieData.getSubjects().get(0).getCasts().get(0).getName();                     // 主演中文名
        String castId = movieData.getSubjects().get(0).getCasts().get(0).getId();                         // 主演 ID
        movieDetails += "主演中文名: " + castName+"\n";
        movieDetails += "主演 ID: " + castId+"\n";



// 导演
        String directorName = movieData.getSubjects().get(0).getDirectors().get(0).getName();                     // 导演中文名
        String directorId = movieData.getSubjects().get(0).getDirectors().get(0).getId();                         // 导演 ID

        movieDetails += "导演中文名: " + directorName+"\n";
        movieDetails += "导演 ID: " + directorId+"\n";





        return movieDetails;
    }
}

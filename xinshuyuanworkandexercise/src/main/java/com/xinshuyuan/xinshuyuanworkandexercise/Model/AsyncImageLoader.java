package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/6/28.
 */

public class AsyncImageLoader {
    /**
     *从url中获取到Bitmap
     * @description：
     * @author ldm
     * @date 2015-8-11 下午1:55:12
     */
    public Bitmap getBitmapByUrl(String urlStr) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(con.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            con.disconnect();
            return bitmap;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void loadImgByAsyncTask(ImageView img, String url) {
        new ImageAsyncTask(img, url).execute(url);
    }


    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private String mUrl;


        public ImageAsyncTask(ImageView imageView, String mUrl) {
            this.imageView = imageView;
            this.mUrl = mUrl;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
// TODO Auto-generated method stub
            return getBitmapByUrl(params[0]);
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            if (imageView.getTag().equals(mUrl)) {
                imageView.setImageBitmap(result);
            }
        }
    }

}

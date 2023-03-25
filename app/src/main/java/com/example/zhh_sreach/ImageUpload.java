package com.example.zhh_sreach;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUpload {

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final OkHttpClient client = new OkHttpClient();
    public static void run(File f) throws Exception {
        final File file=f;
        new Thread() {

            @Override
            public void run()  {
                //子线程需要做的工作
                OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MEDIA_TYPE_JPG, file))
                        .build();
                //设置为自己的ip地址
                Request request = new Request.Builder()
                        .url("http://122.235.98.24:5000/upload")
                        .post(requestBody)
                        .build();
                try(Response response = client.newCall(request).execute()){
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
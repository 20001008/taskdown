package com.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class MyDownLoad extends AsyncTask<String,Integer,Integer> {
    private DownloadListener downloadListener;
    private Boolean isCanceled = false;
    private Boolean isPaused = false;
    private int Lastprocess;

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int process = values[0];
        if (process > Lastprocess)
        {
            downloadListener.onProgress(process);
            Lastprocess=process;
        }
    }

    public interface DownloadListener {
        void onProgress(int progress);

        void onSuccess();

        void onFailed();

        void onPaused();

        void onCanceled();
    }

    public MyDownLoad(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    @Override
    protected Integer doInBackground(String... strings) {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        File fialename = null;
        try {
            Long FileLength = 0L;
            String downloaduri = strings[0];
            String filename = downloaduri.substring(downloaduri.lastIndexOf("/"));
            String downpath = Myappcation.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
            Log.d("tag", "doInBackground: " + downpath);
            fialename = new File(downpath + filename);
            Log.d(TAG, downpath + filename);
            if (fialename.exists()) {
                FileLength = fialename.length();
            }else{
            }
            long contentlength = getContextlengt(downloaduri);
            if (contentlength == 0) {
                return TYPE_FAILED;
            } else if (contentlength == FileLength) {
                return TYPE_SUCCESS;
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(downloaduri).addHeader("RANGE", "bytes=" + FileLength + "-").build();
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                inputStream = response.body().byteStream();
                randomAccessFile = new RandomAccessFile(fialename, "rw");
                randomAccessFile.seek(FileLength);
                byte[] bytes = new byte[1024];
                int len;
                int downloadNUM = 0;//下载量
                while ((len = inputStream.read(bytes)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        downloadNUM += len;
                        randomAccessFile.write(bytes, 0, len);
                        int Process = (int) ((downloadNUM + FileLength) * 100 / contentlength);
                        Log.d(TAG, "\n已下载: "+downloadNUM+" byte"+"\n已完成:"+Integer.toString(Process));
                        publishProgress(Process);
                    }
                }
                response.close();
                return TYPE_SUCCESS;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (isCanceled == true) {
                    fialename.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer){
            case TYPE_SUCCESS:
                downloadListener.onSuccess();
                break;
            case TYPE_CANCELED:
                downloadListener.onCanceled();
                break;
            case TYPE_PAUSED:
                downloadListener.onPaused();
                break;
            case TYPE_FAILED:
                downloadListener.onPaused();
                break;
                default:
                    break;

        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Long getContextlengt(String pathuri) throws IOException {
        Long filelength = null;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(pathuri).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            filelength = response.body().contentLength();
        }
        return filelength;
    }
    public void pauseDownload(){
        isPaused=true;
    }
    public void cancleDownload()
    {
        isCanceled=true;
    }
}


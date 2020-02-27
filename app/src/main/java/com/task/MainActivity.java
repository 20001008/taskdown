package com.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.security.Permission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Cursor cursor;
private NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button2);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
         }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button:
                //申请权限
                if(Build.VERSION.SDK_INT>=21)
                {
                   int Permission=ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(Permission!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},1);
                    }
                }
                Intent intent=new Intent(MainActivity.this,Myservice.class);
                startService(intent);
                new MyDownLoad(new MyDownLoad.DownloadListener() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onSuccess() {
                        Log.d("tag", "下载成功");
                    }

                    @Override
                    public void onFailed() {
                        Log.d("tag", "下载失败");
                    }

                    @Override
                    public void onPaused() {

                    }

                    @Override
                    public void onCanceled() {

                    }
                }).execute("https://product-downloads.atlassian.com/software/sourcetree/windows/ga/SourceTreeSetup-3.3.8.exe");

                break;
            case R.id.button2:
                Intent intent1=new Intent(MainActivity.this,Myservice.class);
                stopService(intent1);
                break;
                default:
        }
    }
}

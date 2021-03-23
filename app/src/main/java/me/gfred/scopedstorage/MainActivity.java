package me.gfred.scopedstorage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button downloadImageBtn, downloadVideoBtn, downloadTextBtn;
    Button viewImageBtn, viewVideoBtn, viewTextBtn;
    private List<String> permissions;
    String imageName = "gfred.jpeg";
    String videoName = "video.mp4";
    String textName = "android.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions = new ArrayList<>();
        grantPermissions();

        downloadImageBtn = findViewById(R.id.button_image);
        downloadVideoBtn = findViewById(R.id.button_video);
        downloadTextBtn = findViewById(R.id.button_txt);

        viewImageBtn = findViewById(R.id.view_image);
        viewVideoBtn = findViewById(R.id.view_video);
        viewTextBtn = findViewById(R.id.view_text);

        downloadImageBtn.setOnClickListener(v -> {
            String imageURL = "https://avatars.githubusercontent.com/u/8388606?s=460&u=5cc5c3e65e11f888ad791893ff0bafc8287deb6e&v=4";

            new Thread(() -> downloadImageFile(imageURL, imageName)).start();
        });

        downloadVideoBtn.setOnClickListener(v -> {
            String videoURL = "http://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4";

            new Thread(() -> downloadVideoFile(videoURL, videoName)).start();
        });

        downloadTextBtn.setOnClickListener(v -> {
            String fileUrl = "https://raw.githubusercontent.com/gfredtech/loan-project/master/loan-backend/app.py";

            new Thread(() -> downloadTextFile(fileUrl, textName)).start();
        });

        viewImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewFileActivity.class);
            intent.putExtra("fileName", imageName);
            intent.putExtra("fileType", "image");
            startActivity(intent);
        });

        viewVideoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewFileActivity.class);
            intent.putExtra("fileName", videoName);
            intent.putExtra("fileType", "video");
            startActivity(intent);
        });

        viewTextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewFileActivity.class);
            intent.putExtra("fileName", textName);
            intent.putExtra("fileType", "text");
            startActivity(intent);
        });
    }




    private void grantPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        String [] arr = permissions.toArray(new String[0]);
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, arr, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int res: grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You must accept all permissions", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void downloadImageFile(String imageURL, String imageName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(this, "PLease test on device running Android 10 or higher", Toast.LENGTH_SHORT).show();
            return;
        }

        runOnUiThread(() -> Toast.makeText(this, "Downloading image...", Toast.LENGTH_SHORT).show());
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            File file = new File(getExternalFilesDir(null), imageName);
            Log.d("Gfredtech", String.valueOf(file.exists()));
            Uri uri = Uri.fromFile(file);
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    byte[] buffer = new byte[]{(byte) (1024 >>> 8), (byte) 1024};
                    int bytes = bufferedInputStream.read(buffer);
                    while (bytes >= 0) {
                        bufferedOutputStream.write(buffer, 0, bytes);
                        bufferedOutputStream.flush();
                        bytes = bufferedInputStream.read(buffer);
                    }
                    bufferedOutputStream.close();

                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Image file has been downloaded", Toast.LENGTH_SHORT).show());
                }
            }
            bufferedInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadVideoFile(String videoURL, String videoName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(this, "PLease test on device running Android 10 or higher", Toast.LENGTH_SHORT).show();
            return;
        }

        runOnUiThread(() -> Toast.makeText(this, "Downloading video file...", Toast.LENGTH_SHORT).show());
        try {
            URL url = new URL(videoURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            File file = new File(getExternalFilesDir(null), videoName);
            Uri uri = Uri.fromFile(file);
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    byte[] buffer = new byte[]{(byte) (1024 >>> 8), (byte) 1024};
                    int bytes = bufferedInputStream.read(buffer);
                    while (bytes >= 0) {
                        bufferedOutputStream.write(buffer, 0, bytes);
                        bufferedOutputStream.flush();
                        bytes = bufferedInputStream.read(buffer);
                    }
                    bufferedOutputStream.close();

                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Video file has been downloaded", Toast.LENGTH_SHORT).show());
                }
            }
            bufferedInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadTextFile(String fileUrl, String fileName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(this, "PLease test on device running Android 10 or higher", Toast.LENGTH_SHORT).show();
            return;
        }

        runOnUiThread(() -> Toast.makeText(this, "Downloading text file...", Toast.LENGTH_SHORT).show());
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            File file = new File(getExternalFilesDir(null), fileName);
            Uri uri = Uri.fromFile(file);
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    byte[] buffer = new byte[]{(byte) (1024 >>> 8), (byte) 1024};
                    int bytes = bufferedInputStream.read(buffer);
                    while (bytes >= 0) {
                        bufferedOutputStream.write(buffer, 0, bytes);
                        bufferedOutputStream.flush();
                        bytes = bufferedInputStream.read(buffer);
                    }
                    bufferedOutputStream.close();

                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Text file has been downloaded", Toast.LENGTH_SHORT).show());
                }
            }
            bufferedInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
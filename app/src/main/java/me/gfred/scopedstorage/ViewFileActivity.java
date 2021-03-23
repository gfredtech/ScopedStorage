package me.gfred.scopedstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class ViewFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);

        Intent intent = getIntent();
        if (intent != null) {
            String fileType = intent.getStringExtra("fileType");
            String fileName= intent.getStringExtra("fileName");
            switch (fileType) {
                case "image": {
                    ImageView imageView = findViewById(R.id.display_image);
                    imageView.setVisibility(View.VISIBLE);
                    // load image
                    try {
                        // get input stream
                        InputStream ims = new FileInputStream(new File(getExternalFilesDir(null), fileName));
                        // load image as Drawable
                        Drawable d = Drawable.createFromStream(ims, null);
                        // set image to ImageView
                        imageView.setImageDrawable(d);
                    }
                    catch(IOException ex) {
                        return;
                    }
                }

                case "text": {
                    TextView textView = findViewById(R.id.display_text);
                    textView.setVisibility(View.VISIBLE);
                    StringBuilder text = new StringBuilder();
                    File file = new File(getExternalFilesDir(null), fileName);

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                    }
                    catch (IOException e) {
                        //You'll need to add proper error handling here
                    }
                    textView.setText(text.toString());
                }

                case "video": {
                    File file = new File(getExternalFilesDir(null), fileName);
                    VideoView vv = findViewById(R.id.display_video);
                    vv.setVisibility(View.VISIBLE);
                    vv.setVideoURI(Uri.fromFile(file));
                    vv.start();

//                    vv.setOnCompletionListener(mp -> finish());
                }
            }
        }
    }
}
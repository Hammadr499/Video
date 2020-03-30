package com.example.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadVideo extends AppCompatActivity {
    private String videoname;
    private VideoView videoView;
    private static final int request_Code = 123;
    private Uri uri;
    private MediaController mediaController;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        mStorageRef = FirebaseStorage.getInstance().getReference("upload").child("videos");
        videoView = findViewById(R.id.Videoview);
        mediaController = new  MediaController(this);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);

                    }
                });
            }
        });
        videoView.start();
    }
    public void videofromGallery(View view) {
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("video/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, request_Code);

        } catch (Exception e) {
            Toast.makeText(this, "SelectVideofromGallery:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_Code && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (uri != null) {
                videoView.setVideoURI(uri);
            }
            else {

                Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"No Video Selected",Toast.LENGTH_SHORT).show();
        }
    }
    public void upload(View view){

        StorageReference riversRef = mStorageRef.child("Hammad");
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

}

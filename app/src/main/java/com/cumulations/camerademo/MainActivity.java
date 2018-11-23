package com.cumulations.camerademo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    public void setImage(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Upload Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(intent, 101);
                }

                if (items[which].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
                }

                if (items[which].equals("Cancel")) {
                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle bundle = data.getExtras();
            Bitmap bitmap = null;
            if (bundle != null) {
                bitmap = (Bitmap) bundle.get("data");
            }
            imageView.setImageBitmap(bitmap);
            Toast.makeText(this, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Didn't take photo.", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 102 && resultCode == RESULT_OK){
            Bitmap bitmap=null;
            if(data!=null){
                try{
                    bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                }catch (Exception e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            imageView.setImageBitmap(bitmap);
            Toast.makeText(this, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 102 && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Didn't select any photo.", Toast.LENGTH_SHORT).show();
        }
    }
}

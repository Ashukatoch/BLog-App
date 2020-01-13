package com.example.myblogapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myblogapp.Model.Blog;
import com.example.myblogapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class addpostactivity extends AppCompatActivity {
private EditText title;
private EditText description;
private ImageButton postimage;
private Button post;
private FirebaseUser muser;
private FirebaseAuth mauth;
private FirebaseDatabase mdatabase;
private DatabaseReference mdatabasereference;
private ProgressDialog mprogress;
private Uri imageuri;
private String downloaduri;
private StorageReference mstorage;
private static final int Galery_code=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpostactivity);
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();
        mprogress=new ProgressDialog(this);
        mdatabase=FirebaseDatabase.getInstance();
        mdatabasereference=mdatabase.getReference("Blog");
        mdatabasereference.keepSynced(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mstorage= FirebaseStorage.getInstance().getReference();
        title=(EditText)findViewById(R.id.posttitle);
        description=(EditText)findViewById(R.id.postdesc);
        postimage=(ImageButton)findViewById(R.id.postimage);
        post=(Button)findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
            }
        });
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeryintent=new Intent(Intent.ACTION_GET_CONTENT);
                galeryintent.setType("image/*");
                startActivityForResult(galeryintent,Galery_code);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Galery_code && resultCode==RESULT_OK)
        {
            imageuri=data.getData();
            CropImage.activity(imageuri).setAspectRatio(1, 1).start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            Picasso.with(getApplicationContext()).load(result.getUri()).placeholder(R.drawable.blogimage).into(postimage);
           // postimage.setImageURI(result.getUri());
        }
    }

    private void startposting()
    {
mprogress.setMessage("Posting to blog...");
mprogress.show();
final String titlestring=title.getText().toString().trim();
final String descstring=description.getText().toString().trim();
if(!titlestring.equals("")&&!descstring.equals("") &imageuri!=null)
{
    Log.d("Image",imageuri.toString());
    //uploadprocess
    final StorageReference filepath=mstorage.child("Blog_images").child(imageuri.getLastPathSegment());
    //filepath.putFile(imageuri)
    filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
        {
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri)
                {

                downloaduri=uri.toString();
                    Log.d("Image2",downloaduri);
                    DatabaseReference newpost=mdatabasereference.push();
                    Map<String, String> datatosave=new HashMap<>();
                    datatosave.put("title",titlestring);
                    datatosave.put("description",descstring);
                    datatosave.put("image",downloaduri.toString());
                    datatosave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    datatosave.put("userid",muser.getUid());
                    newpost.setValue(datatosave);
                    mprogress.dismiss();
                    startActivity(new Intent(getApplicationContext(),PostListActivity.class));
                    finish();

                }
            });
        }
    });


}
    }
}

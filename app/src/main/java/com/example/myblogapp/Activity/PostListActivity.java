package com.example.myblogapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myblogapp.Data.BlogRecyclerviewAdapter;
import com.example.myblogapp.Model.Blog;
import com.example.myblogapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {
    private FirebaseUser muser;
    private FirebaseAuth mauth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mdatabasereference;
    private BlogRecyclerviewAdapter blogRecyclerviewAdapter;
    private RecyclerView recyclerView;
    private List<Blog> blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();
        mdatabase=FirebaseDatabase.getInstance();
        mdatabasereference=mdatabase.getReference().child("Blog");
        mdatabasereference.keepSynced(true);
        blogList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mdatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             Blog bg=dataSnapshot.getValue(Blog.class);
             blogList.add(bg);
             blogRecyclerviewAdapter=new BlogRecyclerviewAdapter(blogList, PostListActivity.this);
             recyclerView.setAdapter(blogRecyclerviewAdapter);
             blogRecyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_add:
                if(muser!=null&&mauth!=null)

                {
                    startActivity(new Intent(PostListActivity.this,addpostactivity.class));
                    PostListActivity.this.finish();
                }
                break;
            case R.id.action_signout:
                if(muser!=null&&mauth!=null) {
                    mauth.signOut();
                    startActivity(new Intent(PostListActivity.this, MainActivity.class));
                    finish();
                }
                break;
                }


        return super.onOptionsItemSelected(item);
    }


}

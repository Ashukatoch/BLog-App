package com.example.myblogapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myblogapp.Model.Blog;
import com.example.myblogapp.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION_CODES.P;

public class BlogRecyclerviewAdapter extends RecyclerView.Adapter<BlogRecyclerviewAdapter.ViewHolder> {
   private List<Blog> blogList;
   private Context context;

    public BlogRecyclerviewAdapter(List<Blog> blogList, Context context)
    {
        this.blogList = blogList;
        this.context = context;
    }

    @NonNull
    @Override
    public BlogRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.postrow,viewGroup,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerviewAdapter.ViewHolder viewHolder, int i) {
      Blog blog=blogList.get(i);
      String imageurl=null;
      viewHolder.title.setText(blog.getTitle());
      viewHolder.description.setText(blog.getDescription());
      java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
      String formatted_date=dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
      viewHolder.date.setText(formatted_date);
      imageurl=blog.getImage();

        Picasso.with(context).load(imageurl).placeholder(R.drawable.blogimage).into(viewHolder.post);
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView description;
        public ImageView post;
        public TextView date;
        String userid;

        public ViewHolder(@NonNull View v,Context ctx ) {
            super(v);
            title=(TextView)v.findViewById(R.id.posttitlelist);
            description=(TextView)v.findViewById(R.id.posttextlist);
            date=(TextView)v.findViewById(R.id.postdatelist);
            post=(ImageView)v.findViewById(R.id.postimagelist);
            userid=null;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}

package com.example.profilesave1.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profilesave1.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

    private ArrayList<User> users;
    private Context context;
    private OnUserClickListener onUserClickListener;

    public UsersAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    public interface OnUserClickListener{  // This click will happen when the user clicks on another username is a chat fragment
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view =LayoutInflater.from(context).inflate(R.layout.user_holder,parent,false);
       return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUsername.setText(users.get(position).getName());
        Glide.with(context).load(users.get(position).getUrl()).error(R.drawable.no_avatar).placeholder(R.drawable.no_avatar).into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    // the bubbles in the chat fragment
    class UserHolder extends RecyclerView.ViewHolder{  // Each line in the list - includes a picture and name of the user
            TextView txtUsername;
            ImageView imageView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername);
            imageView = itemView.findViewById(R.id.img_prof);
        }
    }
}

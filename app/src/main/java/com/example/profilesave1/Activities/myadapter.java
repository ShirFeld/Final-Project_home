package com.example.profilesave1.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profilesave1.Fragments.descfragment;
import com.example.profilesave1.Models.model;
import com.example.profilesave1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
// we can delete
public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>  // bring us data from database to singlerowdesign (search)
{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final model model) {
        holder.nametext.setText(model.getName());
        holder.citytext.setText(model.getCity());
        Glide.with(holder.img1.getContext()).load(model.getUrl()).into(holder.img1); // url

        // open the user details
        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new descfragment(model.getCity(),model.getName(),model.getUrl(),model.getAge(),
                        model.getFavoriteHobby(),model.getHaveAnimals() , model.getSex(), model.getMaritalStatus()
                , model.getHaveChildren(),model.getFavoriteMoviesCategory() , model.getAboutMe())).addToBackStack(null).commit();
            }

        });
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myviewholder(view);
    }
    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView nametext, citytext;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            citytext =itemView.findViewById(R.id.citytext);
        }
    }

}
package com.example.profilesave1.Activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profilesave1.Fragments.descfragment;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> { // connects to recfragment

    /*
    This adapter is connect to recfragment.fragment (to the list - search)
     */
    private ArrayList<User> arrayList;
    public static int indexPosition;
    static double lon1;
    static double lat1;
    static double lon2;
    static double lat2;

    static boolean  flagV = true;

    public MyAdapter2(ArrayList<User> arrayList) {
        this.arrayList = arrayList;
    }
    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nametext.setText(arrayList.get(position).getName());
        holder.citytext.setText(arrayList.get(position).getCity());
        Glide.with(holder.img1.getContext()).load(arrayList.get(position).getUrl()).into(holder.img1); // url
        holder.agetext.setText(arrayList.get(position).getAge());

        if(arrayList.get(position).getAge().equals("")){
            holder.agetext1.setVisibility(View.GONE);
            holder.agetext.setVisibility(View.GONE);
        }

        // after the filter we will show only the right users
        if(arrayList.get(position).getName().equals("adminUser")){
           holder.rel.setVisibility(View.GONE);
        }

        // open the user details - to descframent
        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new descfragment(arrayList.get(position).getCity(),
                        arrayList.get(position).getName(),arrayList.get(position).getUrl(),arrayList.get(position).getAge(),arrayList.get(position)
                        .getFavoriteHobby(),arrayList.get(position).getHaveAnimals() , arrayList.get(position).getSex() , arrayList.get(position).getMaritalStatus(),
                        arrayList.get(position).getHaveChildren(),arrayList.get(position).getFavoriteMoviesCategory(),arrayList.get(position).getAboutMe())).addToBackStack(null).commit();
                indexPosition = position;
            }
        });


//        holder.block.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(v.getRootView().getContext()) .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Block user")
//                        .setMessage("Are you sure you want to block this user?")
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                holder.root.setVisibility(v.GONE);
//                                holder.rel.setVisibility(v.GONE);
//
//                            }
//                        }).setNegativeButton("No",null).show();
//            }
//        });




        // In this method we will receive and display the distances between users on the recyclerView
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    User user = snapshot.getValue(User.class);
                    String lon = user.getLongitude();
                    String lat = user.getLatitude();
                if(!lon.equals("") && !lat.equals("")){
                    lon1 = Double.parseDouble(lon);
                    lat1 = Double.parseDouble(lat);
                }else{
                    lon1 = 0;
                    lat1 = 0;
                }
                if(arrayList.get(position).getLatitude().equals(""))
                    lat2 = 0;

                else
                    lat2 = Double.parseDouble(arrayList.get(position).getLatitude());

                if(arrayList.get(position).getLongitude().equals(""))
                    lon2 = 0;

                else
                    lon2 = Double.parseDouble(arrayList.get(position).getLongitude());

                double ab = distanceBetween(lat1,lon1,lat2,lon2)/1000;
                ab = round(ab,1) ;

                // if there is no city so dont show anything
                if(lon2 == 0 && lat2 == 0 || lon1 == 0 && lat1 == 0  || arrayList.get(position).getCity().equals("")){
                    holder.gpstext0.setVisibility(View.GONE);
                    holder.gpstext1.setVisibility(View.GONE);
                    String ab1 = "";
                    holder.gpstext.setText(ab1);
                }

                else {
                    String ab1 = ab+"";
                    holder.gpstext.setText(ab1);
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @NonNull
    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img1 ;
        TextView nametext, citytext,agetext,agetext1,gpstext,gpstext0,gpstext1;
        CardView root ;
        RelativeLayout rel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            nametext = itemView.findViewById(R.id.nametext);
            citytext = itemView.findViewById(R.id.citytext);
            agetext = itemView.findViewById(R.id.agetext);
            agetext1 = itemView.findViewById(R.id.agetext1);
            gpstext = itemView.findViewById(R.id.gpstext);
            gpstext0 = itemView.findViewById(R.id.gpstext0);
            gpstext1 = itemView.findViewById(R.id.gpstext1);
            root = itemView.findViewById(R.id.single);
            rel = itemView.findViewById(R.id.rel);
//            block = itemView.findViewById(R.id.block_user_img);
        }
    }

    // returns the distance between 2 users
    public static float distanceBetween(double lat1,double lon1,double lat2,double lon2) {
        float[] distance = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        return distance[0];
    }

    // doing the distance with only one number after the '.'
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

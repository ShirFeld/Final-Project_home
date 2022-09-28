package com.example.profilesave1.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profilesave1.Models.Message;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<Message> messages;
    private String senderImg,reciverImg;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String reciverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.reciverImg = reciverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position).getContent());
        ConstraintLayout constraintLayout = holder.ccll;

        // those functions decided what bubble to show
        if(messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            Glide.with(context).load(senderImg).error(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(holder.profImage); // check if there is a photo
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardView,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.LEFT);
            constraintSet.connect(R.id.profile_cardView,ConstraintSet.RIGHT, R.id.ccLayout,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.RIGHT, R.id.profile_cardView,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);
        }

        else{
            Glide.with(context).load(reciverImg).error(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(holder.profImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardView,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.RIGHT);
            constraintSet.connect(R.id.profile_cardView,ConstraintSet.LEFT, R.id.ccLayout,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.LEFT, R.id.profile_cardView,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();

    }

    class MessageHolder extends RecyclerView.ViewHolder{  // The messages themselves, the bubbles
        ConstraintLayout ccll;
        TextView txtMessage;
        ImageView profImage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            ccll = itemView.findViewById(R.id.ccLayout);
            txtMessage = itemView.findViewById(R.id.txt_message_content);
            profImage = itemView.findViewById(R.id.small_profile_img);
        }
    }
}

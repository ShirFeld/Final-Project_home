package com.example.profilesave1.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.profilesave1.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class AboutUsFragment extends Fragment {

    /*
    /*In this fragment the user will see information about the developers of the application
     */

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_about_us, container, false);

        CircleImageView shir = view.findViewById(R.id.shir);
        CircleImageView michael = view.findViewById(R.id.michael);


        shir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri webpage = Uri.parse("https://www.linkedin.com/in/shirfeld/");
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                }catch (ActivityNotFoundException e){
                }}
        });

        michael.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   Uri webpage = Uri.parse("https://www.linkedin.com/in/michaelv84/");
                   Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                   startActivity(webIntent);
               }
               catch (ActivityNotFoundException e){

               }
            }
        });
        return view;
    }

}
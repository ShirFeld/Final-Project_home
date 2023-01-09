package com.example.profilesave1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.profilesave1.Activities.MainActivity;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;

public class LogOutFragment extends Fragment {

    /*
    Here the user can logout from the app.
     */

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth mFirebaseAuth;
    private Button btn_logout;

    private String mParam1;
    private String mParam2;

    public LogOutFragment() {
    }


    public static LogOutFragment newInstance(String param1, String param2) {
        LogOutFragment fragment = new LogOutFragment();
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
       View view = inflater.inflate(R.layout.fragment_log_out, container, false);
       btn_logout = (Button)view.findViewById(R.id.log_out_btn);
       mFirebaseAuth = FirebaseAuth.getInstance();

       // When the user will press the logout button he will transfer to the main page
       btn_logout.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               mFirebaseAuth.signOut();
               Intent intent = new Intent(getActivity(), MainActivity.class);
               startActivity(intent);
           }
       });
       return view;
    }
}
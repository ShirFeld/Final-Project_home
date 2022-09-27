package com.example.profilesave1.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.profilesave1.Activities.MyAdapter2;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.Models.model;
import com.example.profilesave1.R;
import com.example.profilesave1.Activities.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class recfragment extends Fragment implements AdapterView.OnItemSelectedListener {


    /*
       This fragment displays a list of users.
       There is a filter button that the user can activate on the list.
       When any user is clicked we will sew all his information.

     */
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recview;
    RecyclerView.Adapter mAdapter;
    FirebaseAuth mAuth;
    Button filter,search_btn;
    LinearLayout myfilter;
    static int counter =0;
    public static ArrayList<User> listU;
    ProgressDialog mProgressDialog;
    private ProgressBar progressBar;


    static String text ="";
    static String ageStart ="";
    static String ageFinish ="";
    static String genderItems [] ={"Male","Female","Other"};
    EditText ageStartEditView,ageFinishEditView;


    public recfragment() {

    }
    public static recfragment newInstance(String param1, String param2) {
        recfragment fragment = new recfragment();
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

        View view=inflater.inflate(R.layout.fragment_recfragment, container, false);
        progressBar = view.findViewById(R.id.progreeBar);
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Please wait ");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();

        Spinner spinner = view.findViewById(R.id.spinnerGenderFilter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.Gender, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(this);

        ageStartEditView = view.findViewById(R.id.ageStart);
        ageFinishEditView = view.findViewById(R.id.ageFinish);

        // the values at the beginning
        ageStartEditView.setText("18");
        ageFinishEditView.setText("60");

        myfilter = view.findViewById(R.id.searchBar);
        filter = view.findViewById(R.id.btn_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            // will close and open the filter button
            @Override
            public void onClick(View v) {
                if (counter%2 != 0)
                    myfilter.setVisibility(View.GONE);
                else
                    myfilter.setVisibility(View.VISIBLE);
                counter++;
            }
        });

        recview=(RecyclerView)view.findViewById(R.id.recview); // the list
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        listU = new ArrayList<>();
        search_btn = view.findViewById(R.id.search_btn);


        // gives us all the users except of getCurrentUser()
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                filter.setVisibility(View.VISIBLE);
                recview.setVisibility(View.VISIBLE);


                List<User> list = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String uuid = ds.getKey();
                    if (!mAuth.getCurrentUser().getUid().equals(uuid)){
                        list.add(ds.getValue(User.class));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) != null)
                        listU.add(list.get(i));
                }

                // Here the list is going to change according to what the user will enter
                search_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<User> listU1 = new ArrayList<>();
                        int a,ageStart1,ageFinish1;

                        // users value
                        ageStart = ageStartEditView.getText().toString();       // left side on age filter
                        ageFinish = ageFinishEditView.getText().toString();     // right side on age filter

                        for (int i = 0; i < listU.size(); i++) {
                            if(listU.get(i).getAge().equals(""))
                                a = 0;
                            else
                                a = Integer.parseInt(listU.get(i).getAge());

                            if(ageStart.equals(""))
                                ageStart1 = 0;
                            else
                                ageStart1 = Integer.parseInt(ageStart);

                            if(ageFinish.equals(""))
                                ageFinish1 = 99;
                            else
                                ageFinish1 = Integer.parseInt(ageFinish);

                            if(a >= ageStart1 && a <= ageFinish1 && (listU.get(i).getSex().equals(text) || text.equals("All")))
                                listU1.add(listU.get(i));

                        }
                        // show the list after the user filter
                        recview.setHasFixedSize(true);
                        mAdapter = new MyAdapter2(listU1);
                        recview.setAdapter(mAdapter);
                    }
                });

                recview.setHasFixedSize(true);
                mAdapter = new MyAdapter2(listU);
                recview.setAdapter(mAdapter);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
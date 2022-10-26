package com.example.profilesave1.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilesave1.Activities.MyAdapter2;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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


    static String gender ="";
    static String hobby ="";
    static String ageStart ="";
    static String ageFinish ="";
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

//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(getContext());
//            mProgressDialog.setMessage("Please wait ");
//            mProgressDialog.setIndeterminate(true);
//        }
//        mProgressDialog.show();

        Spinner spinner = view.findViewById(R.id.spinnerGenderFilter);
        Spinner spinnerHobby = view.findViewById(R.id.spinnerHobbyFilter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.Gender, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.Hobby, R.layout.spinner_item);

        adapter1.setDropDownViewResource(R.layout.spinner_drop);
        adapter2.setDropDownViewResource(R.layout.spinner_drop);

        spinner.setAdapter(adapter1);
        spinnerHobby.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(this);
        spinnerHobby.setOnItemSelectedListener(this);


        ageStartEditView = view.findViewById(R.id.ageStart);
        ageFinishEditView = view.findViewById(R.id.ageFinish);

        // the values at the beginning
        ageStartEditView.setText("18");
        ageFinishEditView.setText("60");

        myfilter = view.findViewById(R.id.searchBar);
        filter = view.findViewById(R.id.btn_filter);
        LinearLayout linearLayout = view.findViewById(R.id.filter);

        filter.setOnClickListener(new View.OnClickListener() {
            // will close and open the filter button
            @Override
            public void onClick(View v) {
                closeAndOpen();
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
                linearLayout.setVisibility(View.VISIBLE); // the filter window
                recview.setVisibility(View.VISIBLE);

                List<User> list = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String uuid = ds.getKey();
                    if (mAuth.getCurrentUser() != null){
                        if (!mAuth.getCurrentUser().getUid().equals(uuid)){
                            list.add(ds.getValue(User.class));
                        }
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
                        closeAndOpen();
                        ArrayList<User> listU1 = new ArrayList<>();
                        int a,ageStart1,ageFinish1;  // a = user age

                        // we create a new user to save the indexes for the messages
                        User adminUser = new User("adminUser" , "" , "admin","","","","",
                                "","", "","","","","","");

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

                            if(a >= ageStart1 && a <= ageFinish1  &&
                                    (listU.get(i).getSex().equals(gender) || gender.equals("All")) &&
                                    (listU.get(i).getFavoriteHobby().equals(hobby) || hobby.equals("All"))){
                                System.out.println(" hobby = " + hobby);
                                System.out.println(" gender = " + gender);
                                listU1.add(listU.get(i));
                            }
                            else {
                                listU1.add(adminUser);
                            }
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

    // will close and open the filter button
    public void closeAndOpen (){
        if (counter%2 != 0)
            myfilter.setVisibility(View.GONE);
        else
            myfilter.setVisibility(View.VISIBLE);
        counter++;
    }

    // this func gives us the value that the user is chose
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerGenderFilter)
        {
            gender = parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId() == R.id.spinnerHobbyFilter)
        {
            hobby = parent.getItemAtPosition(position).toString();        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }





}
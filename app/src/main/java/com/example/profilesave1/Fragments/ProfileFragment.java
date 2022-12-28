package com.example.profilesave1.Fragments;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.profilesave1.Activities.PhotoActivity;
import com.example.profilesave1.Models.GPStracker;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    /*
    In this fragment there are details and preferences of the user.
    Here the user will select, write and choose the right answer about himself.
     */
    static String genderItems [] ={"Male","Female","Other"};
    static String animalsItems [] ={"No","Yes","No but love them"};
    static String moviesCategoryItems [] ={"Comedy","Action","Drama","Fantasy","Horror","Mystery"};
    static String statusItems [] ={"Single","Married","Widower","Divorce","Complicated","Other"};
    static String hobbies[] = {"Friends" , "Sport" , "Dancing" , "Fishing" , "Running" ,"Baking" };

    ArrayAdapter<String> adapterGenderItems;
    ArrayAdapter<String> adapterGenderItems_animals;
    ArrayAdapter<String> adapterStatusItems;
    ArrayAdapter<String> adapterMoviesCategoryItems;
    ArrayAdapter<String> adapterHobbiesItems;
    ArrayAdapter<String> none;

    // All these (static) vars are for testing to check if the var is changed
    static String currentName;
    static String currentAnimal;
    static String currentAge;
    static String currentCity;
    static String currentSex;
    static String currentStatus;
    static String currentFavoriteMoviesCategory;
    static String currentChildren;
    static String currentHobbies;
    static String currentAboutMe;

    boolean flag =false;
    // GPS
    static String myCountry;
    static String myStreet;
    Button btn_take_by_gps;
    // if the user changes the data and click the GPS button
    static String userNameTemp;
    static String userAnimalTemp;
    static String userAgeTemp;
    static String userSexTemp;
    static String userStatusTemp;
    static String userFavoriteMoviesCategoryTemp;
    static String userChildrenTemp;
    static String useHobbies;
    static String userAboutMeTemp;


    DatabaseReference reference;
    FirebaseUser user;
    EditText userName, age , city , aboutMe;
    ImageView imageView;
    RadioGroup radioGroup;
    RadioButton r1 , r2;
    AutoCompleteTextView sex;
    AutoCompleteTextView animals;
    AutoCompleteTextView moviesCategory;
    AutoCompleteTextView status;
    AutoCompleteTextView hobbiesAuto;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        // init all the vars from the layout
        imageView = view.findViewById(R.id.image_prof);
        userName = view.findViewById(R.id.prof_name_editText_id);
        age = view.findViewById(R.id.prof_old_editText_id);
        city = view.findViewById(R.id.city_edit);
        sex = view.findViewById(R.id.auto_Complete_TextView);
        animals = view.findViewById(R.id.auto_Complete_TextView_animal);
        hobbiesAuto = view.findViewById(R.id.hobby);
        moviesCategory = view.findViewById(R.id.auto_Complete_TextView_favoriteMoviesCategory);
        status = view.findViewById(R.id.auto_Complete_TextView_status);
        aboutMe = view.findViewById(R.id.aboutMe_editText);
        radioGroup = view.findViewById(R.id.radioGroup);
        r1 = view.findViewById(R.id.yesChildren);
        r2 = view.findViewById(R.id.noChildren);

        //GPS
        btn_take_by_gps = (Button) view.findViewById(R.id.btn_take_by_gps);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 123); // request permissions for location
        btn_take_by_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In these variables we will save what the user has entered
                userNameTemp = userName.getText().toString();
                userAnimalTemp = animals.getText().toString();
                userAgeTemp = age.getText().toString();
                userSexTemp = sex.getText().toString();
                userStatusTemp = status.getText().toString();
                userFavoriteMoviesCategoryTemp = moviesCategory.getText().toString();
                userChildrenTemp = radioGroup.getContext().toString();
                useHobbies = hobbiesAuto.getText().toString();
                userAboutMeTemp = aboutMe.getText().toString();

                GPStracker g = new GPStracker(getContext()); //create a tracker
                Location l = g.getLocation(); // get the coordinates (latitude , longitude)
                if(l != null){
                    double lat = l.getLatitude();  // latitude - קו רוחב
                    double lon = l.getLongitude(); // longitude - קו אורך
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> listAddress = geocoder.getFromLocation(lat,lon,1); // all the data about the location (city , street ...)
                        //System.out.println(listAddress);
                        if(listAddress.size()>0){
                            System.out.println(listAddress.get(0).getCountryName());
                            myCountry = listAddress.get(0).getLocality();
                            myStreet = listAddress.get(0).getThoroughfare();
                            city.setText(myCountry);
                            // change the data in the db
                            String newCity = myCountry;
                            if(!currentCity.equals(newCity))
                                reference.child("city").setValue(newCity);
                            // ih the db the type of those values is String so we convert them from double to String
                            reference.child("latitude").setValue( String.valueOf(lat));
                            reference.child("longitude").setValue( String.valueOf(lon));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(e + " !!!!!!!!!!!!");
                    }
                }
                // setTimeout()
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                userName.setText(userNameTemp);
                                animals.setText(userAnimalTemp);
                                age.setText(userAgeTemp);
                                sex.setText(userSexTemp);
                                status.setText(userStatusTemp);
                                moviesCategory.setText(userFavoriteMoviesCategoryTemp);
                                hobbiesAuto.setText(useHobbies);
                                aboutMe.setText(userAboutMeTemp);

                                if (userChildrenTemp.equals("Yes")){
                                    r1.setChecked(true);
                                    r2.setChecked(false);
                                }
                                else if (userChildrenTemp.equals("No")){
                                    r1.setChecked(false);
                                    r2.setChecked(true);
                                }
                                else if (userChildrenTemp.equals("")){
                                    r1.setChecked(false);
                                    r2.setChecked(false);
                                }

                            }
                        }, 100);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {// show the data in the fields
                if (snapshot.hasChildren() ){
                    User user = snapshot.getValue(User.class);  // gives us all the data from the database that in the user

                    //Glide - url
                    Glide.with(view.getContext()).load(user.getUrl()).into(imageView);

                    // this view , each one of the array will be of this type , the array
                    adapterGenderItems = new ArrayAdapter<String>(view.getContext(),R.layout.drop_down_item,genderItems);
                    adapterGenderItems_animals = new ArrayAdapter<String>(view.getContext(),R.layout.drop_down_item,animalsItems);
                    adapterStatusItems = new ArrayAdapter<String>(view.getContext(),R.layout.drop_down_item,statusItems);
                    adapterMoviesCategoryItems = new ArrayAdapter<String>(view.getContext(),R.layout.drop_down_item,moviesCategoryItems);
                    adapterHobbiesItems = new ArrayAdapter<String>(view.getContext(),R.layout.drop_down_item, hobbies);

                    userName.setText(user.getName());
                    currentName = user.getName();
                    currentAnimal = user.getHaveAnimals();
                    currentAge = user.getAge();
                    currentCity = user.getCity();
                    currentSex = user.getSex();
                    currentStatus = user.getMaritalStatus();
                    currentFavoriteMoviesCategory = user.getFavoriteMoviesCategory();
                    currentChildren = user.getHaveChildren();
                    currentHobbies = user.getFavoriteHobby();
                    currentAboutMe = user.getAboutMe();

                    // Displays the radioGroup on the screen
                    if (user.getHaveChildren().equals("")){
                        r1.setChecked(false);
                        r2.setChecked(false);
                    }
                    else if ( user.getHaveChildren().equals("Yes")){
                        r1.setChecked(true);
                        r2.setChecked(false);
                    }
                    else if (user.getHaveChildren().equals("No")) {
                        r1.setChecked(false);
                        r2.setChecked(true);
                    }

                    city.setText(user.getCity());
                    age.setText(user.getAge());
                    sex.setText(user.getSex());
                    hobbiesAuto.setText(user.getFavoriteHobby());
                    animals.setText(user.getHaveAnimals());
                    moviesCategory.setText(user.getFavoriteMoviesCategory());
                    status.setText(user.getMaritalStatus());
                    aboutMe.setText(user.getAboutMe());
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId){
                                case R.id.yesChildren:
                                    user.setHaveChildren("Yes");
                                    break;

                                case R.id.noChildren:
                                    user.setHaveChildren("No");
                                    break;

                                default:
                                    user.setHaveChildren("");
                                    break;
                            }
                        }
                    });

                    // setAdapter - gives us the opportunity to choose from the options in the [] ={"Male","Female","Other"};
                    sex.setAdapter(adapterGenderItems);
                    animals.setAdapter(adapterGenderItems_animals);
                    moviesCategory.setAdapter(adapterMoviesCategoryItems);
                    status.setAdapter(adapterStatusItems);
                    hobbiesAuto.setAdapter(adapterHobbiesItems);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

// ---------------------------------------------------

        // Update user details function
        Button btnUpdate = (Button) view.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = userName.getText().toString();
                String newAge = age.getText().toString();
                String newCity = city.getText().toString();
                // String newMail = email.getText().toString();
                String newGender = sex.getText().toString();
                String newAnimal = animals.getText().toString();
                String newStatus = status.getText().toString();
                String newMovie = moviesCategory.getText().toString();
                String newHobbies = hobbiesAuto.getText().toString();
                String newAboutMe = aboutMe.getText().toString();


                // if the details were changed do the second line
                if(!currentName.equals(newName) )
                    reference.child("name").setValue(newName);

                if(!currentAge.equals(newAge))
                    reference.child("age").setValue(newAge);

                if(!currentCity.equals(newCity))
                    reference.child("city").setValue(newCity);

                if(!currentSex.equals(newGender))
                    reference.child("sex").setValue(newGender);

                if(!currentAnimal.equals(newAnimal))
                    reference.child("haveAnimals").setValue(newAnimal);

                if(!currentStatus.equals(newStatus))
                    reference.child("maritalStatus").setValue(newStatus);

                if(!currentHobbies.equals(newHobbies))
                    reference.child("favoriteHobby").setValue(newHobbies);

                if(!currentFavoriteMoviesCategory.equals(newMovie))
                    reference.child("favoriteMoviesCategory").setValue(newMovie);

                if(!currentAboutMe.equals(newAboutMe) )
                    reference.child("aboutMe").setValue(newAboutMe);

                if (r1.isChecked()){
                    reference.child("haveChildren").setValue("Yes");
                    flag = true;
                }
                else if(r2.isChecked()){
                    reference.child("haveChildren").setValue("No");
                    flag = true;
                }
                else
                    reference.child("haveChildren").setValue("");

                if(currentName.equals(newName) && currentAge.equals(newAge)  && currentCity.equals(newCity) && currentSex.equals(newGender) && currentAnimal.equals(newAnimal)
                        && currentStatus.equals(newStatus) && currentFavoriteMoviesCategory.equals(newMovie)
                        && currentHobbies.equals(newHobbies) && currentAboutMe.equals(newAboutMe )&& flag == false){
                    Toast.makeText(getActivity(), "Data has not updated", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Data was updated", Toast.LENGTH_SHORT).show();

                if (!currentAnimal.equals(newAnimal))
                    animals.setAdapter(none);

                if (!currentSex.equals(newGender))
                    sex.setAdapter(none);


//                onStop();  --> not have to
            }
        });
        OnclickButtonListener();
        return view;
    }

    // when we press the pic so we will transfer to a new activity to upload the img
    public void OnclickButtonListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PhotoActivity.class);
                startActivity(intent);
            }
        });
    }






}
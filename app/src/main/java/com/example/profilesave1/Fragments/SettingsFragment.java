package com.example.profilesave1.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.profilesave1.Activities.MainActivity;
import com.example.profilesave1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SettingsFragment extends Fragment {

    /*
    This fragment includes 3 buttons : change email , change password and delete the account
     */


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    private ProgressDialog progressDialog;
    FirebaseUser firebaseUser ;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;
    Button delete_button , change_password , change_email , change_phone;
    private static final String TAG = "SettingsFragment";


    public SettingsFragment() {
        // Required empty public constructor
    }
    public static SettingsFragment newInstance(String param1, String param2 ) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        delete_button = (Button)view.findViewById(R.id.delete_button);
        change_password = (Button)view.findViewById(R.id.change_password);
        change_email = view.findViewById(R.id.change_email);
//        change_phone = view.findViewById(R.id.change_phone);


        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetEmail= new EditText(v.getContext());
                final AlertDialog.Builder changeEmailDialog = new AlertDialog.Builder(v.getContext());
                changeEmailDialog.setTitle("Do you want to change your email address ?");
                changeEmailDialog.setMessage("Enter a new valid email address");
                changeEmailDialog.setView(resetEmail);

                changeEmailDialog.setPositiveButton("Send" ,(dialog, which) -> {
                    String newEmail = resetEmail.getText().toString();
                    user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Email address reset successfully , log out and enter again", Toast.LENGTH_SHORT).show();
                            // after changing email the user needs to log out
                            reference.child("email").setValue(resetEmail.getText().toString());
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.wrapper, new LogOutFragment())
                                    .commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(), "Password reset failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "log out and try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                changeEmailDialog.setNegativeButton("close" , (dialog, which) -> {
                    // close
                });
                changeEmailDialog.create().show();

            }

        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                final AlertDialog.Builder changePasswordDialog = new AlertDialog.Builder(v.getContext());
                changePasswordDialog.setTitle("Do you want to change your password ?");
                changePasswordDialog.setMessage("Enter a new password with at least 6 characters long");
                changePasswordDialog.setView(resetPassword);


                changePasswordDialog.setPositiveButton("Yes" ,(dialog, which) -> {
                    String newPassword = resetPassword.getText().toString();
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Password reset successfully , log out and enter again", Toast.LENGTH_SHORT).show();
                            // after changing password the user needs to log out
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.wrapper, new LogOutFragment())
                                    .commit();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(), "Password reset failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                changePasswordDialog.setNegativeButton("close" , (dialog, which) -> {
                    // close
                });

                changePasswordDialog.create().show();
            }
        });

        return view;
    }





    // func to delete account
    private void deleteAccount(){
        final AlertDialog.Builder deleteAccount = new AlertDialog.Builder(getContext());
        deleteAccount.setTitle("Are you sure you want to delete your account? ");

        deleteAccount.setPositiveButton("yes" ,(dialog, which) -> {
            // delete from auth
            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    System.out.println("111111111111111111111");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (progressDialog!= null)
                        progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Log out and try again", Toast.LENGTH_SHORT).show();
                }
            });
            // delete data from real time db
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(firebaseUser.getUid())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getActivity(), "Account deleted successfully from db", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }); deleteAccount.setNegativeButton("no" , (dialog, which) -> {
            // close
        });
        deleteAccount.create().show();

    }

}

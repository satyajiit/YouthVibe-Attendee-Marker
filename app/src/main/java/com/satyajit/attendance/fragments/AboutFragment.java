package com.satyajit.attendance.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.satyajit.attendance.LoginActivity;
import com.satyajit.attendance.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    ImageView profile_image;
    private FirebaseAuth mAuth;
    TextView email_profile, name_uid;
    Button logOut;

    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);


        initUI(v);

        setUpData();

        return v;
    }


    void initUI(View view){

        profile_image = view.findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();
        name_uid  = view.findViewById(R.id.name_uid);
        email_profile = view.findViewById(R.id.email_profile);
        logOut = view.findViewById(R.id.logOut);


    }


    void setUpData(){

        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(profile_image);


        name_uid.setText("UID: "+mAuth.getUid());
        email_profile.setText(mAuth.getCurrentUser().getEmail());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken("227138956975-4mlhr1976h9hmg0ukt13m55coragfi6e.apps.googleusercontent.com")
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);





        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mGoogleSignInClient.signOut();
                mAuth.signOut();
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

            }
        });

    }


}

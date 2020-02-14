package com.satyajit.attendance.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.satyajit.attendance.R;
import com.satyajit.attendance.database.AppDatabase;
import com.satyajit.attendance.database.AppExecutors;
import com.satyajit.attendance.models.DbModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;


public class HomeFragment extends Fragment {


    ImageView profile_image;
    private FirebaseAuth mAuth;
    TextView name_dash;

    ProgressBar progress_circular3, progress_circular2, progress_circular1;

    FloatingActionButton fab_reload;

    TextView tvv3, tvv2, tvv1, tvv4;

    public HomeFragment() {
        // Required empty public constructor
    }

    

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initUI(v);

        setUpData();

        loadStats();

            return v;
    }

    void initUI(View view){

        profile_image = view.findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();
        name_dash  = view.findViewById(R.id.name_dash);
        tvv1 = view.findViewById(R.id.tvv1);
        tvv2 = view.findViewById(R.id.tvv2);
        tvv3 = view.findViewById(R.id.tvv3);
        tvv4 = view.findViewById(R.id.tvv4);
        fab_reload = view.findViewById(R.id.fab_reload);
        progress_circular1 = view.findViewById(R.id.progress_circular1);
        progress_circular2 = view.findViewById(R.id.progress_circular2);
        progress_circular3 = view.findViewById(R.id.progress_circular3);


    }

    void getCount(){


        final AppDatabase mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final int persons = mDb.marksDao().getCount();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            tvv3.setText(String.valueOf(persons));

                        }
                    });
                }
            });


    }


    void setUpData(){

        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(profile_image);


        fab_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadStats();

            }
        });

        name_dash.setText(mAuth.getCurrentUser().getDisplayName());




    }



void loadStats(){


    tvv1.setVisibility(View.GONE);
    progress_circular1.setVisibility(View.VISIBLE);


    tvv2.setVisibility(View.GONE);
    progress_circular2.setVisibility(View.VISIBLE);


    tvv4.setVisibility(View.GONE);
    progress_circular3.setVisibility(View.VISIBLE);

    getCount(); //FROM DEVICE

    final Context context = getActivity();

    RequestQueue queue = Volley.newRequestQueue(context);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, "http://youthvibe.lpu.in/api/stats",
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {


                            try {


                                    tvv1.setText(response.getString("uid"));
                                tvv1.setVisibility(View.VISIBLE);
                                    progress_circular1.setVisibility(View.GONE);


                                tvv2.setText(response.getString("accomodation"));
                                tvv2.setVisibility(View.VISIBLE);
                                progress_circular2.setVisibility(View.GONE);


                                tvv4.setText(response.getString("pending"));
                                tvv4.setVisibility(View.VISIBLE);
                                progress_circular3.setVisibility(View.GONE);


                            }
                            catch (Exception e){

                                Toasty.error(context, "Error Fetching Data!",Toasty.LENGTH_SHORT).show();

                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                    Toasty.error(context, "Error Fetching Data!",Toasty.LENGTH_SHORT).show();


                }
            });


        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);





}




}

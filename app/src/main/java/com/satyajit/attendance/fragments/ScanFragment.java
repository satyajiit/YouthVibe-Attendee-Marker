package com.satyajit.attendance.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.satyajit.attendance.LoginActivity;
import com.satyajit.attendance.R;
import com.satyajit.attendance.adapters.MarksAdapter;
import com.satyajit.attendance.database.AppDatabase;
import com.satyajit.attendance.database.AppExecutors;
import com.satyajit.attendance.models.DbModel;
import com.satyajit.attendance.utils.RandomColor;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class ScanFragment extends Fragment {


    String decryptedContentFromQR;

    FloatingActionButton fab_camera;
    private CodeScanner mCodeScanner;
    RecyclerView markersRecycler;
    private MarksAdapter mAdapter;
    private AppDatabase mDb;

    EditText yv_id;

    String lastYVid = "NA";

    Button submit, mark;

    String room_alloc = "none";

    String solo = "", group = "", workshops = "";

    Boolean isFromScanner = false;

    TextView perTv, acmTv, statusTv, uid, room, workshopsTV, groupTV, soloTV;

    LinearLayout personalDetails, accomLay;

    TextView nameTV, fatherTV, cllgTV, genderTV, dobTV, mobileTV, mailTV, cllgIDTV, yvIdTV, daysTV, durationTV, amountTV;

    ShimmerFrameLayout shimmerLoad, shimmerLoad2;
    private FirebaseAuth mAuth;

    public ScanFragment() {
        // Required empty public constructor
    }


    public static ScanFragment newInstance() {
        return new ScanFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);

        initUI(v);

        setUpListners();

        decryptedContentFromQR = "3812";


        perTv.setVisibility(View.GONE);
        acmTv.setVisibility(View.GONE);
        shimmerLoad.setVisibility(View.GONE);
        shimmerLoad2.setVisibility(View.GONE);
        personalDetails.setVisibility(View.GONE);
        accomLay.setVisibility(View.GONE);

    


        setUpMarkers();


        return v;

    }


    void initUI(View v) {

        Typeface Cav = Typeface.createFromAsset(getActivity().getAssets(), "font/cav.ttf");

        Toasty.Config.getInstance().setToastTypeface(Cav).apply();
        fab_camera = v.findViewById(R.id.fab_camera);
        markersRecycler = v.findViewById(R.id.markersRecycler);
        yv_id = v.findViewById(R.id.yv_id);
        submit = v.findViewById(R.id.submit);

        mark = v.findViewById(R.id.mark);

        mAuth = FirebaseAuth.getInstance();

        shimmerLoad = v.findViewById(R.id.shimmerLoad);
        shimmerLoad2 = v.findViewById(R.id.shimmerLoad2);

        uid = v.findViewById(R.id.uid);

        //Init TextViews
        nameTV = v.findViewById(R.id.nameTV);
        fatherTV = v.findViewById(R.id.fatherTV);
        cllgTV = v.findViewById(R.id.cllgTV);
        genderTV = v.findViewById(R.id.genderTV);
        dobTV = v.findViewById(R.id.dobTV);
        mobileTV = v.findViewById(R.id.mobileTV);
        mailTV = v.findViewById(R.id.mailTV);
        cllgIDTV = v.findViewById(R.id.cllgIDTV);

        personalDetails = v.findViewById(R.id.personalDetails);
        accomLay = v.findViewById(R.id.accomLay);

        yvIdTV = v.findViewById(R.id.yvIdTV);


        daysTV = v.findViewById(R.id.daysTV);
        durationTV = v.findViewById(R.id.durationTV);
        amountTV = v.findViewById(R.id.amountTV);

        perTv = v.findViewById(R.id.perTV);
        acmTv = v.findViewById(R.id.acmTv);

        statusTv = v.findViewById(R.id.statusTv);

        room = v.findViewById(R.id.room);

        workshopsTV = v.findViewById(R.id.workshopsTV);
        soloTV = v.findViewById(R.id.soloTV);
        groupTV = v.findViewById(R.id.groupTV);

    }


    void setUpListners() {


        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lastYVid.equals("NA")) {

                    Toasty.error(getActivity(), "Something went Wrong!", Toasty.LENGTH_SHORT).show();

                } else showDialog();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                decryptedContentFromQR = yv_id.getText().toString();
                loadDataFromHash();

            }
        });

        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                showScanner();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();

            }
        });


        yv_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 4) {

                    submit.setVisibility(View.VISIBLE);


                } else submit.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void sendUpdateRequest() {

        mark.setText("Please Wait....");

        mark.setEnabled(false);

        String url = "http://youthvibe.lpu.in/api/markattendance";

        final Context context = getActivity();

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toasty.info(context, response, Toasty.LENGTH_SHORT).show();

                        mark.setEnabled(true);

                        if (response.contains("marked")) {

                            submit.setVisibility(View.GONE);
                            mark.setVisibility(View.GONE);
                            statusTv.setText("MARKED " + lastYVid + " !");


                        } else {

                            mark.setText("MARK");


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mark.setText("MARK");
                        mark.setEnabled(true);
                        Toasty.error(context, "FAILED CODE 04", Toasty.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid", mAuth.getUid());
                params.put("yvnumber", lastYVid);
                params.put("token", "b123acpxxc");
                params.put("room", room_alloc);

                return params;
            }
        };


        postRequest.setShouldCache(false);
        queue.add(postRequest);


    }


    void addToLocalDB(String id, String name, String hash) {


        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        final DbModel dbModel = new DbModel(name, id, hash);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {


                mDb.marksDao().insertMark(dbModel);

            }

        });


    }

    void showDialog() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);


        dialog.setContentView(R.layout.prompt_msg);

        final TextInputEditText editTextRoom = dialog.findViewById(R.id.editTextRoom);
        Button callBtnPop = dialog.findViewById(R.id.confirm);

        callBtnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                room_alloc = editTextRoom.getText().toString();


                dialog.dismiss();

                sendUpdateRequest();

            }
        });

        dialog.show();


    }

    void loadDataFromHash() {


        genderTV.setText("Gender: NA");
        daysTV.setText("Days Registered: NA");
        amountTV.setText("Amount Paid: NA");
        durationTV.setText("Duration: NA");

        solo = "";
        group = "";
        workshops = "";

        soloTV.setText("Solo Events : NOT REGISTERED");
        groupTV.setText("Group Events : NOT REGISTERED");
        workshopsTV.setText("Workshops : NOT REGISTERED");

        perTv.setVisibility(View.VISIBLE);
        acmTv.setVisibility(View.VISIBLE);
        shimmerLoad.setVisibility(View.VISIBLE);
        shimmerLoad2.setVisibility(View.VISIBLE);
        personalDetails.setVisibility(View.GONE);
        accomLay.setVisibility(View.GONE);


        uid.setVisibility(View.GONE);
        statusTv.setVisibility(View.GONE);
        mark.setVisibility(View.GONE);
        room.setVisibility(View.GONE);

        final Context context = getActivity();

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://youthvibe.lpu.in/api/accomodation/" + decryptedContentFromQR, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {


                                try {


                                    if (isFromScanner) {

                                        addToLocalDB(
                                                response.getString("yvnumber"),
                                                response.getString("name"),
                                                decryptedContentFromQR);

                                        isFromScanner = false;


                                        retrieveTasks();

                                    }

                                    shimmerLoad.setVisibility(View.GONE);
                                    shimmerLoad2.setVisibility(View.GONE);
                                    personalDetails.setVisibility(View.VISIBLE);
                                    accomLay.setVisibility(View.VISIBLE);

                                    nameTV.setText("Name: " + response.getString("name"));
                                    mailTV.setText("Email: " + response.getString("email"));
                                    dobTV.setText("DOB: " + response.getString("DOB"));
                                    fatherTV.setText("Father's Name: " + response.getString("father"));
                                    cllgTV.setText("From: " + response.getString("college"));

                                    lastYVid = response.getString("yvnumber");

                                    yvIdTV.setText("YV ID: " + response.getString("yvnumber"));
                                    mobileTV.setText("Mobile: " + response.getString("contact"));
                                    cllgIDTV.setText("CollegeID: " + response.getString("college_id"));


                                    if (!response.isNull("solo_events"))
                                        for (int i = 0; i < response.getJSONArray("solo_events").length(); i++) {
                                            solo = response.getJSONArray("solo_events").getJSONObject(i).getString("name") + "," + solo;
                                        }


                                    if (!solo.equals(""))
                                        soloTV.setText("Solo Events: " + solo.substring(0, solo.length() - 1));


                                    if (!response.isNull("team_events"))
                                        for (int i = 0; i < response.getJSONArray("team_events").length(); i++) {
                                            group = response.getJSONArray("team_events").getJSONObject(i).getString("name") + "," + group;
                                        }

                                    if (!group.equals(""))
                                        groupTV.setText("Group Events: " + group.substring(0, group.length() - 1));


                                    if (!response.isNull("workshops"))
                                        for (int i = 0; i < response.getJSONArray("workshops").length(); i++) {
                                            workshops = response.getJSONArray("workshops").getJSONObject(i).getString("name") + "," + workshops;
                                        }

                                    if (!workshops.equals(""))
                                        workshopsTV.setText("Workshops: " + workshops.substring(0, workshops.length() - 1));


                                    if (!response.isNull("accomodation")) {

                                        genderTV.setText("Gender: " + response.getJSONObject("accomodation").getString("gender"));
                                        daysTV.setText("Days Registered: " + response.getJSONObject("accomodation").getString("days"));
                                        amountTV.setText("Amount Paid: Rs" + response.getJSONObject("accomodation").getInt("ammount"));
                                        durationTV.setText("Duration: " + response.getJSONObject("accomodation").getString("from") + " - " + response.getJSONObject("accomodation").getString("to"));

                                    }

                                    if (!response.isNull("attendance")) {
                                        statusTv.setVisibility(View.VISIBLE);
                                        uid.setVisibility(View.VISIBLE);
                                        room.setVisibility(View.VISIBLE);
                                        uid.setText(response.getJSONObject("attendance").getString("uid"));
                                        room.setText("Room: " + response.getJSONObject("attendance").getString("room"));
                                    } else {

                                        room.setVisibility(View.GONE);
                                        mark.setVisibility(View.VISIBLE);
                                        uid.setVisibility(View.GONE);
                                        room.setVisibility(View.GONE);

                                    }


                                } catch (Exception e) {

                                    shimmerLoad.setVisibility(View.GONE);
                                    shimmerLoad2.setVisibility(View.GONE);
                                    personalDetails.setVisibility(View.GONE);
                                    accomLay.setVisibility(View.GONE);
                                    perTv.setVisibility(View.GONE);
                                    acmTv.setVisibility(View.GONE);

                                }


                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Toasty.error(context, "Error Fetching Data!", Toasty.LENGTH_SHORT).show();
                        shimmerLoad.setVisibility(View.GONE);
                        shimmerLoad2.setVisibility(View.GONE);
                        personalDetails.setVisibility(View.GONE);
                        accomLay.setVisibility(View.GONE);
                        perTv.setVisibility(View.GONE);
                        acmTv.setVisibility(View.GONE);

                    }
                });


        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);


    }


    void showScanner() {


        isFromScanner = true;

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.camera_pop);


        CodeScannerView scannerView = dialog.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        decryptedContentFromQR = result.getText();

                        if (decryptedContentFromQR.contains("http://youthvibe.lpu.in/qr-code/")) {

                            decryptedContentFromQR = decryptedContentFromQR.replace("http://youthvibe.lpu.in/qr-code/", "");
                            PlayBeep();
                            loadDataFromHash();
                            Toasty.success(getActivity(), "Captured!", Toast.LENGTH_LONG, true).show();
                            dialog.dismiss();


                        } else {

                            mCodeScanner.startPreview();
                            Toasty.error(getActivity(), "Invalid QR", Toasty.LENGTH_SHORT, true).show();

                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveTasks();

        if (mCodeScanner != null)
            mCodeScanner.startPreview();
    }

    void PlayBeep() {

        AssetFileDescriptor afd = null;

        try {

            afd = getActivity().getAssets().openFd("beep.mp3");
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPause() {

        if (mCodeScanner != null)
            mCodeScanner.releaseResources();
        super.onPause();
    }

    void setUpMarkers() {


        markersRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        mAdapter = new MarksAdapter(getActivity(), new MarksAdapter.OnShareClickedListener() {
            @Override
            public void onClick(String value) {


                decryptedContentFromQR = value;


                loadDataFromHash();

            }


        });


        markersRecycler.setAdapter(mAdapter);
        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

    }


    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<DbModel> persons = mDb.marksDao().loadAllMarks();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.setTasks(persons);
                    }
                });
            }
        });


    }

}

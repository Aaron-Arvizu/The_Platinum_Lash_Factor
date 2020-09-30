package com.platinumlashes.lashdemomkiv;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PrevAppt extends AppCompatActivity {
    public Button B1, B2, B3, B4 ,B5;
    public Integer numOfPast;
    public String prevId;
    public View view;
    public RemoteViews remoteViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_appt);
        remoteViews = new RemoteViews(getPackageName(), R.layout.activity_prev_appt);
        B1 = (Button) findViewById(R.id.b1);
        B2 = (Button) findViewById(R.id.b2);
        B3 = (Button) findViewById(R.id.b3);
        B4 = (Button) findViewById(R.id.b4);
        B5 = (Button) findViewById(R.id.b5);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                prevId= null;
            } else {
                prevId= extras.getString("IDPREV");
            }
        } else {
            prevId= (String) savedInstanceState.getSerializable("IDPREV");
        }
        view = (ConstraintLayout) findViewById(R.id.cl);

        getNumofPrev();


    }
    public void getNumofPrev(){
        final StorageReference checkSRef = FirebaseStorage.getInstance().getReference().child("00Previous Appointments").child(prevId);
        final StorageReference prevaS1 = checkSRef.child("preva1");
        final StorageReference prevaS2 = checkSRef.child("preva2");
        final StorageReference prevaS3 = checkSRef.child("preva3");
        final StorageReference prevaS4 = checkSRef.child("preva4");
        final StorageReference prevaS5 = checkSRef.child("preva5");
        final DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(prevId);
        final DatabaseReference preva1Ref = checkRef.child("preva1");
        final DatabaseReference preva2Ref = checkRef.child("preva2");
        final DatabaseReference preva3Ref = checkRef.child("preva3");
        final DatabaseReference preva4Ref = checkRef.child("preva4");
        final DatabaseReference preva5Ref = checkRef.child("preva5");
        checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("preva5")){
                    preva5Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String date5 = dataSnapshot.getValue(String.class);
                            //SET B1 as the only available
                            B1.setText(date5);
                            //get the B1 Uri
                            prevaS5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri B5Uri = uri;
                                    B1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Click to enlarge the one image
                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(B5Uri).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });
                                }
                            });

                            preva4Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date4 = dataSnapshot.getValue(String.class);
                                    B2.setText(date4);
                                    prevaS4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B4Uri = uri;
                                            B2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Click to enlarge the one image
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B4Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            preva3Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date3 = dataSnapshot.getValue(String.class);
                                    B3.setText(date3);
                                    prevaS3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B3Uri = uri;
                                            B3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B3Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            preva2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date2 = dataSnapshot.getValue(String.class);
                                    B4.setText(date2);
                                    prevaS2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B2Uri = uri;
                                            B4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B2Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            preva1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date1 = dataSnapshot.getValue(String.class);
                                    B5.setText(date1);
                                    prevaS1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B1Uri = uri;
                                            B5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B1Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if (dataSnapshot.hasChild("preva4")){
                    preva4Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String date4 = dataSnapshot.getValue(String.class);
                            //SET B1 as the only available
                            B1.setText(date4);
                            //get the B1 Uri
                            prevaS4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri B4Uri = uri;
                                    B1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Click to enlarge the one image
                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(B4Uri).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });
                                }
                            });

                            preva3Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date3 = dataSnapshot.getValue(String.class);
                                    B2.setText(date3);
                                    prevaS3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B3Uri = uri;
                                            B2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Click to enlarge the one image
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B3Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            preva2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date2 = dataSnapshot.getValue(String.class);
                                    B3.setText(date2);
                                    prevaS2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B2Uri = uri;
                                            B3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B2Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            preva1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date1 = dataSnapshot.getValue(String.class);
                                    B4.setText(date1);
                                    prevaS1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B1Uri = uri;
                                            B4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B1Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            B5.setText("");
                            B5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if (dataSnapshot.hasChild("preva3")){
                    preva3Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String date3 = dataSnapshot.getValue(String.class);
                            //SET B1 as the only available
                            B1.setText(date3);
                            //get the B1 Uri
                            prevaS3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri B3Uri = uri;
                                    B1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Click to enlarge the one image
                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(B3Uri).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });
                                }
                            });

                            preva2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date2 = dataSnapshot.getValue(String.class);
                                    B2.setText(date2);
                                    prevaS2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B2Uri = uri;
                                            B2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Click to enlarge the one image
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B2Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            preva1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date1 = dataSnapshot.getValue(String.class);
                                    B3.setText(date1);
                                    prevaS1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B1Uri = uri;
                                            B3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B1Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            B4.setText("");
                            B4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            B5.setText("");
                            B5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if (dataSnapshot.hasChild("preva2")){
                    preva2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String date2 = dataSnapshot.getValue(String.class);
                            //SET B1 as the only available
                            B1.setText(date2);
                            //get the B1 Uri
                            prevaS2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri B2Uri = uri;
                                    B1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Click to enlarge the one image
                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(B2Uri).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });
                                }
                            });

                            preva1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String date1 = dataSnapshot.getValue(String.class);
                                    B2.setText(date1);
                                    prevaS1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri B1Uri = uri;
                                            B2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Click to enlarge the one image
                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(B1Uri).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            B3.setText("");
                            B3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            B4.setText("");
                            B4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            B5.setText("");
                            B5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if (dataSnapshot.hasChild("preva1")){
                    preva1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String date1 = dataSnapshot.getValue(String.class);
                            //SET B1 as the only available
                            B1.setText(date1);
                            //get the B1 Uri
                            prevaS1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri B1Uri = uri;
                                    B1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Click to enlarge the one image
                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PrevAppt.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(B1Uri).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });
                                }
                            });

                            B2.setText("");
                            B2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            B3.setText("");
                            B3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            B4.setText("");
                            B4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            B5.setText("");
                            B5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    Log.d("test","no preva children");
                    final AlertDialog.Builder details = new AlertDialog.Builder(PrevAppt.this);
                    details.setTitle("NO PREVIOUS APPOINTMENTS ON RECORD");
                    details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    details.show();
                    B1.setText("");
                    B1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    B2.setText("");
                    B2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    B3.setText("");
                    B3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    B4.setText("");
                    B4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    B5.setText("");
                    B5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



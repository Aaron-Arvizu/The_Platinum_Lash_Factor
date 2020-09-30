package com.platinumlashes.lashdemomkiv;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;


public class TClientDetailActivity extends AppCompatActivity implements Serializable {
    TextView ArtistNameBox;
    public Button appointmentbutton, LashReleaseDetails;
    Context context;
    Integer counter;
    public Intent passIntent;
    DatabaseReference artRef;
    TextView stPushBox, endTimeBox, clientNotesBox;
    Intent intent = getIntent();
    DatabaseReference pushref, timeRef, endTimeRef, notesRef, eyePadsRef, glueRef, apptRef, initImageRef, recentPhoto, LocationRef, AllergyRef;
    public String pushID, initURL2, photoONEuri, photoTWOuri, photoTHREEuri, photoFOURuri,noteAddition,ADD,ZIP,FULLADD,ANS1,ANS2,ANS3,ANS4,ANS5,BDAY,REFERENCE2;
    TextView eyePadsBox, glueBox, apptBox, AllergyBox, LocationBox;
    ImageView mostRecent;
    Dialog galleryDialog;
    public Uri mostRecentpulled;
    public EditText editNotesBox;
    public Integer extrasCount = 1;
    StorageReference recentRef, initRef;
    FirebaseStorage initStorage;
    public TextView  devDisplay,diagramOnClick, photoOnClick;
    public Integer devCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passIntent = new Intent(TClientDetailActivity.this,QuestionaireForReturning.class);
        setContentView(R.layout.activity_client_detail);
        appointmentbutton = findViewById(R.id.apptButton);
        LashReleaseDetails = (Button) findViewById(R.id.rdButton);
        LashReleaseDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReleaseDetails();
            }
        });
        FirebaseAuth.AuthStateListener authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("test","Someone is logged in");

                } else {
                    // User is signed out
                    Log.d("test","no login, required PIN");
                }
                // ...
            }

        };
        FirebaseAuth.getInstance().addAuthStateListener(authlistener);
        counter = 0;
        appointmentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartAppt();
            }
        });
        getIncomingIntent();
        photoOnClick = (TextView) findViewById(R.id.photoOnClick);
       diagramOnClick = (TextView) findViewById(R.id.diagramWindowOnClick);
        Bundle extras = getIntent().getExtras();
        pushID = extras.getString("ID");
        Intent QuestionaireIntent2= new Intent(TClientDetailActivity.this,QuestionaireForReturning.class);
        onNewIntent(QuestionaireIntent2);
       // QuestionaireIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        passIntent.putExtra("Please",pushID);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public void onBackPressed(){
        Intent returnIntent = new Intent(TClientDetailActivity.this,ClientList.class);
        startActivity(returnIntent);
    }
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, Appointments.class);
        Log.d("test","Extra put");
        endTimeBox = (TextView) findViewById(R.id.Endtimedisplay);
        clientNotesBox = (TextView) findViewById(R.id.clientNotesPushed);
        glueBox = (TextView) findViewById(R.id.glueBox);
        ArtistNameBox = (TextView) findViewById(R.id.lashArtistPushed);
        stPushBox = (TextView) findViewById(R.id.stPushed);
        eyePadsBox = (TextView) findViewById(R.id.eyePadsBox);
        apptBox = (TextView) findViewById(R.id.apptTypePushed);
        mostRecent = (ImageView) findViewById(R.id.recentPhoto);
        AllergyBox = (TextView) findViewById(R.id.allergyPushed);
        LocationBox = (TextView) findViewById(R.id.locationBox);
        devDisplay = (TextView) findViewById(R.id.devDisplayBtn);
        LocationRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Location");
        devDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devCounter();
            }
        });
        photoOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items= {"View Most Recent","View Image Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(TClientDetailActivity.this);
                builder.setTitle("View Image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(items[i].equals("View Most Recent")) {
                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                            LayoutInflater inflater2 = getLayoutInflater();
                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                            dialogBuilder2.setView(dialogView2);
                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                            DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
                            checkRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("Photo4")){
                                        final StorageReference dia4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo4");
                                        dia4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo4 = uri.toString();
                                                Picasso.get().load(photo4).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if(dataSnapshot.hasChild("Photo3")){
                                        final StorageReference dia3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo3");
                                        dia3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo3 = uri.toString();
                                                Picasso.get().load(photo3).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if (dataSnapshot.hasChild("Photo2")){
                                        final StorageReference dia2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo2");
                                        dia2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo2 = uri.toString();
                                                Picasso.get().load(photo2).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if (dataSnapshot.hasChild("Photo1")){
                                        final StorageReference dia1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo1");
                                        dia1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo1 = uri.toString();
                                                Picasso.get().load(photo1).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else {
                                        final StorageReference dia0Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("mostRecentPic");
                                        dia0Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String mostRecent = uri.toString();
                                                Picasso.get().load(mostRecent).into(expandedPicture);
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            AlertDialog alertDialog2 = dialogBuilder2.create();
                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                            dialogBuilder2.show();
                        }
                        if(items[i].equals("View Image Gallery")){
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TClientDetailActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.alertdialog, null);
                            dialogBuilder.setView(dialogView);

                            final ImageView mostRecentIV = (ImageView) dialogView.findViewById(R.id.mostRecent);
                            final ImageView photo1IV = (ImageView) dialogView.findViewById(R.id.Photo1);
                            final ImageView photo2IV = (ImageView) dialogView.findViewById(R.id.Photo2);
                            final ImageView photo3IV = (ImageView) dialogView.findViewById(R.id.Photo3);
                            final ImageView photo4IV = (ImageView) dialogView.findViewById(R.id.Photo4);
                            final TextView  mostRecentN = (TextView) dialogView.findViewById(R.id.mostRecentName);
                            final TextView  Photo1N = (TextView) dialogView.findViewById(R.id.Photo1Name);
                            final TextView  Photo2N = (TextView) dialogView.findViewById(R.id.Photo2Name);
                            final TextView  Photo3N = (TextView) dialogView.findViewById(R.id.Photo3Name);
                            final TextView  Photo4N = (TextView) dialogView.findViewById(R.id.Photo4Name);


                            //GET PICTURE VIA STORAGE REFERENCE
                            //NESTED INSIDE OF THAT IS NAME FROM DATABASE
                            final StorageReference mostRecentSRef;
                            mostRecentSRef = FirebaseStorage.getInstance().getReference().child(pushID).child("mostRecentPic");
                            mostRecentSRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("mostRecentName");
                                    picnameref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String mostRecentname = dataSnapshot.getValue(String.class);
                                            mostRecentN.setText(mostRecentname);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    initURL2 = uri.toString();
                                    Picasso.get().load(initURL2).into(mostRecentIV);
                                    mostRecentIV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(initURL2).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });

                                }
                            });
                            final StorageReference photo1SRef;
                            photo1SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo1");
                            photo1SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo1Name");
                                    picnameref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Photo1Name = dataSnapshot.getValue(String.class);
                                            Log.d("test",Photo1Name);
                                            Photo1N.setText(Photo1Name);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    photoONEuri = uri.toString();
                                    Picasso.get().load(photoONEuri).into(photo1IV);
                                    photo1IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder dialogBuilder3 = new AlertDialog.Builder(TClientDetailActivity.this);
                                            LayoutInflater inflater3 = getLayoutInflater();
                                            View dialogView3 = inflater3.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder3.setView(dialogView3);
                                            final ImageView expandedPicture = (ImageView) dialogView3.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(photoONEuri).into(expandedPicture);
                                            AlertDialog alertDialog3 = dialogBuilder3.create();
                                            alertDialog3.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder3.show();
                                        }
                                    });

                                }
                            });
                            final StorageReference photo2SRef;
                            photo2SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo2");
                            photo2SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo2Name");
                                    picnameref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Photo2Name = dataSnapshot.getValue(String.class);
                                            Photo2N.setText(Photo2Name);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    photoTWOuri = uri.toString();
                                    Picasso.get().load(photoTWOuri).into(photo2IV);
                                    photo2IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder dialogBuilder4 = new AlertDialog.Builder(TClientDetailActivity.this);
                                            LayoutInflater inflater4 = getLayoutInflater();
                                            View dialogView4 = inflater4.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder4.setView(dialogView4);
                                            final ImageView expandedPicture = (ImageView) dialogView4.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(photoTWOuri).into(expandedPicture);
                                            AlertDialog alertDialog4 = dialogBuilder4.create();
                                            alertDialog4.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder4.show();
                                        }
                                    });

                                }
                            });
                            final StorageReference photo3SRef;
                            photo3SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo3");
                            photo3SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo3Name");
                                    picnameref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Photo3Name = dataSnapshot.getValue(String.class);
                                            Log.d("test",Photo3Name);
                                            Photo3N.setText(Photo3Name);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    photoTHREEuri = uri.toString();
                                    Picasso.get().load(photoTHREEuri).into(photo3IV);
                                    photo3IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder dialogBuilder6 = new AlertDialog.Builder(TClientDetailActivity.this);
                                            LayoutInflater inflater6 = getLayoutInflater();
                                            View dialogView6 = inflater6.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder6.setView(dialogView6);
                                            final ImageView expandedPicture = (ImageView) dialogView6.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(photoTHREEuri).into(expandedPicture);
                                            AlertDialog alertDialog6 = dialogBuilder6.create();
                                            alertDialog6.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder6.show();
                                        }
                                    });


                                }
                            });
                            final StorageReference photo4SRef;
                            photo4SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo4");
                            photo4SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo4Name");
                                    picnameref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Photo4Name = dataSnapshot.getValue(String.class);
                                            Photo4N.setText(Photo4Name);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    photoFOURuri = uri.toString();
                                    Picasso.get().load(photoFOURuri).into(photo4IV);
                                    photo4IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder dialogBuilder5 = new AlertDialog.Builder(TClientDetailActivity.this);
                                            LayoutInflater inflater5 = getLayoutInflater();
                                            View dialogView5 = inflater5.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder5.setView(dialogView5);
                                            final ImageView expandedPicture = (ImageView) dialogView5.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(photoFOURuri).into(expandedPicture);
                                            AlertDialog alertDialog5 = dialogBuilder5.create();
                                            alertDialog5.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder5.show();
                                        }
                                    });


                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
                            alertDialog.show();
                        }
                        if(items[i].equals("Cancel")){

                        }

                    }

                });
                builder.show();

            /*
            THIS ONE WORKS FOR SINGLE IMAGES!!! GOOD TO STUDY
                final AlertDialog.Builder ImageDialog = new AlertDialog.Builder(ClientDetailActivity.this);
                ImageDialog.setTitle("Title");
                final ImageView showImage = new ImageView(ClientDetailActivity.this);
                final StorageReference photo1SRef;
                photo1SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo1");
                photo1SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoONEuri = uri.toString();
                        Picasso.get().load(photoONEuri).into(showImage);
                    }
                });
                ImageDialog.setView(showImage);
                ImageDialog.setPositiveButton("See Previous", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final StorageReference photo1SRef;
                        photo1SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo1");
                        photo1SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                initURL2 = uri.toString();
                                Picasso.get().load(initURL2).into(showImage);
                                ImageDialog.setView(showImage);

                            }
                        });
                    }
                });
                ImageDialog.show();
*/

            }
        });
        DatabaseReference windowCompare = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
        final StorageReference diagram0Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram0");
        final StorageReference diagram1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram1");
        final StorageReference diagram2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram2");
        final StorageReference diagram3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram3");
        final StorageReference diagram4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram4");
        final ImageView diagramWindow = (ImageView) findViewById(R.id.recentDiagram);
        windowCompare.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Diagram4")){
                    diagram4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramFour = uri.toString();
                            Picasso.get().load(DiagramFour).into(diagramWindow);
                        }
                    });
                }
                else if(dataSnapshot.hasChild("Diagram3")){
                    diagram3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramThree = uri.toString();
                            Picasso.get().load(DiagramThree).into(diagramWindow);
                        }
                    });
                }
                else if(dataSnapshot.hasChild("Diagram2")){
                    diagram2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramTwo = uri.toString();
                            Picasso.get().load(DiagramTwo).into(diagramWindow);

                        }
                    });
                }
                else if(dataSnapshot.hasChild("Diagram1")){
                    diagram1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramOne = uri.toString();
                            Picasso.get().load(DiagramOne).into(diagramWindow);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        clientNotesBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotesDialog();
            }
        });
        diagramOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items= {"View Most Recent","View Diagram Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(TClientDetailActivity.this);
                builder.setTitle("View Diagram");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (items[i].equals("View Most Recent")) {
                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                            LayoutInflater inflater2 = getLayoutInflater();
                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                            dialogBuilder2.setView(dialogView2);
                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                            DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
                            checkRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("Diagram4")){
                                        final StorageReference dia4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram4");
                                        dia4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram4 = uri.toString();
                                                Picasso.get().load(Diagram4).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if(dataSnapshot.hasChild("Diagram3")){
                                        final StorageReference dia3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram3");
                                        dia3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram3 = uri.toString();
                                                Picasso.get().load(Diagram3).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if (dataSnapshot.hasChild("Diagram2")){
                                        final StorageReference dia2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram2");
                                        dia2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram2 = uri.toString();
                                                Picasso.get().load(Diagram2).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if (dataSnapshot.hasChild("Diagram1")){
                                        final StorageReference dia1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram1");
                                        dia1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram1 = uri.toString();
                                                Picasso.get().load(Diagram1).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else {
                                        final StorageReference dia0Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram0");
                                        dia0Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram0 = uri.toString();
                                                Picasso.get().load(Diagram0).into(expandedPicture);
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            AlertDialog alertDialog2 = dialogBuilder2.create();
                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                            dialogBuilder2.show();

                        }
                        else if (items[i].equals("View Diagram Gallery")){
                            AlertDialog.Builder diagramDialogBuilder = new AlertDialog.Builder(TClientDetailActivity.this);
                            LayoutInflater diagramInflater = getLayoutInflater();
                            View dialogView = diagramInflater.inflate(R.layout.diagramdialog, null);
                            diagramDialogBuilder.setView(dialogView);

                            final ImageView diagram0IV = (ImageView) dialogView.findViewById(R.id.diagram0);
                            final ImageView diagram1IV = (ImageView) dialogView.findViewById(R.id.diagram1);
                            final ImageView diagram2IV = (ImageView) dialogView.findViewById(R.id.diagram2);
                            final ImageView diagram3IV = (ImageView) dialogView.findViewById(R.id.diagram3);
                            final ImageView diagram4IV = (ImageView) dialogView.findViewById(R.id.diagram4);
                            final TextView  diagramname0 = (TextView) dialogView.findViewById(R.id.diagramName0);
                            final TextView  diagramname1 = (TextView) dialogView.findViewById(R.id.diagramName1);
                            final TextView  diagramname2 = (TextView) dialogView.findViewById(R.id.diagramName2);
                            final TextView  diagramname3 = (TextView) dialogView.findViewById(R.id.diagramName3);
                            final TextView  diagramname4 = (TextView) dialogView.findViewById(R.id.diagramName4);
                            final StorageReference diagram0Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram0");
                            final StorageReference diagram1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram1");
                            final StorageReference diagram2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram2");
                            final StorageReference diagram3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram3");
                            final StorageReference diagram4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram4");
                            diagram1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo1Name");
                                    picnameref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String diagram1name = dataSnapshot.getValue(String.class);
                                            diagramname1.setText(diagram1name);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    final String diagram11 = uri.toString();
                                    Picasso.get().load(diagram11).into(diagram1IV);
                                    diagram1IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                                            LayoutInflater inflater2 = getLayoutInflater();
                                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                            dialogBuilder2.setView(dialogView2);
                                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                            Picasso.get().load(diagram11).into(expandedPicture);
                                            AlertDialog alertDialog2 = dialogBuilder2.create();
                                            alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                            dialogBuilder2.show();
                                        }
                                    });
                                    diagram2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo2Name");
                                            picnameref2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String diagram2name = dataSnapshot.getValue(String.class);
                                                    diagramname2.setText(diagram2name);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                            final String diagram22 = uri.toString();
                                            Picasso.get().load(diagram22).into(diagram2IV);
                                            diagram2IV.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(diagram22).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                    diagram3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref3 = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo3Name");
                                            picnameref3.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String diagram3name = dataSnapshot.getValue(String.class);
                                                    diagramname3.setText(diagram3name);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                            final String diagram33 = uri.toString();
                                            Picasso.get().load(diagram33).into(diagram3IV);
                                            diagram3IV.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(diagram33).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                    diagram4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref4 = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Photo4Name");
                                            picnameref4.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String diagram4name = dataSnapshot.getValue(String.class);
                                                    diagramname4.setText(diagram4name);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                            final String diagram44 = uri.toString();
                                            Picasso.get().load(diagram44).into(diagram2IV);
                                            diagram4IV.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(diagram44).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            AlertDialog alertDialog = diagramDialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
                            alertDialog.show();
                        }
                        else if (items[i].equals("Cancel")) {

                        }
                    }
                });
                builder.show();
            }
        });
        LocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String LocationFinal = dataSnapshot.getValue(String.class);
                LocationBox.setText(LocationFinal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        AllergyRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Allergy");
        AllergyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String finalAllergy = dataSnapshot.getValue(String.class);
                AllergyBox.setText(finalAllergy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initImageRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("mostRecentURI");
        initImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String initURI = dataSnapshot.getValue(String.class);
                initURL2 = initURI;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        apptRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("ApptType");
        apptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String apptPost = dataSnapshot.getValue(String.class);
                apptBox.setText(apptPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        glueRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Glue");
        glueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gluePost = dataSnapshot.getValue(String.class);
                glueBox.setText(gluePost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        eyePadsRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("EyePads");
        eyePadsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String eyePadsPost = dataSnapshot.getValue(String.class);
                eyePadsBox.setText(eyePadsPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        notesRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Notes");
        notesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String notespost = dataSnapshot.getValue(String.class);
                clientNotesBox.setText(notespost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        endTimeRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("EndTime");
        endTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String endPost = dataSnapshot.getValue(String.class);
                endTimeBox.setText(endPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        timeRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("StartTime");
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String startPost = dataSnapshot.getValue(String.class);
                stPushBox.setText(startPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        artRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Artist");
        artRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String artistnamepost = dataSnapshot.getValue(String.class);
                ArtistNameBox.setText(artistnamepost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
        CheckPhotos();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("first")) {
            String firstnametest = getIntent().getStringExtra("first");
            String midnametest = getIntent().getStringExtra("mid");
            String lastnametest = getIntent().getStringExtra("last");
            String Idpushstart = getIntent().getStringExtra("ID");
            setDetails(firstnametest, lastnametest, midnametest);
        }
    }


    private void setDetails(String firsname, String lasname, String midname) {

        TextView fname3 = findViewById(R.id.fName2);
        TextView mname2 = findViewById(R.id.mName2);
        TextView lname2 = findViewById(R.id.lName2);
        fname3.setText(firsname);
        mname2.setText(midname);
        lname2.setText(lasname);


    }

    public void openStartAppt() {
        Intent intent = new Intent(this, Appointments.class);
        intent.putExtra("ID2", pushID);
        if (getIntent().hasExtra("ID2")) {
            Log.d("test", "itsherebud");
        }
        startActivity(intent);
    }

    public void GoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void CheckPhotos() {

        recentPhoto = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("mostRecentURI");

        final DatabaseReference photo1Ref, photo2Ref;
        photo1Ref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
        photo1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Photo4")) {
                    final StorageReference photo4SRef;
                    photo4SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo4");
                    photo4SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoFOURuri = uri.toString();
                            Picasso.get().load(photoFOURuri).into(mostRecent);
                        }
                    });
                } else if (dataSnapshot.hasChild("Photo3")) {
                    final StorageReference photo3SRef;
                    photo3SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo3");
                    photo3SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoTHREEuri = uri.toString();
                            Picasso.get().load(photoTHREEuri).into(mostRecent);
                        }
                    });
                } else if (dataSnapshot.hasChild("Photo2")) {
                    final StorageReference photo2SRef;
                    photo2SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo2");
                    photo2SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoTWOuri = uri.toString();
                            Picasso.get().load(photoTWOuri).into(mostRecent);
                        }
                    });
                }
                // PHOTO ONE SEPARATION
                else if (dataSnapshot.hasChild("Photo1")) {
                    final StorageReference photo1SRef;
                    photo1SRef = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo1");
                    photo1SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoONEuri = uri.toString();
                            Picasso.get().load(photoONEuri).into(mostRecent);
                        }
                    });
                } else {
                    //DISPLAY INITIAL IMAGE
                    recentRef = FirebaseStorage.getInstance().getReference().child(pushID).child("mostRecentPic");
                    recentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("test", "found the initial image");
                            String initImage = uri.toString();
                            Picasso.get().load(initImage).into(mostRecent);
                        }

                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void editNotesDialog() {
        final CharSequence[] items = {"Edit notes", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TClientDetailActivity.this);
        builder.setTitle("Add a Comment?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Edit notes")) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(TClientDetailActivity.this);
                    LayoutInflater notesinflater = getLayoutInflater();
                    final AlertDialog addNoteDia = builder2.create();
                    View dialogViewnotes =notesinflater.inflate(R.layout.notedialog,null);
                    editNotesBox = (EditText) dialogViewnotes.findViewById(R.id.clientNotesEdited);
                    Button saveadditonButton = (Button) dialogViewnotes.findViewById(R.id.addNoteBtn);
                    addNoteDia.setView(dialogViewnotes);
                    saveadditonButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String noteToAdd = editNotesBox.getText().toString() + " - [Added Post-Appt]";
                            final DatabaseReference pullNotes = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Notes");
                            pullNotes.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String pulledNotes = dataSnapshot.getValue(String.class);
                                    String combonotes = pulledNotes + "\n" + noteToAdd ;
                                    pullNotes.setValue(combonotes);
                                    clientNotesBox.setText(combonotes);
                                    addNoteDia.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                   addNoteDia.show();

                }  else if (items[i].equals("cancel")) {
                    Reset();
                }
            }
        });
        builder.show();
    }
    public void Reset(){
        Intent intent = new Intent(this, TClientDetailActivity.class);
        startActivity(intent);
    }
    public void devCounter() {
        if(devCount == 0) {
            devCount = devCount + 1;
        }
        else if(devCount == 1){
            devCount = devCount +1;
        }
        else if(devCount ==2){
            devCount = devCount +1;
        }
        else
        {
            final CharSequence[] items= {pushID,"close"};
            AlertDialog.Builder builder = new AlertDialog.Builder(TClientDetailActivity.this);
            builder.setTitle("Client ID");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (items[i].equals(pushID)) {


                    } else if (items[i].equals("close")) {
                    }
                }
            });
            builder.show();
        }
    }
    public void ReleaseDetails(){

       final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
       base.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild("zip")){
                   final DatabaseReference ZipRef = base.child("zip");
                   final DatabaseReference AddRef = base.child("Address");
                   final DatabaseReference Ans1Ref = base.child("Ans1");
                   final DatabaseReference Ans2Ref = base.child("Ans2");
                   final DatabaseReference Ans3Ref = base.child("Ans3");
                   final DatabaseReference Ans4Ref = base.child("Ans4");
                   final DatabaseReference Ans5Ref = base.child("Ans5");
                   final DatabaseReference refRef = base.child("HDYHAU");
                   final  DatabaseReference BDayRef = base.child("Birthday");
                   ZipRef.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           ZIP = dataSnapshot.getValue(String.class);
                           AddRef.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   ADD = dataSnapshot.getValue(String.class);
                                   FULLADD = ADD + " "+ZIP;
                                   BDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           BDAY = dataSnapshot.getValue(String.class);
                                           Ans1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   ANS1 = dataSnapshot.getValue(String.class);
                                                   Ans2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                           ANS2 = dataSnapshot.getValue(String.class);
                                                           Ans3Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                   ANS3 = dataSnapshot.getValue(String.class);
                                                                   Ans4Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                           ANS4 = dataSnapshot.getValue(String.class);
                                                                           Ans5Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                               @Override
                                                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                   ANS5 = dataSnapshot.getValue(String.class);
                                                                                   refRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                       @Override
                                                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                           REFERENCE2 = dataSnapshot.getValue(String.class);
                                                                                           AlertDialog.Builder details = new AlertDialog.Builder(TClientDetailActivity.this);
                                                                                           details.setTitle("Lash Release Details");
                                                                                           LinearLayout layout = new LinearLayout(details.getContext());
                                                                                           final TextView address = new TextView(details.getContext());
                                                                                           final TextView bday = new TextView(details.getContext());
                                                                                           final TextView hdyhau = new TextView(details.getContext());
                                                                                           final TextView a1 = new TextView(details.getContext());
                                                                                           final TextView a2 = new TextView(details.getContext());
                                                                                           final TextView a3 = new TextView(details.getContext());
                                                                                           final TextView a4 = new TextView(details.getContext());
                                                                                           final TextView a5 = new TextView(details.getContext());
                                                                                           LinearLayout.LayoutParams padd = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                                   LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                                           address.setText("\n\n" +"  Address: "+FULLADD + "\n\n" + "  Birthday: "+BDAY+ "\n\n"+"  Reffered by: "+REFERENCE2+"\n\n" +"  "+ ANS1 +"\n\n"
                                                                                                   + "  "+ANS2 + "\n\n"+ "  "+ANS3 + "\n\n" + "  "+ ANS4 + "\n\n" +"  "+ANS5);
                                                                                           address.setTextSize(16);

                                                                                           details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                               @Override
                                                                                               public void onClick(DialogInterface dialog, int which) {
                                                                                                   Log.d("test","yay");
                                                                                               }
                                                                                           });
                                                                                           details.setView(address);
                                                                                           details.show();




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

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }
               else
               {
                   AlertDialog.Builder addtoExisting = new AlertDialog.Builder(TClientDetailActivity.this);
                   addtoExisting.setTitle("Would you like to add a Lash Release for this client?");
                   addtoExisting.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           startActivity(passIntent );
                       }
                   });
                   addtoExisting.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           Log.d("test","Okay then");
                       }
                   });
                   addtoExisting.show();
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



    }
    }



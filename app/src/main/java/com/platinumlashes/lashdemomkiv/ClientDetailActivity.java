package com.platinumlashes.lashdemomkiv;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ClientDetailActivity extends AppCompatActivity implements Serializable {
    public TextView ArtistNameBox,stPushBox, endTimeBox, clientNotesBox,eyePadsBox, glueBox, apptBox, AllergyBox, LocationBox,devDisplay, diagramOnClick, photoOnClick;
    public Button appointmentbutton, LashReleaseDetails,EditButton;
    public Integer counter;
    public Integer devCount = 0;
    public Integer extrasCount = 1;
    public Intent passIntent,sendIDtoCovid;
    public Intent intent = getIntent();
    public DatabaseReference pushref,COVIDREF, timeRef,CovidDateSave,COVIDREF2, endTimeRef, notesRef, eyePadsRef, glueRef, apptRef, initImageRef, recentPhoto, LocationRef, AllergyRef,artRef,AdminRef,windowCompare,fNameRef,mNameRef,lNameRef, CovidDateRefs;
    public String pushID, initURL2, photoONEuri, Today,photoTWOuri, photoTHREEuri, photoFOURuri, noteAddition, ADD, ZIP, FULLADD, ANS1, ANS2, ANS3, ANS4, ANS5, BDAY,REFERENCE2,userName,userEmailCap,pr1,pr2,pr3,pr4,pr5,Emailtxt,endPost,endTimeVal,currentDate;
    public ImageView mostRecent,diagramWindow;
    public Dialog galleryDialog;
    public Uri mostRecentpulled;
    public EditText editNotesBox;
    public StorageReference recentRef,diagram0Ref,diagram1Ref,diagram2Ref,diagram3Ref,diagram4Ref;
    public Bitmap wholePage;
    public byte[] WholePage,prev1,prev2,prev3,prev4,prev5;
    public Button prevButton;
    public Date todayDate;
    SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);

        //Definitions
        appointmentbutton = findViewById(R.id.apptButton);
        LashReleaseDetails = (Button) findViewById(R.id.rdButton);
        photoOnClick = (TextView) findViewById(R.id.photoOnClick);
        diagramOnClick = (TextView) findViewById(R.id.diagramWindowOnClick);
        prevButton = (Button) findViewById(R.id.prevButton);
        EditButton = (Button) findViewById(R.id.editButton);
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
        diagramWindow = (ImageView) findViewById(R.id.recentDiagram);
        devDisplay = (TextView) findViewById(R.id.devDisplayBtn);
        //End of Defintitions



        //Onclicks
        LashReleaseDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReleaseDetails();
            }
        });

        devDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devCounter();
            }
        });

        clientNotesBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotesDialog();
            }
        });

        appointmentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartAppt();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToPrevMenu();
            }
        });

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMenu();
            }
        });
        //End of Onclicks


        //checkForAdmin function reveals delete and edit button for Admins
        counter = 0;
        checkForAdmin();
        getIncomingIntent();
        showEditButton();


        //get ID from previous activity
        Bundle extras = getIntent().getExtras();
        pushID = extras.getString("ID");
        Log.d("test",pushID);
        sendIDtoCovid = new Intent(getApplicationContext(),CovidForm.class);
        sendIDtoCovid.putExtra("COVIDID",pushID);
        Intent QuestionaireIntent2 = new Intent(ClientDetailActivity.this, QuestionaireForReturning.class);
        onNewIntent(QuestionaireIntent2);


        //Intents and DatabaseReferences (Need to be below the ID section)
        passIntent = new Intent(ClientDetailActivity.this, QuestionaireForReturning.class);
        LocationRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Location");
        windowCompare = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
        diagram0Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram0");
        diagram1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram1");
        diagram2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram2");
        diagram3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram3");
        diagram4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram4");
        todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("MM-dd-yyyy");
        Today = formatter.format(todayDate);
        CovidDateRefs = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("CovidDates");
        CovidDateSave = CovidDateRefs.child(Today);
        AllergyRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Allergy");
        initImageRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("mostRecentURI");
         fNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("fName");
         mNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("mName");
         lNameRef= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("lName");
         COVIDREF = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("COVIDDATES");
        COVIDREF2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("COVIDDATES").child(Today);


        passIntent.putExtra("Please", pushID);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void onBackPressed() {
        Intent returnIntent = new Intent(ClientDetailActivity.this, ClientList.class);
        startActivity(returnIntent);
    }

    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, Appointments.class);
        //This is to check if the client
        CovidDateRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Today)){
                    Log.d("test","cleared for service");

                }
                else{

                    startActivity(sendIDtoCovid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       //Massive function for displaying a menu, that allows the user to see the most recent photo larger, or view the previous 5 photos.
        photoOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"View Most Recent", "View Image Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetailActivity.this);
                builder.setTitle("View Image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (items[i].equals("View Most Recent")) {
                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
                            LayoutInflater inflater2 = getLayoutInflater();
                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                            dialogBuilder2.setView(dialogView2);
                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                            DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
                            checkRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("Photo4")) {
                                        final StorageReference dia4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo4");
                                        dia4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo4 = uri.toString();
                                                Picasso.get().load(photo4).into(expandedPicture);
                                            }
                                        });
                                    } else if (dataSnapshot.hasChild("Photo3")) {
                                        final StorageReference dia3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo3");
                                        dia3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo3 = uri.toString();
                                                Picasso.get().load(photo3).into(expandedPicture);
                                            }
                                        });
                                    } else if (dataSnapshot.hasChild("Photo2")) {
                                        final StorageReference dia2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo2");
                                        dia2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo2 = uri.toString();
                                                Picasso.get().load(photo2).into(expandedPicture);
                                            }
                                        });
                                    } else if (dataSnapshot.hasChild("Photo1")) {
                                        final StorageReference dia1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Photo1");
                                        dia1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String photo1 = uri.toString();
                                                Picasso.get().load(photo1).into(expandedPicture);
                                            }
                                        });
                                    } else {
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
                        if (items[i].equals("View Image Gallery")) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientDetailActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.alertdialog, null);
                            dialogBuilder.setView(dialogView);

                            // These definitions need to be here instead of up higher due to them being in the dialogView
                            final ImageView mostRecentIV = (ImageView) dialogView.findViewById(R.id.mostRecent);
                            final ImageView photo1IV = (ImageView) dialogView.findViewById(R.id.Photo1);
                            final ImageView photo2IV = (ImageView) dialogView.findViewById(R.id.Photo2);
                            final ImageView photo3IV = (ImageView) dialogView.findViewById(R.id.Photo3);
                            final ImageView photo4IV = (ImageView) dialogView.findViewById(R.id.Photo4);
                            final TextView mostRecentN = (TextView) dialogView.findViewById(R.id.mostRecentName);
                            final TextView Photo1N = (TextView) dialogView.findViewById(R.id.Photo1Name);
                            final TextView Photo2N = (TextView) dialogView.findViewById(R.id.Photo2Name);
                            final TextView Photo3N = (TextView) dialogView.findViewById(R.id.Photo3Name);
                            final TextView Photo4N = (TextView) dialogView.findViewById(R.id.Photo4Name);


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

                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
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
                                            Log.d("test", Photo1Name);
                                            Photo1N.setText(Photo1Name);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    photoONEuri = uri.toString();
                                    Picasso.get().load(photoONEuri).into(photo1IV);
                                    photo1IV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            Log.d("test","Loaded Image");
                                        }
                                    });
                                    photo1IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder dialogBuilder3 = new AlertDialog.Builder(ClientDetailActivity.this);
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
                                    photo2IV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            Log.d("test","Loaded Image");
                                        }
                                    });
                                    photo2IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder dialogBuilder4 = new AlertDialog.Builder(ClientDetailActivity.this);
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
                                            Log.d("test", Photo3Name);
                                            Photo3N.setText(Photo3Name);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    photoTHREEuri = uri.toString();
                                    Picasso.get().load(photoTHREEuri).into(photo3IV);
                                    photo3IV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            Log.d("test","Loaded Image");
                                        }
                                    });
                                    photo3IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder dialogBuilder6 = new AlertDialog.Builder(ClientDetailActivity.this);
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
                                    photo4IV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            Log.d("test","Loaded Image");
                                        }
                                    });
                                    photo4IV.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder dialogBuilder5 = new AlertDialog.Builder(ClientDetailActivity.this);
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
                        if (items[i].equals("Cancel")) {

                        }

                    }

                });
                builder.show();

            }
        });








        //function that decides which Diagram is displayed in the preview window
        windowCompare.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Diagram4")) {
                    diagram4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramFour = uri.toString();
                            Picasso.get().load(DiagramFour).into(diagramWindow);
                        }
                    });
                } else if (dataSnapshot.hasChild("Diagram3")) {
                    diagram3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramThree = uri.toString();
                            Picasso.get().load(DiagramThree).into(diagramWindow);
                        }
                    });
                } else if (dataSnapshot.hasChild("Diagram2")) {
                    diagram2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DiagramTwo = uri.toString();
                            Picasso.get().load(DiagramTwo).into(diagramWindow);

                        }
                    });
                } else if (dataSnapshot.hasChild("Diagram1")) {
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


        //Same menue from earlier, but for diagrams instead of photos
        diagramOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"View Most Recent", "View Diagram Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetailActivity.this);
                builder.setTitle("View Diagram");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (items[i].equals("View Most Recent")) {
                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
                            LayoutInflater inflater2 = getLayoutInflater();
                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                            dialogBuilder2.setView(dialogView2);
                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                            DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
                            checkRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("Diagram4")) {
                                        final StorageReference dia4Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram4");
                                        dia4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram4 = uri.toString();
                                                Picasso.get().load(Diagram4).into(expandedPicture);
                                            }
                                        });
                                    } else if (dataSnapshot.hasChild("Diagram3")) {
                                        final StorageReference dia3Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram3");
                                        dia3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram3 = uri.toString();
                                                Picasso.get().load(Diagram3).into(expandedPicture);
                                            }
                                        });
                                    } else if (dataSnapshot.hasChild("Diagram2")) {
                                        final StorageReference dia2Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram2");
                                        dia2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram2 = uri.toString();
                                                Picasso.get().load(Diagram2).into(expandedPicture);
                                            }
                                        });
                                    } else if (dataSnapshot.hasChild("Diagram1")) {
                                        final StorageReference dia1Ref = FirebaseStorage.getInstance().getReference().child(pushID).child("Diagram1");
                                        dia1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram1 = uri.toString();
                                                Picasso.get().load(Diagram1).into(expandedPicture);
                                            }
                                        });
                                    } else {
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

                        } else if (items[i].equals("View Diagram Gallery")) {
                            AlertDialog.Builder diagramDialogBuilder = new AlertDialog.Builder(ClientDetailActivity.this);
                            LayoutInflater diagramInflater = getLayoutInflater();
                            View dialogView = diagramInflater.inflate(R.layout.diagramdialog, null);
                            diagramDialogBuilder.setView(dialogView);

                            final ImageView diagram0IV = (ImageView) dialogView.findViewById(R.id.diagram0);
                            final ImageView diagram1IV = (ImageView) dialogView.findViewById(R.id.diagram1);
                            final ImageView diagram2IV = (ImageView) dialogView.findViewById(R.id.diagram2);
                            final ImageView diagram3IV = (ImageView) dialogView.findViewById(R.id.diagram3);
                            final ImageView diagram4IV = (ImageView) dialogView.findViewById(R.id.diagram4);
                            final TextView diagramname0 = (TextView) dialogView.findViewById(R.id.diagramName0);
                            final TextView diagramname1 = (TextView) dialogView.findViewById(R.id.diagramName1);
                            final TextView diagramname2 = (TextView) dialogView.findViewById(R.id.diagramName2);
                            final TextView diagramname3 = (TextView) dialogView.findViewById(R.id.diagramName3);
                            final TextView diagramname4 = (TextView) dialogView.findViewById(R.id.diagramName4);
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

                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
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

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
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

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
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

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(ClientDetailActivity.this);
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
                        } else if (items[i].equals("Cancel")) {

                        }
                    }
                });
                builder.show();
            }
        });
        //End of Diagram menu function



        //Set Location Text
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


       //set Allergy TExt
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


        //
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
                clientNotesBox.setMovementMethod(new ScrollingMovementMethod());
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
                 endPost = dataSnapshot.getValue(String.class);
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
        DatabaseReference baseRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
        baseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild("EndTime")){
                SavePrevApptScreenShot();
            }
            else
            {

            }
            Log.d("test","Has no previous appt");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetailActivity.this);
        builder.setTitle("Add a Comment?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Edit notes")) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(ClientDetailActivity.this);
                    LayoutInflater notesinflater = getLayoutInflater();
                    final AlertDialog addNoteDia = builder2.create();
                    View dialogViewnotes = notesinflater.inflate(R.layout.notedialog, null);
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
                                    String combonotes = pulledNotes + "\n" + noteToAdd;
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

                } else if (items[i].equals("cancel")) {
                    Reset();
                }
            }
        });
        builder.show();
    }

    public void Reset() {
        Intent intent = new Intent(this, ClientDetailActivity.class);
        startActivity(intent);
    }

    public void devCounter() {
        if (devCount == 0) {
            devCount = devCount + 1;
        } else if (devCount == 1) {
            devCount = devCount + 1;
        } else if (devCount == 2) {
            devCount = devCount + 1;
        } else {
            final CharSequence[] items = {pushID, "close"};
            AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetailActivity.this);
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

    public void ReleaseDetails() {

        final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
        base.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("zip")) {
                    if(dataSnapshot.hasChild("Email")){
                    final DatabaseReference ZipRef = base.child("zip");
                    final DatabaseReference AddRef = base.child("Address");
                    final DatabaseReference Ans1Ref = base.child("Ans1");
                    final DatabaseReference Ans2Ref = base.child("Ans2");
                    final DatabaseReference Ans3Ref = base.child("Ans3");
                    final DatabaseReference Ans4Ref = base.child("Ans4");
                    final DatabaseReference Ans5Ref = base.child("Ans5");
                    final DatabaseReference refRef = base.child("HDYHAU");
                    final DatabaseReference BDayRef = base.child("Birthday");
                    ZipRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ZIP = dataSnapshot.getValue(String.class);
                            AddRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ADD = dataSnapshot.getValue(String.class);
                                    FULLADD = ADD + " " + ZIP;
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
                                                                                            AlertDialog.Builder details = new AlertDialog.Builder(ClientDetailActivity.this);
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
                                                                                            address.setText("\n\n" + "  Address: " + FULLADD + "\n\n" + "  Birthday: " + BDAY + "\n\n" + "  Reffered by: " + REFERENCE2 + "\n\n" + "  " + ANS1 + "\n\n"
                                                                                                    + "  " + ANS2 + "\n\n" + "  " + ANS3 + "\n\n" + "  " + ANS4 + "\n\n" + "  " + ANS5);
                                                                                            address.setTextSize(16);

                                                                                            details.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                    Log.d("test", "yay");
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
                else{
                    //Info to simply add email
                        AlertDialog.Builder addEmail = new AlertDialog.Builder(ClientDetailActivity.this);
                        addEmail.setTitle("Add Email Address to Record");

                        final EditText emailentry = new EditText(ClientDetailActivity.this);
                        emailentry.setInputType(InputType.TYPE_CLASS_TEXT);
                        addEmail.setView(emailentry);
                        addEmail.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 Emailtxt = emailentry.getText().toString();
                                 if(Emailtxt.equals("")){
                                     Toast.makeText(ClientDetailActivity.this,"Please enter your Email",Toast.LENGTH_LONG).show();
                                 }
                                 else{
                                     DatabaseReference EmailRef= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("Email");
                                     DatabaseReference EmailRef2 = FirebaseDatabase.getInstance().getReference().child("EmailList").child(pushID);
                                     EmailRef.setValue(Emailtxt);
                                     EmailRef2.setValue(Emailtxt);
                                 }
                            }
                        });
                        addEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        addEmail.show();
                    }
                } else {

                    AlertDialog.Builder addtoExisting = new AlertDialog.Builder(ClientDetailActivity.this);
                    addtoExisting.setTitle("Would you like to add a Lash Release for this client?");
                    addtoExisting.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(passIntent);
                        }
                    });
                    addtoExisting.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d("test", "Okay then");
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

    public void SavePrevApptScreenShot() {

            View content = findViewById(R.id.csl);
            Bitmap bitmap = content.getDrawingCache();
            File file = new File( Environment.getExternalStorageDirectory() + "/test.png");
            try
            {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            endTimeRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("EndTime");
            endTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     endTimeVal = dataSnapshot.getValue(String.class);
                     currentDate = endTimeVal;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        final  DatabaseReference compareref = FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID);
        final DatabaseReference prevaref1= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("preva1");
      final  DatabaseReference prevaref2= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("preva2");
        final DatabaseReference prevaref3= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("preva3");
        final  DatabaseReference prevaref4= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("preva4");
        final DatabaseReference prevaref5= FirebaseDatabase.getInstance().getReference().child("Clients").child(pushID).child("preva5");
        final StorageReference Sprevaref1 = FirebaseStorage.getInstance().getReference().child("00Previous Appointments").child(pushID).child("preva1");
        final StorageReference Sprevaref2 = FirebaseStorage.getInstance().getReference().child("00Previous Appointments").child(pushID).child("preva2");
        final StorageReference Sprevaref3 = FirebaseStorage.getInstance().getReference().child("00Previous Appointments").child(pushID).child("preva3");
        final StorageReference Sprevaref4 = FirebaseStorage.getInstance().getReference().child("00Previous Appointments").child(pushID).child("preva4");
        final StorageReference Sprevaref5 = FirebaseStorage.getInstance().getReference().child("00Previous Appointments").child(pushID).child("preva5");

        View FormLayout =  findViewById(R.id.csl);
        FormLayout.setDrawingCacheEnabled(true);
        FormLayout.getDrawingCache();
        FormLayout.buildDrawingCache();
        //WholePage (capitalized) is the official image created
        wholePage = bitmap.createBitmap(FormLayout.getDrawingCache());
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        wholePage.compress(Bitmap.CompressFormat.PNG,100,stream2);
        WholePage = stream2.toByteArray();

        compareref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("preva5")){
                    Log.d("test","has 5,looping");
                    Sprevaref1.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        public void onSuccess(byte[] bytes) {
                            prev1 = bytes;
                            Sprevaref2.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    prev2 = bytes;
                                    Sprevaref3.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            prev3 = bytes;
                                            Sprevaref4.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    prev4= bytes;
                                                    Sprevaref5.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                        @Override
                                                        public void onSuccess(byte[] bytes) {
                                                            prev5 = bytes;
                                                            Log.d("test",prev5.toString());
                                                            prev1 = prev2;
                                                            prev2 = prev3;
                                                            prev3 = prev4;
                                                            Log.d("test","Prev 4:"+prev4.toString());
                                                            prev4 = prev5;
                                                            Log.d("test","Prev 4 abc2 5:"+prev4.toString());
                                                            Sprevaref4.putBytes(prev4);
                                                            Sprevaref3.putBytes(prev3);
                                                            Sprevaref2.putBytes(prev2);
                                                            Sprevaref1.putBytes(prev1);
                                                            prevaref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    pr1 = dataSnapshot.getValue(String.class);
                                                                    prevaref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            pr2 = dataSnapshot.getValue(String.class);
                                                                            prevaref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    pr3 = dataSnapshot.getValue(String.class);
                                                                                    prevaref4.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            pr4 = dataSnapshot.getValue(String.class);
                                                                                            prevaref5.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    pr5 = dataSnapshot.getValue(String.class);
                                                                                                    pr1 = pr2;
                                                                                                    pr2 = pr3;
                                                                                                    pr3 = pr4;
                                                                                                    pr4 = pr5;
                                                                                                    prevaref5.setValue(currentDate);
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
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else if(dataSnapshot.hasChild("preva4")){
                    Log.d("test","has 4");
                    prevaref5.setValue(currentDate);
                    Sprevaref5.putBytes(WholePage);
                }
                else if(dataSnapshot.hasChild("preva3")){
                    Log.d("test","has 3");
                    prevaref4.setValue(currentDate);
                    Sprevaref4.putBytes(WholePage);
                }
                else if(dataSnapshot.hasChild("preva2")){
                    Log.d("test","has 4");
                    prevaref3.setValue(currentDate);
                    Sprevaref3.putBytes(WholePage);
                }
                else if(dataSnapshot.hasChild("preva1")){
                    Log.d("test","has 1");
                    prevaref2.setValue(currentDate);
                    Sprevaref2.putBytes(WholePage);
                }
                else{
                    Log.d("test","has none");
                    String[] justDate = endPost.split("\\s+");
                    Log.d("test",justDate.toString());
                    prevaref1.setValue(currentDate);
                    Sprevaref1.putBytes(WholePage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public void GoToPrevMenu(){
        Intent prevIntent = new Intent(this,PrevAppt.class);
        prevIntent.putExtra("IDPREV",pushID);
        startActivity(prevIntent);
    }

    public void showEditButton(){
        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("TESTING").child("IMATEST");
        placeRef.setValue("TEST");
        Log.d("test","Show Delete button function accessed");
        EditButton.setVisibility(View.VISIBLE);
        DatabaseReference getIdRef = FirebaseDatabase.getInstance().getReference().child("Testing").child("IMATEST");
        getIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void checkForAdmin(){
        FirebaseAuth.AuthStateListener authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    AdminRef = FirebaseDatabase.getInstance().getReference().child("Employees").child("Admins");
                    AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userEmail = user.getEmail();
                            userEmail = userEmail.split("@")[0];
                            userEmailCap = userEmail.substring(0,1).toUpperCase()+userEmail.substring(1);
                            Log.d("testuseremailcap",userEmailCap);
                            if(dataSnapshot.hasChild(userEmailCap)){
                                EditButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed out
                    Log.d("test", "no login, required PIN");
                }
                // ...
            }

        };

        FirebaseAuth.getInstance().addAuthStateListener(authlistener);
    }

    public void backToMain(){
        Intent backIntent = new Intent(this,MainActivity.class);
        startActivity(backIntent);
    }

    public void editMenu() {

        Log.d("test",pushID);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetailActivity.this);
        final AlertDialog DialogBuilder = builder.create();
        builder.setTitle("Edit Client Information");
        final LayoutInflater inflater = getLayoutInflater();

                //These Edit Texts are a part of the Alert Dialog here
                final View dialogView = inflater.inflate(R.layout.editnamedialog, null);
                final EditText fNameEditDisplay = dialogView.findViewById(R.id.fname2edit);
                final EditText mNameEditDisplay= dialogView.findViewById(R.id.mname2edit);
                final EditText lNameEditDisplay = dialogView.findViewById(R.id.lname2edit);
                final Button CancelEditButton = dialogView.findViewById(R.id.closeEdButton);
                final Button DeleteButton = dialogView.findViewById(R.id.deleteButtonEd);
                final Button EditNameButton = dialogView.findViewById(R.id.confirmButtonEd);

                DialogBuilder.setView(dialogView);
                builder.setView(dialogView);
                    fNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            builder.setView(dialogView);
                            String fName2Edit = dataSnapshot.getValue(String.class);

                            fNameEditDisplay.setText(fName2Edit,TextView.BufferType.EDITABLE);
                           mNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   String mName2Edit = dataSnapshot.getValue(String.class);
                                   mNameEditDisplay.setText(mName2Edit,TextView.BufferType.EDITABLE);
                                   lNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           String lName2Edit = dataSnapshot.getValue(String.class);
                                           lNameEditDisplay.setText(lName2Edit,TextView.BufferType.EDITABLE);
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

                DialogBuilder.show();


                EditNameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newfName = fNameEditDisplay.getText().toString();
                        String newmName = mNameEditDisplay.getText().toString();
                        String newlName = lNameEditDisplay.getText().toString();
                        Log.d("edittest",newfName+" "+newmName+" "+newlName);
                        fNameRef.setValue(newfName);
                        mNameRef.setValue(newmName);
                        lNameRef.setValue(newlName);
                        DialogBuilder.dismiss();


                    }
                });

                CancelEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder.dismiss();
                    }
                });



            }



        }
// builder.setItems(items, new DialogInterface.OnClickListener() {
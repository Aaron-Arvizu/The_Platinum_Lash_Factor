package com.platinumlashes.lashdemomkiv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.google.firebase.auth.FirebaseAuth;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.FontRequest;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointments extends AppCompatActivity {
    public Bitmap BmpSelected,myBitmap;
    public Bitmap Bmp = null;
    public Uri selectedImageUri;
    TextView startTime,endTime, FFnameBox,FMnameBox,FLnameBox;
    TextView ClientNotes;
    TextView ArtistName,AllergyInfo;
    ImageView pastPicture,diagramWindow;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button endAppointmentButton,uploadNewPhotoBtn;
    public StorageReference mStorageReference,Ref,pushSPhoto1,recentRef,getPhoto4,getPhoto3,getPhoto2,getPhoto1,getinit,
            diagram0Ref,diagram1Ref,diagram2Ref,diagram3Ref,diagram4Ref;
    Integer Request_camera=1, Select_file=0;
    public String initImage, finalpushID,photoONEuri,photoTWOuri,photoTHREEuri,photoFOURuri;
    public DatabaseReference startTimeRef,mDatabase2,mDatabase3,mDatabase4,mDatabase5,mDatabase6,
            mDatabase7,mDatabase8,mDatabase9,uri1Reference,pullNotesRef,pullNotesIfRef;
    public byte [] photo1,photo2,photo3,photo4,cameraphoto,diagramFinal;
    public Uri diagram0,diagram1,diagram2,diagram3,diagram4;
    public String diagram00, diagram11,diagram22,diagram33,diagram44,Original;
    public SimpleDateFormat simpleDateFormatFinal;
    public Context context = this;
    public String Photo1Name, mostRecentName,Photo2Name,Photo3Name,Photo4Name;
    public String diagram0name,diagram1name,diagram2name,diagram3name,diagram4name,userEmailCap,finalfName,finalmName,finallName, Location, LashArtist, EyePads,Glue,ApptType,Allergies;
    public Button lastApptDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        FirebaseAuth.AuthStateListener authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String userName = user.getEmail();
                    Log.d("test",userName);
                    userName = userName.split("@")[0];
                     userEmailCap = userName.substring(0,1).toUpperCase()+userName.substring(1);

                } else {
                    // User is signed out

                }
                // ...
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authlistener);
        AllowEmojis();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
       final Spinner LocSpinner = (Spinner) findViewById(R.id.locationSpinner);
       AllergyInfo = (TextView) findViewById(R.id.allergyEnter);
       lastApptDetails = (Button) findViewById(R.id.lastdetbtn);
       final Spinner GlueSpinner = (Spinner) findViewById(R.id.glueSpinner);
       final Spinner EyePadSpinner = (Spinner) findViewById(R.id.eyePadSpinner);
       final Spinner ApptSpinner = (Spinner) findViewById(R.id.apptSpinner);
       final TextView pastOnClick = (TextView) findViewById(R.id.pastPicOnClick);
       final TextView DiagramOnClick = (TextView) findViewById(R.id.pastDiagramOnClick);
       FFnameBox = (TextView) findViewById(R.id.fName4);
       FMnameBox = (TextView) findViewById(R.id.mName4);
       FLnameBox = (TextView) findViewById(R.id.lName4);
        ClientNotes = (EditText) findViewById(R.id.clientNotes);
        Bundle bundle=getIntent().getExtras();

        if(getIntent().hasExtra("ID2")) {

            finalpushID = bundle.getString("ID2");
        }
        lastApptDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference locRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Location");
                final DatabaseReference ArtRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Artist");
               final DatabaseReference PadRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("EyePads");
                final DatabaseReference GlueRef2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Glue");
                final  DatabaseReference ApptRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("ApptType");
               final  DatabaseReference AllerRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Allergy");
                locRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Location = dataSnapshot.getValue(String.class);
                        ArtRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                LashArtist = dataSnapshot.getValue(String.class);
                                PadRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        EyePads = dataSnapshot.getValue(String.class);
                                        GlueRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Glue = dataSnapshot.getValue(String.class);
                                                ApptRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        ApptType = dataSnapshot.getValue(String.class);
                                                        AllerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Allergies = dataSnapshot.getValue(String.class);
                                                                AlertDialog.Builder details = new AlertDialog.Builder(Appointments.this);
                                                                details.setTitle("Last Appointment info");
                                                                LinearLayout layout = new LinearLayout(details.getContext());
                                                                final TextView address = new TextView(details.getContext());
                                                                LinearLayout.LayoutParams padd = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                address.setText("\n\n" +"  Location: "+Location + "\n\n" + "  Lash Artist: "+LashArtist+ "\n\n"+"  Eye Pads: "+EyePads+"\n\n" +"  Glue: "+ Glue +"\n\n"
                                                                        + "  Appt. Type:  "+ApptType + "\n\n"+ "  Allergies: "+Allergies);
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
        });
        Log.d("test",finalpushID);
        DatabaseReference getFNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("fName");
        DatabaseReference getMNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("mName");
        DatabaseReference getLNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("lName");
        getFNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finalfName = dataSnapshot.getValue(String.class);
                FFnameBox.setText(finalfName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getMNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finalmName = dataSnapshot.getValue(String.class);
                FMnameBox.setText(finalmName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getLNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finallName = dataSnapshot.getValue(String.class);
                FLnameBox.setText(finallName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ArrayAdapter<String> glueAdapter = new ArrayAdapter<String>(Appointments.this,R.layout.apptspinneritem,getResources().getStringArray(R.array.GlueOptions));
        ArrayAdapter<String> eyeAdapter = new ArrayAdapter<String>(Appointments.this,R.layout.apptspinneritem,getResources().getStringArray(R.array.EyeOptions));
        ArrayAdapter<String> apptAdapter = new ArrayAdapter<String>(Appointments.this,R.layout.apptspinneritem,getResources().getStringArray(R.array.ApptOptions));
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(Appointments.this,R.layout.apptspinneritem,getResources().getStringArray(R.array.LocationOptions));
        glueAdapter.setDropDownViewResource(R.layout.apptspinneritem);
        GlueSpinner.setAdapter(glueAdapter);
        eyeAdapter.setDropDownViewResource(R.layout.apptspinneritem);
        EyePadSpinner.setAdapter(eyeAdapter);
        apptAdapter.setDropDownViewResource(R.layout.apptspinneritem);
        ApptSpinner.setAdapter(apptAdapter);
        locAdapter.setDropDownViewResource(R.layout.apptspinneritem);
        LocSpinner.setAdapter(locAdapter);
        Button uploadDiagramButton = (Button)findViewById(R.id.uploadDiagramBtn);


        uploadNewPhotoBtn = (Button) findViewById(R.id.uploadPhotoBtn);
        pastPicture = (ImageView) findViewById(R.id.recentPhotoAppt);

        Bundle bundle2=getIntent().getExtras();

        if(getIntent().hasExtra("ID2")) {

             finalpushID = bundle.getString("ID2");
        }
        diagram0Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram0");
        diagram1Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram1");
        diagram2Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram2");
        diagram3Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram3");
        diagram4Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram4");
        simpleDateFormatFinal = new SimpleDateFormat("MM/dd/yy  hh:mm");
        final String starttime = simpleDateFormatFinal.format(new Date());
        startTime = (TextView) findViewById(R.id.startTime);
        endAppointmentButton = (Button) findViewById(R.id.endBtn);
        startTime.setText(starttime);
        startTimeRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("StartTime");
        startTimeRef.setValue(starttime);
        diagramWindow = (ImageView) findViewById(R.id.diagramWindow);
        final Drawable diagramDrawable = getResources().getDrawable(R.drawable.diagramfinal);
        final View myDrawView = new MyDrawView(Appointments.this);
        uploadDiagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDrawView.getParent() !=null){
                    ((ViewGroup)myDrawView.getParent()).removeView(myDrawView);
                }
                final AlertDialog.Builder diagramDialog = new AlertDialog.Builder(Appointments.this);
                LayoutInflater inflater = getLayoutInflater();
               final View dialogView = inflater.inflate(R.layout.content_draw_test, null);
                ImageView diagramBackground = (ImageView) dialogView.findViewById(R.id.diagrambackground);
                diagramBackground.setImageDrawable(diagramDrawable);
                diagramDialog.setView(dialogView);
                final ConstraintLayout parent = (ConstraintLayout) dialogView.findViewById(R.id.signImageParent);
                final ConstraintLayout justEyes = (ConstraintLayout) dialogView.findViewById(R.id.justEyes);
                Button clearButton = (Button) dialogView.findViewById(R.id.clearButton);
                Button saveDiagramButton = (Button) dialogView.findViewById(R.id.diagramSaveButton);

                justEyes.addView(myDrawView);
                clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MyDrawView) myDrawView).clear();
                    }
                });
                final AlertDialog alertDDialog = diagramDialog.create();
                saveDiagramButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        justEyes.setDrawingCacheEnabled(true);
                        Bitmap savedDiagram = Bitmap.createBitmap(justEyes.getDrawingCache());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        savedDiagram.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        diagramFinal = stream.toByteArray();
                        diagramWindow.setImageBitmap(savedDiagram);
                        alertDDialog.dismiss();
                    }
                });
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                alertDDialog.show();

            }
        });
        DiagramOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items= {"View Most Recent","View Diagram Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Appointments.this);
                builder.setTitle("View Diagram");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (items[i].equals("View Most Recent")) {
                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
                            LayoutInflater inflater2 = getLayoutInflater();
                            View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                            dialogBuilder2.setView(dialogView2);
                            final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                            DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
                            checkRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("Diagram4")){
                                        final StorageReference dia4Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram4");
                                        dia4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram4 = uri.toString();
                                                Picasso.get().load(Diagram4).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if(dataSnapshot.hasChild("Diagram3")){
                                        final StorageReference dia3Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram3");
                                        dia3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram3 = uri.toString();
                                                Picasso.get().load(Diagram3).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if (dataSnapshot.hasChild("Diagram2")){
                                        final StorageReference dia2Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram2");
                                        dia2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram2 = uri.toString();
                                                Picasso.get().load(Diagram2).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else if (dataSnapshot.hasChild("Diagram1")){
                                        final StorageReference dia1Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram1");
                                        dia1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String Diagram1 = uri.toString();
                                                Picasso.get().load(Diagram1).into(expandedPicture);
                                            }
                                        });
                                    }
                                    else {
                                        final StorageReference dia0Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram0");
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
                            AlertDialog.Builder diagramDialogBuilder = new AlertDialog.Builder(Appointments.this);
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
                            final StorageReference diagram0Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram0");
                            final StorageReference diagram1Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram1");
                            final StorageReference diagram2Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram2");
                            final StorageReference diagram3Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram3");
                            final StorageReference diagram4Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Diagram4");
                            diagram1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
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

                                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
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
                                            DatabaseReference picnameref2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
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

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
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
                                            DatabaseReference picnameref3 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
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

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
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
                                            DatabaseReference picnameref4 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");
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

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
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


        uploadNewPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();

            }
        });
        final DatabaseReference photo1Ref,photo2Ref;
        photo1Ref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
        photo1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Photo4")) {
                    final StorageReference photo4SRef;
                    photo4SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo4");
                    photo4SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoFOURuri = uri.toString();
                            Picasso.get().load(photoFOURuri).into(pastPicture);
                            Original = photoFOURuri;
                        }
                    });
                }
                else if(dataSnapshot.hasChild("Photo3")){
                    final StorageReference photo3SRef;
                    photo3SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo3");
                    photo3SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoTHREEuri = uri.toString();
                            Picasso.get().load(photoTHREEuri).into(pastPicture);
                            Original = photoTHREEuri;
                        }
                    });
                }
                else if(dataSnapshot.hasChild("Photo2")) {
                    final StorageReference photo2SRef;
                    photo2SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo2");
                    photo2SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoTWOuri = uri.toString();
                            Picasso.get().load(photoTWOuri).into(pastPicture);
                            Original = photoTWOuri;
                        }
                    });
                }
                // PHOTO ONE SEPARATION
                else if(dataSnapshot.hasChild("Photo1")) {
                    final StorageReference photo1SRef;
                    photo1SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo1");
                    photo1SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoONEuri = uri.toString();
                            Picasso.get().load(photoONEuri).into(pastPicture);
                            Original = photoONEuri;
                        }
                    });
                }
                else{
                    //DISPLAY INITIAL IMAGE
                    recentRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("mostRecentPic");
                    recentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("test","found the initial image");
                             initImage = uri.toString();
                            Picasso.get().load(initImage).into(pastPicture);
                            Original = initImage;
                        }

                    });

                }
                pastOnClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence[] items= {"View Most Recent","View Image Gallery","Cancel"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(Appointments.this);
                        builder.setTitle("View Image");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(items[i].equals("View Most Recent")) {
                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
                                    LayoutInflater inflater2 = getLayoutInflater();
                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                    dialogBuilder2.setView(dialogView2);
                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                    DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
                                    checkRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild("Photo4")){
                                                final StorageReference dia4Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo4");
                                                dia4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String photo4 = uri.toString();
                                                        Picasso.get().load(photo4).into(expandedPicture);
                                                    }
                                                });
                                            }
                                            else if(dataSnapshot.hasChild("Photo3")){
                                                final StorageReference dia3Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo3");
                                                dia3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String photo3 = uri.toString();
                                                        Picasso.get().load(photo3).into(expandedPicture);
                                                    }
                                                });
                                            }
                                            else if (dataSnapshot.hasChild("Photo2")){
                                                final StorageReference dia2Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo2");
                                                dia2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String photo2 = uri.toString();
                                                        Picasso.get().load(photo2).into(expandedPicture);
                                                    }
                                                });
                                            }
                                            else if (dataSnapshot.hasChild("Photo1")){
                                                final StorageReference dia1Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo1");
                                                dia1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String photo1 = uri.toString();
                                                        Picasso.get().load(photo1).into(expandedPicture);
                                                    }
                                                });
                                            }
                                            else {
                                                final StorageReference dia0Ref = FirebaseStorage.getInstance().getReference().child(finalpushID).child("mostRecentPic");
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
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Appointments.this);
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
                                    mostRecentSRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("mostRecentPic");
                                    mostRecentSRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("mostRecentName");
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
                                            initImage = uri.toString();
                                            Picasso.get().load(initImage).into(mostRecentIV);
                                            mostRecentIV.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Appointments.this);
                                                    LayoutInflater inflater2 = getLayoutInflater();
                                                    View dialogView2 = inflater2.inflate(R.layout.alertdialogexpaned, null);
                                                    dialogBuilder2.setView(dialogView2);
                                                    final ImageView expandedPicture = (ImageView) dialogView2.findViewById(R.id.imageViewExpaned);
                                                    Picasso.get().load(initImage).into(expandedPicture);
                                                    AlertDialog alertDialog2 = dialogBuilder2.create();
                                                    alertDialog2.getWindow().setBackgroundDrawableResource(R.color.background);
                                                    dialogBuilder2.show();
                                                }
                                            });

                                        }
                                    });
                                    final StorageReference photo1SRef;
                                    photo1SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo1");
                                    photo1SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
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

                                                    AlertDialog.Builder dialogBuilder3 = new AlertDialog.Builder(Appointments.this);
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
                                    photo2SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo2");
                                    photo2SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
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

                                                    AlertDialog.Builder dialogBuilder4 = new AlertDialog.Builder(Appointments.this);
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
                                    photo3SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo3");
                                    photo3SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
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
                                                    AlertDialog.Builder dialogBuilder6 = new AlertDialog.Builder(Appointments.this);
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
                                    photo4SRef = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo4");
                                    photo4SRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference picnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");
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
                                                    AlertDialog.Builder dialogBuilder5 = new AlertDialog.Builder(Appointments.this);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference windowCompare = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
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
       pullNotesRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Notes");
        pullNotesIfRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
        pullNotesIfRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Notes")){
                    pullNotesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String pulledClientNotes = dataSnapshot.getValue(String.class);
                            ClientNotes.setText(pulledClientNotes,TextView.BufferType.EDITABLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Log.d("test","No notes.....yet");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        endAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Saving Appointment",Toast.LENGTH_LONG).show();
                mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Artist");
                mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("EndTime");
                mDatabase4 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Notes");
                mDatabase5 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Glue");
                mDatabase6 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("EyePads");
                mDatabase7 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("ApptType");
                mDatabase8 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Location");
                mDatabase9 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Allergy");
                DatabaseReference mDatabase10 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Day");
                SimpleDateFormat Day = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy    hh:mm");
                final String endtime = simpleDateFormatFinal.format(new Date());
                String glueChoice = GlueSpinner.getSelectedItem().toString();
                Log.d("test",glueChoice);
                String eyeChoice = EyePadSpinner.getSelectedItem().toString();
                String apptChoice = ApptSpinner.getSelectedItem().toString();
                String locationChoice = LocSpinner.getSelectedItem().toString();
                String allergyString = AllergyInfo.getText().toString();

                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                String currentDate = simpleDateFormat2.format(new Date());
                mDatabase10.setValue(currentDate);
                String notesfinal = ClientNotes.getText().toString()+ " ~ ["+userEmailCap+" "+currentDate+"]";
                if(glueChoice.equals("Select a Glue")){
                    Toast.makeText(Appointments.this,"Select a Glue",Toast.LENGTH_LONG).show();
                }
                else if(eyeChoice.equals("Select Eye Pads")){
                    Toast.makeText(Appointments.this,"Select Eye Pads",Toast.LENGTH_LONG).show();
                }
                else if(locationChoice.equals("Select Location")){
                    Toast.makeText(Appointments.this,"Select Location",Toast.LENGTH_LONG).show();
                }
                else if (apptChoice.equals("Select Appointment Type")){
                    Toast.makeText(Appointments.this,"Select Appointment Type",Toast.LENGTH_SHORT).show();
                }
                else if (Bmp == (null)){
                    Toast.makeText(Appointments.this,"Upload a Photo",Toast.LENGTH_SHORT).show();
                }
                else if(diagramFinal == null){
                    Toast.makeText(Appointments.this,"Upload a Diagram",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDatabase2.setValue(userEmailCap);
                    mDatabase3.setValue(endtime);
                    mDatabase4.setValue(notesfinal);
                    mDatabase5.setValue(glueChoice);
                    mDatabase9.setValue(allergyString);
                    mDatabase8.setValue(locationChoice);
                    mDatabase6.setValue(eyeChoice);
                    mDatabase7.setValue(apptChoice);
                    uploadDiagrams();
                    uploadToStorage();
                    goBacktoDetails();
                }
            }
        });

    }

    public void goBacktoDetails() {
        Log.d("Test","Go back to details triggered");
        Intent intent2 = new Intent(this, MainActivity.class);
        Toast.makeText(this,"Appointment Completed",Toast.LENGTH_LONG).show();
        startActivity(intent2);
    }


    private void SelectImage() {
        final CharSequence[] items= {"Camera","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Appointments.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {
                    openBackCamera();

                } else if (items[i].equals("cancel")) {

                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == 1) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                cameraphoto = stream.toByteArray();
                Bmp = BitmapFactory.decodeByteArray(cameraphoto, 0, cameraphoto.length);
                pastPicture.setImageBitmap(Bitmap.createScaledBitmap(Bmp,pastPicture.getWidth(),pastPicture.getHeight(),false));

            }
        }
    }

    public void uploadToStorage () {
        getallPhotoNames();
        getPhoto1 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo1");
        getPhoto2 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo2");
        getPhoto3 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo3");
        getPhoto4 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo4");
        getinit = FirebaseStorage.getInstance().getReference().child(finalpushID).child("mostRecentPic");
        final DatabaseReference getmostRecentName = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("mostRecentName");
        final DatabaseReference getPhoto1Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
        final DatabaseReference getPhoto2Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
        final DatabaseReference getPhoto3Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
        final DatabaseReference getPhoto4Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");

        DatabaseReference checkReference2;
        checkReference2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
        checkReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Photo4")){
                   getPhoto4.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                       @Override
                       public void onSuccess(byte[] bytes) {
                           photo4 = bytes;
                           getPhoto3.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                               @Override
                               public void onSuccess(byte[] bytes) {
                                   photo3 = bytes;
                                   getPhoto2.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                       @Override
                                       public void onSuccess(byte[] bytes) {
                                           photo2 = bytes;
                                           getPhoto1.getBytes(2048*2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                               @Override
                                               public void onSuccess(byte[] bytes) {
                                                   String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                                   photo1 = bytes;
                                                   getPhoto3Name.setValue(Photo3Name);
                                                   getPhoto3.putBytes(photo4);
                                                   getPhoto2Name.setValue(Photo3Name);
                                                   getPhoto2.putBytes(photo3);
                                                   getPhoto1Name.setValue(Photo2Name);
                                                   getPhoto1.putBytes(photo2);
                                                   getmostRecentName.setValue(Photo1Name);
                                                   getinit.putBytes(photo1);
                                                   getmostRecentName.setValue(simpleDateFormat);
                                                   if(cameraphoto == null){
                                                       getPhoto4.putFile(selectedImageUri);
                                                       pastPicture.setImageURI(selectedImageUri);
                                                   }
                                                   else{
                                                       getPhoto4.putBytes(cameraphoto);
                                                       pastPicture.setImageBitmap(myBitmap);
                                                   }


                                               }
                                           });
                                       }
                                   });
                               }
                           });
                       }
                   });
                    goBacktoDetails();
                }
                else if(dataSnapshot.hasChild("Photo3")){

                    final DatabaseReference savePhoto4Marker = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4");
                    final StorageReference savePhoto4 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo4");
                    if(cameraphoto == null){
                        savePhoto4.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto4Marker.setValue(selectedImageUri.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo4Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");
                                photo4Name.setValue(simpleDateFormat);
                                cameraphoto = null;
                                pastPicture.setImageURI(selectedImageUri);
                            }
                        });
                    }
                    else
                    {
                        savePhoto4.putBytes(cameraphoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto4Marker.setValue(cameraphoto.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo4Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");
                                photo4Name.setValue(simpleDateFormat);
                                cameraphoto = null;
                                pastPicture.setImageBitmap(myBitmap);
                            }
                        });
                    }

                }
               else if(dataSnapshot.hasChild("Photo2")){

                    final DatabaseReference savePhoto3Marker = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3");
                    final StorageReference savePhoto3 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo3");
                    if(cameraphoto == null){
                        savePhoto3.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto3Marker.setValue(selectedImageUri.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo3Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
                                photo3Name.setValue(simpleDateFormat);
                                pastPicture.setImageURI(selectedImageUri);
                                cameraphoto = null;
                            }
                        });
                    }
                    else {
                        savePhoto3.putBytes(cameraphoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto3Marker.setValue(cameraphoto.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo3Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
                                photo3Name.setValue(simpleDateFormat);
                                pastPicture.setImageBitmap(myBitmap);
                                cameraphoto = null;
                            }
                        });
                    }

                }
                else if(dataSnapshot.hasChild("Photo1")) {

                    final DatabaseReference savePhoto2Marker = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2");
                    final StorageReference savePhoto2 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo2");
                    if(cameraphoto == null) {
                        savePhoto2.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto2Marker.setValue(selectedImageUri.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo2Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
                                pastPicture.setImageURI(selectedImageUri);
                                photo2Name.setValue(simpleDateFormat);
                                cameraphoto = null;;
                            }
                        });
                    }
                    else {
                        savePhoto2.putBytes(cameraphoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto2Marker.setValue(cameraphoto.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo2Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
                                pastPicture.setImageBitmap(myBitmap);
                                photo2Name.setValue(simpleDateFormat);
                                cameraphoto = null;
                            }
                        });
                    }

                }
                else {
                    final DatabaseReference savePhoto1Marker = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1");

                    StorageReference savePhoto1 = FirebaseStorage.getInstance().getReference().child(finalpushID).child("Photo1");
                    if(cameraphoto == null) {
                        savePhoto1.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto1Marker.setValue(selectedImageUri.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo1Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
                                photo1Name.setValue(simpleDateFormat);
                                pastPicture.setImageURI(selectedImageUri);
                                cameraphoto = null;
                            }
                        });
                    }
                    else {
                        savePhoto1.putBytes(cameraphoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                savePhoto1Marker.setValue(cameraphoto.toString());
                                String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                                DatabaseReference photo1Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
                                pastPicture.setImageBitmap(myBitmap);
                                photo1Name.setValue(simpleDateFormat);
                                cameraphoto = null;
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public class MyDrawView extends View {

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private Paint mPaint, mPaintE;

        public MyDrawView(Context c) {
            super(c);

            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFF000000);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(3);
            /////////////////////////////
            mPaintE = new Paint();
            mPaintE.setAntiAlias(true);
            mPaintE.setDither(true);
            mPaintE.setColor(Color.TRANSPARENT);
            mPaintE.setStyle(Paint.Style.STROKE);
            mPaintE.setStrokeJoin(Paint.Join.ROUND);
            mPaintE.setStrokeCap(Paint.Cap.ROUND);
            mPaintE.setStrokeWidth(3);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath(mPath, mPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        public void clear(){
            mBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            System.gc();
        }}
    private String pictureImagePath = "";
    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
         startActivityForResult(cameraIntent, 1);
    }
    public void uploadDiagrams(){
        getallDiaNames();
        DatabaseReference checkchildren = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID);
        checkchildren.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Diagram4")){
                    diagram1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            diagram1=uri;
                            diagram2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    diagram2 = uri;
                                    diagram3Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            diagram3 = uri;
                                            diagram4Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    diagram4 = uri;
                                                    diagram0Ref.putFile(diagram1);
                                                    diagram1Ref.putFile(diagram2);
                                                    diagram2Ref.putFile(diagram3);
                                                    diagram3Ref.putFile(diagram4);
                                                    diagram4Ref.putBytes(diagramFinal);
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                        }
                    });
                }
               else if(dataSnapshot.hasChild("Diagram3")){
                    Log.d("fuck","found dia 3");
                    diagram4Ref.putBytes(diagramFinal).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            DatabaseReference diagram4mark = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Diagram4");
                            diagram4mark.setValue("dia4");
                        }
                    });
                }
               else if(dataSnapshot.hasChild("Diagram2")){
                    Log.d("fuck","found dia 2");
                    diagram3Ref.putBytes(diagramFinal).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            DatabaseReference diagram3mark = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Diagram3");
                            diagram3mark.setValue("dia3");
                        }
                    });
                }
                else if(dataSnapshot.hasChild("Diagram1")){
                    Log.d("fuck","found dia 1");
                    diagram2Ref.putBytes(diagramFinal).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            DatabaseReference diagram2mark = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Diagram2");
                            diagram2mark.setValue("dia2");;
                        }
                    });
                }
                else{
                    diagram1Ref.putBytes(diagramFinal).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            DatabaseReference diagram1mark = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Diagram1");
                            diagram1mark.setValue("dia1");
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getallDiaNames(){
        DatabaseReference getmostRecentName = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("mostRecentName");
        final DatabaseReference getPhoto1Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
        final DatabaseReference getPhoto2Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
        final DatabaseReference getPhoto3Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
        final DatabaseReference getPhoto4Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");
        getmostRecentName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diagram0name = dataSnapshot.getValue(String.class);
                getPhoto1Name.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        diagram1name = dataSnapshot.getValue(String.class);
                        getPhoto2Name.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                diagram2name = dataSnapshot.getValue(String.class);
                                getPhoto3Name.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        diagram3name = dataSnapshot.getValue(String.class);
                                        getPhoto4Name.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                diagram4name = dataSnapshot.getValue(String.class);
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
    public void getallPhotoNames(){
        DatabaseReference getmostRecentName = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("mostRecentName");
        final DatabaseReference getPhoto1Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo1Name");
        final DatabaseReference getPhoto2Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo2Name");
        final DatabaseReference getPhoto3Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo3Name");
        final DatabaseReference getPhoto4Name = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalpushID).child("Photo4Name");
       getmostRecentName.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mostRecentName = dataSnapshot.getValue(String.class);
               getPhoto1Name.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       Photo1Name = dataSnapshot.getValue(String.class);
                       getPhoto2Name.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               Photo2Name = dataSnapshot.getValue(String.class);
                               getPhoto3Name.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       Photo3Name = dataSnapshot.getValue(String.class);
                                       getPhoto4Name.addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               Photo4Name = dataSnapshot.getValue(String.class);
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
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to leave appointment?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       Intent leaveIntent = new Intent(Appointments.this,ClientList.class);
                       startActivity(leaveIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("test","Staying here");
                    }
                });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }

    public void AllowEmojis(){
        EmojiCompat.Config config = new BundledEmojiCompatConfig(Appointments.this);
        EmojiCompat.init(config);

    }
}
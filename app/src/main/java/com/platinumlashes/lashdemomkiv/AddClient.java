package com.platinumlashes.lashdemomkiv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddClient extends AppCompatActivity {
    ImageView mimageToUpload;
    ClientModel cli;
    public int fnamecheck,mnamecheck,lnamecheck;
    Button buploadImage;
    Integer Request_camera = 1, Select_file = 0;
    private EditText mfNameText, mlNameText, mmNameText;
    private Button maddDataButton;
    private DatabaseReference mDatabase;
    Context context;
    public String fnamevalue, initImage, lnamevalue, mnamevalue, userID, userID2;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    public StorageReference mStorageReference;
    public Uri selectedImageUri;
    public byte[] cameraphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
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
        mimageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        mfNameText = (EditText) findViewById(R.id.fName);
        mlNameText = (EditText) findViewById(R.id.lName);
        mmNameText = (EditText) findViewById(R.id.mName);
        maddDataButton = (Button) findViewById(R.id.addDataBtn);
        cli = new ClientModel();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy    hh:mm");

        userID = UUID.randomUUID().toString();
        userID2 = userID;

        buploadImage = (Button) findViewById(R.id.uploadBtn);
        buploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();

        maddDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Clients").child(userID);
                fnamevalue = mfNameText.getText().toString();
                lnamevalue = mlNameText.getText().toString();
                mnamevalue = mmNameText.getText().toString();
                cli.setIdNum(userID2);
                if (fnamevalue != null && !fnamevalue.isEmpty() && !fnamevalue.equals("null")) {
                    cli.setfName(fnamevalue);
                    fnamecheck = 1;
                } else {
                    new AlertDialog.Builder(AddClient.this)
                            .setTitle("Information Missing")
                            .setMessage("Client's First Name is Blank")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Reset();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert).show();
                }
                CheckMName();
                CheckLName();
                if(fnamecheck == 1 && mnamecheck == 1 && lnamecheck ==1)
                {
                    mDatabase.setValue(cli);
                    DatabaseReference markMostRecent = FirebaseDatabase.getInstance().getReference().child("Clients").child(userID2).child("mostRecentPic");
                    markMostRecent.setValue("I'm pointless");
                    String initimagenameref = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                    Log.d("Test",initimagenameref);
                    DatabaseReference uploadnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(userID2).child("mostRecentName");
                    uploadnameref.setValue(initimagenameref);
                    Toast.makeText(AddClient.this, fnamevalue + " " + lnamevalue + " added", Toast.LENGTH_LONG).show();
                    Log.d("test",fnamevalue);
                    Log.d("test",mnamevalue);
                    Log.d("test",lnamevalue);
                    goToCard();
                }


            }
        });

    }
public void goToCard(){
    Intent intent = new Intent(AddClient.this, ClientDetailActivity.class);
    intent.putExtra("first",fnamevalue);
    intent.putExtra("mid",mnamevalue);
    intent.putExtra("last",lnamevalue);
    String testbb = ""+userID2;
    intent.putExtra("ID",testbb);
    startActivity(intent);
}
    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddClient.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {
                    openBackCamera();

                }  else if (items[i].equals("cancel")) {
                    Reset();
                }
            }
        });
        builder.show();
    }

    private String pictureImagePath = "";

    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
         startActivityForResult(cameraIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        StorageReference uploadref = FirebaseStorage.getInstance().getReference().child(userID2).child("mostRecentPic");
        StorageReference nulldiagram = FirebaseStorage.getInstance().getReference().child("diagramfinal1.jpg");
        final StorageReference nulldiagramRef = FirebaseStorage.getInstance().getReference().child(userID2).child("Diagram0");
        DatabaseReference uploadnameref = FirebaseDatabase.getInstance().getReference().child("Clients").child(userID2).child("mostRecentName");

        if (requestCode == 1) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                cameraphoto = stream.toByteArray();
                final DatabaseReference saveInit = FirebaseDatabase.getInstance().getReference().child("Clients").child(userID).child("mostRecentURI");
                nulldiagram.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       Uri nullDiagramuri = uri;
                        nulldiagramRef.putFile(nullDiagramuri);

                    }
                });
                String initimagenameref = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                uploadref.putBytes(cameraphoto);
                mimageToUpload.setImageBitmap(myBitmap);

            }
        }
    }

    public void Reset() {
        Intent intent = new Intent(this, AddClient.class);
        startActivity(intent);
    }

    public void Leave() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void CheckMName() {
        Log.d("test",mnamevalue);
        if (mnamevalue != null && !mnamevalue.isEmpty() && !mnamevalue.equals("null")){
            cli.setmName(mnamevalue);
            mnamecheck = 1;
            Log.d("test","found a middle name");
        }
        else
        {
            Log.d("test","missing a middle name");
            new AlertDialog.Builder(AddClient.this)
                    .setTitle("Information Missing")
                    .setMessage("Client's Middle Name is Blank")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Reset();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    public void CheckLName(){
        if (lnamevalue != null && !lnamevalue.isEmpty() && !lnamevalue.equals("null")) {
            cli.setlName(lnamevalue);
            lnamecheck=1;
        } else {
            new AlertDialog.Builder(AddClient.this)
                    .setTitle("Information Missing")
                    .setMessage("Client's Last Name is Blank")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Reset();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }
}

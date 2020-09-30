package com.platinumlashes.lashdemomkiv;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button mclientlistbutton,AddEmployeeButton,AddEmpButton,LashReleaseBtn,TodayButton,SignOutButton,COVIDTESTBUTTON;
    public String name,userEmail,pin,userEmailCap,passwordEntered,passwordFinal,emailEntered,emailFinal;
    public TextView EmployeeName,EmployeePin,userName;
    public String m_Text = "";
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference EmployeeNameRef,PinEmailRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ASK FOR PERMISSION TO ACCESS STORAGE AND CAMERA
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
        // END OF REQUEST (SHOULD ONLY APPEAR ONCE)


        //Button & Text Defs
        mAuth = FirebaseAuth.getInstance();
        AddEmpButton = (Button) findViewById(R.id.addEmpBtn);
        LashReleaseBtn = (Button) findViewById(R.id.lashReleaseBtn);
        mclientlistbutton = (Button) findViewById(R.id.clientListBtn);
        SignOutButton = (Button) findViewById(R.id.signOutBtn);
        userName = (TextView) findViewById(R.id.usersName);
        //End of Button & Text Defs

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            CheckForAdmin();
        }
        else
        {
            SignIn();
        }

        //OnClick Button Rules
        LashReleaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent releaseIntent = new Intent(MainActivity.this,Questionaire.class);
                startActivity(releaseIntent);
            }
        });

        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                userName.setText("");
                AddEmpButton.setVisibility(View.INVISIBLE);
                LashReleaseBtn.setVisibility(View.INVISIBLE);
                SignIn();
            }
        });

        mclientlistbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientList.class );
                startActivity(intent);
            }
        });

        AddEmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEmp();
            }
        });
        //End of Button Rules



    }

    @Override
    public void onStart() {
        super.onStart();

    }



    public void SignIn(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        final AlertDialog.Builder loginBuilder = new AlertDialog.Builder(this);
        loginBuilder.setCancelable(false);
        loginBuilder.setTitle("Enter Employee Pin");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        loginBuilder.setView(input);
        loginBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(m_Text.length() !=6)
                {
                    loginBuilder.create().cancel();
                    Toast.makeText(MainActivity.this,"Bad Login Try again",Toast.LENGTH_LONG).show();
                    SignIn();

                }
                DatabaseReference getNameRef = FirebaseDatabase.getInstance().getReference().child("Pins").child(m_Text).child("Email");
                DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Pins").child(m_Text);
                checkRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==0){
                            Long count = dataSnapshot.getChildrenCount();
                            String count2 = count.toString();
                            Log.d("test",count2);
                            loginBuilder.create().cancel();
                            Toast.makeText(MainActivity.this,"Bad Login Try again",Toast.LENGTH_LONG).show();
                            SignIn();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                getNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String Email = dataSnapshot.getValue(String.class);

                        if (Email != null) {
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.signInWithEmailAndPassword(Email, m_Text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    CheckForAdmin();
                                }

                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        loginBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SignIn();
            }
        });

        loginBuilder.show();

    }

    @Override
    public void onBackPressed() {
        Log.d("test","No main menu boi");
    }



    public void CheckForAdmin(){

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            SignIn();
        }
        else {
            userEmail = currentUser.getEmail();
            Log.d("test1", userEmail);
            int index = userEmail.indexOf("@");
            final String emailClipping = userEmail.substring(0,index);
            Log.d("test1",emailClipping);


            DatabaseReference AdminListReference = FirebaseDatabase.getInstance().getReference().child("Employees").child("Admins");
            AdminListReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(emailClipping)){

                        AddEmpButton.setVisibility(View.VISIBLE);
                        LashReleaseBtn.setVisibility(View.VISIBLE);
                    }
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        String userEmail = user.getEmail();
                        userEmail = userEmail.split("@")[0];
                        userEmailCap = userEmail.substring(0,1).toUpperCase()+userEmail.substring(1);
                        userName.setText(userEmailCap);
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void AddEmp(){
        final AlertDialog.Builder loginBuilder = new AlertDialog.Builder(this);
        loginBuilder.setCancelable(false);
        loginBuilder.setTitle("Register Employee");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.logindialog, null);

        //Definitions need to be here due to dialogview
        final EditText emailBox = (EditText) dialogView.findViewById(R.id.userName);
        final EditText passBox = (EditText) dialogView.findViewById(R.id.password);


        loginBuilder.setView(dialogView);
        loginBuilder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                passwordEntered = passBox.getText().toString();
                if(passwordEntered.length()==4){
                    passwordFinal = "00"+passwordEntered;
                    emailEntered = emailBox.getText().toString();
                    emailFinal = emailEntered+"@gmail.com";
                    mAuth.createUserWithEmailAndPassword(emailFinal, passwordFinal).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                EmployeeNameRef = FirebaseDatabase.getInstance().getReference().child("Employees").child(emailEntered).child("Name");
                                EmployeeNameRef.setValue(emailEntered);
                                PinEmailRef = FirebaseDatabase.getInstance().getReference().child("Pins").child(passwordFinal).child("Email");
                                PinEmailRef.setValue(emailFinal);
                                mAuth.signOut();
                                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                startActivity(intent);


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this,"Failed to add Employee",Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Password not long enough",Toast.LENGTH_LONG).show();
                    AddEmp();
                }



            }
        });





        loginBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();


            }
        });

        loginBuilder.show();
    }
}






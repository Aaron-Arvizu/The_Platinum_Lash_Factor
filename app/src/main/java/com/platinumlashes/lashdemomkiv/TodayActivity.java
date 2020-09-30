package com.platinumlashes.lashdemomkiv;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayActivity extends AppCompatActivity {
public Button GlenButton, ArcButton, LVButton;
public List<String> idlist = new ArrayList<String>();
public List<String> LVidList = new ArrayList<String>();
public List<String> GlenidList = new ArrayList<String>();
public List<String> ArcidList = new ArrayList<String>();
TClientModel TCli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        TCli = new TClientModel();

        // Define Buttons
        GlenButton = findViewById(R.id.glendoraBtn);
        LVButton = findViewById(R.id.laverneBtn);
        ArcButton = findViewById(R.id.arcadiaBtn);

        GetTodaysClients();
        LVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LVSort();
            }
        });

        GlenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlendoraSort();
            }
        });

        ArcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcSort();
            }
        });
    }

    public void GetTodaysClients() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = sdf.format(new Date());
        DatabaseReference pplRef = FirebaseDatabase.getInstance().getReference().child("Clients");
        pplRef.orderByChild("Day").equalTo(currentDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    String todayID = ds.getKey();
                    idlist.add(todayID);
                    //DatabaseReference testRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(todayID).child("fName");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void LVSort(){
        for( int i=0; i<idlist.size(); i++){
            final String lvid = idlist.get(i);
            DatabaseReference LVREF = FirebaseDatabase.getInstance().getReference().child("Clients").child(idlist.get(i)).child("Location");
            LVREF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Local = dataSnapshot.getValue(String.class);
                    if(Local.equals("La Verne")){
                        Log.d("test",lvid);
                        LVidList.add(lvid);
                        TCli.setIdNum2(lvid);
                        DatabaseReference getf = FirebaseDatabase.getInstance().getReference().child("Clients").child(lvid).child("fName");
                        getf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String first = dataSnapshot.getValue(String.class);
                                DatabaseReference getm = FirebaseDatabase.getInstance().getReference().child("Clients").child(lvid).child("mName");
                                getm.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String mid = dataSnapshot.getValue(String.class);
                                        DatabaseReference getl = FirebaseDatabase.getInstance().getReference().child("Clients").child(lvid).child("lName");
                                        getl.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                final String last = dataSnapshot.getValue(String.class);
                                                TCli.setfName2(first);
                                                TCli.setmName2(mid);
                                                TCli.setlName2(last);
                                                SimpleDateFormat today = new SimpleDateFormat("MM-dd-yyyy");
                                                String currentDate = today.format(new Date());
                                                DatabaseReference savePerson = FirebaseDatabase.getInstance().getReference().child("Days").child("LVDAYS").child(currentDate).child(lvid);
                                                savePerson.setValue(TCli);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void ArcSort(){
        for( int i=0; i<idlist.size(); i++){
            final String arcid = idlist.get(i);
            DatabaseReference ArcREF = FirebaseDatabase.getInstance().getReference().child("Clients").child(idlist.get(i)).child("Location");
            ArcREF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Local = dataSnapshot.getValue(String.class);
                    if(Local.equals("Arcadia")){
                        Log.d("test",arcid);
                        ArcidList.add(arcid);
                        Log.d("test",ArcidList.toString());
                        TCli.setIdNum2(arcid);
                        DatabaseReference getf = FirebaseDatabase.getInstance().getReference().child("Clients").child(arcid).child("fName");
                        getf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String first = dataSnapshot.getValue(String.class);
                                DatabaseReference getm = FirebaseDatabase.getInstance().getReference().child("Clients").child(arcid).child("mName");
                                getm.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String mid = dataSnapshot.getValue(String.class);
                                        DatabaseReference getl = FirebaseDatabase.getInstance().getReference().child("Clients").child(arcid).child("lName");
                                        getl.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                final String last = dataSnapshot.getValue(String.class);
                                                TCli.setfName2(first);
                                                TCli.setmName2(mid);
                                                TCli.setlName2(last);
                                                SimpleDateFormat today = new SimpleDateFormat("MM-dd-yyyy");
                                                String currentDate = today.format(new Date());
                                                DatabaseReference savePerson = FirebaseDatabase.getInstance().getReference().child("Days").child("ARCDAYS").child(currentDate).child(arcid);
                                                savePerson.setValue(TCli);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void GlendoraSort(){
        for( int i=0; i<idlist.size(); i++){
            final String glenid = idlist.get(i);
            DatabaseReference GlenREF = FirebaseDatabase.getInstance().getReference().child("Clients").child(idlist.get(i)).child("Location");
            GlenREF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Local = dataSnapshot.getValue(String.class);
                    if(Local.equals("Glendora")){
                        Log.d("test",glenid);
                        GlenidList.add(glenid);
                        Log.d("test2",GlenidList.toString());
                        String glenid2 = glenid;
                        TCli.setIdNum2(glenid2);
                        DatabaseReference getf = FirebaseDatabase.getInstance().getReference().child("Clients").child(glenid).child("fName");
                        getf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String first = dataSnapshot.getValue(String.class);
                                DatabaseReference getm = FirebaseDatabase.getInstance().getReference().child("Clients").child(glenid).child("mName");
                                getm.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String mid = dataSnapshot.getValue(String.class);
                                        DatabaseReference getl = FirebaseDatabase.getInstance().getReference().child("Clients").child(glenid).child("lName");
                                        getl.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                final String last = dataSnapshot.getValue(String.class);
                                                Log.d("test",glenid +" "+first+" "+mid+" "+last);
                                                TCli.setfName2(first);
                                                TCli.setmName2(mid);
                                                TCli.setlName2(last);
                                                SimpleDateFormat today = new SimpleDateFormat("MM-dd-yyyy");
                                                String currentDate = today.format(new Date());
                                                DatabaseReference savePerson = FirebaseDatabase.getInstance().getReference().child("Days").child("GlenDays").child(currentDate).child(glenid);
                                                savePerson.setValue(TCli);

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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Intent intent = new Intent(TodayActivity.this,GlenClientList.class);
        startActivity(intent);
    }
}

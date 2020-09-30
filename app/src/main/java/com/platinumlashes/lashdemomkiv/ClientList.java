package com.platinumlashes.lashdemomkiv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClientList extends AppCompatActivity implements Serializable {

    DatabaseReference reference;
    RecyclerView recyclerView;
    public ArrayList<ClientModel> list;
    MyAdapter adapter;
    public String isLoaded;
    public TextView SearchBar;
    //ON BACK PRESSED RETURN TO MAIN MENU
    public void onBackPressed(){
        Intent returnIntent = new Intent(ClientList.this,MainActivity.class);
        startActivity(returnIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        //Definitions
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        SearchBar = findViewById(R.id.edttxt);
        //End of Definitions


        //MAKE THE SEARCH BAR INVISIBLE (JUST UNTIL THE RECYCLER VIEW LOADS)
        SearchBar.setVisibility(View.INVISIBLE);

        //SearchBarFunctions
        SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
            private void filter(String text) {
                ArrayList<ClientModel> filteredList = new ArrayList<>();

                for (ClientModel client : list) {
                    if (client.getfName().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(client);
                    }
                }
                adapter.filterList(filteredList);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ClientModel>();


        reference = FirebaseDatabase.getInstance().getReference().child("Clients");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    ClientModel p = dataSnapshot1.getValue(ClientModel.class);
                    list.add(p);
                    Collections.sort(list, new Comparator<ClientModel>(){

                        public int compare(ClientModel d1, ClientModel d2){
                            return d1.getfName().compareTo(d2.getfName());
                        }
                    });
                }
                isLoaded = "n";
                adapter = new MyAdapter(ClientList.this,list);
                recyclerView.setAdapter(adapter);
                recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Log.d("test","Loaded");
                        SearchBar.setVisibility(View.VISIBLE);
                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ClientList.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
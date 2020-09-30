package com.platinumlashes.lashdemomkiv;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GlenClientList extends AppCompatActivity implements Serializable {

    DatabaseReference reference;
    RecyclerView recyclerView2;
    public ArrayList<TClientModel> tlist;
    public FirebaseRecyclerOptions<TClientModel> cli;
    public FirebaseRecyclerAdapter<TClientModel,GFVH> tadapter;
    List<TClientModel> list;
    public DatabaseReference ref;

    protected void onStart(){
        super.onStart();
        tadapter.startListening();
    }

    protected void onStop(){
        super.onStop();
        tadapter.stopListening();
    }


    public void onBackPressed() {
        Intent returnIntent = new Intent(GlenClientList.this, MainActivity.class);
        startActivity(returnIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        recyclerView2 = (RecyclerView) findViewById(R.id.myRecycler);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(LM);
        list = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("Days").child("Glendays");
        ref.keepSynced(true);
        cli = new FirebaseRecyclerOptions.Builder<TClientModel>().setQuery(ref,TClientModel.class).build();
        tadapter = new FirebaseRecyclerAdapter<TClientModel, GFVH>(cli) {
            @Override
            protected void onBindViewHolder(@NonNull GFVH holder, int position, @NonNull TClientModel model) {
                holder.fName.setText(model.getfName2());
                Log.d("test",model.getfName2());
                holder.mName.setText(model.getmName2());
                holder.lName.setText(model.getlName2());
            }

            @NonNull
            @Override
            public GFVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new GFVH(LayoutInflater.from(GlenClientList.this).inflate(R.layout.tcardview,viewGroup));
            }
        };
recyclerView2.setAdapter(tadapter);
    }

}
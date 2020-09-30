package com.platinumlashes.lashdemomkiv;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Serializable{

    Context context;
    ArrayList<ClientModel> profiles;
    public  String testbb;


    public MyAdapter(Context c, ArrayList<ClientModel> p)
    {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        final ClientModel clientModel = profiles.get(position);
        myViewHolder.firstname.setText(profiles.get(position).getfName());
        myViewHolder.middlename.setText(profiles.get(position).getmName());
        myViewHolder.lastname.setText(profiles.get(position).getlName());

        myViewHolder.detailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ClientDetailActivity.class);
                intent.putExtra("first",clientModel.getfName());
                intent.putExtra("mid",clientModel.getmName());
                intent.putExtra("last",clientModel.getlName());
                testbb = ""+clientModel.getIdNum();
                intent.putExtra("ID",testbb);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements Serializable
    {
        TextView firstname,middlename,lastname;
       Button detailsbtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            firstname=(TextView) itemView.findViewById(R.id.fName);
            middlename= (TextView) itemView.findViewById(R.id.mName);
            lastname = (TextView) itemView.findViewById(R.id.lName);
            detailsbtn = (Button) itemView.findViewById(R.id.clientExpandBtn);
        }
    }
    public void filterList (ArrayList<ClientModel> filteredList) {
        profiles = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

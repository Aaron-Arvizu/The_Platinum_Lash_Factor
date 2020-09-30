package com.platinumlashes.lashdemomkiv;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GFVH extends RecyclerView.ViewHolder {
    public TextView fName,mName,lName;

    public GFVH(@NonNull View itemView){
        super(itemView);

        fName = itemView.findViewById(R.id.fName3);
        mName = itemView.findViewById(R.id.mName3);
        lName = itemView.findViewById(R.id.lName3);
        Button ViewCardButton = itemView.findViewById(R.id.clientExpandBtn);
    }
}

package com.platinumlashes.lashdemomkiv;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionaireForReturning extends AppCompatActivity {

    public String Fname,Mname,Lname,Zip,Bday,Add,HowDYHAU,BMonthSt,BDAYSt,Emailtxt;
    public TextView FirstName,MiddleInit,LastName,Address,Birthday,ZipCode,HDYHAU,Email;
    public Switch Switch1,Switch2,Switch3,Switch4,Switch5;
    public Intent QuestionaireIntent;
    ClientModel clientModel;
    public Intent QtoL;
    public Spinner BDaySppiner,BMonthSPinner;
    public ArrayAdapter<String> BDayAdapter, BMonthAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

        Email = (TextView) findViewById(R.id.emailans);
        QuestionaireIntent = new Intent(QuestionaireForReturning.this,LashReleaseActivityForReturning.class);
        FirstName = (TextView) findViewById(R.id.firstName);
        LastName= (TextView)findViewById(R.id.lastName);
        MiddleInit = (TextView) findViewById(R.id.middleInit);
        final Button ContinueButton = (Button) findViewById(R.id.finishBtn2);
        Switch1 = (Switch) findViewById(R.id.switch1);
        Switch2 = (Switch) findViewById(R.id.switch2);
        Switch3 = (Switch) findViewById(R.id.switch7);
        Switch4 = (Switch) findViewById(R.id.switch4);
        Switch5 = (Switch) findViewById(R.id.switch5);
        Address = (TextView) findViewById(R.id.address);
         BMonthSPinner = (Spinner) findViewById(R.id.bMonthSpinner);
         BDaySppiner = (Spinner) findViewById(R.id.bDaySpinner);
        BMonthAdapter = new ArrayAdapter<String>(QuestionaireForReturning.this,R.layout.apptspinneritem,getResources().getStringArray(R.array.Months));
        BDayAdapter = new ArrayAdapter<String>(QuestionaireForReturning.this,R.layout.apptspinneritem,getResources().getStringArray(R.array.Days));
        BMonthSPinner.setAdapter(BMonthAdapter);
        BDaySppiner.setAdapter(BDayAdapter);
        ZipCode = (TextView) findViewById(R.id.zipCode);
        HDYHAU = (TextView) findViewById(R.id.hdyhau);
        Intent intent = getIntent();
        if(intent.hasExtra("Please")){
            String ID4 = getIntent().getStringExtra("Please");
            QuestionaireIntent.putExtra("ID4",ID4);

        }
        else{
            Log.d("test","Nothing");
        }
        ContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckText();

            }
        });


    }

    public void CheckText(){
        Emailtxt = Email.getText().toString();
        Fname = FirstName.getText().toString();
        Log.d("test",Fname);
        Mname = MiddleInit.getText().toString();
        Lname = LastName.getText().toString();
        Zip = ZipCode.getText().toString();
        Add = Address.getText().toString();
        HowDYHAU = HDYHAU.getText().toString();
        BMonthSt = BMonthSPinner.getSelectedItem().toString();
        BDAYSt = BDaySppiner.getSelectedItem().toString();


        if(Fname.equals("")){

        Toast.makeText(this,"Please enter your first name",Toast.LENGTH_LONG).show();

        }
        else if(Emailtxt.equals("")){

            Toast.makeText(this,"Please enter your Email address",Toast.LENGTH_LONG).show();
        }
        else if(Mname.equals("")){

            Toast.makeText(this,"Please enter your middle initial",Toast.LENGTH_LONG).show();

        }
        else if(Lname.equals("")){

            Toast.makeText(this,"Please enter your last name",Toast.LENGTH_LONG).show();

        }
        else if(ZipCode.equals("")){

            Toast.makeText(this,"Please enter your zip code",Toast.LENGTH_LONG).show();

        }
        else if(Address.equals("")){

            Toast.makeText(this,"Please enter your address",Toast.LENGTH_LONG).show();

        }
        else if(BMonthSt.equals("Month")){
            Toast.makeText(this,"Please select your Birth Month",Toast.LENGTH_LONG).show();
        }
        else if(BDAYSt.equals("Day")){

            Toast.makeText(this,"Please select your Birthday",Toast.LENGTH_LONG).show();
        }
        else
        {
           Intent intent = new Intent(QuestionaireForReturning.this,LashReleaseActivity.class);
            String Birthday = BMonthSt + "/" +BDAYSt;
            QuestionaireIntent.putExtra("Firstn",Fname);
            QuestionaireIntent.putExtra("Midllen",Mname);
            QuestionaireIntent.putExtra("Lastn",Lname);
            QuestionaireIntent.putExtra("Bday",Birthday);
            QuestionaireIntent.putExtra("Add",Add);
            QuestionaireIntent.putExtra("Zip",Zip);
            QuestionaireIntent.putExtra("HDYHAU",HowDYHAU);
            Log.d("test","Extras pushed moving to CheckYNQuestions");
            CheckYNQuestions();


        }

    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public void CheckYNQuestions(){
String Ans1,Ans2,Ans3,Ans4,Ans5;

        if(Switch1.isChecked()){
            Ans1  = "Has worn lashes before.";
        }
        else {
            Ans1 = "No Lash experience.";
        }

        if(Switch2.isChecked()){
            Ans2 = "Allergic to Glue or Adhesives.";
        }
        else
        {
            Ans2="No glue allergies.";
        }
        if(Switch3.isChecked())
        {
            Ans3 = "Allergic to acrylic nails.";
        }
        else
        {
            Ans3 = "No acrylic nail allergies.";
        }
        if(Switch4.isChecked())
        {
            Ans4 = "Wears contact lenses.";
        }
        else
        {
            Ans4 = "Does not wear contact lenses.";
        }
        if(Switch5.isChecked())
        {
            Ans5 = "Has had eye surgery in the last 6 months.";

        }
        else{
            Ans5 = "No recent eye surgery.";
        }
        QuestionaireIntent.putExtra("Email",Emailtxt);
        QuestionaireIntent.putExtra("Ans1",Ans1);
        QuestionaireIntent.putExtra("Ans2",Ans2);
        QuestionaireIntent.putExtra("Ans3",Ans3);
        QuestionaireIntent.putExtra("Ans4",Ans4);
        QuestionaireIntent.putExtra("Ans5",Ans5);
        Log.d("test","passed new extras, moving to new activity");
        startActivity(QuestionaireIntent);

        }

public void onBackPressed(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Do you want to go back to Main Menu?")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent leaveIntent = new Intent(QuestionaireForReturning.this,MainActivity.class);
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
}

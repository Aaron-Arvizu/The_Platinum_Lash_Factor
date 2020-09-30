package com.platinumlashes.lashdemomkiv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CovidForm extends AppCompatActivity {
public TextView CovidNameFill,NextText;
public String CovidClientID,FirstName,LastName,Today;
public DatabaseReference GetFiretNameRef,GetLastNameRef,NameRefBase;
public ImageView InitialArea,NextButton;
public ConstraintLayout signLayout,FullPage;
public Bitmap savedDiagram,wholePage;
public SimpleDateFormat formatter;
public Date todayDate;
public byte[] signatureFinal, formFinal,WholePage,finalPicForCard;
public StorageReference TestStoreageRef, CovidSaveBase,CovidSave1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_form);

        //Definitions

        CovidNameFill = (TextView) findViewById(R.id.covidNameBox3);
        InitialArea = (ImageView) findViewById(R.id.initialArea);
        NextButton = (ImageView) findViewById(R.id.nxtBtn);
        NextButton.setVisibility(View.INVISIBLE);
        NextText = (TextView) findViewById(R.id.nxtText);
        NextText.setVisibility(View.INVISIBLE);
        FullPage = (ConstraintLayout) findViewById(R.id.fullPage);
        //End of Definitions

        //Pull ID from Client Detail Activity
        Bundle extras = getIntent().getExtras();
        CovidClientID = extras.getString("COVIDID");
        Log.d("test",CovidClientID+"CovidID");


        //References
        todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("MMddyyyy");
        Today = formatter.format(todayDate);
        NameRefBase = FirebaseDatabase.getInstance().getReference().child("Clients").child(CovidClientID).child("CovidReleaseDates").child(Today);
        GetFiretNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(CovidClientID).child("fName");
        GetLastNameRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(CovidClientID).child("lName");
        TestStoreageRef = FirebaseStorage.getInstance().getReference().child("TestCovid").child("test1");
        CovidSaveBase = FirebaseStorage.getInstance().getReference().child("00COVIDFORMS").child(CovidClientID);
        CovidSave1 = CovidSaveBase.child(Today+"COVID1");

        //End of References

        //OnClicks
        InitialArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CovidForm.MyDrawView myDrawView = new CovidForm.MyDrawView(CovidForm.this);
                if (myDrawView.getParent() != null) {
                    ((ViewGroup) myDrawView.getParent()).removeView(myDrawView);
                }
                final AlertDialog.Builder diagramDialog = new AlertDialog.Builder(CovidForm.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.signview, null);
                ImageView WhiteBack = (ImageView) dialogView.findViewById(R.id.imageView3);
                Button DoneButton = (Button) dialogView.findViewById(R.id.doneSignBtn);
                Button ClearButton = (Button) dialogView.findViewById(R.id.clearButton2);
                ConstraintLayout fullActivity = (ConstraintLayout) dialogView.findViewById(R.id.signLayout);
                signLayout = (ConstraintLayout) dialogView.findViewById(R.id.signLayout2);
                final AlertDialog SignDia = diagramDialog.create();
                SignDia.setCancelable(false);
                SignDia.setCanceledOnTouchOutside(false);
                SignDia.setView(dialogView);
                signLayout.addView(myDrawView);
                ClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CovidForm.MyDrawView) myDrawView).clear();
                    }
                });
                DoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get Signature Photo
                        signLayout.setDrawingCacheEnabled(true);
                        savedDiagram = Bitmap.createBitmap(signLayout.getDrawingCache());

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        savedDiagram.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        signatureFinal = stream.toByteArray();
                        InitialArea.setImageBitmap(savedDiagram);
                        NextButton.setVisibility(View.VISIBLE);
                        NextText.setVisibility(View.VISIBLE);
                        SignDia.dismiss();

                    }
                });
                NextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(savedDiagram.equals(null)) {
                            Log.d("test","no initials?");
                            Toast.makeText(getApplicationContext(),"Please Initial the bottom left",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Bitmap savedFullPage;
                            Log.d("test","initials are here");
                            Intent covid2intent = new Intent(CovidForm.this, CovidForm2.class);
                            FullPage.setDrawingCacheEnabled(true);
                            savedFullPage = Bitmap.createBitmap(FullPage.getDrawingCache());
                            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                            savedFullPage.compress(Bitmap.CompressFormat.PNG,100,stream2);
                            WholePage = stream2.toByteArray();
                            CovidSave1.putBytes(WholePage);
                            Log.d("FullPagetest",WholePage.toString());
                            covid2intent.putExtra("COVIDID2",CovidClientID);
                            startActivity(covid2intent);
                        }
                    }
                });
                SignDia.show();

            }
        });







        //End of OnClicks



    }
    @Override
    public void onBackPressed() {
        Intent ListIntnent = new Intent(CovidForm.this, ClientList.class);
        startActivity(ListIntnent);
    }

    public void SetName(){
        GetFiretNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirstName = dataSnapshot.getValue(String.class);
                GetLastNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LastName = dataSnapshot.getValue(String.class);
                        CovidNameFill.setText("           "+FirstName+" "+LastName+" ");
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
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
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

        public void clear() {
            mBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            System.gc();
        }
    }

}

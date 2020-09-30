package com.platinumlashes.lashdemomkiv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CovidForm2 extends AppCompatActivity {

public TextView NextText;
public String CovidClientID2,Today;
public ImageView NextButton2, InitialArea2;
public ConstraintLayout signLayout2,FullPageLayout;
public Bitmap savedDiagram2,savedFullPage;
public byte[] signatureFinal,WholePage;
public StorageReference CovidSaveBase,CovidSave2;
public Date todayDate;
public SimpleDateFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_form2);


        //Definitions
        NextButton2 = (ImageView) findViewById(R.id.nxtBtn2);
        NextButton2.setVisibility(View.INVISIBLE);
        InitialArea2 = (ImageView) findViewById(R.id.initialArea2);
        NextText = (TextView) findViewById(R.id.nxtText2);
        NextText.setVisibility(View.INVISIBLE);
        FullPageLayout = (ConstraintLayout) findViewById(R.id.FullPageLayout);
        //End of Defs


        //Get Client ID
        Bundle extras = getIntent().getExtras();
        CovidClientID2 = extras.getString("COVIDID2");
        Log.d("test",CovidClientID2+"CovidID");

        //References
        CovidSaveBase = FirebaseStorage.getInstance().getReference().child("00COVIDFORMS").child(CovidClientID2);

        todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("MMddyyyy");
        Today = formatter.format(todayDate);
        CovidSave2 = CovidSaveBase.child(Today+"COVID2");
        Log.d("test",Today);
        //End of References

        //OnClicks
        InitialArea2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CovidForm2.MyDrawView myDrawView = new CovidForm2.MyDrawView(CovidForm2.this);
                if (myDrawView.getParent() != null) {
                    ((ViewGroup) myDrawView.getParent()).removeView(myDrawView);
                }
                final AlertDialog.Builder diagramDialog = new AlertDialog.Builder(CovidForm2.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.signview, null);
                ImageView WhiteBack = (ImageView) dialogView.findViewById(R.id.imageView3);
                Button DoneButton = (Button) dialogView.findViewById(R.id.doneSignBtn);
                Button ClearButton = (Button) dialogView.findViewById(R.id.clearButton2);
                ConstraintLayout fullActivity = (ConstraintLayout) dialogView.findViewById(R.id.signLayout);
                signLayout2 = (ConstraintLayout) dialogView.findViewById(R.id.signLayout2);
                final AlertDialog SignDia = diagramDialog.create();
                SignDia.setCancelable(false);
                SignDia.setCanceledOnTouchOutside(false);
                SignDia.setView(dialogView);
                signLayout2.addView(myDrawView);
                ClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CovidForm2.MyDrawView) myDrawView).clear();
                    }
                });
                DoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get Signature Photo
                        signLayout2.setDrawingCacheEnabled(true);
                        savedDiagram2 = Bitmap.createBitmap(signLayout2.getDrawingCache());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        savedDiagram2.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        signatureFinal = stream.toByteArray();
                        InitialArea2.setImageBitmap(savedDiagram2);
                        NextButton2.setVisibility(View.VISIBLE);
                        NextText.setVisibility(View.VISIBLE);
                        SignDia.dismiss();

                    }
                });
                NextButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(savedDiagram2.equals(null)) {
                            Log.d("test","no initials?");
                            Toast.makeText(getApplicationContext(),"Please Initial the bottom left",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("test","initials are here");
                            FullPageLayout.setDrawingCacheEnabled(true);
                            savedFullPage= Bitmap.createBitmap(FullPageLayout.getDrawingCache());
                            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                            savedFullPage.compress(Bitmap.CompressFormat.PNG,100,stream2);
                            WholePage= stream2.toByteArray();
                            CovidSave2.putBytes(WholePage);
                            Intent covid3intent = new Intent(CovidForm2.this, CovidForm3.class);
                            covid3intent.putExtra("COVIDID3",CovidClientID2);
                            startActivity(covid3intent);
                        }
                    }
                });
                SignDia.show();

            }
        });

        //End of OnClicks
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

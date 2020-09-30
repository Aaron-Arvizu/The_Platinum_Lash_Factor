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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CovidForm3 extends AppCompatActivity {
public ImageView SignedArea;
public EditText NameView;
public TextView DateView;
public String Today,CovidClientID3,DisplayToday;
public SimpleDateFormat formatter,DisplayFormatter;
public DatabaseReference CovidDateRefs,CovidDateSave;
public Date todayDate;
public Button FinishButton;
public ConstraintLayout signLayout3,FullPageLayout;
public Bitmap savedDiagram3,savedFullPage;
public byte[] signatureFinal,WholePage;
public StorageReference CovidSaveBase,CovidSave3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_form3);

        //Definitions
        NameView = (EditText) findViewById(R.id.NameBox);
        SignedArea = (ImageView) findViewById(R.id.signArea);
        FinishButton = (Button) findViewById(R.id.finshButton);
        FinishButton.setVisibility(View.INVISIBLE);
        DateView = (TextView) findViewById(R.id.dateView);
        FullPageLayout = (ConstraintLayout) findViewById(R.id.fullPageLayout);
        todayDate = Calendar.getInstance().getTime();
        DisplayFormatter = new SimpleDateFormat("MMddyyyy");
        formatter = new SimpleDateFormat("MM-dd-yyyy");
        Today = formatter.format(todayDate);
        DisplayToday = DisplayFormatter.format(todayDate);
        // End of Defs

        //Get Client ID
        Bundle extras = getIntent().getExtras();
        CovidClientID3 = extras.getString("COVIDID3");

        //References
        CovidDateRefs = FirebaseDatabase.getInstance().getReference().child("Clients").child(CovidClientID3).child("CovidDates");
        CovidDateSave = CovidDateRefs.child(Today);

        CovidSaveBase = FirebaseStorage.getInstance().getReference().child("00COVIDFORMS").child(CovidClientID3);
        CovidSave3 = CovidSaveBase.child(Today);
        //End of References

        //Set date in DateView
        DateView.setText("Date: "+Today);

        SignedArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CovidForm3.MyDrawView myDrawView = new CovidForm3.MyDrawView(CovidForm3.this);
                if (myDrawView.getParent() != null) {
                    ((ViewGroup) myDrawView.getParent()).removeView(myDrawView);
                }
                final AlertDialog.Builder diagramDialog = new AlertDialog.Builder(CovidForm3.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.signview, null);
                ImageView WhiteBack = (ImageView) dialogView.findViewById(R.id.imageView3);
                Button DoneButton = (Button) dialogView.findViewById(R.id.doneSignBtn);
                Button ClearButton = (Button) dialogView.findViewById(R.id.clearButton2);
                ConstraintLayout fullActivity = (ConstraintLayout) dialogView.findViewById(R.id.signLayout);
                signLayout3 = (ConstraintLayout) dialogView.findViewById(R.id.signLayout2);
                final AlertDialog SignDia = diagramDialog.create();
                SignDia.setCancelable(false);
                SignDia.setCanceledOnTouchOutside(false);
                SignDia.setView(dialogView);
                signLayout3.addView(myDrawView);
                ClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CovidForm3.MyDrawView) myDrawView).clear();
                    }
                });
                DoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get Signature Photo
                        signLayout3.setDrawingCacheEnabled(true);
                        savedDiagram3 = Bitmap.createBitmap(signLayout3.getDrawingCache());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        savedDiagram3.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        signatureFinal = stream.toByteArray();
                        SignedArea.setImageBitmap(savedDiagram3);
                        SignDia.dismiss();
                        FinishButton.setVisibility(View.VISIBLE);

                    }
                });
                SignDia.show();

                FinishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(NameView!= null){
                           FullPageLayout.setDrawingCacheEnabled(true);
                           savedFullPage= Bitmap.createBitmap(FullPageLayout.getDrawingCache());
                           ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                           savedFullPage.compress(Bitmap.CompressFormat.PNG,100,stream2);
                           WholePage= stream2.toByteArray();
                           CovidSave3.putBytes(WholePage);
                           CovidDateSave.setValue(Today);
                           Intent CLientListIntent = new Intent(CovidForm3.this,ClientList.class);
                           startActivity(CLientListIntent);

                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(),"Please enter your name",Toast.LENGTH_LONG).show();
                       }
                    }
                });
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

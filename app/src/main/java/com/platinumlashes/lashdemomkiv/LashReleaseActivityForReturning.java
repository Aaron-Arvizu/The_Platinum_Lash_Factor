package com.platinumlashes.lashdemomkiv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class LashReleaseActivityForReturning extends AppCompatActivity {

    public String Lname, Mname,userID;
    public ClientModel cli2;
    public Bitmap savedDiagram,wholePage,tempPicforCard;
    public String fBday, fAdd, fFname, fMname, fLname, fZip, ans1, ans2, ans3, ans4, ans5, fHDYHAU,finalID, EmailTxt;
    public ConstraintLayout signLayout,FormLayout;
    public byte[] signatureFinal, formFinal,WholePage,finalPicForCard;
    public RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lash_release);
        cli2 = new ClientModel();
        FormLayout = (ConstraintLayout) findViewById(R.id.formLayout);
        radioButton = (RadioButton) findViewById(R.id.radioButton);


        if (getIntent().hasExtra("Ans1")) {
            EmailTxt = getIntent().getStringExtra("Email");
            finalID = getIntent().getStringExtra("ID4");
            Log.d("test",finalID);
            ans1 = getIntent().getStringExtra("Ans1");
            ans2 = getIntent().getStringExtra("Ans2");
            ans3 = getIntent().getStringExtra("Ans3");
            ans4 = getIntent().getStringExtra("Ans4");
            ans5 = getIntent().getStringExtra("Ans5");
            fBday = getIntent().getStringExtra("Bday");
            fAdd = getIntent().getStringExtra("Add");
            fFname = getIntent().getStringExtra("Firstn");
            fMname = getIntent().getStringExtra("Midllen");
            fLname = getIntent().getStringExtra("Lastn");
            fZip = getIntent().getStringExtra("Zip");
            fHDYHAU = getIntent().getStringExtra("HDYHAU");


        } else {
            Log.d("test", "no extra");
        }
        TextView Release = (TextView) findViewById(R.id.lashRelease);
        Release.setMovementMethod(new ScrollingMovementMethod());


        final ImageView SignedArea = (ImageView) findViewById(R.id.signatureBox);
        final Button ContinueButton = (Button) findViewById(R.id.finishBtn);
        final TextView SigText = (TextView) findViewById(R.id.signatureText);
        getExtras();

        ContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completeRelease();

            }
        });
        final MyDrawView myDrawView = new MyDrawView(LashReleaseActivityForReturning.this);
        SignedArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDrawView.getParent() != null) {
                    ((ViewGroup) myDrawView.getParent()).removeView(myDrawView);
                }
                final AlertDialog.Builder diagramDialog = new AlertDialog.Builder(LashReleaseActivityForReturning.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.signview, null);
                ImageView WhiteBack = (ImageView) dialogView.findViewById(R.id.imageView3);
                Button DoneButton = (Button) dialogView.findViewById(R.id.doneSignBtn);
                Button ClearButton = (Button) dialogView.findViewById(R.id.clearButton2);
                ConstraintLayout fullActivity = (ConstraintLayout) dialogView.findViewById(R.id.signLayout);
                signLayout = (ConstraintLayout) dialogView.findViewById(R.id.signLayout2);
                final AlertDialog SignDia = diagramDialog.create();
                SignDia.setView(dialogView);
                signLayout.addView(myDrawView);
                ClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MyDrawView) myDrawView).clear();
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
                        SigText.setText("");
                        SignedArea.setImageBitmap(savedDiagram);
                        SignDia.dismiss();

                    }
                });

                SignDia.show();

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

    public void getExtras() {



            /*
            fMname = getIntent().getStringExtra("Midllen");
            Log.d("test", fMname);
            fLname = getIntent().getStringExtra("Lastn");
            Log.d("test", fLname);
            fBday = getIntent().getStringExtra("Bday");
            Log.d("test", fBday);
            fAdd = getIntent().getStringExtra("Add");
            Log.d("test", fAdd);
            fZip = getIntent().getStringExtra("Zip");
            Log.d("test", fZip);
            */

    }

    public void completeRelease() {
        Random r = new Random();
        int i = r.nextInt(1000 - 0);
        String ReleaseID = fFname + " " + fMname + "  " + fLname + i;
        DatabaseReference BaseRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalID);

        StorageReference saveRef = FirebaseStorage.getInstance().getReference().child(ReleaseID);
        StorageReference saveSig = saveRef.child("Signature");
        StorageReference savePage = saveRef.child("FullPage");
        if(radioButton.isChecked()){
            if(savedDiagram == null){
                Toast.makeText(this,"Please sign the document",Toast.LENGTH_LONG).show();
            }
            else
            {

                saveSig.putBytes(signatureFinal);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                FormLayout.setDrawingCacheEnabled(true);
                wholePage = Bitmap.createBitmap(FormLayout.getDrawingCache());
                wholePage.compress(Bitmap.CompressFormat.PNG,100,stream2);
                WholePage = stream2.toByteArray();
                savePage.putBytes(WholePage);

                AlertDialog.Builder completeDialog = new AlertDialog.Builder(LashReleaseActivityForReturning.this);
                completeDialog.setTitle("You're all set! You may turn in the Tablet");
                completeDialog.setCancelable(false);
                completeDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference newCardBase = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalID);
                        DatabaseReference newCard = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalID);
                        DatabaseReference EmailRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(finalID).child("Email");
                        DatabaseReference EmailRef2 = FirebaseDatabase.getInstance().getReference().child("EmailList").child(finalID);
                        DatabaseReference ZipRef = newCardBase.child("zip");
                        DatabaseReference BdayRef = newCardBase.child("Birthday");
                        DatabaseReference AddressRef = newCardBase.child("Address");
                        DatabaseReference HDYHAURef = newCardBase.child("HDYHAU");
                        DatabaseReference ans1Ref = newCardBase.child("Ans1");
                        DatabaseReference ans2Ref = newCardBase.child("Ans2");
                        DatabaseReference ans3Ref = newCardBase.child("Ans3");
                        DatabaseReference ans4Ref = newCardBase.child("Ans4");
                        DatabaseReference ans5Ref = newCardBase.child("Ans5");
                        ZipRef.setValue(fZip).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("test","worked");
                            }
                        });
                        EmailRef.setValue(EmailTxt);
                        EmailRef2.setValue(EmailTxt);
                        BdayRef.setValue(fBday);
                        AddressRef.setValue(fAdd);
                        HDYHAURef.setValue(fHDYHAU);
                        ans1Ref.setValue(ans1);
                        ans2Ref.setValue(ans2);
                        ans3Ref.setValue(ans3);
                        ans4Ref.setValue(ans4);
                        ans5Ref.setValue(ans5);

                        String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                        tempPicforCard = BitmapFactory.decodeResource(LashReleaseActivityForReturning.this.getResources(),R.drawable.aflr);
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        tempPicforCard.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                        StorageReference savemrp = FirebaseStorage.getInstance().getReference().child(finalID);

                        DatabaseReference mrpRef = newCardBase.child("mostRecentPic");
                        DatabaseReference mrnRef = newCardBase.child("mostRecentName");
                        mrpRef.setValue("I'm pointless");

                        finalPicForCard = stream2.toByteArray();
                        savemrp.putBytes(finalPicForCard);
                        mrnRef.setValue(timeStamp);
                        Intent leaveIntent = new Intent(LashReleaseActivityForReturning.this,MainActivity.class);
                        startActivity(leaveIntent);
                    }
                });
                completeDialog.show();

            }
        }
        else
        {
            Toast.makeText(this,"Please agree to the document",Toast.LENGTH_LONG).show();
        }

    }

}
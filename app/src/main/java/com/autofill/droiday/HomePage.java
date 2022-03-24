package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity {

    String name;
    int xp;
    TextView UserName, xpText;
    ImageView avatarImage;
    Button leaderboard;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    private Target mTarget;
    TextView title;
    Picasso picasso;
    LocalDate today = LocalDate.now();
    List<Integer> monthParticipation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = FirebaseFirestore.getInstance();
        UserName = findViewById(R.id.UserName);
        xpText = findViewById(R.id.xpText);
        avatarImage = findViewById(R.id.AvatarImg);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        leaderboard = findViewById(R.id.btn2);
        TextView notif = findViewById(R.id.notif);
        title = findViewById(R.id.title_home);
        notif.setVisibility(View.INVISIBLE);
        TextView xpbar = findViewById(R.id.xpbar);

        //UserName.setText("");
        String text = "<font color = #0F3567 >We have got <br/>valuable</font><font color = #2D7CE1> resources</font> <font color = #0F3567 >to <br/>share with you !</font> ";
        ImageView indic  = (ImageView) findViewById(R.id.indic);
        ImageView home_but = (ImageView) findViewById(R.id.home_but);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but);
        ImageView games_but = (ImageView) findViewById(R.id.games_but);

        title.setText(Html.fromHtml(text));

        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                name = "" + document.getData().get("first");
                                xp = Integer.valueOf(document.getData().get("xp").toString());
                                UserName.setText(name);
                                xpText.setText(""+xp);
                                Log.d("URL log", "onComplete: "+ mUser.getPhotoUrl().toString());
                                loadImage(mUser.getPhotoUrl().toString());

                            } else {
                                //Toast.makeText(MainActivity.this, "Document Not Found", Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "No such document");
                            }
                        } else {
                            //Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        long day;
        db.collection("users")
            .document(mUser.getUid())
            .collection("Participation")
            .document("" + today.getYear() + "-" + today.getMonthValue())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            monthParticipation = (List<Integer>) document.get("mnthP");
                            //Log.d("ffg", "onComplete: " + monthParticipation);
                        }else{
                            monthParticipation = new ArrayList<>();
                            monthParticipation.add(-1);
                            Map<String, Object> map = new HashMap<>();
                            map.put("mnthP", monthParticipation);
                            db.collection("users").document(mUser.getUid()).collection("Participation")
                                    .document("" + today.getYear() + "-" + today.getMonthValue())
                                    .set(map);
                        }
                        long todayDayOfMonth = Integer.valueOf(today.getDayOfMonth());
                        if(!monthParticipation.contains(todayDayOfMonth)){
                            notif.setVisibility(View.VISIBLE);
                        }
                        else{
                            notif.setVisibility(View.INVISIBLE);
                        }
                        //monthParticipation.contains(todayDayOfMonth)
                    }
                };
            });


        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, LeaderboardActivity.class);
                startActivity(intent);
                /*db.collection("users")
                        .orderBy("xp", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("leader", "onComplete: " + document.getData().get("first").toString());
                                    }
                                }
                            }
                        });*/
            }
        });

        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CalenderActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });
        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, ResourcesActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });

    }
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    void loadImage(String url) {

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (Bitmap bitmap, Picasso.LoadedFrom from){
                bitmap = getCroppedBitmap(bitmap);
                avatarImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        picasso.get()
                .load(url)
                .into(mTarget);
    }
}
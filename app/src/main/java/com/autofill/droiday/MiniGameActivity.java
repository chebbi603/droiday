package com.autofill.droiday;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class MiniGameActivity extends Activity implements OnTouchListener {
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    int count = 0;
    float player_x,player_y;
    coords playerPos = new coords(1,1);
    boolean touchFlag=false;
    boolean dropFlag=false;
    boolean done =true;
    int[][] map = new int[5][5];
    LayoutParams imageParams;
    ImageView MoveU,MoveD,MoveR,MoveL,Player,start,finish;
    List<ImageView> targets = new ArrayList<>();
    int crashX,crashY;
    Button Submit;
    List<Bounds> bnd = new ArrayList<>();
    RelativeLayout.LayoutParams lp;
    int[] directions = new int[5];
    final Handler handler = new Handler(Looper.getMainLooper());
    MediaPlayer correctMp = new MediaPlayer();

    public static class Bounds
    {
        private final int Top;
        private final int Bottom;
        private final int Left;
        private final int Right;

        public Bounds(int aTop, int aBottom, int aLeft , int aRight)
        {
            this.Top   = aTop;
            this.Bottom = aBottom;
            this.Left = aLeft;
            this.Right = aRight;
        }

        public int Top()   { return Top; }
        public int Bottom() { return Bottom; }
        public int Left() { return Left; }
        public int Right() { return Right; }
    }
    public static class coords
    {
        private  int x;
        private  int y;

        public coords(int X, int Y)
        {
            this.x = X;
            this.y = Y;
        }

        public int x()   { return x; }
        public int y() { return y; }
        public void setX(int a)   { this.x = a; }
        public void setY(int b) { this.y = b; }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mini_game);
        ViewGroup container = findViewById(R.id.container);
        LottieAnimationView lottie = findViewById(R.id.lottie2);
        Player = findViewById(R.id.player);
        MoveU= findViewById(R.id.moveUp);
        MoveD=findViewById(R.id.moveD);
        MoveR=findViewById(R.id.moveR);
        MoveL=findViewById(R.id.moveL);
        start = findViewById(R.id.departCase);
        finish = findViewById(R.id.ArriveeCase);
        targets.add(findViewById(R.id.target));
        targets.add(findViewById(R.id.target2));
        targets.add(findViewById(R.id.target3));
        targets.add(findViewById(R.id.target4));
        targets.add(findViewById(R.id.target5));

        correctMp = MediaPlayer.create(this, R.raw.correct);

        Submit = findViewById(R.id.SubmitButton);

        map[1][1] = 1;
        map[2][1] = 1;
        map[3][1] = 1;
        map[3][2] = 1;
        map[3][3] = 1;
        map[2][3] = 1;

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //Toast.makeText(this, "onCreate: " + dpWidth + " " + dpHeight, Toast.LENGTH_SHORT).show();

        player_x = (380 * dpWidth / 411);
        player_y = (217 * dpHeight / 731);
        lp = new RelativeLayout.LayoutParams((int) (45 * 2.8 * dpWidth / 411), (int) (45 * 2.8 * dpWidth / 411));
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//380-552-724//172
        lp.setMargins((int) player_x, (int) player_y, 0, 0);//217-410//193
        Player.setLayoutParams(lp);
        Player.setScaleX(1);
        Player.setScaleY(1);

        lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.8 * dpWidth / 411), (int) (55 * 2.8 * dpHeight / 731));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(0, 0, (int) (15 * displayMetrics.density * dpWidth / 411), (int) (332 * displayMetrics.density * dpHeight / 731));
        MoveU.setLayoutParams(lp);
        MoveU.setScaleX(1);
        MoveU.setScaleY(1);

        lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.8 * dpWidth / 411), (int) (55 * 2.8 * dpHeight / 731));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(0, 0, (int) (15 * displayMetrics.density * dpWidth / 411), (int) (272 * displayMetrics.density * dpHeight / 731));
        MoveD.setLayoutParams(lp);
        MoveD.setScaleX(1);
        MoveD.setScaleY(1);

        lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.8 * dpWidth / 411), (int) (55 * 2.8 * dpHeight / 731));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(0, 0, (int) (15 * displayMetrics.density * dpWidth / 411), (int) (212 * displayMetrics.density * dpHeight / 731));
        MoveR.setLayoutParams(lp);
        MoveR.setScaleX(1);
        MoveR.setScaleY(1);

        lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.8 * dpWidth / 411), (int) (55 * 2.8 * dpHeight / 731));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(0, 0, (int) (15 * displayMetrics.density * dpWidth / 411), (int) (152 * displayMetrics.density * dpHeight / 731));
        MoveL.setLayoutParams(lp);
        MoveL.setScaleX(1);
        MoveL.setScaleY(1);

        int height = 350;
        lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.5 * dpWidth / 411), (int) (55 * 2.5 * dpHeight / 731));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.setMargins((int) (15 * displayMetrics.density * dpWidth / 411), 0, 0, (int) (height * displayMetrics.density * dpHeight / 731));
        start.setLayoutParams(lp);
        start.setScaleX(1);
        start.setScaleY(1);

        for(ImageView trg : targets) {
            height -= 36;
            lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.5 * dpWidth / 411), (int) (55 * 2.5 * dpHeight / 731));
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.setMargins((int) (15 * displayMetrics.density * dpWidth / 411), 0, 0, (int) (height * displayMetrics.density * dpHeight / 731));
            trg.setLayoutParams(lp);
            trg.setScaleX(1);
            trg.setScaleY(1);
            trg.bringToFront();
            start.bringToFront();
        }

        height -= 28;
        lp = new RelativeLayout.LayoutParams((int) (55 * 3.13 * 2.5 * dpWidth / 411), (int) (55 * 0.8259722 * 2.5 * dpHeight / 731));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.setMargins((int) (15 * displayMetrics.density * dpWidth / 411), 0, 0, (int) (height * displayMetrics.density * dpHeight / 731));
        finish.setLayoutParams(lp);
        finish.setScaleX(1);
        finish.setScaleY(1);


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(done) {
                    boolean check = true;
                    for (int e : directions) {
                        if (e == 0) {
                            check = false;
                        }
                    }
                    if (!check) {
                        Toast.makeText(MiniGameActivity.this, "Use All The Moves Available", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(MiniGameActivity.this, "" + directions[0], Toast.LENGTH_SHORT).show();
                        done = false;
                        count = 0;
                        Runnable runnable = new Runnable() {
                            public void run() {
                                if (count < 5) {
                                    switch (directions[count]) {
                                        case 1:
                                            playerPos.setY(playerPos.y()-1);
                                            if(map[playerPos.x()][playerPos.y()]==0){
                                                Toast.makeText(MiniGameActivity.this, "Can't go up", Toast.LENGTH_SHORT).show();
                                            }else {
                                                player_y -= (193 * dpHeight / 731);
                                                lp = new RelativeLayout.LayoutParams((int) (45 * 2.8 * dpWidth / 411), (int) (45 * 2.8 * dpWidth / 411));
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//380-552-724//172
                                                lp.setMargins((int) player_x, (int) player_y, 0, 0);//217//193
                                                Player.setLayoutParams(lp);
                                            }
                                            break;
                                        case 2:
                                            playerPos.setY(playerPos.y()+1);
                                            if(map[playerPos.x()][playerPos.y()]==0){
                                                Toast.makeText(MiniGameActivity.this, "Can't go down", Toast.LENGTH_SHORT).show();
                                            }else {
                                                player_y += (193 * dpHeight / 731);
                                                lp = new RelativeLayout.LayoutParams((int) (45 * 2.8 * dpWidth / 411), (int) (45 * 2.8 * dpWidth / 411));
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                                lp.setMargins((int) player_x, (int) player_y, 0, 0);
                                                Player.setLayoutParams(lp);
                                            }
                                            break;
                                        case 3:
                                            playerPos.setX(playerPos.x()+1);
                                            if(map[playerPos.x()][playerPos.y()]==0){
                                                Toast.makeText(MiniGameActivity.this, "Can't go right", Toast.LENGTH_SHORT).show();
                                            }else {
                                                player_x += (172 * dpWidth / 411);
                                                lp = new RelativeLayout.LayoutParams((int) (45 * 2.8 * dpWidth / 411), (int) (45 * 2.8 * dpWidth / 411));
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                                lp.setMargins((int) player_x, (int) player_y, 0, 0);
                                                Player.setLayoutParams(lp);
                                            }
                                            break;
                                        case 4:
                                            playerPos.setX(playerPos.x()-1);
                                            if(map[playerPos.x()][playerPos.y()]==0){
                                                Toast.makeText(MiniGameActivity.this, " Can't go left", Toast.LENGTH_SHORT).show();
                                            }else {
                                                player_x -= (172 * dpWidth / 411);
                                                lp = new RelativeLayout.LayoutParams((int) (45 * 2.8 * dpWidth / 411), (int) (45 * 2.8 * dpWidth / 411));
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                                lp.setMargins((int) player_x, (int) player_y, 0, 0);
                                                Player.setLayoutParams(lp);
                                            }
                                            break;
                                    }
                                    if(map[playerPos.x()][playerPos.y()]==0){
                                        count=5;
                                    }
                                    count++;
                                    handler.postDelayed(this, 500);
                                }else{
                                    if(map[playerPos.x()][playerPos.y()]==1){
                                        //Toast.makeText(MiniGameActivity.this, "Well Done", Toast.LENGTH_SHORT).show();
                                        lottie.bringToFront();
                                        lottie.setAnimation("welldone.json");
                                        lottie.playAnimation();
                                        correctMp.start();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(MiniGameActivity.this, TracksActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                            }
                                        }, 4500);

                                    }else{
                                        playerPos = new coords(1,1);
                                        player_x = (380 * dpWidth / 411);
                                        player_y = (217 * dpHeight / 731);
                                        lp = new RelativeLayout.LayoutParams((int) (45 * 2.8 * dpWidth / 411), (int) (45 * 2.8 * dpWidth / 411));
                                        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                        lp.setMargins((int) player_x, (int) player_y, 0, 0);
                                        Player.setLayoutParams(lp);
                                        done = true;
                                    }

                                }
                            }
                        };
                        handler.post(runnable);
                    }
                }
            }
        });

        container.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if(touchFlag)
                {
                    bnd = new ArrayList<>();
                    Bounds b1= new Bounds(targets.get(0).getTop(), targets.get(0).getBottom(), targets.get(0).getLeft(), targets.get(0).getRight());
                    Bounds b2= new Bounds(targets.get(1).getTop(), targets.get(1).getBottom(), targets.get(1).getLeft(), targets.get(1).getRight());
                    Bounds b3= new Bounds(targets.get(2).getTop(), targets.get(2).getBottom(), targets.get(2).getLeft(), targets.get(2).getRight());
                    Bounds b4= new Bounds(targets.get(3).getTop(), targets.get(3).getBottom(), targets.get(3).getLeft(), targets.get(3).getRight());
                    Bounds b5= new Bounds(targets.get(4).getTop(), targets.get(4).getBottom(), targets.get(4).getLeft(), targets.get(4).getRight());
                    bnd.add(b1);
                    bnd.add(b2);
                    bnd.add(b3);
                    bnd.add(b4);
                    bnd.add(b5);

                    System.err.println("Display If  Part ::->"+touchFlag);
                    switch (event.getActionMasked())
                    {
                        case MotionEvent.ACTION_DOWN :

                            /*topy=target.getTop();
                            leftX=target.getLeft();
                            rightX=target.getRight();
                            bottomY=target.getBottom();

                            System.err.println("Display Top-->"+topy);
                            System.err.println("Display Left-->"+leftX);
                            System.err.println("Display Right-->"+rightX);
                            System.err.println("Display Bottom-->"+bottomY);*/

                            break;
                        case MotionEvent.ACTION_MOVE:
                            crashX=(int) event.getX();
                            crashY=(int) event.getY();
                            System.err.println("Display Here X Value-->"+crashX);
                            System.err.println("Display Here Y Value-->"+crashY);


                            int x = (int) event.getX() - offset_x;
                            int y = (int) event.getY() - offset_y;
                            //int w = getWindowManager().getDefaultDisplay().getWidth() - 100;
                            //int h = getWindowManager().getDefaultDisplay().getHeight() - 100;
                            int w = getWindowManager().getDefaultDisplay().getWidth() - 50;
                            int h = getWindowManager().getDefaultDisplay().getHeight() - 10;
                            if (x > w)
                                x = w;
                            if (y > h)
                                y = h;

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(  selected_item.getLayoutParams().width , selected_item.getLayoutParams().height));
                            lp.setMargins(x, y, 0, 0);

                            for(int i = 0; i < targets.size() ; i++){
                                if(directions[i]==0) {
                                    if (crashX > bnd.get(i).Left() && crashX < bnd.get(i).Right() && crashY > bnd.get(i).Top() && crashY < bnd.get(i).Bottom()) {
                                        //targets.get(i).setBackgroundTintList(ColorStateList.valueOf(0xffe5e5e5));
                                        targets.get(i).setBackgroundResource(R.drawable.empty_hover);
                                    } else {
                                        //targets.get(i).setBackgroundTintList(ColorStateList.valueOf(0xffdbdbdb));
                                        targets.get(i).setBackgroundResource(R.drawable.empty);
                                    }
                                }
                            }

                            //Drop Image Here
                            selected_item.setLayoutParams(lp);
                            break;
                        case MotionEvent.ACTION_UP:

                            for(int i = 0; i < targets.size() ; i++){
                                if(crashX > bnd.get(i).Left() && crashX < bnd.get(i).Right() && crashY > bnd.get(i).Top() && crashY < bnd.get(i).Bottom() )
                                {
                                    if(selected_item==MoveU){
                                        directions[i]=1;
                                        targets.get(i).setBackgroundResource(R.drawable.move_up);
                                    }if(selected_item==MoveD){
                                        directions[i]=2;
                                        targets.get(i).setBackgroundResource(R.drawable.move_down);
                                    }if(selected_item==MoveR){
                                        directions[i]=3;
                                        targets.get(i).setBackgroundResource(R.drawable.move_right);
                                    }if(selected_item==MoveL){
                                        directions[i]=4;
                                        targets.get(i).setBackgroundResource(R.drawable.move_left);
                                    }

                                    targets.get(i).setBackgroundTintList(null);
                                    //Drawable temp=selected_item.getBackground();
                                    //imageDrop.setBackgroundDrawable(temp);
                                    //targets.get(i).bringToFront();
                                    dropFlag=true;
                                    //selected_item.setVisibility(View.INVISIBLE);
                                }
                            }


                            touchFlag=false;
                            selected_item.setLayoutParams(imageParams);
                            /*
                            if(dropFlag==true)
                            {
                                dropFlag=false;
                            }
                            else
                            {
                                selected_item.setLayoutParams(imageParams);
                            }*/
                            break;
                        default:
                            break;
                    }
                }else
                {
                    System.err.println("Display Else Part ::->"+touchFlag);
                }
                return true;
            }
        });

        MoveU.setOnTouchListener(this);
        MoveD.setOnTouchListener(this);
        MoveR.setOnTouchListener(this);
        MoveL.setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                touchFlag=true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                selected_item = v;
                v.bringToFront();
                imageParams=v.getLayoutParams();
                break;
            case MotionEvent.ACTION_UP:
                selected_item=null;
                touchFlag=false;
                break;
            default:
                break;
        }
        return false;
    }
}
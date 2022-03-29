package com.autofill.droiday;

import android.content.res.ColorStateList;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class MiniGameActivity extends Activity implements OnTouchListener {
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    boolean touchFlag=false;
    boolean dropFlag=false;
    LayoutParams imageParams;
    ImageView MoveU,MoveD,MoveR,MoveL,Player,start,finish;
    List<ImageView> targets = new ArrayList<>();
    int crashX,crashY,ScreenX,ScreenY;
    Button Submit;
    List<Bounds> bnd = new ArrayList<>();
    RelativeLayout.LayoutParams lp;
    int[] directions = new int[5];

    public static class Bounds
    {
        private final int Top;
        private final int  Bottom;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mini_game);
        ViewGroup container = findViewById(R.id.container);
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

        Submit = findViewById(R.id.SubmitButton);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        Toast.makeText(this, "onCreate: " + dpWidth + " " + dpHeight, Toast.LENGTH_SHORT).show();

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
                boolean check = true;
                for(int e : directions){
                    if(e == 0){
                        check = false;
                    }
                }
                if(!check){
                    Toast.makeText(MiniGameActivity.this, "Use All The Moves Available", Toast.LENGTH_SHORT).show();
                }else{
                    for(int e : directions){
                        switch(e){
                            case 1:

                                break;
                            case 2:

                                break;
                            case 3:

                                break;
                            case 4:

                                break;
                        }
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
                                        Toast.makeText(MiniGameActivity.this, "up", Toast.LENGTH_SHORT).show();
                                        directions[i]=1;
                                        targets.get(i).setBackgroundResource(R.drawable.move_up);
                                    }if(selected_item==MoveD){
                                        Toast.makeText(MiniGameActivity.this, "down", Toast.LENGTH_SHORT).show();
                                        directions[i]=2;
                                        targets.get(i).setBackgroundResource(R.drawable.move_down);
                                    }if(selected_item==MoveR){
                                        Toast.makeText(MiniGameActivity.this, "left", Toast.LENGTH_SHORT).show();
                                        directions[i]=3;
                                        targets.get(i).setBackgroundResource(R.drawable.move_right);
                                    }if(selected_item==MoveL){
                                        Toast.makeText(MiniGameActivity.this, "right", Toast.LENGTH_SHORT).show();
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
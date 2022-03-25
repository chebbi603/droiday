package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MiniGameActivity extends Activity implements OnTouchListener {
    /** Called when the activity is first created. */
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    Boolean touchFlag=false;
    boolean dropFlag=false;
    LayoutParams imageParams;
    ImageView target, target2, target3, target4,MoveU,MoveD,MoveR,MoveL;
    int crashX,crashY;
    Drawable dropDrawable,selectDrawable;
    Rect dropRect,selectRect;
    int topy,leftX,rightX,bottomY;
    List<Bounds> bnd = new ArrayList<>();
    List<ImageView> targets = new ArrayList<>();
    int var[] = new int[4];

    int dropArray[];

    public class Bounds
    {
        private final int Top;
        private final int  Bottom;
        private final int Left;
        private final int Right;

        public Bounds(int aTop, int aBottom, int aLeft , int aRight)
        {
            Top   = aTop;
            Bottom = aBottom;
            Left = aLeft;
            Right = aRight;
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
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        /*target=(ImageView) findViewById(R.id.target);
        target2=(ImageView) findViewById(R.id.target2);
        target3=(ImageView) findViewById(R.id.target3);
        target4=(ImageView) findViewById(R.id.target4);*/
        MoveU=(ImageView) findViewById(R.id.moveUp);
        MoveD=(ImageView) findViewById(R.id.moveD);
        MoveR=(ImageView) findViewById(R.id.moveR);
        MoveL=(ImageView) findViewById(R.id.moveL);

        /*targets.add(imageDrop);
        targets.add(imageDrop2);*/
        targets.add((ImageView) findViewById(R.id.target));
        targets.add((ImageView) findViewById(R.id.target2));
        targets.add((ImageView) findViewById(R.id.target3));
        targets.add((ImageView) findViewById(R.id.target4));

        /*topy=imageDrop.getTop();
        leftX=imageDrop.getLeft();
        rightX=imageDrop.getRight();
        bottomY=imageDrop.getBottom();
        bnd.add(new Bounds(topy, bottomY, leftX,rightX));

        topy=imageDrop2.getTop();
        leftX=imageDrop2.getLeft();
        rightX=imageDrop2.getRight();
        bottomY=imageDrop2.getBottom();
        bnd.add(new Bounds(topy, bottomY, leftX,rightX));*/

        container.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if(touchFlag==true)
                {
                    List<Bounds> bnd = new ArrayList<>();
                    Bounds b1= new Bounds(targets.get(0).getTop(), targets.get(0).getBottom(), targets.get(0).getLeft(), targets.get(0).getRight());
                    Bounds b2= new Bounds(targets.get(1).getTop(), targets.get(1).getBottom(), targets.get(1).getLeft(), targets.get(1).getRight());
                    Bounds b3= new Bounds(targets.get(2).getTop(), targets.get(2).getBottom(), targets.get(2).getLeft(), targets.get(2).getRight());
                    Bounds b4= new Bounds(targets.get(3).getTop(), targets.get(3).getBottom(), targets.get(3).getLeft(), targets.get(3).getRight());
                    bnd.add(b1);
                    bnd.add(b2);
                    bnd.add(b3);
                    bnd.add(b4);

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

                            //opRect.
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
                            /*if(var[0]==0) {
                                if (crashX > bnd.get(0).Left() && crashX < bnd.get(0).Right() && crashY > bnd.get(0).Top() && crashY < bnd.get(0).Bottom()) {
                                    target.setBackgroundTintList(ColorStateList.valueOf(0xffe5e5e5));
                                } else {
                                    target.setBackgroundTintList(ColorStateList.valueOf(0xffdbdbdb));
                                    //imageDrop.setBackgroundTintList(null);EDE5E5
                                }
                            }*/
                            for(int i = 0; i < targets.size() ; i++){
                                if(var[i]==0) {
                                    if (crashX > bnd.get(i).Left() && crashX < bnd.get(i).Right() && crashY > bnd.get(i).Top() && crashY < bnd.get(i).Bottom()) {
                                        targets.get(i).setBackgroundTintList(ColorStateList.valueOf(0xffe5e5e5));
                                    } else {
                                        targets.get(i).setBackgroundTintList(ColorStateList.valueOf(0xffdbdbdb));
                                        //imageDrop.setBackgroundTintList(null);EDE5E5
                                    }
                                }
                            }

                            //Drop Image Here
                            selected_item.setLayoutParams(lp);
                            break;
                        case MotionEvent.ACTION_UP:
                            //
                            /*if (crashX > bnd.get(0).Left() && crashX < bnd.get(0).Right() && crashY > bnd.get(0).Top() && crashY < bnd.get(0).Bottom())
                            {
                                if(selected_item==MoveU){
                                    Toast.makeText(MiniGameActivity.this, "up", Toast.LENGTH_SHORT).show();
                                    var[0]=1;
                                }if(selected_item==MoveD){
                                    Toast.makeText(MiniGameActivity.this, "down", Toast.LENGTH_SHORT).show();
                                    var[0]=2;
                                }if(selected_item==MoveR){
                                    Toast.makeText(MiniGameActivity.this, "left", Toast.LENGTH_SHORT).show();
                                    var[0]=3;
                                }if(selected_item==MoveL){
                                    Toast.makeText(MiniGameActivity.this, "right", Toast.LENGTH_SHORT).show();
                                    var[0]=4;
                                }

                                target.setBackgroundTintList(null);
                                //Drawable temp=selected_item.getBackground();
                                //imageDrop.setBackgroundDrawable(temp);
                                //imageDrop.bringToFront();
                                dropFlag=true;
                                //selected_item.setVisibility(View.INVISIBLE);
                            }*/

                            for(int i = 0; i < targets.size() ; i++){
                                if(crashX > bnd.get(i).Left() && crashX < bnd.get(i).Right() && crashY > bnd.get(i).Top() && crashY < bnd.get(i).Bottom() )
                                {
                                    if(selected_item==MoveU){
                                        Toast.makeText(MiniGameActivity.this, "up", Toast.LENGTH_SHORT).show();
                                        var[i]=1;
                                    }if(selected_item==MoveD){
                                        Toast.makeText(MiniGameActivity.this, "down", Toast.LENGTH_SHORT).show();
                                        var[i]=2;
                                    }if(selected_item==MoveR){
                                        Toast.makeText(MiniGameActivity.this, "left", Toast.LENGTH_SHORT).show();
                                        var[i]=3;
                                    }if(selected_item==MoveL){
                                        Toast.makeText(MiniGameActivity.this, "right", Toast.LENGTH_SHORT).show();
                                        var[i]=4;
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
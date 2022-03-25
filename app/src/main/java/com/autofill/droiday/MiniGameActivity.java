package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
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

public class MiniGameActivity extends AppCompatActivity implements OnTouchListener {

    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    Boolean touchFlag=false;
    boolean dropFlag=false;
    LayoutParams imageParams;
    ImageView imageDrop,image1,image2;
    int crashX,crashY;
    Drawable dropDrawable,selectDrawable;
    Rect dropRect,selectRect;
    int topy,leftX,rightX,bottomY;

    int dropArray[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mini_game);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        imageDrop = (ImageView) findViewById(R.id.ImgDrop);
        image1 = (ImageView) findViewById(R.id.img);
        image2 = (ImageView) findViewById(R.id.img2);
        container.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (touchFlag == true) {
                    System.err.println("Display If  Part ::->" + touchFlag);
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:

                            topy = imageDrop.getTop();
                            leftX = imageDrop.getLeft();
                            rightX = imageDrop.getRight();
                            bottomY = imageDrop.getBottom();
                            System.err.println("Display Top-->" + topy);
                            System.err.println("Display Left-->" + leftX);
                            System.err.println("Display Right-->" + rightX);
                            System.err.println("Display Bottom-->" + bottomY);


                            //opRect.
                            break;
                        case MotionEvent.ACTION_MOVE:
                            crashX = (int) event.getX();
                            crashY = (int) event.getY();
                            System.err.println("Display Here X Value-->" + crashX);
                            System.err.println("Display Here Y Value-->" + crashY);

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
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                            lp.setMargins(x, y, 0, 0);

                            //Drop Image Here
                            if (crashX > leftX && crashX < rightX && crashY > topy && crashY < bottomY) {
                                Drawable temp = selected_item.getBackground();
                                imageDrop.setBackgroundDrawable(temp);
                                imageDrop.bringToFront();
                                dropFlag = true;
                                selected_item.setVisibility(View.INVISIBLE);
                            }
                            //Drop Image Here
                            selected_item.setLayoutParams(lp);
                            break;
                        case MotionEvent.ACTION_UP:
                            //
                            touchFlag = false;
                            if (dropFlag == true) {
                                dropFlag = false;
                            } else {
                                selected_item.setLayoutParams(imageParams);
                            }
                    }
                }
            }
        });
    }

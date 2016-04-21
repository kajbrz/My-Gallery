package com.example.kajetan.mygallery;

import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.Math;

import com.bumptech.glide.Glide;

public class FullscreenImageAcitivity extends AppCompatActivity {
    private ImageView imageFullScreen;
    private ImageView zoomIn, zoomOut;
    private GestureDetector movingImage;
    private String absoluteFilePathName;
    private Animation showFullScreen;
    private boolean saveValuesDoubleFinger = true;

    private MotionEvent.PointerCoords firstHistoryFingerPosition, secondHistoryFingerPosition;
    private float vecHistoryLength;
    private float tempLengthVec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFullScreen = AnimationUtils.loadAnimation(this, R.anim.showfullscreen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //////////////
        setContentView(R.layout.activity_fullscreen_image_acitivity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        absoluteFilePathName = getIntent().getExtras().getString("absoluteFilePathName");
        onCreateRenderImage();
        onCreateZoomInOut();
    }


    private void onCreateMoveListener() {
        imageFullScreen.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movingImage = new GestureDetector(v.getContext(), my);
                movingImage.onTouchEvent(event);
                return true;
            };
        });
    }
    ///double finger listener
    private void onCreateZoomInOut() {
        firstHistoryFingerPosition = new MotionEvent.PointerCoords();
        secondHistoryFingerPosition = new MotionEvent.PointerCoords();

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.rlFullScreen);
        //ImageView rl = (ImageView) findViewById(R.id.imageViewFullscreen);
        rl.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            movingImage = new GestureDetector(v.getContext(), my);
            movingImage.onTouchEvent(event);
            doubleFingerListener(event);
            return true;
        }

    });

    }
    private void doubleFingerListener(MotionEvent m) {
        int pointerCount = m.getPointerCount();
        if (pointerCount == 1)
            saveValuesDoubleFinger = true;
        if (pointerCount == 2) {
            if (saveValuesDoubleFinger)
            {
                m.getPointerCoords(0, firstHistoryFingerPosition);
                m.getPointerCoords(1, secondHistoryFingerPosition);
                vecHistoryLength = (float)(Math.hypot(
                    secondHistoryFingerPosition.x - firstHistoryFingerPosition.x,
                    secondHistoryFingerPosition.y - firstHistoryFingerPosition.y
                ));

                saveValuesDoubleFinger = false;
            }
            MotionEvent.PointerCoords firstFingerPosition, secondFingerPosition, vecFingerFirst,
                    vecFingerSecond;
            float lengthVec;

            firstFingerPosition = new MotionEvent.PointerCoords();
            secondFingerPosition = new MotionEvent.PointerCoords();


            int action = m.getActionMasked();
            int actionIndex = m.getActionIndex();

            m.getPointerCoords(0, firstFingerPosition);
            m.getPointerCoords(1, secondFingerPosition);

            if (action == m.ACTION_MOVE){
                lengthVec = (float)(Math.hypot(
                        secondFingerPosition.x - firstFingerPosition.x,
                        secondFingerPosition.y - firstFingerPosition.y
                ));

                tempLengthVec = lengthVec - vecHistoryLength;

                vecHistoryLength = lengthVec;

                imageFullScreen.setScaleX(
                        (tempLengthVec/100.f)+ imageFullScreen.getScaleX());
                imageFullScreen.setScaleY(
                        (tempLengthVec/100.f) + imageFullScreen.getScaleY());

                if(imageFullScreen.getScaleX() > 10) {
                    imageFullScreen.setScaleX(10);
                    imageFullScreen.setScaleY(10);
                }

                if(imageFullScreen.getScaleX() < 1) {
                    imageFullScreen.setScaleX(1);
                    imageFullScreen.setScaleY(1);
                }


            }
        }
    }

    private void onCreateRenderImage() {
        imageFullScreen = (ImageView)findViewById(R.id.imageViewFullscreen);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load(
                        "file:///" + absoluteFilePathName).into(imageFullScreen);
                        //new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build()
            }
        });

        if (absoluteFilePathName != null)
        {
        }
        thread.run();
        imageFullScreen.startAnimation(showFullScreen);
    }

    private OnClickListener onClickZoomInListener = new OnClickListener(){

        @Override
        public void onClick(View v) {

            imageFullScreen.setScaleX(imageFullScreen.getScaleX() + 1);
            imageFullScreen.setScaleY(imageFullScreen.getScaleY() + 1);
            if(imageFullScreen.getScaleX() > 10) {
                imageFullScreen.setScaleX(10);
                imageFullScreen.setScaleY(10);
            }

        }
    };
    private OnClickListener onClickZoomOutListener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            imageFullScreen.setScaleX(imageFullScreen.getScaleX() - 1);
            imageFullScreen.setScaleY(imageFullScreen.getScaleY() - 1);
            if(imageFullScreen.getScaleX() < 1) {
                imageFullScreen.setScaleX(1);
                imageFullScreen.setScaleY(1);
            }
        }
    };
    //listening to moving image in fullscreen mode
    private GestureDetector.OnGestureListener my = new GestureDetector.OnGestureListener() {
        private float movementX = 0, movementY = 0;
        private boolean saveValues = false;

        @Override
        public boolean onDown(MotionEvent e) {
            saveValues = false;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.rlFullScreen);
            if (!saveValues) {
                saveValues = true;
                movementX = distanceX;
                movementY = distanceY;
            } else {
                imageFullScreen.setX(rl.getX() + (movementX - distanceX));
                imageFullScreen.setY(rl.getY() + (movementY - distanceY));
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

}

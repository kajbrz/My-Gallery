package com.example.kajetan.mygallery;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.gesture.GestureOverlayView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class FullscreenImageAcitivity extends AppCompatActivity {
    ImageView imageFullScreen;
    ImageView zoomIn, zoomOut;
    GestureDetector movingImage;
    String absoluteFilePathName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turning on fullscreen and hiding actionbar
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
        onCreateMoveListener();
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

    private void onCreateZoomInOut() {
        zoomIn = (ImageView)findViewById(R.id.buttonZoomIn);
        zoomOut = (ImageView)findViewById(R.id.buttonZoomOut);


        zoomIn.setOnClickListener(onClickZoomInListener);
        zoomOut.setOnClickListener(onClickZoomOutListener);

    }

    private void onCreateRenderImage() {
        imageFullScreen = (ImageView)findViewById(R.id.imageViewFullscreen);

        if (absoluteFilePathName != null)
            ImageLoader.getInstance().displayImage(
                    "file:///" + absoluteFilePathName,
                    imageFullScreen,
                    new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build()
            );
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

//            ImageLoader.getInstance().displayImage(
//                    "file:///" + absoluteFilePathName,
//                    imageFullScreen,
//                    new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build()
//            );


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
//            imageFullScreen.setImageBitmap(ImageLoader.getInstance().loadImageSync(
//                    "file:///" + absoluteFilePathName,
//                    new ImageSize(
//                            (int) (imageFullScreen.getWidth() * imageFullScreen.getScaleX()),
//                            (int) (imageFullScreen.getHeight() * imageFullScreen.getScaleY()))
//            ));

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
            if (!saveValues) {
                saveValues = true;
                movementX = distanceX;
                movementY = distanceY;
            } else {
                imageFullScreen.setX(imageFullScreen.getX() + (movementX - distanceX));
                imageFullScreen.setY(imageFullScreen.getY() + (movementY - distanceY));
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

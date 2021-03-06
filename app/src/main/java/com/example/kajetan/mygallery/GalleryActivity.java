package com.example.kajetan.mygallery;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListImageAdapter adapter;

    public static int xSizeOfScreen, ySizeOfScreen;

    private Animation showImage, showFullScreen;
    private Transition showFullscreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        showImage = AnimationUtils.loadAnimation(this,R.anim.show);
        showFullScreen = AnimationUtils.loadAnimation(this, R.anim.showfullscreen);

        onCreateSetupImageLoader();
        getSizeOfScreen();
        onCreateAddRecyclerView();


    }



    private void onCreateSetupImageLoader() {

    }

    private void getSizeOfScreen() {
        WindowManager wm = (WindowManager) GalleryActivity.this.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        xSizeOfScreen = point.x;
        ySizeOfScreen = point.y;
    }

    private void onCreateAddRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new ListImageAdapter(GalleryActivity.this, getData(), showImage, showFullscreenImage);

        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public static List<RecordOfView> getData() {
        ArrayList<RecordOfView> data = new ArrayList<>();
        return getData("", data);
    }

    private static List<RecordOfView> getData(String folderPath, ArrayList<RecordOfView> listOfImages) {
        File[] files = null;
        files = Environment.getExternalStoragePublicDirectory(folderPath).listFiles();
        String[] extension, extension2;

        if(files == null) {
            //Not have found any files
            return listOfImages;
        }

        String src;
        for (int i = 0; i < files.length; i++) {
            RecordOfView recordOfFileName = new RecordOfView();
            src = files[i].getAbsolutePath();
            extension = src.split("\\.");
            Log.i("blad", "hha:" + extension[extension.length-1]);
            if (extension.length == 1)
            {
                extension2 = src.split("/");
                getData(folderPath + "/" + extension2[extension2.length - 1], listOfImages);
            }
            if (extension[extension.length-1].toLowerCase().equals("png")
                    || extension[extension.length-1].toLowerCase().equals("jpg")
                    || extension[extension.length-1].toLowerCase().equals("jpeg")) {
            } else {
                continue;
            }
            extension = src.split("/");
            recordOfFileName.sourcePathName = src;
            recordOfFileName.sourceNameFile = extension[extension.length-1];
            listOfImages.add(recordOfFileName);
        }

        return listOfImages;
    }
}

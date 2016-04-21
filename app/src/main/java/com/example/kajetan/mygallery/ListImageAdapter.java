package com.example.kajetan.mygallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import javax.xml.datatype.Duration;

/**
 * Created by Kajetan on 2015-10-18.
 */
public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<RecordOfView> data = Collections.emptyList();

    private Animation showImage;
    private Transition transition;

    private int xySizeOfThumbnail;
    private Context myContext;
    public ListImageAdapter(Context context, List<RecordOfView> data, Animation anim, Transition transition) {
        showImage = anim;
        inflater = LayoutInflater.from(context);

        if (data != null)
            this.data = data;

        myContext = context;
        setTheThumbnailSize();
    }
    private void setTheThumbnailSize()
    {
        int tempSize = GalleryActivity.xSizeOfScreen / 2 - 10;

        xySizeOfThumbnail = tempSize;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view, myContext);


        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecordOfView rec = data.get(position);
        holder.imageName.setText(rec.sourceNameFile);

        holder.absoluteImagePathName.setText(rec.sourcePathName);
        holder.image.setAlpha(1.f);
        Glide.with(myContext).load("file:///" + rec.sourcePathName)
                .asBitmap().centerCrop()
                .into(holder.image);
        holder.image.setBackgroundColor(Color.BLACK);
        holder.image.startAnimation(showImage);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView imageName;
        TextView absoluteImagePathName;
        ImageView image;
        Context myContext;
        public MyViewHolder(View itemView, Context context  ) {
            super(itemView);
            imageName = (TextView) itemView.findViewById(R.id.textView);
            absoluteImagePathName = (TextView) itemView.findViewById(R.id.absoluteFilePathName);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            cardView.setCardBackgroundColor(Color.BLACK);
            myContext = context;
            onClickImageEvent();
            onLongClickImageEvent();
        }
        //listening to open new activity with clicked image


        private void onClickImageEvent() {
            cardView.setOnClickListener(new View.OnClickListener() {
                //@TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            itemView.getContext(),
                            imageName.getText(),
                            Toast.LENGTH_SHORT
                    ).show();
                    Intent intent = new Intent(
                            cardView.getContext(),
                            FullscreenImageAcitivity.class
                    );


                    intent = intent.putExtra(
                            "absoluteFilePathName",
                            absoluteImagePathName.getText().toString()
                    );

                    try {
                        Scene mScene = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            mScene = Scene.getSceneForLayout(
                                    (ViewGroup) cardView.getParent(), R.layout.activity_fullscreen_image_acitivity, myContext
                            );
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.go(mScene, transition);
                        }

                    } catch (Exception e) {
                        Toast.makeText(myContext, e.toString(), Toast.LENGTH_SHORT);
                    } finally {
                        cardView.getContext().startActivity(intent);
                    }
                }
            });
        }

        private void onLongClickImageEvent()
        {
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

        }
    }


}

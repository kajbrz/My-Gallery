package com.example.kajetan.mygallery;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kajetan on 2015-10-18.
 */
public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<RecordOfView> data = Collections.emptyList();

    private int xySizeOfThumbnail;

    public ListImageAdapter(Context context, List<RecordOfView> data) {
        inflater = LayoutInflater.from(context);

        if (data != null)
            this.data = data;

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

        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecordOfView rec = data.get(position);

        holder.imageName.setText(rec.sourceNameFile);
        holder.absoluteImagePathName.setText(rec.sourcePathName);
        holder.image.getLayoutParams().height = xySizeOfThumbnail;
        ImageLoader.getInstance().displayImage("file:///" + rec.sourcePathName, holder.image);
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

        public MyViewHolder(View itemView) {
            super(itemView);
            imageName = (TextView) itemView.findViewById(R.id.textView);
            absoluteImagePathName = (TextView) itemView.findViewById(R.id.absoluteFilePathName);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            cardView = (CardView)itemView.findViewById(R.id.cv);

            onClickImageEvent();
        }
        //listening to open new activity with clicked image
        private void onClickImageEvent() {
            cardView.setOnClickListener(new View.OnClickListener() {
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
                    cardView.getContext().startActivity(
                            intent.putExtra(
                                    "absoluteFilePathName",
                                    absoluteImagePathName.getText().toString()
                            )
                    );
                }
            });
        }
    }


}

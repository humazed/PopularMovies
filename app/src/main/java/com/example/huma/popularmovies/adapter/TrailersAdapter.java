package com.example.huma.popularmovies.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Trailer;

import java.util.List;

/**
 * User: YourPc
 * Date: 3/29/2017
 */

public class TrailersAdapter extends BaseQuickAdapter<Trailer, BaseViewHolder> {
    private static final String TAG = TrailersAdapter.class.getSimpleName();

    public TrailersAdapter(List<Trailer> trailers) {
        super(R.layout.row_trailer, trailers);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Trailer trailer) {
        ImageView thumbnailImageView = viewHolder.getView(R.id.thumbnail_imageView);

        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Log.d(TAG, "thumbnailUrl = " + thumbnailUrl);
        Glide.with(mContext)
                .load(thumbnailUrl)
                .crossFade()
                .into(thumbnailImageView);

        thumbnailImageView.setOnClickListener(v ->
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl()))));
    }
}


package com.example.huma.popularmovies.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Review;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User: YourPc
 * Date: 3/29/2017
 */

public class ReviewsAdapter extends BaseQuickAdapter<Review, BaseViewHolder> {
    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    @BindView(R.id.author_textView) TextView mAuthorTextView;
    @BindView(R.id.content_textView) ExpandableTextView mContentTextView;
    @BindView(R.id.read_more_textView) TextView mReadMoreTextView;
    @BindView(R.id.tv_show_description_container) LinearLayout mTvShowDescriptionContainer;

    public ReviewsAdapter(List<Review> reviews) {
        super(R.layout.row_review, reviews);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Review review) {
        ButterKnife.bind(this, viewHolder.getConvertView());


        mAuthorTextView.setText(review.getAuthor());
        mContentTextView.setText(review.getContent());

        // toggle the ExpandableTextView
        mContentTextView.setAnimationDuration(200L);
        mTvShowDescriptionContainer.setOnClickListener(v -> {
            mContentTextView.toggle();
            mReadMoreTextView.setText(mContentTextView.isExpanded() ? R.string.read_less : R.string.read_more);
        });


    }
}


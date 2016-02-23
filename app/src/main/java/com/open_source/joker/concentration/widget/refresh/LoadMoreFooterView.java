package com.open_source.joker.concentration.widget.refresh;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.open_source.joker.concentration.R;

/**
 * Created by liu on 2016/2/22.
 */
public class LoadMoreFooterView extends FrameLayout implements SwipeTrigger,SwipeLoadMoreTrigger {
    ImageView ivLoadMore;
    private int mTriggerOffset;
    private RingProgressDrawable ringProgressDrawable;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ringProgressDrawable = new RingProgressDrawable(context);
        Resources res = getResources();
        ringProgressDrawable.setColors(
                res.getColor(R.color.google_blue),
                res.getColor(R.color.google_red),
                res.getColor(R.color.google_yellow),
                res.getColor(R.color.google_green));
        mTriggerOffset = context.getResources().getDimensionPixelOffset(R.dimen.load_more_trigger_offset_google);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivLoadMore = (ImageView) findViewById(R.id.ivLoadMore);
        ivLoadMore.setBackgroundDrawable(ringProgressDrawable);
    }

    @Override
    public void onLoadMore() {
        ringProgressDrawable.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int y, boolean isComplete) {
        if (!isComplete){
            ringProgressDrawable.setPercent(-y / (float) mTriggerOffset);
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void complete() {
        ringProgressDrawable.stop();
    }

    @Override
    public void onReset() {

    }
}

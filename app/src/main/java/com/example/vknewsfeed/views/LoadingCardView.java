package com.example.vknewsfeed.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.vknewsfeed.R;

public class LoadingCardView extends CardView {
    private static final int[] STATE_LOADING = {R.attr.state_loading};
    private boolean isLoading = true;

    public LoadingCardView(Context context) {
        super(context);
    }

    public LoadingCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (isLoading) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, STATE_LOADING);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    public void setLoading(boolean isLoading) {
        if (this.isLoading != isLoading) {
            this.isLoading = isLoading;
            refreshDrawableState();
        }
    }
}

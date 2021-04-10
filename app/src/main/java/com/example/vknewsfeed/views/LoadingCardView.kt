package com.example.vknewsfeed.views

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import com.example.vknewsfeed.R

class LoadingCardView : CardView {
    private var isLoading = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return if (isLoading) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            mergeDrawableStates(drawableState, STATE_LOADING)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }
    }

    fun setLoading(isLoading: Boolean) {
        if (this.isLoading != isLoading) {
            this.isLoading = isLoading
            refreshDrawableState()
        }
    }

    companion object {
        private val STATE_LOADING = intArrayOf(R.attr.state_loading)
    }
}
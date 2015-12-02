package com.example.choa.examplequickreturn;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Choa on 2015. 12. 1..
 */
public class QuickReturnBehavior extends CoordinatorLayout.Behavior<View> {

    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = -1;

    private int mScrollingDirection;
    private int mScrollDistance;
    private int mScrollTrigger;

    private int mActionBarSize;

    private boolean isShowing = true;

    private ObjectAnimator mAnimator;



    public QuickReturnBehavior() {
    }

    public QuickReturnBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarSize = (int) a.getDimension(0, 0);
        a.recycle();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mScrollingDirection != DIRECTION_UP) {
            mScrollingDirection = DIRECTION_UP;
//            mScrollDistance = 0;
        } else if (dy < 0 && mScrollingDirection != DIRECTION_DOWN) {
            mScrollingDirection = DIRECTION_DOWN;
//            mScrollDistance = 0;
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        Log.d("nestedScroll", "dyConsumed:" + dyConsumed + ", dyUnconsumed" + dyUnconsumed);

        float translationY = child.getTranslationY();
        float resultY = translationY - dyConsumed;

        if (resultY > 0) {
            resultY = 0;
        } else if (resultY < -mActionBarSize) {
            resultY = -mActionBarSize;
        }

        if (resultY != translationY) {
            child.setTranslationY(resultY);
            Log.d("scroll", "res:" + resultY);
        }

        /*mScrollDistance += dyConsumed;
        if (mScrollDistance > mActionBarSize
                && mScrollTrigger != DIRECTION_UP) {
            //Hide the target view
            mScrollTrigger = DIRECTION_UP;
            restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
        } else if (mScrollDistance < 0
                && mScrollTrigger != DIRECTION_DOWN) {
            //Return the target view
            mScrollTrigger = DIRECTION_DOWN;
            restartAnimator(child, 0f);
        }*/

    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout,
                                 View child, View target,
                                 float velocityX, float velocityY,
                                 boolean consumed) {

        Log.d("fling", "velocityY:" + velocityY + ", target.id:" + target.getId());

        //We only care when the target view is already handling the fling
        if (consumed) {
            if (velocityY > 0 && mScrollTrigger != DIRECTION_UP) {
                mScrollTrigger = DIRECTION_UP;
                restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
            } else if (velocityY < 0 && mScrollTrigger != DIRECTION_DOWN) {
                mScrollTrigger = DIRECTION_DOWN;
                restartAnimator(child, 0f);
            }
        }

        return false;
    }

    /* Helper Method */
    private void restartAnimator(View target, float value) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        mAnimator = ObjectAnimator
                .ofFloat(target, View.TRANSLATION_Y, value)
                .setDuration(250);
        mAnimator.start();
    }

    private float getTargetHideValue(ViewGroup parent, View target) {
        if (target instanceof AppBarLayout) {
            return -target.getHeight();
        } else if (target instanceof FloatingActionButton) {
            return parent.getHeight() - target.getTop();
        }

        return 0f;
    }
}

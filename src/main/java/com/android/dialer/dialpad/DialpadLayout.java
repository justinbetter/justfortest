package com.android.dialer.dialpad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by justi on 2017/6/22.
 */

public class DialpadLayout {

    public static class DialpadSlidingLinearLayout extends LinearLayout {

        public DialpadSlidingLinearLayout(Context context) {
            super(context);
        }

        public DialpadSlidingLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public DialpadSlidingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @NeededForReflection
        public float getYFraction() {
            final int height = getHeight();
            if (height == 0) return 0;
            return getTranslationY() / height;
        }

        @NeededForReflection
        public void setYFraction(float yFraction) {
            setTranslationY(yFraction * getHeight());
        }
    }

    /**
     * LinearLayout that always returns true for onHoverEvent callbacks, to fix
     * problems with accessibility due to the dialpad overlaying other fragments.
     */
    public static class HoverIgnoringLinearLayout extends LinearLayout {

        public HoverIgnoringLinearLayout(Context context) {
            super(context);
        }

        public HoverIgnoringLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public HoverIgnoringLinearLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean onHoverEvent(MotionEvent event) {
            return true;
        }
    }

}

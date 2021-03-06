package net.jejer.hipda.ui.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import net.jejer.hipda.R;
import net.jejer.hipda.bean.HiSettingsHelper;

/**
 * Created by GreenSkinMonster on 2016-11-10.
 */

public class RecyclerItemClickListener implements View.OnTouchListener {

    private static final long MIN_CLICK_INTERVAL = 600;

    private final OnItemClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;
    private View mChildView;
    private long mLastClickTime;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mChildView != null && mListener != null) {
                    long currentClickTime = System.currentTimeMillis();
                    long elapsedTime = currentClickTime - mLastClickTime;
                    mLastClickTime = currentClickTime;

                    if (elapsedTime <= MIN_CLICK_INTERVAL)
                        return true;

                    mListener.onItemClick((int) mChildView.getTag());
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mChildView != null && mListener != null) {
                    mListener.onLongItemClick(mChildView, (int) mChildView.getTag());
                }
                super.onLongPress(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mChildView != null && mListener != null) {
                    mListener.onDoubleTap();
                }
                return super.onDoubleTap(e);
            }
        });
    }

    @Override
    public boolean onTouch(final View view, MotionEvent event) {
        mChildView = view;
        mGestureDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        //hack to delay ripple effect, should be replaced by better way
        if (HiSettingsHelper.getInstance().isClickEffect()) {
            view.drawableHotspotChanged(x, y);

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    view.setTag(R.id.rippleKey, "");
                    view.postDelayed(() -> {
                        try {
                            if (view.getTag(R.id.rippleKey) != null)
                                view.setPressed(true);
                        } catch (Exception ignored) {
                        }
                    }, 200);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.setTag(R.id.rippleKey, null);
                    view.setPressed(false);
                    break;
            }
        }
        return true;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onLongItemClick(View view, int position);

        void onDoubleTap();
    }
}

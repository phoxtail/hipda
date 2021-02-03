package net.jejer.hipda.ui.widget;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by GreenSkinMonster on 2015-03-10.
 */
public abstract class OnViewItemSingleClickListener implements AdapterView.OnItemClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime;

    public abstract void onItemSingleClick(View view, int i);

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        long currentClickTime = System.currentTimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;
        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        onItemSingleClick(view, i);
    }

}

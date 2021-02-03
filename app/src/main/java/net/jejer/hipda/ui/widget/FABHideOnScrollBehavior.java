package net.jejer.hipda.ui.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by GreenSkinMonster on 2016-11-14.
 */
public class FABHideOnScrollBehavior extends FloatingActionButton.Behavior {

    public FABHideOnScrollBehavior() {
        super();
    }

    public static void hideFab(FloatingActionButton child) {
        child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                fab.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        boolean isNearBottom = target instanceof XRecyclerView && ((XRecyclerView) target).isNearBottom();
        if (isNearBottom && child.getVisibility() == View.INVISIBLE) {
            child.show();
        } else {
            if (!isNearBottom && dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                hideFab(child);
            } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
                child.show();
            }
        }
    }

    // http://stackoverflow.com/a/39875070
    @Override
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull Rect rect) {
        super.getInsetDodgeRect(parent, child, rect);
        return false;
    }
}
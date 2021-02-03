package net.jejer.hipda.ui.widget.swipeback;

/**
 * @author Yrom
 */
public interface SwipeBackActivityBase {

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();

}

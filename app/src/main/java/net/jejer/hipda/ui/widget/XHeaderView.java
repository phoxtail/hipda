package net.jejer.hipda.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import net.jejer.hipda.R;
import net.jejer.hipda.utils.ColorHelper;
import net.jejer.hipda.utils.Utils;

/**
 * Created by GreenSkinMonster on 2016-11-08.
 */
public class XHeaderView extends RelativeLayout {

    public final static int STATE_HIDDEN = 0;
    public final static int STATE_LOADING = 1;
    public final static int STATE_READY = 2;
    public final static int STATE_ERROR = 4;
    private final View mLayout;
    private final ProgressBar mProgressBar;
    private final TextView mTitle;
    private int mState = STATE_HIDDEN;

    public XHeaderView(Context context) {
        this(context, null);
    }

    public XHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayout = inflate(getContext(), R.layout.vw_header, null);
        mLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                Utils.dpToPx(XRecyclerView.HEIGHT_IN_DP)));
        addView(mLayout);

        mProgressBar = findViewById(R.id.header_progressbar);
        mProgressBar.getIndeterminateDrawable()
                .setColorFilter(Color.LTGRAY, android.graphics.PorterDuff.Mode.SRC_IN);
        mTitle = findViewById(R.id.header_text);
    }

    protected int getState() {
        return mState;
    }

    protected void setState(int state) {
        mState = state;
        switch (state) {
            case STATE_READY:
                mProgressBar.setVisibility(GONE);
                mTitle.setTextColor(ColorHelper.getTextColorSecondary(getContext()));
                mTitle.setVisibility(VISIBLE);
                break;

            case STATE_LOADING:
                mTitle.setVisibility(GONE);
                mProgressBar.setVisibility(VISIBLE);
                break;

            case STATE_ERROR:
                mProgressBar.setVisibility(View.GONE);
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.md_orange_800));
                mTitle.setText(R.string.footer_hint_error);
                break;

            default:
                break;
        }
    }

    protected int getTopMargin() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();
        return lp.topMargin;
    }

    protected void setTopMargin(int margin) {
        if (margin < 0) return;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();
        lp.topMargin = margin;
        mLayout.setLayoutParams(lp);
    }

}
package net.jejer.hipda.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.jejer.hipda.R;
import net.jejer.hipda.bean.HiSettingsHelper;
import net.jejer.hipda.emoji.EmojiPopup;
import net.jejer.hipda.utils.ColorHelper;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.Utils;

import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by GreenSkinMonster on 2017-06-14.
 */

public class BaseActivity extends AppCompatActivity {

    public String mSessionId;
    protected View mRootView;
    protected View mMainFrameContainer;
    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;
    protected FloatingActionButton mMainFab;
    protected FloatingActionButton mNotificationFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionId = UUID.randomUUID().toString();

        try {
            if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == HiSettingsHelper.getInstance().getScreenOrientation()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == HiSettingsHelper.getInstance().getScreenOrientation()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }
        } catch (Exception ignored) {
            //avoid android 8.0 bug
        }

        int theme = HiUtils.getThemeValue(this,
                HiSettingsHelper.getInstance().getActiveTheme(),
                HiSettingsHelper.getInstance().getPrimaryColor());
        setTheme(theme);

        if (HiSettingsHelper.getInstance().isNavBarColored()) {
            getWindow().setNavigationBarColor(ColorHelper.getColorPrimary(this));
            View view = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (theme == R.style.ThemeLight_White) {
                    view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                } else {
                    view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mAppBarLayout != null) {
            if (HiSettingsHelper.getInstance().isWhiteTheme()) {
                mAppBarLayout.setStateListAnimator(null);
                mAppBarLayout.setElevation(Utils.dpToPx(2));
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (HiApplication.isFontSet())
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        else
            super.attachBaseContext(newBase);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAppBarScrollFlag() {
        setAppBarCollapsible(HiSettingsHelper.getInstance().isAppBarCollapsible());
    }

    protected void setAppBarCollapsible(boolean collapsible) {
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        if (collapsible) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        } else {
            params.setScrollFlags(0);
        }
        mToolbar.setLayoutParams(params);
    }

    public FloatingActionButton getMainFab() {
        return mMainFab;
    }

    public FloatingActionButton getNotificationFab() {
        return mNotificationFab;
    }

    public EmojiPopup.Builder getEmojiBuilder() {
        return EmojiPopup.Builder.fromRootView(mRootView);
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame_container);
        if (fragment instanceof BaseFragment) {
            if (!((BaseFragment) fragment).onBackPressed()) {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    public void finishWithNoSlide() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close_exit);
    }

    public void finishWithDefault() {
        super.finish();
    }

}

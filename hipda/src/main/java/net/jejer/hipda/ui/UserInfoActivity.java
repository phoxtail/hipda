package net.jejer.hipda.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.AppBarLayout;

import net.jejer.hipda.R;

/**
 * Created by GreenSkinMonster on 2017-06-15.
 */

public class UserInfoActivity extends SwipeBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);
        mRootView = findViewById(R.id.main_activity_root_view);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_frame_container) == null) {
            Bundle arguments = getIntent().getExtras();
            UserInfoFragment fragment = new UserInfoFragment();
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .add(R.id.main_frame_container, fragment).commit();
        }
    }

}

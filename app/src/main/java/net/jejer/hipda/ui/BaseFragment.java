package net.jejer.hipda.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.jejer.hipda.R;
import net.jejer.hipda.async.PostSmsAsyncTask;
import net.jejer.hipda.emoji.EmojiEditText;
import net.jejer.hipda.emoji.EmojiPopup;
import net.jejer.hipda.ui.widget.OnSingleClickListener;
import net.jejer.hipda.utils.UIUtils;
import net.jejer.hipda.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * a base fragment
 * Created by GreenSkinMonster on 2015-05-09.
 */
public abstract class BaseFragment extends Fragment {

    public String mSessionId;
    protected EmojiPopup mEmojiPopup;
    protected IconicsDrawable mKeyboardDrawable;
    protected IconicsDrawable mFaceDrawable;
    protected FloatingActionButton mMainFab;
    protected FloatingActionButton mNotificationFab;

    protected void setActionBarTitle(CharSequence title) {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            String t = Utils.nullToText(title);
            if (actionBar != null && !t.contentEquals(actionBar.getTitle())) {
                actionBar.setTitle(t);
            }
        }
    }

    void setActionBarTitle(@StringRes int resId) {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
                actionBar.setTitle(resId);
        }
    }

    protected void setActionBarSubtitle(CharSequence title) {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            String t = Utils.nullToText(title);
            if (actionBar != null && !t.contentEquals(actionBar.getTitle())) {
                actionBar.setSubtitle(t);
            }
        }
    }

    void setupFab() {
        if (getActivity() != null) {
            if (mMainFab != null) {
                mMainFab.setVisibility(View.GONE);
            }
            if (mNotificationFab != null) {
                mNotificationFab.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionId = UUID.randomUUID().toString();
        setRetainInstance(true);

        if (getActivity() instanceof BaseActivity) {
            BaseActivity activity = ((BaseActivity) getActivity());
            mMainFab = activity.getMainFab();
            mNotificationFab = activity.getNotificationFab();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            BaseActivity activity = ((BaseActivity) getActivity());
            if (activity != null) {
                mMainFab = activity.getMainFab();
                mNotificationFab = activity.getNotificationFab();
                setupFab();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mEmojiPopup != null)
            mEmojiPopup.cleanup();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setupFab();
    }

    public void scrollToTop() {
    }

    public void stopScroll() {
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        stopScroll();
        super.onDestroyView();
    }

    void showSendSmsDialog(final String uid, final String username, final PostSmsAsyncTask.SmsPostListener listener) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.dialog_send_sms, null);

        final EditText etSmsContent = viewLayout.findViewById(R.id.et_sms_content);
        final EditText etRecipient = viewLayout.findViewById(R.id.et_sms_recipient);
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());

        if (!TextUtils.isEmpty(uid)) {
            etRecipient.setVisibility(View.GONE);
        }

        popDialog.setTitle("发送短消息" + (!TextUtils.isEmpty(uid) ? "给 " + Utils.nullToText(username) : ""));
        popDialog.setView(viewLayout);
        popDialog.setPositiveButton("发送", null);
        popDialog.setNegativeButton("取消", null);
        final AlertDialog dialog = popDialog.create();

        if (etRecipient.getVisibility() == View.VISIBLE) {
            etRecipient.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean textNeeded = (etRecipient.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etRecipient.getText()))
                            || TextUtils.isEmpty(etSmsContent.getText());
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!textNeeded);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        etSmsContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean textNeeded = (etRecipient.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etRecipient.getText()))
                        || TextUtils.isEmpty(etSmsContent.getText());
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!textNeeded);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String recipient = username;
                String content = Utils.nullToText(etSmsContent.getText().toString()).trim();
                if (TextUtils.isEmpty(uid))
                    recipient = etRecipient.getText().toString().trim();

                if (TextUtils.isEmpty(uid) && TextUtils.isEmpty(recipient)) {
                    UIUtils.toast("请填写收件人");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    UIUtils.toast("请填写内容");
                    return;
                }
                new PostSmsAsyncTask(getActivity(), uid, recipient, listener, dialog).execute(content);
                UIUtils.hideSoftKeyboard(getActivity());
            }
        });

        theButton.setEnabled(false);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void setUpEmojiPopup(final EmojiEditText mEtContent, final ImageButton mIbEmojiSwitch) {
        if (mKeyboardDrawable == null)
            mKeyboardDrawable = new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_keyboard).sizeDp(28).color(Color.GRAY);
        if (mFaceDrawable == null)
            mFaceDrawable = new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_tag_faces).sizeDp(28).color(Color.GRAY);

        mIbEmojiSwitch.setImageDrawable(mFaceDrawable);
        mIbEmojiSwitch.setOnClickListener(v -> mEmojiPopup.toggle());

        mEtContent.setOnClickListener(v -> {
            if (mEmojiPopup.isShowing())
                mEmojiPopup.dismiss();
        });

        mEmojiPopup = ((BaseActivity) getActivity()).getEmojiBuilder()
                .setOnEmojiClickedListener(emoji -> mEtContent.requestFocus()).setOnEmojiPopupShownListener(() -> mIbEmojiSwitch.setImageDrawable(mKeyboardDrawable)).setOnEmojiPopupDismissListener(() -> mIbEmojiSwitch.setImageDrawable(mFaceDrawable)).setOnSoftKeyboardCloseListener(() -> mEmojiPopup.dismiss()).build(mEtContent);
    }

}

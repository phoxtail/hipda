package net.jejer.hipda.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.jejer.hipda.R;
import net.jejer.hipda.async.BlacklistHelper;
import net.jejer.hipda.async.PostSmsAsyncTask;
import net.jejer.hipda.bean.HiSettingsHelper;
import net.jejer.hipda.bean.SearchBean;
import net.jejer.hipda.bean.SimpleListBean;
import net.jejer.hipda.bean.SimpleListItemBean;
import net.jejer.hipda.bean.UserInfoBean;
import net.jejer.hipda.cache.SignatureContainer;
import net.jejer.hipda.glide.GlideHelper;
import net.jejer.hipda.job.JobMgr;
import net.jejer.hipda.job.SimpleListEvent;
import net.jejer.hipda.job.SimpleListJob;
import net.jejer.hipda.okhttp.OkHttpHelper;
import net.jejer.hipda.ui.adapter.RecyclerItemClickListener;
import net.jejer.hipda.ui.adapter.SimpleListAdapter;
import net.jejer.hipda.ui.widget.HiProgressDialog;
import net.jejer.hipda.ui.widget.OnSingleClickListener;
import net.jejer.hipda.ui.widget.SimpleDivider;
import net.jejer.hipda.ui.widget.XFooterView;
import net.jejer.hipda.ui.widget.XRecyclerView;
import net.jejer.hipda.utils.Constants;
import net.jejer.hipda.utils.HiParser;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.Logger;
import net.jejer.hipda.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends BaseFragment implements PostSmsAsyncTask.SmsPostListener {

    public static final String ARG_USERNAME = "USERNAME";
    public static final String ARG_UID = "UID";
    private final List<SimpleListItemBean> mSimpleListItemBeans = new ArrayList<>();
    private String mUid;
    private String mUsername;
    private String mAvatarUrl;
    private String mFormHash;
    private ImageView mAvatarView;
    private TextView mDetailView;
    private TextView mUsernameView;
    private TextView mOnlineView;
    private XRecyclerView mRecyclerView;
    private SimpleListAdapter mSimpleListAdapter;
    private Button mButton;

    private boolean isShowThreads;
    private boolean isThreadsLoaded;

    private int mPage = 1;
    private boolean mLoading = false;
    private String mSearchId;
    private int mMaxPage;

    private HiProgressDialog smsPostProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_USERNAME)) {
            mUsername = getArguments().getString(ARG_USERNAME);
        }

        if (getArguments().containsKey(ARG_UID)) {
            mUid = getArguments().getString(ARG_UID);
        }

        RecyclerItemClickListener itemClickListener = new RecyclerItemClickListener(getActivity(), new OnItemClickListener());
        mSimpleListAdapter = new SimpleListAdapter(this, SimpleListJob.TYPE_SEARCH_USER_THREADS, itemClickListener);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        view.setClickable(false);

        mAvatarView = view.findViewById(R.id.userinfo_avatar);
        if (HiSettingsHelper.getInstance().isLoadAvatar()) {
            mAvatarView.setVisibility(View.VISIBLE);
            mAvatarView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (!TextUtils.isEmpty(mAvatarUrl)) {
                        GlideHelper.clearAvatarCache(mAvatarUrl);
                        GlideHelper.loadAvatar(UserInfoFragment.this, mAvatarView, mAvatarUrl);
                        UIUtils.toast("头像已经刷新");
                    } else {
                        UIUtils.toast("用户未设置头像");
                    }
                }
            });
        } else {
            mAvatarView.setVisibility(View.GONE);
        }

        mUsernameView = view.findViewById(R.id.userinfo_username);
        mUsernameView.setText(mUsername);
        mUsernameView.setTextSize(HiSettingsHelper.getInstance().getPostTextSize() + 2);

        mOnlineView = view.findViewById(R.id.user_online);
        mOnlineView.setVisibility(View.INVISIBLE);

        mDetailView = view.findViewById(R.id.userinfo_detail);
        mDetailView.setText("正在获取信息...");
        mDetailView.setTextSize(HiSettingsHelper.getInstance().getPostTextSize());

        //to avoid click through this view
        view.setOnTouchListener((v, event) -> true);

        mRecyclerView = view.findViewById(R.id.rv_search_threads);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDivider(getActivity()));

        mButton = view.findViewById(R.id.btn_search_threads);
        mButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                isShowThreads = !isShowThreads;
                if (isShowThreads) {
                    mButton.setText("显示信息");
                    mDetailView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (!isThreadsLoaded) {
                        mButton.setEnabled(false);
                        SearchBean bean = new SearchBean();
                        bean.setUid(mUid);
                        bean.setSearchId(mSearchId);
                        SimpleListJob job = new SimpleListJob(UserInfoFragment.this.getActivity(), mSessionId,
                                SimpleListJob.TYPE_SEARCH_USER_THREADS,
                                mPage,
                                bean);
                        JobMgr.addJob(job);
                    }
                } else {
                    mButton.setText("搜索帖子");
                    mRecyclerView.setVisibility(View.GONE);
                    mDetailView.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mDetailView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        OkHttpHelper.getInstance().asyncGet(HiUtils.UserInfoUrl + mUid, new UserInfoCallback());

        mRecyclerView.setAdapter(mSimpleListAdapter);
        mRecyclerView.addOnScrollListener(new OnScrollListener());
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_userinfo, menu);
        menu.findItem(R.id.action_send_sms).setIcon(new IconicsDrawable(getActivity(),
                GoogleMaterial.Icon.gmd_insert_comment).actionBar()
                .color(HiSettingsHelper.getInstance().getToolbarTextColor()));

        setActionBarTitle("用户信息");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {// Implemented in activity
            return false;
        } else if (itemId == R.id.action_send_sms) {
            showSendSmsDialog(mUid, mUsername, this);
            return true;
        } else if (itemId == R.id.action_blacklist) {
            BlacklistHelper.addBlacklist(mFormHash, mUsername);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onSmsPrePost() {
        smsPostProgressDialog = HiProgressDialog.show(getActivity(), "正在发送...");
    }

    @Override
    public void onSmsPostDone(int status, final String message, AlertDialog dialog) {
        if (status == Constants.STATUS_SUCCESS) {
            smsPostProgressDialog.dismiss(message);
            if (dialog != null)
                dialog.dismiss();
        } else {
            smsPostProgressDialog.dismissError(message);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleListEvent event) {
        if (!mSessionId.equals(event.mSessionId))
            return;

        EventBus.getDefault().removeStickyEvent(event);

        if (event.mStatus == Constants.STATUS_IN_PROGRESS) {
            return;
        }

        SimpleListBean list = event.mData;
        mLoading = false;

        if (mButton != null)
            mButton.setEnabled(true);

        if (list == null || list.getCount() == 0) {
            UIUtils.toast("帖子加载失败");
            return;
        }

        mSearchId = list.getSearchId();
        mMaxPage = list.getMaxPage();
        mSimpleListItemBeans.addAll(list.getAll());
        mSimpleListAdapter.setDatas(mSimpleListItemBeans);
        isThreadsLoaded = true;
    }

    private class UserInfoCallback implements OkHttpHelper.ResultCallback {
        @Override
        public void onError(Exception e) {
            Logger.e(e);
            mDetailView.setText("获取信息失败 : " + OkHttpHelper.getErrorMessage(e));
        }

        @Override
        public void onResponse(String response) {
            UserInfoBean info = HiParser.parseUserInfo(response);
            if (info != null) {
                if (HiSettingsHelper.getInstance().isLoadAvatar()) {
                    mAvatarView.setVisibility(View.VISIBLE);
                    GlideHelper.loadAvatar(UserInfoFragment.this, mAvatarView, info.getAvatarUrl());
                    mAvatarUrl = info.getAvatarUrl();
                } else {
                    mAvatarView.setVisibility(View.GONE);
                }
                String sig = SignatureContainer.getSignature(mUid);
                mDetailView.setText((!TextUtils.isEmpty(sig) ? "签名: " + sig + "\n\n" : "") + info.getDetail());
                mUsername = info.getUsername();
                mUsernameView.setText(mUsername);
                mFormHash = info.getFormHash();
                if (info.isOnline()) {
                    mOnlineView.setVisibility(View.VISIBLE);
                } else {
                    mOnlineView.setVisibility(View.INVISIBLE);
                }
            } else {
                mDetailView.setText("解析信息失败, 请重试.");
            }
        }
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        int visibleItemCount, totalItemCount;

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) {
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItem) >= totalItemCount - 5) {
                    if (!mLoading) {
                        mLoading = true;
                        if (mPage < mMaxPage) {
                            mPage++;
                            mRecyclerView.setFooterState(XFooterView.STATE_LOADING);
                            SearchBean bean = new SearchBean();
                            bean.setUid(mUid);
                            bean.setSearchId(mSearchId);
                            SimpleListJob job = new SimpleListJob(UserInfoFragment.this.getActivity(), mSessionId,
                                    SimpleListJob.TYPE_SEARCH_USER_THREADS,
                                    mPage,
                                    bean);
                            JobMgr.addJob(job);
                        } else {
                            mRecyclerView.setFooterState(XFooterView.STATE_END);
                        }
                    }
                }
            }
        }
    }

    private class OnItemClickListener implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
            if (position < 0 || position >= mSimpleListAdapter.getItemCount()) {
                return;
            }
            SimpleListItemBean item = mSimpleListAdapter.getItem(position);
            FragmentUtils.showThreadActivity(getActivity(), false, item.getTid(), item.getTitle(), -1, -1, null, -1);
        }

        @Override
        public void onLongItemClick(View view, int position) {
            if (position < 0 || position >= mSimpleListAdapter.getItemCount()) {
                return;
            }
            SimpleListItemBean item = mSimpleListAdapter.getItem(position);
            FragmentUtils.showThreadActivity(getActivity(), false, item.getTid(), item.getTitle(), ThreadDetailFragment.LAST_PAGE, ThreadDetailFragment.LAST_FLOOR, null, -1);
        }

        @Override
        public void onDoubleTap() {
        }
    }

}

package net.jejer.hipda.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import net.jejer.hipda.bean.ThreadBean;
import net.jejer.hipda.ui.widget.ThreadItemLayout;

public class ThreadListAdapter extends BaseRvAdapter<ThreadBean> {

    private final RequestManager mGlide;

    public ThreadListAdapter(RequestManager glide, RecyclerItemClickListener listener) {
        mGlide = glide;
        mListener = listener;
    }

    @Override
    public ViewHolderImpl onCreateViewHolderImpl(ViewGroup parent) {
        return new ViewHolderImpl(new ThreadItemLayout(parent.getContext(), mGlide));
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, int position) {
        ThreadBean thread = getItem(position);
        if (viewHolder instanceof ViewHolderImpl)
            ((ViewHolderImpl) viewHolder).mItemLayout.setData(thread);
    }

    private static class ViewHolderImpl extends RecyclerView.ViewHolder {
        final ThreadItemLayout mItemLayout;

        ViewHolderImpl(View itemView) {
            super(itemView);
            mItemLayout = (ThreadItemLayout) itemView;
        }
    }

}

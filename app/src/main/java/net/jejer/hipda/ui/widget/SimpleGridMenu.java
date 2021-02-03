package net.jejer.hipda.ui.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

import net.jejer.hipda.R;
import net.jejer.hipda.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by GreenSkinMonster on 2017-06-24.
 */

public class SimpleGridMenu {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final LinkedHashMap<String, MenuItem> mMenuItems = new LinkedHashMap<>();
    private final List<String> mActionKeys = new ArrayList<>();
    private String mTitle;
    private AlertDialog mDialog;
    private DialogInterface.OnDismissListener mOnDismissListener;

    public SimpleGridMenu(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public void show() {
        View view = mInflater.inflate(R.layout.dialog_grid_menu, null);
        GridView gridView = view.findViewById(R.id.grid_view);
        TextView tvTitle = view.findViewById(R.id.tv_title);

        gridView.setAdapter(new MenuActionAdapter(mContext));
        tvTitle.setText(mTitle);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        mDialog = builder.create();
        mDialog.setOnDismissListener(mOnDismissListener);
        mDialog.show();

        gridView.setOnItemClickListener((adapterView, view1, position, row) -> {
            String actionKey = (String) view1.getTag();
            MenuItem menuItem = mMenuItems.get(actionKey);
            if (menuItem != null) {
                menuItem.listener.onItemClick(adapterView, view1, position, row);
            }
            dismiss();
        });
    }

    public void dismiss() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    public void add(String actionKey, String actionName, AdapterView.OnItemClickListener listener) {
        add(actionKey, actionName, listener, null, null);
    }

    public void add(String actionKey, String actionName, AdapterView.OnItemClickListener listener,
                    IIcon icon, View.OnClickListener iconListener) {
        MenuItem menuItem = new MenuItem();
        menuItem.actionName = actionName;
        menuItem.listener = listener;
        menuItem.icon = icon;
        menuItem.iconListener = iconListener;
        mMenuItems.put(actionKey, menuItem);
        if (!mActionKeys.contains(actionKey))
            mActionKeys.add(actionKey);
    }

    private static class MenuItem {
        String actionName;
        AdapterView.OnItemClickListener listener;
        IIcon icon;
        View.OnClickListener iconListener;
    }

    private class MenuActionAdapter extends ArrayAdapter<String> {
        MenuActionAdapter(Context context) {
            super(context, 0, mActionKeys);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.item_grid_menu, parent, false);
            } else {
                view = convertView;
            }
            final String actionKey = mActionKeys.get(position);
            view.setTag(actionKey);

            final MenuItem menuItem = mMenuItems.get(actionKey);
            TextView textView = view.findViewById(R.id.action_text);
            textView.setText(mMenuItems.get(actionKey).actionName);

            ImageView imageView = view.findViewById(R.id.action_image);
            if (menuItem.icon != null) {
                imageView.setVisibility(View.VISIBLE);
                int pading = Utils.dpToPx(16);
                imageView.setPadding(pading, pading, pading, pading);
                imageView.setClickable(true);
                imageView.setImageDrawable(new IconicsDrawable(mContext, menuItem.icon)
                        .sizeDp(24).color(ContextCompat.getColor(mContext, R.color.background_grey)));
                imageView.setOnClickListener(v -> {
                    if (menuItem.iconListener != null)
                        menuItem.iconListener.onClick(v);
                    dismiss();
                });

            } else {
                imageView.setVisibility(View.GONE);
            }
            return view;
        }
    }

}

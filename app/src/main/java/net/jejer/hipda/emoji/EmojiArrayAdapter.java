package net.jejer.hipda.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.jejer.hipda.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class EmojiArrayAdapter extends ArrayAdapter<Emoji> {
    @Nullable
    private OnEmojiClickedListener onEmojiClickedListener;

    @SuppressWarnings("PMD.UseVarargs")
    EmojiArrayAdapter(final Context context, final Emoji[] data) {
        super(context, R.layout.emoji_text_view, toList(data));
    }

    /**
     * we need this because Arrays.asList does not support {@link Collection#clear()}
     */
    @SuppressWarnings("PMD.UseVarargs")
    private static List<Emoji> toList(final Emoji[] data) {
        final List<Emoji> list = new ArrayList<>(data.length);
        Collections.addAll(list, data);
        return list;
    }

    @Override
    public View getView(final int position, final View convertView, @NotNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.emoji_text_view, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.icon = view.findViewById(R.id.emoji_icon);
            view.setTag(holder);
        }

        final Emoji emoji = getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.emoji = emoji;
        holder.icon.setText(emoji.getEmoji());

        view.setOnClickListener(v -> {
            if (onEmojiClickedListener != null) {
                final ViewHolder tag = (ViewHolder) v.getTag();
                onEmojiClickedListener.onEmojiClicked(tag.emoji);
            }
        });

        return view;
    }

    public void updateEmojis(final Collection<Emoji> emojis) {
        clear();
        addAll(emojis);
        notifyDataSetChanged();
    }

    public void setOnEmojiClickedListener(@Nullable final OnEmojiClickedListener onEmojiClickedListener) {
        this.onEmojiClickedListener = onEmojiClickedListener;
    }

    static class ViewHolder {
        Emoji emoji;
        TextView icon;
    }
}

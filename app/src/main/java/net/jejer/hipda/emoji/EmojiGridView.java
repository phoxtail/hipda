package net.jejer.hipda.emoji;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import androidx.annotation.Nullable;

import net.jejer.hipda.R;

final class EmojiGridView extends FrameLayout {
    EmojiGridView(final Context context) {
        super(context);

        View.inflate(context, R.layout.emoji_grid, this);
    }

    public EmojiGridView init(final Emoji[] emojis, @Nullable final OnEmojiClickedListener onEmojiClickedListener) {
        final GridView gridView = findViewById(R.id.emoji_grid_view);

        final EmojiArrayAdapter emojiArrayAdapter = new EmojiArrayAdapter(getContext(), emojis);
        emojiArrayAdapter.setOnEmojiClickedListener(onEmojiClickedListener);
        gridView.setAdapter(emojiArrayAdapter);
        return this;
    }
}

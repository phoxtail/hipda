package net.jejer.hipda.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import net.jejer.hipda.R;

public class EmojiEditText extends AppCompatEditText {
    private int emojiSize;

    public EmojiEditText(final Context context) {
        super(context);
        init(null);
    }

    public EmojiEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable final AttributeSet attrs) {
        if (attrs == null) {
            emojiSize = (int) getTextSize();
        } else {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EmojiEditText);

            try {
                emojiSize = (int) a.getDimension(R.styleable.EmojiEditText_emojiSize, getTextSize());
            } finally {
                a.recycle();
            }
        }

        setText(getText());
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
        EmojiHandler.addEmojis(getContext(), getText(), emojiSize);
    }

    public void backspace() {
        final KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        dispatchKeyEvent(event);
    }

    public void input(final Emoji emoji) {
        if (emoji != null) {
            final int start = getSelectionStart();
            final int end = getSelectionEnd();
            if (start < 0) {
                append(emoji.getEmoji());
            } else {
                getText().replace(Math.min(start, end), Math.max(start, end), emoji.getEmoji(), 0, emoji.getEmoji().length());
            }
        }
    }
}

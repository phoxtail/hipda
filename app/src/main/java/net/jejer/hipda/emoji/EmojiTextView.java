package net.jejer.hipda.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import net.jejer.hipda.R;

public class EmojiTextView extends AppCompatTextView {
    private int emojiSize;

    public EmojiTextView(final Context context) {
        super(context);
        init(null);
    }

    public EmojiTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiTextView(final Context context, final AttributeSet attrs, final int defStyle) {
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
    public void setText(final CharSequence rawText, final BufferType type) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        EmojiHandler.addEmojis(getContext(), spannableStringBuilder, emojiSize);
        super.setText(spannableStringBuilder, type);
    }

}

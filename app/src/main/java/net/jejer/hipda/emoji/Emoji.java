package net.jejer.hipda.emoji;

import androidx.annotation.NonNull;

import java.io.Serializable;

public final class Emoji implements Serializable {
    private static final long serialVersionUID = 1L;
    @NonNull
    private final String emoji;

    public Emoji(@NonNull final String emoji) {
        this.emoji = emoji;
    }

    public static Emoji fromEmoji(final String emoji) {
        return new Emoji(emoji);
    }

    @NonNull
    public String getEmoji() {
        return emoji;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Emoji e = (Emoji) o;
        return emoji.equals(e.emoji);
    }

    @Override
    public int hashCode() {
        return emoji.hashCode();
    }
}

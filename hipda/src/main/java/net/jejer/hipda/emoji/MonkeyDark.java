package net.jejer.hipda.emoji;

import net.jejer.hipda.R;
import net.jejer.hipda.emoji.Emoji;

/**
 * Created by GreenSkinMonster on 2016-04-11.
 */
public class MonkeyDark {
    public final static String[] EMOJIS = {
            "{:2_41:}",
            "{:2_42:}",
            "{:2_43:}",
            "{:2_44:}",
            "{:2_45:}",
            "{:2_46:}",
            "{:2_47:}",
            "{:2_48:}",
            "{:2_49:}",
            "{:2_50:}",
            "{:2_51:}",
            "{:2_52:}",
            "{:2_53:}",
            "{:2_54:}",
            "{:2_55:}",
            "{:2_56:}"
    };

    public final static String[] IMG_SRCS = {
            "coolmonkey_01",
            "coolmonkey_02",
            "coolmonkey_03",
            "coolmonkey_04",
            "coolmonkey_05",
            "coolmonkey_06",
            "coolmonkey_07",
            "coolmonkey_08",
            "coolmonkey_09",
            "coolmonkey_10",
            "coolmonkey_11",
            "coolmonkey_12",
            "coolmonkey_13",
            "coolmonkey_14",
            "coolmonkey_15",
            "coolmonkey_16"
    };

    public final static int[] DRAWABLES = {
            R.drawable.coolmonkey_01_dark,
            R.drawable.coolmonkey_02_dark,
            R.drawable.coolmonkey_03_dark,
            R.drawable.coolmonkey_04_dark,
            R.drawable.coolmonkey_05_dark,
            R.drawable.coolmonkey_06_dark,
            R.drawable.coolmonkey_07_dark,
            R.drawable.coolmonkey_08_dark,
            R.drawable.coolmonkey_09_dark,
            R.drawable.coolmonkey_10_dark,
            R.drawable.coolmonkey_11_dark,
            R.drawable.coolmonkey_12_dark,
            R.drawable.coolmonkey_13_dark,
            R.drawable.coolmonkey_14_dark,
            R.drawable.coolmonkey_15_dark,
            R.drawable.coolmonkey_16_dark
    };

    public final static Emoji[] DATA = new Emoji[EMOJIS.length];

    static {
        for (int i = 0; i < EMOJIS.length; i++) {
            DATA[i] = Emoji.fromEmoji(EMOJIS[i]);
        }
    }
}

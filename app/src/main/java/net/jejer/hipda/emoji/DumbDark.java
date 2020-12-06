package net.jejer.hipda.emoji;

import net.jejer.hipda.R;
import net.jejer.hipda.emoji.Emoji;

/**
 * Created by GreenSkinMonster on 2016-04-11.
 */
public class DumbDark {
    public final static String[] EMOJIS = {
            "{:3_57:}",
            "{:3_58:}",
            "{:3_59:}",
            "{:3_60:}",
            "{:3_61:}",
            "{:3_62:}",
            "{:3_63:}",
            "{:3_64:}",
            "{:3_65:}",
            "{:3_66:}",
            "{:3_67:}",
            "{:3_68:}",
            "{:3_69:}",
            "{:3_70:}",
            "{:3_71:}",
            "{:3_72:}",
            "{:3_73:}",
            "{:3_74:}",
            "{:3_75:}",
            "{:3_76:}",
            "{:3_77:}",
            "{:3_78:}",
            "{:3_79:}",
            "{:3_80:}"
    };

    public final static String[] IMG_SRCS = {
            "grapeman_01",
            "grapeman_02",
            "grapeman_03",
            "grapeman_04",
            "grapeman_05",
            "grapeman_06",
            "grapeman_07",
            "grapeman_08",
            "grapeman_09",
            "grapeman_10",
            "grapeman_11",
            "grapeman_12",
            "grapeman_13",
            "grapeman_14",
            "grapeman_15",
            "grapeman_16",
            "grapeman_17",
            "grapeman_18",
            "grapeman_19",
            "grapeman_20",
            "grapeman_21",
            "grapeman_22",
            "grapeman_23",
            "grapeman_24"
    };

    public final static int[] DRAWABLES = {
            R.drawable.grapeman_01_dark,
            R.drawable.grapeman_02_dark,
            R.drawable.grapeman_03_dark,
            R.drawable.grapeman_04_dark,
            R.drawable.grapeman_05_dark,
            R.drawable.grapeman_06_dark,
            R.drawable.grapeman_07_dark,
            R.drawable.grapeman_08_dark,
            R.drawable.grapeman_09_dark,
            R.drawable.grapeman_10_dark,
            R.drawable.grapeman_11_dark,
            R.drawable.grapeman_12_dark,
            R.drawable.grapeman_13_dark,
            R.drawable.grapeman_14_dark,
            R.drawable.grapeman_15_dark,
            R.drawable.grapeman_16_dark,
            R.drawable.grapeman_17_dark,
            R.drawable.grapeman_18_dark,
            R.drawable.grapeman_19_dark,
            R.drawable.grapeman_20_dark,
            R.drawable.grapeman_21_dark,
            R.drawable.grapeman_22_dark,
            R.drawable.grapeman_23_dark,
            R.drawable.grapeman_24_dark
    };

    public final static Emoji[] DATA = new Emoji[EMOJIS.length];

    static {
        for (int i = 0; i < EMOJIS.length; i++) {
            DATA[i] = Emoji.fromEmoji(EMOJIS[i]);
        }
    }
}

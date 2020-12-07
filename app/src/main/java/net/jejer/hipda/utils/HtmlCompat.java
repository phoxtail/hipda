package net.jejer.hipda.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by GreenSkinMonster on 2016-10-17.
 */

public class HtmlCompat {

    public static Spanned fromHtml(String source) {
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }
}

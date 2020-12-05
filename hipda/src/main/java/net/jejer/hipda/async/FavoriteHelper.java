package net.jejer.hipda.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import net.jejer.hipda.okhttp.OkHttpHelper;
import net.jejer.hipda.okhttp.ParamsMap;
import net.jejer.hipda.ui.HiApplication;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.Logger;
import net.jejer.hipda.utils.UIUtils;
import net.jejer.hipda.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

import okhttp3.Request;


public class FavoriteHelper {

    public final static String TYPE_FAVORITE = "favorites";
    public final static String TYPE_ATTENTION = "attention";

    private final static int MAX_CACHE_PAGE = 3;
    private final static String FAV_CACHE_PREFS = "FavCachePrefsFile";

    private final static String FAVORITES_CACHE_KEY = "favorites";
    private final static String ATTENTION_CACHE_KEY = "attention";

    private SharedPreferences mCachePref;

    private Set<String> mFavoritesCache;
    private Set<String> mAttentionCache;

    private FavoriteHelper() {
        mCachePref = HiApplication.getAppContext().getSharedPreferences(FAV_CACHE_PREFS, 0);
        mFavoritesCache = mCachePref.getStringSet(FAVORITES_CACHE_KEY, new HashSet<String>());
        mAttentionCache = mCachePref.getStringSet(ATTENTION_CACHE_KEY, new HashSet<String>());
    }

    public static FavoriteHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void fetchMyFavorites() {
        Set<String> favTids = new HashSet<>();
        for (int i = 1; i <= MAX_CACHE_PAGE; i++) {
            ParseResult result = fetchMyFavorites(TYPE_FAVORITE, i);
            if (result.error)
                return;
            if (result.tids == null || result.tids.size() == 0)
                break;
            favTids.addAll(result.tids);
            if (i + 1 > result.lastPage)
                break;
        }
        mFavoritesCache = favTids;
        SharedPreferences.Editor editor = mCachePref.edit();
        editor.remove(FAVORITES_CACHE_KEY).apply();
        editor.putStringSet(FAVORITES_CACHE_KEY, mFavoritesCache).apply();
    }

    public void fetchMyAttention() {
        Set<String> attTids = new HashSet<>();
        for (int i = 1; i <= MAX_CACHE_PAGE; i++) {
            ParseResult result = fetchMyFavorites(TYPE_ATTENTION, i);
            if (result.error)
                return;
            if (result.tids == null || result.tids.size() == 0)
                break;
            attTids.addAll(result.tids);
            if (i + 1 > result.lastPage)
                break;
        }
        mAttentionCache = attTids;
        SharedPreferences.Editor editor = mCachePref.edit();
        editor.remove(ATTENTION_CACHE_KEY).apply();
        editor.putStringSet(ATTENTION_CACHE_KEY, mAttentionCache).apply();
    }

    private ParseResult fetchMyFavorites(String item, int page) {
        ParseResult result = new ParseResult();
        if (page <= 1) page = 1;

        String url = HiUtils.FavoritesUrl.replace("{item}", item);
        if (page > 1)
            url += "&page=" + page;

        try {
            String response = OkHttpHelper.getInstance().get(url);
            Document doc = Jsoup.parse(response);
            int last_page = 1;
            //if this is the last page, page number is in <strong>
            Elements divPageES = doc.select("div.pages");
            if (divPageES.size() > 0) {
                Element divPage = divPageES.first();
                Elements pagesES = divPage.select("div.pages a");
                pagesES.addAll(divPage.select("div.pages strong"));
                if (pagesES.size() > 0) {
                    for (Node n : pagesES) {
                        int tmp = Utils.getIntFromString(((Element) n).text());
                        if (tmp > last_page) {
                            last_page = tmp;
                        }
                    }
                }
            }
            result.lastPage = last_page;

            //get favories tid
            Set<String> tids = new HashSet<>();
            Elements checkboxes = doc.select("input.checkbox[name=delete[]]");
            for (Node n : checkboxes) {
                String tid = n.attr("value");
                if (HiUtils.isValidId(tid)) {
                    tids.add(tid);
                }
            }
            result.tids = tids;
        } catch (Exception e) {
            Logger.e(e);
            result.error = true;
        }
        return result;
    }

    public void addToCahce(String item, String tid) {
        if (HiUtils.isValidId(tid)) {
            if (TYPE_FAVORITE.equals(item) && !mFavoritesCache.contains(tid)) {
                mFavoritesCache.add(tid);
                SharedPreferences.Editor editor = mCachePref.edit();
                editor.remove(FAVORITES_CACHE_KEY).apply();
                editor.putStringSet(FAVORITES_CACHE_KEY, mFavoritesCache).apply();
            } else if (TYPE_ATTENTION.equals(item) && !mAttentionCache.contains(tid)) {
                mAttentionCache.add(tid);
                SharedPreferences.Editor editor = mCachePref.edit();
                editor.remove(ATTENTION_CACHE_KEY).apply();
                editor.putStringSet(ATTENTION_CACHE_KEY, mAttentionCache).apply();
            }
        }
    }

    public void removeFromCahce(String item, String tid) {
        if (TYPE_FAVORITE.equals(item)) {
            mFavoritesCache.remove(tid);
            SharedPreferences.Editor editor = mCachePref.edit();
            editor.remove(FAVORITES_CACHE_KEY).apply();
            editor.putStringSet(FAVORITES_CACHE_KEY, mFavoritesCache).apply();
        } else if (TYPE_ATTENTION.equals(item)) {
            mAttentionCache.remove(tid);
            SharedPreferences.Editor editor = mCachePref.edit();
            editor.remove(ATTENTION_CACHE_KEY).apply();
            editor.putStringSet(ATTENTION_CACHE_KEY, mAttentionCache).apply();
        }
    }

    public void addToCahce(String item, Set<String> tids) {
        for (String tid : tids) {
            if (TYPE_FAVORITE.equals(item)) {
                if (!mFavoritesCache.contains(tid))
                    mFavoritesCache.add(tid);
            } else if (TYPE_ATTENTION.equals(item)) {
                if (!mAttentionCache.contains(tid))
                    mAttentionCache.add(tid);
            }
        }
        if (TYPE_FAVORITE.equals(item)) {
            SharedPreferences.Editor editor = mCachePref.edit();
            editor.remove(FAVORITES_CACHE_KEY).apply();
            editor.putStringSet(FAVORITES_CACHE_KEY, mFavoritesCache).apply();
        } else if (TYPE_ATTENTION.equals(item)) {
            SharedPreferences.Editor editor = mCachePref.edit();
            editor.remove(ATTENTION_CACHE_KEY).apply();
            editor.putStringSet(ATTENTION_CACHE_KEY, mAttentionCache).apply();
        }
    }

    public void clearAll() {
        mCachePref.edit().clear().apply();
    }

    public boolean isInFavorite(String tid) {
        return mFavoritesCache.contains(tid);
    }

    public boolean isInAttention(String tid) {
        return mAttentionCache.contains(tid);
    }

    public void addFavorite(final Context ctx, final String item, final String tid) {
        if (TextUtils.isEmpty(item) || TextUtils.isEmpty(tid)) {
            UIUtils.toast("参数错误");
            return;
        }

        String url = HiUtils.FavoriteAddUrl.replace("{item}", item).replace("{tid}", tid);

        OkHttpHelper.getInstance().asyncGet(url, new OkHttpHelper.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                UIUtils.toast("添加失败 : " + OkHttpHelper.getErrorMessage(e));
            }

            @Override
            public void onResponse(String response) {
                String result = "";
                Document doc = Jsoup.parse(response, "", Parser.xmlParser());
                for (Element e : doc.select("root")) {
                    result = e.text();
                    if (result.contains("<"))
                        result = result.substring(0, result.indexOf("<"));
                }
                addToCahce(item, tid);
                UIUtils.toast(result);
            }
        });
    }

    public void removeFavorite(final Context ctx, final String item, final String tid) {
        if (TextUtils.isEmpty(item) || TextUtils.isEmpty(tid)) {
            UIUtils.toast("参数错误");
            return;
        }

        String url = HiUtils.FavoriteRemoveUrl.replace("{item}", item).replace("{tid}", tid);

        OkHttpHelper.getInstance().asyncGet(url, new OkHttpHelper.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                UIUtils.toast("移除失败 : " + OkHttpHelper.getErrorMessage(e));
            }

            @Override
            public void onResponse(String response) {
                String result = "";
                Document doc = Jsoup.parse(response, "", Parser.xmlParser());
                for (Element e : doc.select("root")) {
                    result = e.text();
                    if (result.contains("<"))
                        result = result.substring(0, result.indexOf("<"));
                }
                removeFromCahce(item, tid);
                UIUtils.toast(result);
            }
        });
    }

    public void deleteFavorite(final Context ctx, final String formhash, final String item, final String tid) {
        if (TextUtils.isEmpty(item) || TextUtils.isEmpty(tid) || TextUtils.isEmpty(formhash)) {
            UIUtils.toast("参数错误");
            return;
        }

        String url = HiUtils.FavoriteDeleteUrl.replace("{item}", item);

        ParamsMap params = new ParamsMap();
        if (FavoriteHelper.TYPE_FAVORITE.equals(item)) {
            params.put("favsubmit", "true");
        } else {
            params.put("attentionsubmit", "true");
        }

        params.put("delete[]", tid);
        params.put("formhash", formhash);
        try {
            OkHttpHelper.getInstance().asyncPost(url, params, new OkHttpHelper.ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    UIUtils.toast("移除失败 : " + OkHttpHelper.getErrorMessage(e));
                }

                @Override
                public void onResponse(String response) {
                    String result = "已取消" + (FavoriteHelper.TYPE_FAVORITE.equals(item) ? "收藏" : "关注");
                    removeFromCahce(item, tid);
                    UIUtils.toast(result);
                }
            });
        } catch (Exception e) {
            UIUtils.toast("移除失败 : " + OkHttpHelper.getErrorMessage(e));
        }
    }

    private static class SingletonHolder {
        public static final FavoriteHelper INSTANCE = new FavoriteHelper();
    }

    private class ParseResult {
        Set<String> tids;
        int lastPage = 1;
        boolean error = false;
    }

}
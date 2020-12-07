package net.jejer.hipda.async;

import android.content.SharedPreferences;
import android.text.TextUtils;

import net.jejer.hipda.okhttp.OkHttpHelper;
import net.jejer.hipda.okhttp.ParamsMap;
import net.jejer.hipda.ui.HiApplication;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.HashSet;
import java.util.Set;

import okhttp3.Request;


public class FavoriteHelper {

    public final static String TYPE_FAVORITE = "favorites";
    public final static String TYPE_ATTENTION = "attention";

    private final static String FAV_CACHE_PREFS = "FavCachePrefsFile";

    private final static String FAVORITES_CACHE_KEY = "favorites";
    private final static String ATTENTION_CACHE_KEY = "attention";

    private final SharedPreferences mCachePref;

    private final Set<String> mFavoritesCache;
    private final Set<String> mAttentionCache;

    private FavoriteHelper() {
        mCachePref = HiApplication.getAppContext().getSharedPreferences(FAV_CACHE_PREFS, 0);
        mFavoritesCache = mCachePref.getStringSet(FAVORITES_CACHE_KEY, new HashSet<>());
        mAttentionCache = mCachePref.getStringSet(ATTENTION_CACHE_KEY, new HashSet<>());
    }

    public static FavoriteHelper getInstance() {
        return SingletonHolder.INSTANCE;
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
                mFavoritesCache.add(tid);
            } else if (TYPE_ATTENTION.equals(item)) {
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

    public void addFavorite(final String item, final String tid) {
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

    public void removeFavorite(final String item, final String tid) {
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

    public void deleteFavorite(final String formhash, final String item, final String tid) {
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
}
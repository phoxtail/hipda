package net.jejer.hipda.glide;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

import okhttp3.OkHttpClient;

public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final OkHttpClient client;

    private OkHttpUrlLoader(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl model, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(model), new ImageStreamFetcher(client, model));
    }

    @Override
    public boolean handles(GlideUrl model) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private OkHttpClient client;

        Factory(OkHttpClient client) {
            this.client = client;
        }

        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory factories) {
            return new OkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {
        }
    }

}

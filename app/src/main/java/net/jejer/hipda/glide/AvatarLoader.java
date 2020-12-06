package net.jejer.hipda.glide;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import okhttp3.OkHttpClient;

public class AvatarLoader implements ModelLoader<AvatarModel, InputStream> {

    private final OkHttpClient client;

    private AvatarLoader(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull AvatarModel model, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(model), new AvatarStreamFetcher(client, model));
    }

    @Override
    public boolean handles(@NonNull AvatarModel model) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<AvatarModel, InputStream> {
        private final OkHttpClient client;

        Factory(OkHttpClient client) {
            this.client = client;
        }

        @NonNull
        @Override
        public ModelLoader<AvatarModel, InputStream> build(@NotNull MultiModelLoaderFactory factories) {
            return new AvatarLoader(client);
        }

        @Override
        public void teardown() {
        }
    }
}

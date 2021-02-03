package net.jejer.hipda.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.jejer.hipda.bean.ContentImg;
import net.jejer.hipda.cache.ImageContainer;
import net.jejer.hipda.cache.ImageInfo;
import net.jejer.hipda.glide.GlideHelper;
import net.jejer.hipda.ui.widget.ImageViewerLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * adapter for image gallery
 * Created by GreenSkinMonster on 2015-05-20.
 */
public class ImageViewerAdapter extends PagerAdapter {

    private final List<ContentImg> mImages;
    private final Activity mActivity;

    private boolean mFirstShow = true;

    public ImageViewerAdapter(Activity activity, List<ContentImg> images) {
        mActivity = activity;
        mImages = images;
    }

    @Override
    public int getCount() {
        return mImages != null ? mImages.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {

        final ContentImg contentImg = mImages.get(position);
        final String imageUrl = contentImg.getContent();
        ImageInfo imageInfo = ImageContainer.getImageInfo(imageUrl);

        final ImageViewerLayout imageLayout = new ImageViewerLayout(mActivity, contentImg);

        //ScaleImageView has about 100ms delay, so show image with normal ImageView first
        if (mFirstShow) {
            mFirstShow = false;
            if (!imageInfo.isGif() && GlideHelper.isOkToLoad(mActivity)) {
                ImageInfo thumbInfo = ImageContainer.getImageInfo(contentImg.getThumbUrl());
                ImageInfo info = thumbInfo.isSuccess() ? thumbInfo : imageInfo;
                //load argument must match ThreadDetailFragment to hit memory cache
                if (info.isSuccess()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(info.getUrl())
                            .override(info.getBitmapWidth(), info.getBitmapHeight())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageLayout.getImageView());
                }
            }
        }

        container.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        ImageViewerLayout imageLayout = (ImageViewerLayout) object;
        container.removeView(imageLayout);
    }

}

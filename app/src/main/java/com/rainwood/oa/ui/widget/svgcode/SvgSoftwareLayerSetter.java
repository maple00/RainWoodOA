package com.rainwood.oa.ui.widget.svgcode;

import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

/**
 * @Author: a797s
 * @Date: 2020/6/11 18:00
 * @Desc: SVG格式不能硬解码，自定义RequestListener，在onResourceReady时设置ImageView为软解码
 */
public class SvgSoftwareLayerSetter implements RequestListener<PictureDrawable> {

    @Override
    public boolean onLoadFailed(GlideException e, Object model, Target<PictureDrawable> target,
                                boolean isFirstResource) {
        ImageView view = ((ImageViewTarget<?>) target).getView();
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null);
        return false;
    }

    @Override
    public boolean onResourceReady(PictureDrawable resource, Object model,
                                   Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
        ImageView view = ((ImageViewTarget<?>) target).getView();
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
        return false;
    }
}
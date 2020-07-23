package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.ui.activity.ImageActivity;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 10:12
 * @Desc: 员工的辅助信息
 */
public final class StaffPhotoAdapter extends RecyclerView.Adapter<StaffPhotoAdapter.ViewHolder> {

    private List<StaffPhoto> mList;
    private ArrayList<String> photoLit;
    private Context mContext;

    public void setList(List<StaffPhoto> list) {
        mList = list;
        photoLit = new ArrayList<>();
        for (StaffPhoto photo : mList) {
            photoLit.add(photo.getOrigin());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_staff_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getOrigin())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(8)))
                .error(R.drawable.ic_none_contact)
                .placeholder(R.drawable.ic_none_contact)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(true)
                .centerInside()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        LogUtils.d("sxs", "加载失败 errorMsg:" + (e != null ? e.getMessage() : "null"));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LogUtils.d("sxs", "成功  Drawable Name:" + resource.getClass().getCanonicalName());
                        return false;
                    }
                })
                .into(holder.photo);

        // 查看大图
        holder.photo.setOnClickListener(v ->
                ImageActivity.start(mContext, photoLit, position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.iv_photo)
        private ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}

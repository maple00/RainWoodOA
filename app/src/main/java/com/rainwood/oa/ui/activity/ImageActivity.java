package com.rainwood.oa.ui.activity;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.ui.adapter.ImagePagerAdapter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.tools.annotation.ViewInject;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

/**
 * @Author: a797s
 * @Date: 2020/5/25 11:47
 * @Desc: 查看大图
 */
public final class ImageActivity extends BaseActivity {

    @ViewInject(R.id.vp_image_pager)
    private ViewPager mViewPager;
    @ViewInject(R.id.pv_image_indicator)
    private PageIndicatorView mIndicatorView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image;
    }

    @Override
    protected void initView() {
        mIndicatorView.setViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        ArrayList<String> images = getIntent().getStringArrayListExtra(Constants.PICTURE);
        int index = getIntent().getIntExtra(Constants.INDEX, 0);
        if (images != null && images.size() > 0) {
            mViewPager.setAdapter(new ImagePagerAdapter(this, images));
            if (index != 0 && index <= images.size()) {
                mViewPager.setCurrentItem(index);
            }
        } else {
            finish();
        }
    }

    @Override
    protected void initPresenter() {

    }

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, ArrayList<String> urls) {
        start(context, urls, 0);
    }

    public static void start(Context context, ArrayList<String> urls, int index) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(Constants.PICTURE, urls);
        intent.putExtra(Constants.INDEX, index);
        context.startActivity(intent);
    }
}

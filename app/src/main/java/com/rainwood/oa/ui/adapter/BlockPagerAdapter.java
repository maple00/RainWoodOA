package com.rainwood.oa.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rainwood.oa.ui.fragment.BlockLogPagerFragment;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/17 14:49
 * @Desc: 待办事项适配器adapter
 */
public final class BlockPagerAdapter extends FragmentPagerAdapter {

    private List<String> titleList = new ArrayList<>();

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
        notifyDataSetChanged();
    }

    public BlockPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        LogUtils.d("sxs", "getItem ---- > " + position);
        BlockLogPagerFragment pagerFragment = BlockLogPagerFragment.getInstance(titleList.get(position));
        return pagerFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(titleList);
    }
}

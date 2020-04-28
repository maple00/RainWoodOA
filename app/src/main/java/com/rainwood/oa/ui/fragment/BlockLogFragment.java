package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 待办事项fragment
 */
public final class BlockLogFragment extends BaseFragment {

    @ViewInject(R.id.sxs_status_bar)
    private View statusBar;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_blocklog;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        // 设置状态栏高度
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(getContext()));
        statusBar.setLayoutParams(layoutParams);
    }

}

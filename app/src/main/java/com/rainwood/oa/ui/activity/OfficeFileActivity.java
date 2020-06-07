package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.OfficeFile;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.ui.adapter.OfficeFileAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 14:20
 * @Desc: 办公文件activity
 */
public final class OfficeFileActivity extends BaseActivity implements IAttachmentCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTips;
    //content
    @ViewInject(R.id.gti_type)
    private GroupTextIcon fileType;
    @ViewInject(R.id.gti_file_format)
    private GroupTextIcon fileFormat;
    @ViewInject(R.id.gti_secret)
    private GroupTextIcon fileSecret;
    @ViewInject(R.id.gti_default_sort)
    private GroupTextIcon fileSort;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_office_file)
    private RecyclerView officeFileView;

    private OfficeFileAdapter mOfficeFileAdapter;
    private IAttachmentPresenter mAttachmentPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_office_file;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTips.setText("输入文件名称");
        // 设置布局管理器
        officeFileView.setLayoutManager(new GridLayoutManager(this, 1));
        officeFileView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mOfficeFileAdapter = new OfficeFileAdapter();
        // 设置适配器
        officeFileView.setAdapter(mOfficeFileAdapter);
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mAttachmentPresenter = PresenterManager.getOurInstance().getAttachmentPresenter();
        mAttachmentPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mAttachmentPresenter.requestOfficeFileData();
    }

    @Override
    protected void initEvent() {
        fileType.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("文件分类");
            }
        });
        fileFormat.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("文格式");
            }
        });
        fileSecret.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("文件是否保密");
            }
        });
        fileSort.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("文件默认排序");
            }
        });
        // 文件预览下载
        mOfficeFileAdapter.setItemFile(new OfficeFileAdapter.OnClickItemFile() {
            @Override
            public void onClickDownload(OfficeFile file, int position) {
                FileManagerUtil.fileDownload(OfficeFileActivity.this, TbsActivity.class, file.getSrc(),
                        file.getName(), file.getFormat());
            }

            @Override
            public void onClickPreview(OfficeFile file, int position) {
                FileManagerUtil.filePreview(OfficeFileActivity.this, TbsActivity.class, file.getSrc(),
                        file.getName(), file.getFormat());
            }
        });
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getOfficeFileData(List<OfficeFile> fileList) {
        mOfficeFileAdapter.setFileList(fileList);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}

package com.rainwood.oa.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @Author: a797s
 * @Date: 2019/12/20 13:31
 * @Desc: TBS集成
 */
public final class TbsActivity extends BaseActivity implements TbsReaderView.ReaderCallback {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tbs;
    }

    @ViewInject(R.id.tbs_view)
    private RelativeLayout mRelativeLayout;
    @ViewInject(R.id.cl_tbs)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    private TbsReaderView mTbsReaderView;
    private String docUrl;
    private String download = Environment.getExternalStorageDirectory() + "/download/rainwood/document/";
    private String tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);

        mTbsReaderView = new TbsReaderView(this, this);
        mRelativeLayout.addView(mTbsReaderView, new RelativeLayout.LayoutParams(-1, -1));
        docUrl = getIntent().getStringExtra("path");
        initDoc();
    }

    @Override
    protected void initData() {
        // title
        String fileName = getIntent().getStringExtra("fileName");
        pageTitle.setText(fileName);
    }

    private void initDoc() {
        int i = docUrl.lastIndexOf("/");
        String docName = docUrl.substring(i);
        //判断是否在本地/[下载/直接打开]
        File docFile = new File(download, docName);
        if (docFile.exists()) {
            //存在本地;
            LogUtils.d("sxs", "the word is existed");
            displayFile(docFile.toString(), docName);
        } else {
            LogUtils.d("sxs", "先下载");
            // 先下载
            OkGo.get(docUrl)//
                    .tag(this)//
                    .execute(new FileCallback(download, docName) {
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            // file 即为文件数据，文件保存在指定目录
                            Log.d(TAG, "file download success ");
                            displayFile(download + file.getName(), file.getName());
                            Log.d(TAG, "" + file.getName());
                        }

                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            //这里回调下载进度(该回调在主线程,可以直接更新ui)
                            Log.d(TAG, "fileSize---" + totalSize + "---the file progress is ---" + progress);
                        }
                    });
        }
    }


    @Override
    protected void initPresenter() {

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

    private void displayFile(String filePath, String fileName) {

        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile = new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            Log.d(TAG, "ready to create /TbsReaderTemp！！");
            boolean mkdir = bsReaderTempFile.mkdir();
            if (!mkdir) {
                Log.d(TAG, "created the /TbsReaderTemp fail！！！！！");
            }
        }
        Bundle bundle = new Bundle();

       /*   1.TbsReader: Set reader view exception:Cannot add a null child view to a ViewGroup
            TbsReaderView: OpenFile failed! [可能是文件的路径错误]*/
       /*   2.插件加载失败
            so文件不支持;*/
       /*
       ndk {
                //设置支持的SO库架构  'arm64-v8a',
                abiFilters 'armeabi', "armeabi-v7a",  'x86'
            } */
       /*
            3.自适应大小
        */

        LogUtils.d(TAG, "filePath" + filePath);  // 可能是路径错误
        LogUtils.d(TAG, "tempPath" + tbsReaderTemp);
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tbsReaderTemp);

        boolean result = mTbsReaderView.preOpen(getFileType(fileName), true);

        LogUtils.d(TAG, "query the world : " + result);
        if (result) {
            mTbsReaderView.openFile(bundle);
        } else {
            toast("暂不支持该格式预览");
        }
    }

    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d(TAG, "i <= -1");
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

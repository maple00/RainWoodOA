package com.rainwood.oa.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.rainwood.oa.R;
import com.rainwood.oa.network.aop.Permissions;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.network.app.AppConfig;
import com.rainwood.oa.network.io.Downloader;
import com.rainwood.oa.network.io.OnDownloadListener;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.action.AnimAction;

import java.io.File;

/**
 * author : a797s
 * time   : 2020/04/29
 * desc   : 升级对话框
 */
public final class UpdateDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private final TextView mNameView;
        private final TextView mContentView;
        private final ProgressBar mProgressView;

        private final TextView mUpdateView;
        private final TextView mCloseView;
        /**
         * Apk 文件
         */
        private File mApkFile;
        /**
         * 下载地址
         */
        private String mDownloadUrl;
        /**
         * 文件 MD5
         */
        private String mFileMD5;
        /**
         * 当前是否下载中
         */
        private boolean mDownloading;
        /**
         * 当前是否下载完毕
         */
        private boolean mDownloadComplete;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_update);
            setAnimStyle(AnimAction.BOTTOM);
            setCancelable(false);

            mNameView = findViewById(R.id.tv_update_name);
            mContentView = findViewById(R.id.tv_update_content);
            mProgressView = findViewById(R.id.pb_update_progress);

            mUpdateView = findViewById(R.id.tv_update_update);
            mCloseView = findViewById(R.id.tv_update_close);

            setOnClickListener(R.id.tv_update_update, R.id.tv_update_close);
        }

        /**
         * 设置版本名
         */
        public Builder setVersionName(CharSequence name) {
            mNameView.setText(name);
            return this;
        }

        /**
         * 设置更新日志
         */
        public Builder setUpdateLog(CharSequence text) {
            mContentView.setText(text);
            mContentView.setVisibility(text == null ? View.GONE : View.VISIBLE);
            return this;
        }

        /**
         * 设置强制更新
         */
        public Builder setForceUpdate(boolean force) {
            mCloseView.setVisibility(force ? View.GONE : View.VISIBLE);
            setCancelable(!force);
            return this;
        }

        /**
         * 设置下载 url
         */
        public Builder setDownloadUrl(String url) {
            mDownloadUrl = url;
            return this;
        }

        /**
         * 设置文件 md5
         */
        public Builder setFileMD5(String md5) {
            mFileMD5 = md5;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            if (v == mCloseView) {
                dismiss();
            } else if (v == mUpdateView) {
                // 判断下载状态
                if (mDownloadComplete) {
                    // 下载完毕，安装 Apk
                    installApk();
                } else if (!mDownloading) {
                    // 没有下载，开启下载
                    downloadApk();
                }
            }
        }

        /**
         * 下载 Apk
         */
        private void downloadApk() {
            // 创建要下载的文件对象
            mApkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    getString(R.string.app_name) + "_v" + mNameView.getText().toString().trim() + ".apk");
            LogUtils.d("sxs", "---- 文件下载的路径 -------- " + mApkFile.getPath());
            // 设置对话框不能被取消
            setCancelable(false);
            new Downloader.Builder()
                    .url(mDownloadUrl)
                    .name(mApkFile.getName())
                    .folder(mApkFile.getAbsolutePath())
                    .isBreakpoint(true)
                    .listener(new OnDownloadListener() {
                        @Override
                        public void start() {
                            // 标记为下载中
                            mDownloading = true;
                            // 标记成未下载完成
                            mDownloadComplete = false;
                            // 后台更新
                            mCloseView.setVisibility(View.GONE);
                            // 显示进度条
                            mProgressView.setVisibility(View.VISIBLE);
                            mUpdateView.setText(R.string.update_status_start);
                        }

                        @Override
                        public void onDownloading(long total, long progress, int percent) {
                            mUpdateView.setText(String.format(getString(R.string.update_status_running), percent));
                            mProgressView.setProgress(percent);
                        }

                        @Override
                        public void onDownloadCompleted(File file) {
                            mUpdateView.setText(R.string.update_status_successful);
                            // 标记成下载完成
                            mDownloadComplete = true;
                            // 安装 Apk
                            installApk();
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            LogUtils.d("sxs----", "onDownloadFailed----" + e.toString());
                            ToastUtils.show(getString(R.string.update_status_failed));
                            mUpdateView.setText(R.string.update_status_failed);
                            dismiss();
                        }
                    }).build();
        }

        /**
         * 安装 Apk
         */
        @Permissions({Permission.REQUEST_INSTALL_PACKAGES})
        private void installApk() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getContext(), AppConfig.getPackageName() + ".provider", mApkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(mApkFile);
            }
            //Uri.parse("file://" + path);
            // uri = Uri.parse("file://" + mApkFile.getAbsolutePath());
            // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }
}
package com.rainwood.oa.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.rainwood.oa.ui.activity.ImageActivity;
import com.rainwood.tools.toast.ToastUtils;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * @Author: a797s
 * @Date: 2020/5/28 16:44
 * @Desc: 文件的下载/预览 util
 */
public final class FileManagerUtil {

    private static String download = Environment.getExternalStorageDirectory() + "/download/rainwood/document/";

    /**
     * 文件下载
     *
     * @param context
     * @param clazz
     * @param filePath 文件地址
     * @param fileName 文件名称
     * @param format   文件格式
     */
    public static void fileDownload(Context context, Class<? extends Object> clazz,
                                    String filePath, String fileName, String format) {

        if (TextUtils.isEmpty(filePath)) {
            ToastUtils.show("文件地址错误！请确认");
            return;
        }
        int i = filePath.lastIndexOf("/");
        String docName = filePath.substring(i);
        // https://www.yumukeji.com/file/201901/LiG120070208dI.docx
        //判断是否在本地/[下载/直接打开]
        File docFile = new File(download, docName);
        if (docFile.exists()) {
            //存在本地;
            ToastUtils.show(fileName + "本地已经存在\n" + docFile.getAbsolutePath());
        } else {
            LogUtils.d("sxs", "文件路径--- " + download);
            OkGo.get(filePath)//
                    .tag(context)//
                    .execute(new FileCallback(download, docName) {
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            // file 即为文件数据，文件保存在指定目录
                            ToastUtils.show(fileName + "下载成功 \n" + file.getAbsolutePath());
                        }

                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            //这里回调下载进度(该回调在主线程,可以直接更新ui)
                            Log.d(TAG, "总大小---" + totalSize + "---文件下载进度---" + progress);
                        }
                    });
        }
    }

    /**
     * 文件预览
     *
     * @param context
     * @param clazz
     * @param filePath
     * @param fileName
     * @param format
     */
    public static void filePreview(Context context, Class<? extends Object> clazz,
                                   String filePath, String fileName, String format) {
        if (TextUtils.isEmpty(format)) {
            ToastUtils.show("文件地址错误！请确认");
            return;
        }
        // 根据不同的文件类型进行预览
        if ("png".equals(format)
                || "jpg".equals(format)) {        // 加载大图
            ImageActivity.start(context, filePath);
        } else {     // 加载文档--- 需要打开权限
            Intent intent = new Intent(context, clazz);
            Bundle bundle = new Bundle();
            bundle.putString("path", filePath);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}

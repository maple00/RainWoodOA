package com.rainwood.oa.network.io;

import com.rainwood.oa.utils.LogUtils;

import java.lang.reflect.Method;

/**
 * Created by Relin
 * on 2018-06-06.
 */

public class MIME {

    public static String _3GP = "video/3gpp";
    public static String APK = "video/application/vnd.android.package-archive";
    public static String ASF = "video/x-ms-asf";
    public static String AVI = "video/x-msvideo";
    public static String BIN = "application/octet-stream";
    public static String BMP = "image/bmp";
    public static String C = "text/plain";
    public static String CLASS = "application/octet-stream";
    public static String CONF = "text/plain";
    public static String CPP = "text/plain";
    public static String DOC = "application/msword";
    public static String exe = "application/octet-stream";
    public static String GIF = "image/gif";
    public static String GTAR = "application/x-gtar";
    public static String GZ = "application/x-gzip";
    public static String H = "text/plain";
    public static String HTM = "text/html";
    public static String HTML = "text/html";
    public static String JAR = "application/java-archive";
    public static String JAVA = "text/plain";
    public static String JPEG = "image/jpeg";
    public static String JPG = "image/jpeg";
    public static String JS = "application/x-javascript";
    public static String LOG = "text/plain";
    public static String M3U = "audio/x-mpegurl";
    public static String M4A = "audio/x-mpegurl";
    public static String M4B = "audio/mp4a-latm";
    public static String M4P = "audio/mp4a-latm";
    public static String M4U = "video/vnd.mpegurl";
    public static String M4V = "video/x-m4v";
    public static String MOV = "video/quicktime";
    public static String MP2 = "audio/x-mpeg";
    public static String MP3 = "audio/x-mpeg";
    public static String MP4 = "video/mp4";
    public static String MPC = "application/vnd.mpohun.certificate";
    public static String MPE = "video/mpeg";
    public static String MPEG = "video/mpeg";
    public static String MPG = "video/mpeg";
    public static String MPG4 = "video/mp4";
    public static String MPGA = "audio/mpeg";
    public static String MSG = "application/vnd.ms-outlook";
    public static String OGG = "audio/ogg";
    public static String PDF = "application/pdf";
    public static String PNG = "image/png";
    public static String PPS = "application/vnd.ms-powerpoint";
    public static String PPT = "application/vnd.ms-powerpoint";
    public static String PROP = "text/plain";
    public static String RAR = "application/x-rar-compressed";
    public static String RC = "text/plain";
    public static String RMVB = "audio/x-pn-realaudio";
    public static String RTF = "application/rtf";
    public static String SH = "text/plain";
    public static String TAR = "application/x-tar";
    public static String TGZ = "application/x-compressed";
    public static String TXT = "text/plain";
    public static String WAV = "audio/x-wav";
    public static String WMA = "audio/x-ms-wma";
    public static String WMV = "audio/x-ms-wmv";
    public static String WPS = "application/vnd.ms-works";
    public static String XNL = "text/plain";
    public static String Z = "application/x-compress";
    public static String ZIP = "application/zip";
    public static String ANY = "*/*";
    public static String FOLDER = "file/*";

    /**
     * 匹配文件类型
     */
    public static String matchFileType(String filePath) {
        MIME mime = new MIME();
        Class<? extends MIME> clz = mime.getClass();
        // 获取所有的属性
        Method[] methods = clz.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            LogUtils.d("sxs", "---------- " + name);
        }
        return "";
    }
}

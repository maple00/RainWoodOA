package com.rainwood.oa.utils;

/**
 * @Author: a797s
 * @Date: 2020/4/27 15:50
 * @Desc: 常量
 */
public final class Constants {

    /**
     * 后台请求BaseURL
     */
    // public static final String BASE_URL = "https://www.yumukeji.com/interface/controlApp";
    public static final String BASE_URL = "https://www.yumukeji.com/interface/controlApp/app.php?";


    /**
     * 无网络-What
     */
    public static final int WHAT_MSG_NET_OFFLINE = 0xa2;

    /**
     * 请求失败-What
     */
    public static final int WHAT_MSG_RESPONSE_FAILED = 0xa3;

    /**
     * 授权-请求码
     */
    public static final int REQUEST_CODE_PERMISSIONS = 0xa4;

    /**
     * Handler-消息Key
     */
    public static final String MSG_KEY = "0x1";

    /**
     * Handler-消息Code
     */
    public static final String MSG_CODE = "0x2";

    /**
     * 无网络的提示
     */
    public static final String EXCEPTION_MSG_NET_OFFLINE = "连网失败";

    /**
     * 请求失败的提示
     */
    public static final String EXCEPTION_MSG_RESPONSE_FAILED = "请求失败";

    /**
     * 请求失败的提示
     */
    public static final String EXCEPTION_MSG_REQUEST_TIMEOUT = "请求超时";

    /**
     * 没有网络
     */
    public static final String HTTP_MSG_NET_OFFLINE = "Network connection failed,please check the mobile network.";

    /**
     * 请求失败
     */
    public static final String HTTP_MSG_RESPONSE_FAILED = "The request data failed and the response code is not 200,code = ";


    /**
     * activity、fragment 之间相互跳转请求码
     */
    public static final int HOME_FRAGMENT_RESULT_SIZE = 0x101;
    public static final int MANAGER_FRAGMENT_RESULT_SIZE = 0x102;
    public static final int BLOCK_FRAGMENT_RESULT_SIZE = 0x103;
    public static final int MINE_FRAGMENT_RESULT_SIZE = 0x104;

    /**
     * 需求填写请求码
     */
    public static final int CUSTOM_DEMAND_WRITE_SIZE = 0x1001;

    /**
     * IMEI
     */
    public static String IMEI = "";

    /**
     * 图片
     */
    public static final String PICTURE = "picture";

    /**
     * 音频
     */
    public static final String VOICE = "voice";
    /**
     * 视频
     */
    public static final String VIDEO = "video";

    /**
     * 索引
     */
    public static final String INDEX = "index";

    /**
     * 当前选中的客户
     */
    public static String CUSTOM_ID;

}

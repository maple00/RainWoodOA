package com.rainwood.oa.utils;

/**
 * @Author: a797s
 * @Date: 2020/4/27 15:50
 * @Desc: 常量
 */
public final class Constants {

    /**
     * 网络请求失败
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
     * 后台请求BaseURL
     */
    public static final String BASE_URL = "https://www.yumukeji.com/interface/controlApp";


    //home pager fragment bundle key.
    public static final String KEY_STAFF_ACCOUNT_TITLE = "key_home_pager_title";
    public static final String KEY_STAFF_ACCOUNT_TYPE_ID = "key_home_pager_material_id";

}

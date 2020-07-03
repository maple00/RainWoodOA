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
    public static final int HOME_FRAGMENT_RESULT_SIZE = 101;
    public static final int MANAGER_FRAGMENT_RESULT_SIZE = 102;
    public static final int BLOCK_FRAGMENT_RESULT_SIZE = 103;
    public static final int MINE_FRAGMENT_RESULT_SIZE = 104;

    /**
     * 需求填写请求码
     */
    public static final int CUSTOM_DEMAND_WRITE_SIZE = 0x1001;

    /**
     * 选择员工
     */
    public static final int CHOOSE_STAFF_REQUEST_SIZE = 0x1002;

    /**
     * 两层页面跳转码
     */
    // 添加费用计提
    public static final int COST_OF_PROVISION = 0x1003;
    // 添加跟进记录
    public static final int FOLLOW_OF_RECORDS = 0x1004;
    // 选择附件
    public static final int FILE_SELECT_CODE = 0x1005;
    // 职位搜索
    public static final int PAGE_SEARCH_CODE = 0x1006;

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

    /**
     * 页面跳转menu
     */
    public static final String PERSONAL_OVER_TIME_DETAIL_MENU = "personalOverTimeDetail";
    public static final String PERSONAL_ASK_LEAVE_DETAIL_MENU = "personalAskLeaveDetail";
    public static final String PERSONAL_ASK_OUT_DETAIL_MENU = "personalAskOutDetail";
    public static final String PERSONAL_REISSUE_CARD_DETAIL_MENU = "personalReissueCardDetail";
    public static final String KEY_BLOCK_PAGER_State = "0x1003";
    public static final String POSITION_BLOCK_PAGER_State = "0x1004";
}

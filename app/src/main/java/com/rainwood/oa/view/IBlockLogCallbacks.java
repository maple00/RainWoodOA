package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.BlockLog;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/17 14:38
 * @Desc: 待办事项
 */
public interface IBlockLogCallbacks extends IBaseCallback {

    /**
     * 获取状态
     */
    default void getBlockState(List<String> stateList) {
    }

    default String getState() {
        return "";
    }

    /**
     * 获取状态内容
     */
    default void getBlockContent(List<BlockLog> blockLogList) {
    }
}

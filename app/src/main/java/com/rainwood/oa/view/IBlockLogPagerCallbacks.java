package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.BlockLog;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/19 10:16
 * @Desc: 待办事项pager
 */
public interface IBlockLogPagerCallbacks extends IBaseCallback {

    String getState();

    /**
     * 获取状态内容
     */
    default void getBlockContent(List<BlockLog> blockLogList) {
    }
}

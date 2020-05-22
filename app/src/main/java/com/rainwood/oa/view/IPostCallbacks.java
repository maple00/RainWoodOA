package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Post;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:54
 * @Desc: 职位管理
 */
public interface IPostCallbacks extends IBaseCallback {

    /**
     * 获取所有的职位
     * @param postList
     */
    void getALlPostData(List<Post> postList);
}

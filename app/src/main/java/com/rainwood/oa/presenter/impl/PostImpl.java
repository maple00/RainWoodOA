package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.presenter.IPostPresenter;
import com.rainwood.oa.view.IPostCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:56
 * @Desc: 职位管理
 */
public final class PostImpl implements IPostPresenter {

    private IPostCallbacks mPostCallbacks;

    @Override
    public void getAllPost() {
        // 模拟所有的职位信息
        List<Post> postList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Post post = new Post();
            post.setPost("架构师");
            post.setDepart("研发部");
            post.setRole("架构师一号");
            post.setBaseSalary("2000");
            post.setPostAllowance("1500");
            post.setPostDesc("某些项目的一些功能模块需要甲方开发");
            postList.add(post);
        }

        mPostCallbacks.getALlPostData(postList);
    }

    @Override
    public void registerViewCallback(IPostCallbacks callback) {
        mPostCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IPostCallbacks callback) {
        mPostCallbacks = null;
    }
}

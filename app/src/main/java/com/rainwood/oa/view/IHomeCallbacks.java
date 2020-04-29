package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.FontAndFont;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/29 9:36
 * @Desc: 首页callbaks
 */
public interface IHomeCallbacks extends IBaseCallback {

    /**
     * 获取首页的工资曲线数据
     */
    void getSalariesData(List<FontAndFont> salaries);

}

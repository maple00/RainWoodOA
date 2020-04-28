package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.TempAppMine;
import com.rainwood.oa.model.domain.TempMineAccount;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:03
 * @Desc: 我的
 */
public interface IMineCallbacks  extends IBaseCallback {

    /**
     * 获取我的界面数据
     */
    // 模拟
    // 账户信息、我的管理、application信息
    void getMenuData(List<TempMineAccount> accounts,
                     List<IconAndFont> iconAndFonts,
                     List<TempAppMine> appMines);
}

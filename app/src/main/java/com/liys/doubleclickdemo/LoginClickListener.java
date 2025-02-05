package com.liys.doubleclickdemo;

import android.view.View;
import android.widget.Toast;

import com.liys.doubleclicklibrary.listener.BaseClickListener;


/**
 * @Description:
 * @Author: liys
 * @CreateDate: 2019/8/22 15:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/22 15:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginClickListener extends BaseClickListener {

    public static boolean isLogin = false;

    @Override
    public boolean isNext(View v) {
        //判断登录逻辑
        if(!isLogin){
            LogUtil.d("未登录");
            Toast.makeText(v.getContext(), "未登录", Toast.LENGTH_SHORT).show();
        }
        return isLogin;
    }


    @Override
    public void after(View view) {
        //执行click后
    }

}

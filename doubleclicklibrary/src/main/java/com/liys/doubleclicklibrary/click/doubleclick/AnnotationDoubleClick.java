package com.liys.doubleclicklibrary.click.doubleclick;

import android.util.Log;
import android.view.View;

import com.liys.doubleclicklibrary.AnnotationHelper;
import com.liys.doubleclicklibrary.ViewHelper;
import com.liys.doubleclicklibrary.listener.IOnClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 注解处理方式
 * @Author: liys
 * @CreateDate: 2019/8/27 19:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/27 19:00
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AnnotationDoubleClick extends BaseDoubleClick{

    private  List<Class> mClassList; //取消的
    private  Map<Class, Map<Integer, Long>> mAddViewMap; //单个添加
    private  Map<Class, Map<Integer, Class>> mViewListenerMap; //拦截 并自定义click

    public AnnotationDoubleClick(Class annotationClass){
        if(annotationClass != null){
            mClassList = AnnotationHelper.getACancelActivity(annotationClass);
            mAddViewMap = AnnotationHelper.getAddDoubleClick(annotationClass);
            mViewListenerMap = AnnotationHelper.getClickListener(annotationClass);
        }else{
            mClassList = new ArrayList<>();
            mAddViewMap = new HashMap<>();
            mViewListenerMap = new HashMap<>();
        }
    }

    @Override
    public void hookActivityViews(final long delayTime) {
        //1. 自定义拦截
        final Map<Integer, Class> listenerIdsMap = mViewListenerMap.get(mActivity.getClass());
        //2.单独的View
        final Map<Integer, Long> idsMap = mAddViewMap.get(mActivity.getClass());

        //3. 处理当前Activity的hook事件
        final View decorView = mActivity.getWindow().getDecorView();
        //获取 activity中的所有view
        List<View> list = ViewHelper.getAllChildViews(decorView);
        for (int i = 0; i < list.size(); i++) {
            View view = list.get(i);

            //1.自定义拦截
            if(listenerIdsMap!=null && listenerIdsMap.containsKey(view.getId())){
                Class clazz = listenerIdsMap.get(view.getId());
                hookViewListener(view, clazz, delayTime);
            }

            //2.单独处理的View
            if(idsMap!=null && idsMap.containsKey(view.getId())){
                long time = idsMap.get(view.getId());
                hookView(view, time);
            }else{
                //3.判断当前Activity是否取消hook事件
                if(mClassList == null || !mClassList.contains(mActivity.getClass())){
                    hookView(view, delayTime);
                }
            }
        }
    }

    /**
     * 添加自定义
     */
    private void hookViewListener(final View view, final Class listenerclazz, final long delayTime){
        IOnClickListener iOnClickListener = null;
        try {
            Object obj = listenerclazz.newInstance();
            if(obj instanceof IOnClickListener){
                iOnClickListener = (IOnClickListener)obj;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        hookView(view, delayTime, iOnClickListener);
    }
}

package com.itcast.www.myapplication.sub_pages;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.itcast.www.myapplication.bean.NewsCenterBean;
import com.itcast.www.myapplication.pager.BasePager;

/**
 * Created by LSL on 2016/6/6.
 */
public class InterActionPager extends BasePager {

    private TextView textView;

    public InterActionPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        return textView;
    }

    @Override
    public void initData() {
        textView.setText("我是互动子界面");
    }
}

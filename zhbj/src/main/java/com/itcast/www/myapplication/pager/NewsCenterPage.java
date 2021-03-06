package com.itcast.www.myapplication.pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itcast.www.myapplication.MainActivity;
import com.itcast.www.myapplication.R;
import com.itcast.www.myapplication.bean.NewsCenterBean;
import com.itcast.www.myapplication.fragment.MenuFragment;
import com.itcast.www.myapplication.sub_pages.ArrPicPager;
import com.itcast.www.myapplication.sub_pages.InterActionPager;
import com.itcast.www.myapplication.sub_pages.NewsPager;
import com.itcast.www.myapplication.sub_pages.TopicPager;
import com.itcast.www.myapplication.utils.NetUrl;
import com.itcast.www.myapplication.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSL on 2016/6/4.
 */
public class NewsCenterPage extends BasePager {

    @ViewInject(R.id.btn_left)
    private Button btn_left;
    @ViewInject(R.id.imgbtn_left)
    private ImageButton imgbtn_left;
    @ViewInject(R.id.txt_title)
    private TextView txt_title;
    @ViewInject(R.id.imgbtn_title)
    private ImageButton imgbtn_title;
    @ViewInject(R.id.imgbtn_right)
    private ImageButton imgbtn_right;
    @ViewInject(R.id.imgbtn_right2)
    private ImageButton imgbtn_right2;
    @ViewInject(R.id.ll_title_bar)
    private LinearLayout ll_title_bar;
    @ViewInject(R.id.fl_news_center)
    private FrameLayout fl_news_center;

    private List<BasePager> subPages = new ArrayList<>();

    public NewsCenterPage(Context context) {
        super(context);
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_news_center, null);
        ViewUtils.inject(this,view);
        initTitleView();
        return view;

    }

    //初始化标题栏
    private void initTitleView() {
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setBackgroundResource(R.drawable.img_menu);
        imgbtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainactivity = (MainActivity) context;
                mainactivity.toggle();
            }
        });
        txt_title.setText("新闻");
        btn_left.setVisibility(View.GONE);
        imgbtn_title.setVisibility(View.GONE);
        imgbtn_right.setVisibility(View.GONE);
        imgbtn_right2.setVisibility(View.GONE);

    }

    @Override
    public void initData() {
        //先从本地读取，读取不到，在从网络获取
        String jsonString = StringUtils.getSp(context, NetUrl.NEWS_CENTER_CATEGORIES);
        if (!TextUtils.isEmpty(jsonString)) {
//            parseJson(jsonString);
        }
        requestData();
    }

    private void requestData() {
        sendRequest(NetUrl.NEWS_CENTER_CATEGORIES, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.i("NewsCenterPage", result);
                parseJson(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Log.i("NewsCenterPage", "请求失败" + msg);
            }
        });
    }



    private void parseJson(String result) {
        Gson gson = new Gson();
        NewsCenterBean newsCenterBean = gson.fromJson(result, NewsCenterBean.class);

        //将解析出来的数据传递给MenuFragment界面
        MainActivity mainActivity = (MainActivity) context;
        MenuFragment menuFragment = mainActivity.getMenuFragmentByTag();
        menuFragment.initSubMenu(newsCenterBean.data);

        subPages.add(new NewsPager(context,newsCenterBean.data.get(0)));
        subPages.add(new TopicPager(context,newsCenterBean.data.get(1)));
        subPages.add(new ArrPicPager(context,newsCenterBean.data.get(2)));
        subPages.add(new InterActionPager(context,newsCenterBean.data.get(3)));
        switchSubMenu(0);
    }

    public void switchSubMenu(int i) {
        BasePager basePager = subPages.get(i);
        View currentView = basePager.initView();
        basePager.initData();

        fl_news_center.removeAllViews();
        fl_news_center.addView(currentView);

        switch (i) {
            case 0:
                Log.i("NewsCenterPager","点击了新闻中心");
                break;
            case 1:
                Log.i("NewsCenterPager","点击了专题中心");
                break;
            case 2:
                Log.i("NewsCenterPager","点击了组图中心");
                break;
            case 3:
                Log.i("NewsCenterPager","点击了互动中心");
                break;
        }


    }
}

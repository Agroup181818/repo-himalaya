package com.example.himalaya;

import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.himalaya.adapters.IndicatorAdapter;
import com.example.himalaya.adapters.MainContentAdapter;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.utils.LogUtil;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    public static final String TAG = "MainActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.OnIndicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                LogUtil.d(TAG,"click index is ----> " + index);
                if (mContentPager != null){
                    mContentPager.setCurrentItem(index);
                }
            }
        });
    }

    private void initView() {
        mMagicIndicator = this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));


        //创建indicator的适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //自动调节Indicator宽度和高度
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);


        //viewPager
        mContentPager = this.findViewById(R.id.content_pager);

        //创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);

        mContentPager.setAdapter(mainContentAdapter);


        //把viewPager和indicator绑定在一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mContentPager);


        Map<String, String> map = new HashMap<>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(@Nullable CategoryList categoryList) {
                List<Category> categories = categoryList.getCategories();
                if (categories != null) {
                    int size = categories.size();
                    Log.d(TAG, "categories size ---- >" + size);
                    for (Category category : categories) {
//                        Log.d(TAG, "category ------>" + category.getCategoryName());
                        LogUtil.d(TAG,"categroy- ----->" + category.getCategoryName());
                    }
                }
            }
            @Override
            public void onError(int code, String message) {
//                Log.d(TAG , "error code --" + code + "error message" + message);
                LogUtil.d(TAG,"error code --->" + code + "error message ----->" + message);
            }
        });

    }

}

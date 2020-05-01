package com.example.himalaya.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.DetailActivity;
import com.example.himalaya.R;
import com.example.himalaya.adapters.AlbumListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.presenters.RecommendPresenter;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetrayClickListener, AlbumListAdapter.onRecommendItemClickListener {

    public static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView  mRecommendRV;
    private AlbumListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;


    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {

        //实现控制UI页面四种状态 成功、正在加载、网络连接错误、内容为空
        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater, container);
            }
        };

        mUiLoader.setOnRetrayListener(this);

        //获取到逻辑层的对象、内有数据
        mRecommendPresenter = RecommendPresenter.getInstance();

        //先要设置通知接口的注册
        mRecommendPresenter.registerViewCallback(this);

        //获取推荐列表
        mRecommendPresenter.getRecommendList();

        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup)mUiLoader.getParent()).removeView(mUiLoader);
        }
        //返回view给界面显示
        return mUiLoader;
    }

    @SuppressLint("WrongConstant")
    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        //View 加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container,false);
        //recycleView的使用
        //1.找到对应的控件
        mRecommendRV = mRootView.findViewById(R.id.recommend_list);
        //2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRV.setLayoutManager(linearLayoutManager);
        //设置间距
        mRecommendRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //dip2px dp转像素
                outRect.top= UIUtil.dip2px(view.getContext(),5);
                outRect.bottom=UIUtil.dip2px(view.getContext(),5);
                outRect.left=UIUtil.dip2px(view.getContext(),5);
                outRect.right=UIUtil.dip2px(view.getContext(),5);

            }
        });
        //3.设置适配器
        mRecommendListAdapter = new AlbumListAdapter();
        mRecommendRV.setAdapter(mRecommendListAdapter);
        mRecommendListAdapter.setOnRecommendItemClickLister(this);
        return mRootView;
    }


    @Override
    public void onRecommendListLoaded(List<Album> result) {
        LogUtil.d(TAG , "onRecommendListLoaded");
        //当获取到推荐内容的时候，这个方法会被调用(成功了)
        // 数据回来以后，就是更新UI了
        //把数据设置给适配器 并且更新
        mRecommendListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        LogUtil.d(TAG , "onNetworkError");
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROE);
    }

    @Override
    public void onEmpty() {
        LogUtil.d(TAG , "onEmpty");
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        LogUtil.d(TAG , "onLoading");
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRetryClick() {
        //表示网络不佳、用户点击重试
        //重新获取数据即可
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        //Item被点击了,跳转到详情界面
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);

    }
}

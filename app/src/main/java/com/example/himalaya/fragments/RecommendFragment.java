package com.example.himalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.example.himalaya.adapters.RecommendListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment {

    public static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView  mRecommendRV;
    private RecommendListAdapter mRecommendListAdapter;
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        //View 加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container,false);
        //recycleView的使用
        //1.找到对应的控件
        mRecommendRV = mRootView.findViewById(R.id.recommend_list);
        //2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecommendRV.setLayoutManager(linearLayoutManager);
        //3.设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRV.setAdapter(mRecommendListAdapter);
        //拿数据回来
        getRecommendData();
        //返回view给界面显示
        return mRootView;
    }

    //获取推荐内容，其实就是“猜你喜欢”
    private void getRecommendData(){
        //封装参数
        Map<String, String> map = new HashMap<>();
        //这个参数表示一页数据多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG," thread name -- > " +Thread.currentThread().getName());
                //数据返回成功
                if(gussLikeAlbumList != null){
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //数据回来 更新UI
                    UpRecommendUI(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //数据返回出错
                LogUtil.d(TAG," error -- > "+i);
                LogUtil.d(TAG," errorMsg -- > "+s);
            }
        });
    }

    private void UpRecommendUI(List<Album> albumList) {
        //把数据设置给适配器 并且更新
        mRecommendListAdapter.setData(albumList);
    }

}

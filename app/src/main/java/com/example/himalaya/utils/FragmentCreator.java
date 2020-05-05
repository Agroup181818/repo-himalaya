package com.example.himalaya.utils;

import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.fragments.RecommendFragment;
import com.example.himalaya.fragments.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentCreator {


    public final static int INDEX_RECOMMEND = 0;
    public final static int INDEX_SUBSCRIPTION = 1;

    public final static int PAGE_COUNT = 2;

    private static Map<Integer, BaseFragment> sCache = new HashMap<>();

    public static BaseFragment getFragment(int index) {
        BaseFragment baseFragment = sCache.get(index);
        if (baseFragment != null) {
            return baseFragment;

        }
        switch (index) {
            case INDEX_RECOMMEND:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPTION:
                baseFragment = new SubscriptionFragment();
                break;

        }

        sCache.put(index, baseFragment);
        return baseFragment;
    }
}

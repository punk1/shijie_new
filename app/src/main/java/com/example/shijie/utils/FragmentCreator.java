package com.example.shijie.utils;



import com.example.shijie.base.BaseFragment;
import com.example.shijie.fragments.HistoryFragment;
import com.example.shijie.fragments.RecommendFragment;
import com.example.shijie.fragments.SubscriptionFragment;
import com.example.shijie.fragments.quanFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentCreator {

    public final static int INDEX_RECOMMND = 0;

    public final static int INDEX_HISTORY = 1;

    public final static int PAGER_COUNT = 2;
    private static Map<Integer, BaseFragment> sCache = new HashMap<>();

    public static BaseFragment getFragment(int index) {
        BaseFragment baseFragment = sCache.get(index);
        if (baseFragment != null) {
            return baseFragment;
        }
        switch (index) {
            case INDEX_RECOMMND:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_HISTORY:
                baseFragment = new HistoryFragment();
                break;

        }
            sCache.put(index, baseFragment);

            return baseFragment;


    }
}

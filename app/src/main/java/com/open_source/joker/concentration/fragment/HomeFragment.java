package com.open_source.joker.concentration.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.adapter.HomeAdapter;
import com.open_source.joker.concentration.app.CONFragment;
import com.open_source.joker.concentration.widget.recyclerViewdivider.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jing on 2016/2/23.
 */
public class HomeFragment extends CONFragment implements OnRefreshListener, OnLoadMoreListener, HomeAdapter.OnRecyclerViewItemClickListener {
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private ArrayList<String> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList = new ArrayList<>();
        mList.addAll(Arrays.asList(modlerList));
        mHomeAdapter = new HomeAdapter(getActivity(), mList);
        mHomeAdapter.setOnItemClickListener(this);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mHomeAdapter);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        }, 3000);
    }


    private String[] modlerList = {"二维码", "定位实践"};


    @Override
    public void onItemClick(View view, String data) {
        if (data.equals(modlerList[1])) {
            startActivity("concentration://location");
        }
    }
}

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

import java.util.ArrayList;

/**
 * Created by jing on 2016/2/23.
 */
public class HomeFragment extends CONFragment implements OnRefreshListener, OnLoadMoreListener{
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
        mHomeAdapter = new HomeAdapter(getActivity(), mList);
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
        if (mHomeAdapter.getItemCount() != 0) mList.clear();
        for (int i = 0; i < 20; i++) {
            mList.add("Refresh" + i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHomeAdapter.notifyDataSetChanged();
                mSwipeToLoadLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i < 10; i++) {
            mList.add("LoadMore" + i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHomeAdapter.notifyDataSetChanged();
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        }, 3000);
    }


}

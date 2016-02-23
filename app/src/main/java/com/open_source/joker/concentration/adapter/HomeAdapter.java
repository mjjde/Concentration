package com.open_source.joker.concentration.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open_source.joker.concentration.R;

import java.util.ArrayList;

/**
 * Created by jing on 2016/2/23.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<String> list;

    public HomeAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeHolder homeHolder = (HomeHolder) holder;
        homeHolder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder{
        private TextView tv;

        public HomeHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}

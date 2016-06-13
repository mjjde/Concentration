package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

/**
 *     ：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/5/4
 * 作者: joker
 */
public class AnimationAct extends CONActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_list);
        initView();
    }

    public void initView(){
        String [] arr = {"View 动画","帧动画","属性动画","举个🌰"};
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,arr));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    startActivity("concentration://viewAct");
                }
                if(position == 1){
                    startActivity("concentration://frameact");
                }
                if(position == 2){
                    startActivity("concentration://propertyact");
                }
                if(position == 3){
                    startActivity("concentration://testanimation");
                }
            }
        });
    }


}

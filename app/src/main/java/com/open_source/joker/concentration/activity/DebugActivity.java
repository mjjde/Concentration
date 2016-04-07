package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/2/24
 * 作者: joker
 */
public class DebugActivity extends CONActivity {
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        name = (EditText) findViewById(R.id.activity_name);

        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = name.getText().toString().trim();
                if (!str.startsWith("concentration://")) {
                    Toast.makeText(DebugActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(str);
            }
        });
    }

}

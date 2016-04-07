package com.open_source.joker.concentration.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jing.scan.CaptureActivity;
import com.jing.scan.encode.Encoder;
import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

public class ScanActivity extends CONActivity {
    private TextView mResult;
    private EditText encodeText;
    private ImageView encodeImg;

    private Encoder mEncoder;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            encodeImg.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mResult = (TextView) findViewById(R.id.scan_result);
        encodeText = (EditText) findViewById(R.id.encode_text);
        encodeImg = (ImageView) findViewById(R.id.encode_image);

        mEncoder = new Encoder.Builder()
                .setBackgroundColor(0xFFFFFFFF)
                .setCodeColor(0xFF000000)
                .build();

        findViewById(R.id.encodeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encodeImg();
            }
        });

    }

    public void encodeImg(){
        final String s = encodeText.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(this, "请输入内容生成二维码", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mEncoder.encode(s);
                handler.sendMessage(handler.obtainMessage(0, bitmap));
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CaptureActivity.SCAN_CODE){
                String result = data.getStringExtra(CaptureActivity.SCAN_RESULT);
                mResult.setText("扫描结果: " + result);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.scan:
                startActivityForResult(new Intent(ScanActivity.this, CaptureActivity.class), CaptureActivity.SCAN_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

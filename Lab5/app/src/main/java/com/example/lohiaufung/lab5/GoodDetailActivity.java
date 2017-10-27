package com.example.lohiaufung.lab5;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class GoodDetailActivity extends AppCompatActivity {
    boolean hasIntentRespond = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        Intent intent = getIntent();
        // goodName
        String goodName = intent.getStringExtra("goodName");
        TextView goodNameTextView = (TextView)findViewById(R.id.goodNameInDetailPage);
        goodNameTextView.setText(goodName);
        //price
        double goodPrice = intent.getDoubleExtra("price", -1);
        TextView priceTextView = (TextView)findViewById(R.id.goodPriceInDetail);
        priceTextView.setText("￥"+Double.toString(goodPrice));
        // goodType
        String goodType = intent.getStringExtra("goodType");
        TextView goodTypeTextView = (TextView)findViewById(R.id.goodTypeInDetail);
        goodTypeTextView.setText(goodType);
        //goodTypeInfo
        String goodTypeInfo = intent.getStringExtra("goodInfo");
        TextView goodTypeInfoTextVew = (TextView)findViewById(R.id.goodTypeInfoInDetail);
        goodTypeInfoTextVew.setText(goodTypeInfo);
        //imageSrcID
        int imageSrcID = intent.getIntExtra("imageSrcID",-1);
        ImageView goodDetailImage = (ImageView)findViewById(R.id.goodDetailImage);
        goodDetailImage.setImageResource(imageSrcID);

        // 空心、实心星星切换
        ImageView starImage = (ImageView) findViewById(R.id.star);
        starImage.setTag(0);
        starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView star = (ImageView) v;
                if(0 == (int)v.getTag()) {
                    star.setImageResource(R.drawable.full_star);
                    star.setTag(1);
                } else {
                    star.setImageResource(R.drawable.empty_star);
                    star.setTag(0);
                }
            }
        });

        // 返回图标
        ImageView backImageView = (ImageView) findViewById(R.id.backToLastPage);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(false == hasIntentRespond) {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });

        // 添加到购物车
        ImageView addToShoppingCarImageView = (ImageView)findViewById(R.id.addToShoppingCarButtonInDetailPage);
        addToShoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToAdd = ((TextView)findViewById(R.id.goodNameInDetailPage)).getText().toString();
                Intent intent = new Intent();
                intent.putExtra("goodToAddByName", nameToAdd);
                setResult(RESULT_OK, intent);
                hasIntentRespond = true;
                // 弹出添加成功
                Toast.makeText(GoodDetailActivity.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(false == hasIntentRespond) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

}

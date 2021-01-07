package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 水果数据修改页面
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class ModifyFruit extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    List<FruitBean> date;
    RecyclerViewFruitAdapter adapter = new RecyclerViewFruitAdapter(this,date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_fruit);

        final Intent i = getIntent();

        final int id = i.getIntExtra("id",0);
        final String name = i.getStringExtra("name");
        final int amount = i.getIntExtra("amount",10);
        String date = i.getStringExtra("date");
        int imgid = i.getIntExtra("imgid",R.drawable.caomei);

        initdata();

        ImageView imageView = findViewById(R.id.imageView4);
        imageView.setImageResource(imgid);
        TextView textView = findViewById(R.id.textView28);
        textView.setText(name);
        final EditText editText = findViewById(R.id.editText_amountfruit);
        editText.setText(String.valueOf(amount));
        final EditText editText1 = findViewById(R.id.editText_datefruit);
        editText1.setText(String.valueOf(date));

        /***
         * 取消按钮
         */
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent();
                intent.setClass(ModifyFruit.this,FruitDetails.class);
                startActivity(intent);
            }
        });

        /***
         * 确认按钮，接收数据更改update
         */
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FruitBean bean = new FruitBean(0,"草莓",10,"1998-01-01",R.drawable.caomei);
                bean.amount_fruit = Integer.parseInt(editText.getText().toString());
                bean.date_fruit = editText1.getText().toString();
                dbHelper.updatefoodById_fruit(bean,id);
                Toast.makeText(ModifyFruit.this,
                        "数据更新完成："+name+" \n库存量："+editText.getText().toString()+" \n过期时间："+editText1.getText().toString(), LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent();
                intent.setClass(ModifyFruit.this,FruitDetails.class);
                startActivity(intent);
            }
        });
    }

    /***
     * 初始化数据库，打开数据库，获取所有水果信息
     */
    private void initdata(){
        dbHelper = new DBHelper(this);
        dbHelper.open();
//        dbHelper.initData_fruit();
        date = dbHelper.getAllFoods_fruit();
    }
}

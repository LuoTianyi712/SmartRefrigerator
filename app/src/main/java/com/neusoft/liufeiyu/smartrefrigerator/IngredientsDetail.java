package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 修改食材的页面，输入日期和总数进行修改
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

import static android.widget.Toast.*;

public class IngredientsDetail extends AppCompatActivity {

    DBHelper dbHelper=new DBHelper(this);
    List<FoodBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_detail);

        //接收intent数据
        Intent i = getIntent();
        //拆包，获取数据
        final int id = i.getIntExtra("id",0);
        final String name = i.getStringExtra("name");
        int amount = i.getIntExtra("amount",10);
        String date = i.getStringExtra("date");
        int imgid = i.getIntExtra("imgid",R.drawable.huanggua);

        //显示到控件上
        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(imgid);
        TextView textView1 = findViewById(R.id.textView6);
        textView1.setText(name);
        final EditText editText = findViewById(R.id.editText2);
        editText.setText(String.valueOf(amount));
        final EditText editText1 = findViewById(R.id.editText_3);
        editText1.setText(String.valueOf(date));

        initData();

        /***
         * 取消按钮，返回上一页面，finish当前页面
         */
        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent();
                intent.setClass(IngredientsDetail.this,ModifyIngredients.class);
                startActivity(intent);
            }
        });

        /***
         * 确认按钮
         * 更新数据库，将输入的数据存入数据库中，finish当前页面，重启修改食材列表页面
         */
        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodBean bean = new FoodBean(0
                        ,"黄瓜",7,"1999-01-01",R.drawable.huanggua);
                bean.date = editText1.getText().toString();
                bean.amount = Integer.parseInt(editText.getText().toString());
                dbHelper.updatefoodById(bean,id);
                Toast.makeText(IngredientsDetail.this,
                        "数据更新完成："+name
                                +"\n库存："+editText.getText().toString()
                                +"\n过期时间："+editText1.getText().toString(), LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent();
                intent.setClass(IngredientsDetail.this,ModifyIngredients.class);
                startActivity(intent);
            }
        });
    }

    /***
     * 初始化数据库，打开数据库
     */
    private void initData() {
        dbHelper = new DBHelper(this);
        dbHelper.open();
//        dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        data = dbHelper.getAllFoods();
    }
}

package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 主页面，设定日期天气显示，设置缺货过期推荐提示，设置按钮跳转页面
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "http://wthrcdn.etouch.cn/weather_mini?city=";
    TextView textView_weather;
    TextView textView_locate;
    TextView textView_lost;
    TextView textView_overdate;
    TextView textView_recommend;

    DBHelper dbHelper;
    List<FoodBean> data;
    List<FruitBean> data1;
    static final String TABLE_NAME = "food_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        /**
         * 主页面获取系统时间，System.currentTimeMillis()获取当前时间
         */
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);

        TextView textView_date = findViewById(R.id.textView_Date);
        textView_date.setText(str);

        textView_weather = findViewById(R.id.textView_Weather);
        textView_locate = findViewById(R.id.textView_Locate);
        textView_lost = findViewById(R.id.textView16);
        textView_overdate = findViewById(R.id.textView8);
        textView_recommend = findViewById(R.id.textView31);

        getWeatherMsg(url + textView_locate.getText().toString());

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ExistingIngredients.class);
                startActivity(i);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ModifyIngredients.class);
                startActivity(i);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FruitDetails.class);
                startActivity(i);
            }
        });

        outofstock ();
        beoverdue();
        setTextView_recommend();
    }

    /***
     * 初始化数据库，打开数据库，获取所有食物
     */
    private void initData() {
        dbHelper = new DBHelper(this);
        dbHelper.open();
//        dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        data = dbHelper.getAllFoods();
    }

    /**
     * 向天气查询API发送GET请求
     * @param path
     */
    public void getWeatherMsg(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //线程执行的内容
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.connect();
                    //获得服务器的响应码
                    int response = connection.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        String html = dealResponseResult(inputStream);
                        //处理服务器相应结果
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = html;
                        mHandler.sendMessage(msg);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    //错误处理
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e.toString();
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /***
     * 处理Http返回结果
     * @param inputStream
     * @return
     */
    private String dealResponseResult(InputStream inputStream) {
        StringBuilder html = new StringBuilder();      //存储处理结果
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String s;
            while ((s = reader.readLine()) != null) {
                html.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html.toString();
    }

    /**
     * 跨线程更新UI界面
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //成功返回结果
                    Gson gson = new Gson();
                    WeatherBean bean = gson.fromJson(msg.obj.toString(),WeatherBean.class);
                    textView_weather.setText(
                             "\n当前温度："+bean.getData().getWendu()
                            +"\n当前天气："+bean.getData().getForecast().get(0).getType()
                            +"\n今日风向："+bean.getData().getForecast().get(0).getFengxiang());
                    break;
                case 2:
                    //出错
                    textView_weather.setText("网络错误"+msg.obj.toString());
                    break;
            }
            return false;
        }
    });

    /***
     * 菜单点击事件和操作
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.object_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_suggest:
                setTextView_recommend();
                outofstock ();
                beoverdue();
                getWeatherMsg(url + textView_locate.getText().toString());
                Toast.makeText(MainActivity.this,"页面刷新成功!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_about:
                dbHelper = new DBHelper(this);
                dbHelper.open();
                dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
                data = dbHelper.getAllFoods();
                dbHelper.initData_fruit();
                data1 = dbHelper.getAllFoods_fruit();
                Toast.makeText(MainActivity.this,"数据库初始化成功!",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * 缺货提示，通过遍历数据库内容对比amount实现
     */
    public void outofstock (){
        StringBuilder stringBuilder = new StringBuilder();
        Cursor cursor = dbHelper.db.query(TABLE_NAME,new String[]{"name","amount"},
                null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String amount = cursor.getString(cursor.getColumnIndex("amount"));
            int n = Integer.parseInt(amount);
            if(n==0){
                stringBuilder.append(name+" ");
            }
        }textView_lost.setText("缺货提示："+stringBuilder.toString());
        cursor.close();
    }

    /***
     * 过期提示，遍历数据库，利用数据库时间和系统时间比对
     */
    public void beoverdue (){
        StringBuilder stringBuilder = new StringBuilder();
        Cursor cursor = dbHelper.db.query(TABLE_NAME,new String[]{"name","date"},
                null,null,null,null,null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = dateFormat.parse(date);
                date2 = new Date(System.currentTimeMillis());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date1.getTime() < date2.getTime()) {
                stringBuilder.append(name + " ");
            }
        }textView_overdate.setText("过期提示："+stringBuilder.toString());
        cursor.close();
    }

    /***
     * 食谱推荐
     * 实力有限，写不出更好的算法
     */
    public void setTextView_recommend(){
        Cursor cursor = dbHelper.db.query(TABLE_NAME,new String[]{"name","amount"},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String amount = cursor.getString(cursor.getColumnIndex("amount"));

            if (name.equals("竹笋")){
                int n = Integer.parseInt(amount);
                if (n>3){
                    textView_recommend.setText("晚餐推荐：清炒竹笋");
                }else {
                    while (cursor.moveToNext()){
                        String name1 = cursor.getString(cursor.getColumnIndex("name"));
                        if (name1.equals("猪肉") && name.equals("竹笋")){
                            int n1 = Integer.parseInt(amount);
                            if (n1 > 3 && n > 3){
                                textView_recommend.setText("晚餐推荐：竹笋炒肉");
                            }
                        }
                    }
                }
            }
        }
    }
}

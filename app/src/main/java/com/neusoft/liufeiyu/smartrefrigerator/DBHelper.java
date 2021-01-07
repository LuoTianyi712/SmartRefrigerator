package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 数据库的操作，封装好的函数体
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neusoft.liufeiyu.smartrefrigerator.FoodBean;
import com.neusoft.liufeiyu.smartrefrigerator.FruitBean;
import com.neusoft.liufeiyu.smartrefrigerator.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper {
    Context context;
    SQLiteDatabase db;
    static final String DB_NAME = "food.db";
    static final String TABLE_NAME = "food_list";
    static final String TABLE_NAME_FRUIT = "fruit_list";

    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    String str = formatter.format(curDate);

    //食材名
    public static final String[] names = {
            "黄瓜", "豆芽", "胡萝卜", "茄子", "青椒", "豌豆", "玉米", "猪肉", "竹笋",
    };

    //水果名
    public static final String[] fruit_names = {
            "草莓","橙子","葡萄","香瓜","樱桃",
    };

    //食材图片
    public static final int[] imgid = {
            R.drawable.huanggua,
            R.drawable.douya,
            R.drawable.huluobo,
            R.drawable.qiezi,
            R.drawable.qingjiao,
            R.drawable.wandou,
            R.drawable.yumi,
            R.drawable.zhurou,
            R.drawable.zhusun,
    };

    //水果图片
    public static final int[] imgid_fruit = {
            R.drawable.caomei,
            R.drawable.chengzi,
            R.drawable.putao,
            R.drawable.xianggua,
            R.drawable.yingtao,
    };

    /**
     * 数据库构造方法
     */
    DBHelper(Context c) {
        context = c;
    }

    /**
     * 打开数据方法
     */
    public boolean open() {
        String path = context.getFilesDir() + "/" + DB_NAME;
        db = SQLiteDatabase.openOrCreateDatabase(path, null, null);
        db.execSQL("create table if not exists "+TABLE_NAME+
                "(id integer primary key autoincrement, name vchar(30), amount int, date date,imgid int)");
        db.execSQL("create table if not exists "+TABLE_NAME_FRUIT+
                "(id_fruit integer primary key autoincrement,name_fruit vchar(30),amount_fruit int, date_fruit date, imgid_fruit int)");
        return true;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        db.close();
    }

    /**
     * 初始化数据库--食材表的数据
     */
    public void initData() {
        deleteAllfoods();
        for (int i = 0; i < 9;i++) {
            FoodBean bean = new FoodBean(1,names[i], 10, str, imgid[i]);
            inserfood(bean);
        }
    }

    /***
     * 初始化水果数据
     */
    public void initData_fruit() {
        deleteAllfoods_fruit();
        for (int i = 0; i < 5;i++) {
            FruitBean bean = new FruitBean(i,fruit_names[i], 10, str, imgid_fruit[i]);
            inserfood_fruit(bean);
        }
    }

    /**
     * 插入食物信息
     */
    public boolean inserfood(FoodBean bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("imgid", bean.getImgid());
        values.put("date",bean.getDate());
        values.put("amount", bean.getAmount());
        long i = db.insert(TABLE_NAME, null, values);
        if (i > 0) return true;
        else return false;
    }

    /**
     * 插入水果信息
     */
    public boolean inserfood_fruit(FruitBean fruitBean) {
        ContentValues values = new ContentValues();
        values.put("name_fruit", fruitBean.getName());
        values.put("amount_fruit", fruitBean.getAmount());
        values.put("date_fruit",fruitBean.getDate());
        values.put("imgid_fruit", fruitBean.getImgid());
        long i = db.insert(TABLE_NAME_FRUIT, null, values);
        if (i > 0) return true;
        else return false;
    }

    /**
     * 获取所有食物信息列表
     */
    public List<FoodBean> getAllFoods() {
        List<FoodBean> data = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null,
                null, null, null, null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            int amount = c.getInt(2);
            String date = c.getString(3);
            int imgid = c.getInt(4);
            FoodBean foodBean = new FoodBean(id, name, amount, date, imgid);
            data.add(foodBean);
        }
        return data;
    }

    /***
     * 获取所有水果信息列表
     * @return
     */
    public List<FruitBean> getAllFoods_fruit() {
        List<FruitBean> data = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME_FRUIT, null, null,
                null, null, null, null);
        while (c.moveToNext()) {
            int id_fruit = c.getInt(0);
            String name_fruit = c.getString(1);
            int amount_fruit = c.getInt(2);
            String date_fruit = c.getString(3);
            int imgid_fruit = c.getInt(4);
            FruitBean fruitBean = new FruitBean(id_fruit, name_fruit, amount_fruit, date_fruit, imgid_fruit);
            data.add(fruitBean);
        }
        return data;
    }

    /***
     * 清空食材表food_list数据
     */
    public void deleteAllfoods(){
        db.delete(TABLE_NAME,null,null);
    }

    /***
     * 清空水果fruit_list中所有数据
     */
    public void deleteAllfoods_fruit(){
        db.delete(TABLE_NAME_FRUIT,null,null);
    }

    /***
     * 根据食材id删除食材
     * @param id
     * @return
     */
    public int deleteFruitById(int id) {
        return db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }

    /***
     * 通过水果id删除水果
     * @param id_fruit
     * @return
     */
    public int deleteFruitById_fruit(int id_fruit) {
        return db.delete(TABLE_NAME_FRUIT, "id_fruit=?", new String[]{String.valueOf(id_fruit)});
    }

    /***
     * 食材表food_list update更新数据
     * @param bean
     * @param id
     * @return
     */
    public boolean updatefoodById(FoodBean bean,int id){
        ContentValues values = new ContentValues();
        /*在values中添加内容*/
        values.put("amount", bean.getAmount());
        values.put("date", bean.getDate());
        long i = db.update(TABLE_NAME,values,"id=?",new String[]{String.valueOf(id)});
        if (i > 0) return true;
        else return false;
    }

    /***
     * update更新水果表fruit_list数据
     * @param bean
     * @param id
     * @return
     */
    public boolean updatefoodById_fruit(FruitBean bean,int id){
        ContentValues values = new ContentValues();
        /*在values中添加内容*/
        values.put("amount_fruit", bean.getAmount());
        values.put("date_fruit", bean.getDate());
        long i = db.update(TABLE_NAME_FRUIT,values,"id_fruit=?",new String[]{String.valueOf(id)});
        if (i > 0) return true;
        else return false;
    }
}

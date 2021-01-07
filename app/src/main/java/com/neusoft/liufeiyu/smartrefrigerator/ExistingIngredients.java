package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 查看现有食材，使用listview来实现，使用菜单添加食材
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExistingIngredients extends AppCompatActivity {

    ListView listView;
    List<FoodBean> foodBeans =new ArrayList<FoodBean>();
    ListViewAdapter adapter;

    DBHelper dbHelper =new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_ingredients);

        listView = findViewById(R.id.listView);
        initData();
        adapter = new ListViewAdapter(this,foodBeans);
        foodBeans = dbHelper.getAllFoods();
        listView.setAdapter(adapter);
    }

    /***
     * 初始化数据库，打开数据库
     */
    private void initData() {
        dbHelper = new DBHelper(this);
        dbHelper.open();
//        dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        foodBeans = dbHelper.getAllFoods();
    }

    /***
     * 菜单创建与点击事件
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_add:
                makeAddFruitDialog();
            case R.id.item_flash:
                adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * 创建dialog对话框，用于添加水果，增加水果
     */
    private void makeAddFruitDialog() {
        //1.准备自定义视图，为spinner下拉框填装数据
        final View view = View.inflate(this, R.layout.add_food_dialog, null);
        final Spinner spinner = view.findViewById(R.id.spinner);
        final EditText etdate = view.findViewById(R.id.editText_setdate);
        final EditText etAmount = view.findViewById(R.id.editText_setamount);
        final ImageView imageView = view.findViewById(R.id.imageView3);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, DBHelper.names);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                imageView.setImageResource(DBHelper.imgid[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //创建对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("添加食材")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FoodBean bean = new FoodBean(1,"黄瓜",7,"1999-01-01",R.drawable.huanggua);
                        int index = spinner.getSelectedItemPosition();
                        bean.name = DBHelper.names[index];
                        bean.imgid = DBHelper.imgid[index];
                        bean.date =(etdate.getText().toString());
                        bean.amount = Integer.valueOf(etAmount.getText().toString());
                        dbHelper.inserfood(bean);
                        adapter.addItem(bean);
                        Toast.makeText(ExistingIngredients.this,"添加水果："+bean.name,Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    static class ViewHolder {
        public TextView txt_name;
        public TextView txt_amount;
        public TextView txt_date;
        public ImageView imageView;
    }
}

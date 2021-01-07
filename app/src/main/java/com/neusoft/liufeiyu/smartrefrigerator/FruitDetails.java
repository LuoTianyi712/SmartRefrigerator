package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 水果列表显示，使用RecyclerView显示
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class FruitDetails extends AppCompatActivity {

    List<FruitBean> data;
    RecyclerView recyclerView_fruit;
    RecyclerViewFruitAdapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_details);

        recyclerView_fruit = findViewById(R.id.recyclerViewFruit);
        initData();
        adapter = new RecyclerViewFruitAdapter(this,data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_fruit.setLayoutManager(layoutManager);
        recyclerView_fruit.setAdapter(adapter);
    }

    /***
     * 初始化数据，打开数据库
     */
    private void initData() {
        dbHelper = new DBHelper(this);
        dbHelper.open();
        //dbHelper.initData_fruit();//这个方法只在头一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        data = dbHelper.getAllFoods_fruit();
    }

    /***
     * 菜单：上下文点击事件，长按item弹出上下文菜单
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_revise:
                Intent i = new Intent();
                i.setClass(FruitDetails.this,ModifyFruit.class);
                i.putExtra("id",adapter.getSelectedItemData().getId());
                i.putExtra("name",adapter.getSelectedItemData().getName());
                i.putExtra("amount",adapter.getSelectedItemData().getAmount());
                i.putExtra("date",adapter.getSelectedItemData().getDate());
                i.putExtra("imgid",adapter.getSelectedItemData().getImgid());
                Toast.makeText(this,"修改水果："+adapter.getSelectedItemData().getName(),Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
                break;
            case R.id.item_delete:
                dbHelper.deleteFruitById_fruit(adapter.getSelectedItemData().getId());
                adapter.removeSelectionItem();
                Toast.makeText(this,"删除水果："+adapter.getSelectedItemData().getName(),Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /***
     * 创建菜单与菜单点击事件
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
     * 创建添加水果对话框
     */
    private void makeAddFruitDialog() {
        //1.准备自定义视图，为spinner下拉框填装数据
        final View view = View.inflate(this, R.layout.add_food_dialog, null);
        final Spinner spinner = view.findViewById(R.id.spinner);
        final EditText etdate = view.findViewById(R.id.editText_setdate);
        final EditText etAmount = view.findViewById(R.id.editText_setamount);
        final ImageView imageView = view.findViewById(R.id.imageView3);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, DBHelper.fruit_names);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                imageView.setImageResource(DBHelper.imgid_fruit[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //创建对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("添加水果")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FruitBean bean = new FruitBean(0,"草莓",7,"1998-10-28",R.drawable.caomei);
                        int index = spinner.getSelectedItemPosition();
                        bean.name_fruit = DBHelper.fruit_names[index];
                        bean.imgid_fruit = DBHelper.imgid_fruit[index];
                        bean.date_fruit =(etdate.getText().toString());
                        bean.amount_fruit = Integer.valueOf(etAmount.getText().toString());
                        dbHelper.inserfood_fruit(bean);
                        adapter.addItem(bean);
                        Toast.makeText(FruitDetails.this,"添加水果："+bean.name_fruit,Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }
}

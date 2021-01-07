package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 修改食材页面列表显示，使用RecyclerView显示
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class ModifyIngredients extends AppCompatActivity {

    List<FoodBean> data;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ingredients);

        recyclerView = findViewById(R.id.recyclerView);
        initData();
        adapter = new RecyclerViewAdapter(this, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    /***
     * 初始化数据，打开数据库
     */
    private void initData() {
        dbHelper = new DBHelper(this);
        dbHelper.open();
//        dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        data = dbHelper.getAllFoods();
    }

    /***
     * 上下文菜单弹出
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_revise:
                Intent i = new Intent();
                i.setClass(ModifyIngredients.this,IngredientsDetail.class);
                i.putExtra("id",adapter.getSelectedItemData().getId());
                i.putExtra("name",adapter.getSelectedItemData().getName());
                i.putExtra("imgid",adapter.getSelectedItemData().getImgid());
                i.putExtra("date",adapter.getSelectedItemData().getDate());
                i.putExtra("amount",adapter.getSelectedItemData().getAmount());
                Toast.makeText(this,"修改食材："+adapter.getSelectedItemData().name,Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
                break;
            case R.id.item_delete:
                Toast.makeText(this,"删除食材："+adapter.getSelectedItemData().name,Toast.LENGTH_SHORT).show();
                dbHelper.deleteFruitById(adapter.getSelectedItemData().getId());
                adapter.removeSelectedItem();
                break;
        }
        return super.onContextItemSelected(item);
    }
}

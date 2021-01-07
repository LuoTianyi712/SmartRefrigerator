package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 设置ListView适配器
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<FoodBean> foodBeans;
    private LayoutInflater inflater;
    FoodBean selectedItemData;

    /***
     * 获取选中行信息
     * @return
     */
    public FoodBean getSelectedItemData() {
        return selectedItemData;
    }

    public ListViewAdapter(Context context, List<FoodBean> foodBeans){
        super();
        this.context = context;
        this.foodBeans = foodBeans;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foodBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 自定义增加item条目的方法，为了方便对列表的操作
     */
    public void addItem(FoodBean bean){
        foodBeans.add(bean);
        notifyDataSetChanged();
    }

    /**
     * 自定义删除选定item方法，为了方便对列表的操作
     */
    public void removeSelectedItem(){
        foodBeans.remove(selectedItemData);
        notifyDataSetChanged();
    }

    /***
     * 将数值插入数据表中
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FoodBean f = foodBeans.get(position);
        ExistingIngredients.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ExistingIngredients.ViewHolder();
            convertView = inflater.inflate(R.layout.listitem, null);
            viewHolder.txt_name = (TextView) convertView
                    .findViewById(R.id.textView_vegname);
            viewHolder.txt_amount = (TextView) convertView
                    .findViewById(R.id.textView_amount);
            viewHolder.txt_date = (TextView) convertView
                    .findViewById(R.id.textView_overdate);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView);
            convertView.setTag(viewHolder);


        }else {
            viewHolder = (ExistingIngredients.ViewHolder) convertView.getTag();
        }

        //插入数据
        viewHolder.txt_name.setText(f.getName());
        viewHolder.txt_amount.setText(String.valueOf(f.getAmount()));
        viewHolder.txt_date.setText(f.getDate());
        viewHolder.imageView.setImageResource(f.getImgid());
        return convertView;
    }
}

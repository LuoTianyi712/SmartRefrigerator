package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 定义水果变量与getset方法
 */
public class FruitBean {
    public int id_fruit;
    public String name_fruit;
    public int amount_fruit;
    public String date_fruit;
    public int imgid_fruit;

public FruitBean(int id_fruit, String name_fruit, int amount_fruit, String date_fruit, int imgid_fruit){
        super();
        this.id_fruit = id_fruit;
        this.name_fruit = name_fruit;
        this.amount_fruit = amount_fruit;
        this.date_fruit = date_fruit;
        this.imgid_fruit = imgid_fruit;
    }
    //ID
    public int getId() {
        return id_fruit;
    }

    public void setId(int id_fruit) {
        this.id_fruit = id_fruit;
    }
    //名称
    public String getName() {
        return name_fruit;
    }

    public void setName(String name_fruit) {
        this.name_fruit = name_fruit;
    }
    //库存
    public int getAmount() {
        return amount_fruit;
    }

    public void setAmount(int amount_fruit) {
        this.amount_fruit = amount_fruit;
    }
    //日期
    public String getDate() {
        return date_fruit;
    }

    public void setDate(String date_fruit) {
        this.date_fruit = date_fruit;
    }
    //图片
    public int getImgid() {
        return imgid_fruit;
    }

    public void setImgid(int imgid_fruit) {
        this.imgid_fruit = imgid_fruit;
    }
}

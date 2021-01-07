package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 版权所有 @ 0121-刘飞宇
 * 定义食材变量与getset方法
 */
public class FoodBean {
    public int id;
    public String name;
    public int amount;
    public String date;
    public int imgid;

    public FoodBean(int id, String name, int amount, String date, int imgid){
        super();
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.imgid = imgid;
        this.date = date;
    }
    //ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //名称
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //库存
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    //日期
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //图片
    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}

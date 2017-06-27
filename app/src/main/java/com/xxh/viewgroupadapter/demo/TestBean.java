package com.xxh.viewgroupadapter.demo;

/**
 * Created by 解晓辉 on 2017/6/27.
 * 作用：
 */

public class TestBean {
    private int imageId;
    private String name;

    public TestBean(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

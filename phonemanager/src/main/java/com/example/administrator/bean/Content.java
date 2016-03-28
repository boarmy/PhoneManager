package com.example.administrator.bean;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Content {
    String name;
    String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Content(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Content() {
    }

    @Override
    public String toString() {
        return "Content{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}

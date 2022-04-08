package com.tack;

import java.io.Serializable;

public class Medication implements Serializable {
    private String name;
    private int weight;
    private String code;
    private String image;
    public Medication(){

    }
    public Medication(String name,String code,int weight,String image){
        this.name = name;
        this.code = code;
        this.weight = weight;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

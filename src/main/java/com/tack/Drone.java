package com.tack;

import java.io.Serializable;

public class Drone implements Serializable {
    private String serial_number;
    private String model;
    private int weight;
    private int battery_capacity;
    private String state;
    public Drone(){

    }
    public Drone(String serial_number,String model,int weight,String state,int battery_capacity){
        this.battery_capacity = battery_capacity;
        this.state = state;
        this.weight = weight;
        this.model = model;
        this.serial_number = serial_number;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBattery_capacity() {
        return battery_capacity;
    }

    public void setBattery_capacity(int battery_capacity) {
        this.battery_capacity = battery_capacity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

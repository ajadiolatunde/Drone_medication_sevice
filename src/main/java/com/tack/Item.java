package com.tack;

import java.io.Serializable;

public class Item implements Serializable {
    private String drone; //serial number
    private String medication;//code

    public Item(){}

    public String getDrone() {
        return drone;
    }

    public void setDrone(String drone) {
        this.drone = drone;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }
}

package com.tack;

import java.io.Serializable;

public class Item implements Serializable {
    private String drone_serialnumber;
    private String medication_code;

    public String getDrone_serialnumber() {
        return drone_serialnumber;
    }

    public void setDrone_serialnumber(String drone_serialnumber) {
        this.drone_serialnumber = drone_serialnumber;
    }

    public String getMedication_code() {
        return medication_code;
    }

    public void setMedication_code(String medication_code) {
        this.medication_code = medication_code;
    }

}

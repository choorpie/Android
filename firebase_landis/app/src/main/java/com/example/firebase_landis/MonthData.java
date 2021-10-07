package com.example.firebase_landis;

import java.util.HashMap;
import java.util.Map;

public class MonthData {
    HashMap<String, Integer> keyvalue;

    public MonthData(){

    }

    public MonthData(HashMap<String, Integer> keyvalue) {
        this.keyvalue = keyvalue;
    }

    public HashMap<String, Integer> getKeyvalue() {
        return keyvalue;
    }
}

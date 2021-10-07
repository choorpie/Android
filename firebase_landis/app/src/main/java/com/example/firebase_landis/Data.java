package com.example.firebase_landis;

public class Data {
    String time;
    int humid;

    public Data() {
    }

    public Data(int humid) {
        this.humid = humid;
    }

    public Data(String time, int humid) {
        this.time = time;
        this.humid = humid;
    }

    public int getHumid() {
        return humid;
    }

//    public String getTime() {
//        return time;
//    }
}

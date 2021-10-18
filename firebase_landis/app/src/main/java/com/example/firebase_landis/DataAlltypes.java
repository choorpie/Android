package com.example.firebase_landis;

public class DataAlltypes {
    int humid, co2, k, n, o2, p, soil_humid, soil_temp, temp;

    public DataAlltypes() {
    }

    public DataAlltypes(int humid, int co2, int k, int n, int o2, int p, int soil_humid, int soil_temp, int temp) {
        this.humid = humid;
        this.co2 = co2;
        this.k = k;
        this.n = n;
        this.o2 = o2;
        this.p = p;
        this.soil_humid = soil_humid;
        this.soil_temp = soil_temp;
        this.temp = temp;
    }

    public int getHumid() {
        return humid;
    }

    public int getCO2() {
        return co2;
    }

    public int getK() {
        return k;
    }

    public int getN() {
        return n;
    }

    public int getO2() {
        return o2;
    }

    public int getP() {
        return p;
    }

    public int getSoil_humid() {
        return soil_humid;
    }

    public int getSoil_temp() {
        return soil_temp;
    }

    public int getTemp() {
        return temp;
    }
}

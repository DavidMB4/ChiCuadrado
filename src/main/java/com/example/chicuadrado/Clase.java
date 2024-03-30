package com.example.chicuadrado;

public class Clase {

    private int clases1;
    private String clases2;
    private int frecAbs;
    private int frecAcum;

    public Clase(int clases1, String clases2, int frecAbs, int frecAcum){
        this.clases1 = clases1;
        this.clases2 = clases2;
        this.frecAbs = frecAbs;
        this.frecAcum = frecAcum;
    }

    public int getClases1() {
        return clases1;
    }

    public void setClases1(int clases1) {
        this.clases1 = clases1;
    }

    public String getClases2() {
        return clases2;
    }

    public void setClases2(String clases2) {
        this.clases2 = clases2;
    }

    public int getFrecAbs() {
        return frecAbs;
    }

    public void setFrecAbs(int frecAbs) {
        this.frecAbs = frecAbs;
    }

    public int getFrecAcum() {
        return frecAcum;
    }

    public void setFrecAcum(int frecAcum) {
        this.frecAcum = frecAcum;
    }
}

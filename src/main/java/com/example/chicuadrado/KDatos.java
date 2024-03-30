package com.example.chicuadrado;

public class KDatos {

private double valorKColumn;
private String kColumn;


public KDatos(double valorKColumn, String kColumn){
    this.valorKColumn = valorKColumn;
    this.kColumn = kColumn;
}

    public double getValorKColumn() {

    return valorKColumn;
    }

    public void setValorKColumn(double valorKColumn) {
        this.valorKColumn = valorKColumn;
    }

    public String getKColumn() {

        return kColumn;
    }

    public void setKColumn(String kColumn) {
        this.kColumn = kColumn;
    }

}

package com.example.chicuadrado;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Scanner;
import java.util.Arrays;

import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML private TextArea Datos;
    @FXML private TextField numDatos;
    @FXML private TextField NcText;
    @FXML private TextField KField;
    @FXML private TextField AmpField;
    @FXML private TextField ValorX2;
    @FXML private TextField x2Prueba;
    @FXML private TextField pruebaHip;

    @FXML private TableView<Clase> table;
    @FXML private TableColumn<Clase , Integer> clases1;
    @FXML private TableColumn<Clase , String> clases2;
    @FXML private TableColumn<Clase , Integer> frecAbs;
    @FXML private TableColumn<Clase , Integer> frecAcum;

    @FXML private TableView<KDatos> kTable;
    @FXML private TableColumn<KDatos , Double> valorKColumn;
    @FXML private TableColumn<KDatos , String> kColumn;

    public void initialize(){
        valorKColumn.setCellValueFactory(new PropertyValueFactory<KDatos, Double>("ValorKColumn"));
        kColumn.setCellValueFactory(new PropertyValueFactory<KDatos, String>("kColumn"));

        clases1.setCellValueFactory(new PropertyValueFactory<Clase, Integer>("clases1"));
        clases2.setCellValueFactory(new PropertyValueFactory<Clase, String>("clases2"));
        frecAbs.setCellValueFactory(new PropertyValueFactory<Clase, Integer>("frecAbs"));
        frecAcum.setCellValueFactory(new PropertyValueFactory<Clase, Integer>("frecAcum"));
    }

    public void start(javafx.stage.Stage primaryStage) throws Exception {
        // Inicialización de la escena, etc.
    }

    public void button(){

        int datos = Integer.parseInt(numDatos.getText());

        String inputData = Datos.getText();
        String[] dataStrings = inputData.split("\n");
        double[] observed = Arrays.stream(dataStrings).mapToDouble(Double::parseDouble).toArray();

        int n = observed.length;
        String kString = KField.getText();
        String ampliString = AmpField.getText();
        double k;
        double Ampli = 0;

        if(kString.isEmpty()){
            Ampli = Double.parseDouble(ampliString);
            k = 1/Ampli;
        } else {
            k = Double.parseDouble(kString);
            if (k == 0){
                Ampli = Double.parseDouble(ampliString);
                k = 1/Ampli;
            } else {
                Ampli = 1/k;
            }

        }
        System.out.println();
        double min = observed[0];
        double max = observed[0];
        double rango;
        double Xmedia = 0;
        double desvEstand;
        double varianza;
        double Ai = observed.length*Ampli;
        double chiCuad;
        double Nc = Double.parseDouble(NcText.getText());


        for (int i = 1; i < observed.length; i++) {
            if (observed[i] < min) {
                min = observed[i];
            }
        }

        for (int i = 1; i < observed.length; i++) {
            if (observed[i] > max) {
                max = observed[i];
            }
        }

        rango = max-min;

        for(int i=0; i < observed.length; i++){
            Xmedia = Xmedia + observed[i];
        }
        Xmedia = Xmedia/observed.length;

        double sumaCuadrados = 0;
        for(int i=0; i < observed.length; i++){
            sumaCuadrados = sumaCuadrados + Math.pow(observed[i]-Xmedia, 2);
        }

        varianza = VarianzaMetod(observed, Xmedia, sumaCuadrados);
        desvEstand = Math.sqrt(varianza);
        chiCuad = FrecuenciasK(k, Ampli, observed, Ai);
        String chiDecimal = String.format("%.4f", chiCuad);
        chiCuad = Double.parseDouble(chiDecimal);
        System.out.println(chiCuad);

        double alfa = 1-(1 - Nc / 100);
        System.out.println("Alfa: "+alfa);
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(k-1);
        double inverseChiSquare = chiSquaredDistribution.inverseCumulativeProbability(alfa);
        System.out.println(inverseChiSquare);

        String condicion = pruebaDeHip(inverseChiSquare, chiCuad);
        System.out.println(condicion);

        ValorX2.setText(String.valueOf(inverseChiSquare));
        x2Prueba.setText(String.valueOf(chiCuad));
        pruebaHip.setText(condicion);

        int frecuenciaAcumulada = 0;
        double contadorAmpli = Ampli;
        chiCuad = 0;
        double chiCuadTotal = 0;
        String KdatosString = "k"+1;
        System.out.println(KdatosString);

        for(int i=0; i<k; i++){
            int frecuencia = 0;
            for(int u = 0; u<observed.length; u++){
                if(observed[u]<contadorAmpli && (contadorAmpli-Ampli)<= observed[u]){
                    frecuencia ++;
                }
            }
            String AmpliMinString = String.format("%.2f", contadorAmpli-Ampli);
            double contadorAmpliMin = Double.parseDouble(AmpliMinString);
            String AmpliMaxString = String.format("%.2f", contadorAmpli);
            double contadorAmpliMax = Double.parseDouble(AmpliMaxString);

            String intervalos = "("+contadorAmpliMin+", "+contadorAmpliMax+")";
            System.out.println(frecuencia);
            chiCuad = Math.pow((frecuencia-Ai),2)/Ai;
            chiCuadTotal += chiCuad;
            contadorAmpli += Ampli;
            frecuenciaAcumulada += frecuencia;
            KdatosString = "k"+(i+1);
           KDatos numero = new KDatos (chiCuad, KdatosString);
            this.kTable.getItems().add(numero);
            Clase numero2 = new Clase((i+1),intervalos, frecuencia, frecuenciaAcumulada);
            this.table.getItems().add(numero2);
        }

    }

    public static String pruebaDeHip(double inverseChiSquare, double chiCuad){
        if(chiCuad>inverseChiSquare){
            return("Ho se rechaza y los datos no son de una serie U(0, 1)");
        } else{
            return ("Ho se acepta y los datos son de una serie U(0, 1)");
        }
    }

    public static double VarianzaMetod(double observed[], double Xmedia, double sumaCuadrados){
        double standarDerivation = Math.sqrt(sumaCuadrados/observed.length);
        return standarDerivation;
    }

    public static double FrecuenciasK(double k, double Ampli, double[] observed, double Ai){
        int frecuenciaAcumulada = 0;
        double contadorAmpli = Ampli;
        double chiCuad = 0;
        double chiCuadTotal = 0;

        for(int i=0; i<k; i++){
            int frecuencia = 0;
            for(int u = 0; u<observed.length; u++){
                if(observed[u]<contadorAmpli && (contadorAmpli-Ampli)<= observed[u]){
                    frecuencia ++;
                }
            }

            chiCuad = Math.pow((frecuencia-Ai),2)/Ai;
            chiCuadTotal += chiCuad;
            contadorAmpli += Ampli;
            frecuenciaAcumulada += frecuencia;
        }
        return chiCuadTotal;
    }
}
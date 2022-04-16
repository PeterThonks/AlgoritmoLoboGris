package algoritmo.lobogris.estructura;

import algoritmo.shared.util.Constante;
import org.javatuples.Pair;

import java.security.InvalidParameterException;

public class Columna {
    private String nombreColumna;
    private Pair<Integer, Integer> tupla;
    private double frecuenciaUso;
    private long cantidadBytes;
    private double probabilidadEleccion;
    private float penalidad;
    private boolean esPkFk;

    public Columna(String nombreColumna, int tabla, int columna, double frecuenciaUso, long cantidadBytes, boolean esPkFk) {
        if (nombreColumna == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (tabla < 0 || columna < 0 || frecuenciaUso < 0 || cantidadBytes < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);

        this.nombreColumna = nombreColumna;
        this.tupla = new Pair<>(tabla,columna);
        this.frecuenciaUso = frecuenciaUso;
        this.cantidadBytes = cantidadBytes;
        this.probabilidadEleccion = Math.random();
        this.penalidad = 1;
        this.esPkFk = esPkFk;
    }

    public Columna(Columna otro) {
        this.nombreColumna = otro.getNombreColumna();
        this.tupla = otro.getTupla();
        this.frecuenciaUso = otro.getFrecuenciaUso();
        this.cantidadBytes = otro.getCantidadBytes();
        this.probabilidadEleccion = otro.getProbabilidadEleccion();
        this.penalidad = otro.getPenalidad();
        this.esPkFk = otro.isEsPkFk();
    }

    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public double getTuplaTabla() {
        return tupla.getValue0();
    }

    public double getTuplaColumna() {
        return tupla.getValue1();
    }

    public Pair<Integer, Integer> getTupla() {
        return tupla;
    }

    public void setTupla(int tabla, int columna) {
        this.tupla = new Pair<>(tabla,columna);
    }

    public double getFrecuenciaUso() {
        return frecuenciaUso;
    }

    public void setFrecuenciaUso(double frecuenciaUso) {
        this.frecuenciaUso = frecuenciaUso;
    }

    public long getCantidadBytes() {
        return cantidadBytes;
    }

    public void setCantidadBytes(int cantidadBytes) {
        this.cantidadBytes = cantidadBytes;
    }

    public double getProbabilidadEleccion() {
        return probabilidadEleccion;
    }

    public void setProbabilidadEleccion(double probabilidadEleccion) {
        this.probabilidadEleccion = probabilidadEleccion;
    }

    public void setProbabilidadEleccion() {
        this.probabilidadEleccion = Math.random();
    }

    public float getPenalidad() {
        return penalidad;
    }

    public void setPenalidad(float penalidad) {
        this.penalidad = penalidad;
    }

    public boolean isEsPkFk() {
        return esPkFk;
    }

    public void setEsPkFk(boolean esPkFk) {
        this.esPkFk = esPkFk;
    }

    public void printColumna(){
        System.out.println("Columna " + this.nombreColumna + " n° " + this.tupla + " con frecuencia " + this.frecuenciaUso
                + ", cantidad de bytes " + this.cantidadBytes + ", probabilidad de elección de " + this.probabilidadEleccion
                + ", penalidad de " + this.penalidad + " y " + (this.esPkFk ? "sí" : "no") + " es PK");
    }
}

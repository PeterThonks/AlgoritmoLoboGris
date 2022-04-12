package algoritmo.lobogris.estructura;

import org.javatuples.Pair;

public class Columna {
    private String nombreColumna;
    private Pair<Integer, Integer> tupla;
    private double frecuenciaUso;
    private long cantidadBytes;
    private double probabilidadEleccion;

    public Columna(String nombreColumna, int tabla, int columna, double frecuenciaUso, long cantidadBytes) {
        this.nombreColumna = nombreColumna;
        this.tupla = new Pair<>(tabla,columna);
        this.frecuenciaUso = frecuenciaUso;
        this.cantidadBytes = cantidadBytes;
        this.probabilidadEleccion = Math.random();
    }

    public Columna(Columna otro) {
        this.nombreColumna = otro.getNombreColumna();
        this.tupla = otro.getTupla();
        this.frecuenciaUso = otro.getFrecuenciaUso();
        this.cantidadBytes = otro.getCantidadBytes();
        this.probabilidadEleccion = otro.getProbabilidadEleccion();
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
        this.tupla.setAt0(tabla);
        this.tupla.setAt1(columna);
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

    public void printColumna(){
        System.out.println("Columna " + this.nombreColumna + " n° " + this.tupla + " con frecuencia " + this.frecuenciaUso
                + ", cantidad de bytes " + this.cantidadBytes + " y probabilidad de elección de " + this.probabilidadEleccion);
    }
}

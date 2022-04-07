package algoritmo.lobogris.estructura;

import org.javatuples.Pair;

public class Columna {
    private String nombreColumna;
    private Pair<Integer, Integer> tupla = new Pair<>(0,0);
    private double frecuenciaUso;
    private int cantidadBits;
    private double probabilidadEleccion;

    public Columna(String nombreColumna, int tabla, int columna, double frecuenciaUso, int cantidadBits) {
        this.nombreColumna = nombreColumna;
        this.tupla.setAt0(tabla);
        this.tupla.setAt1(columna);
        this.frecuenciaUso = frecuenciaUso;
        this.cantidadBits = cantidadBits;
        this.probabilidadEleccion = Math.random();
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

    public int getCantidadBits() {
        return cantidadBits;
    }

    public void setCantidadBits(int cantidadBits) {
        this.cantidadBits = cantidadBits;
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
}

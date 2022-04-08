package algoritmo.lobogris.estructura;

import algoritmo.lobogris.estructura.Columna;
import algoritmo.lobogris.estructura.Tabla;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Lobo implements Comparable<Lobo> {
    private String querys;
    private double tiempoEjecucion;
    private double espacio;
    private double frecuenciaTotal;
    private List<Columna> columnas;
    private List<Columna> columnasSeleccionadas;
    private double fitness;

    public Lobo(String querys, List<Columna> columnasSeleccionadas) {
        this.querys = querys;
        for(Columna col : columnas){
            col.setProbabilidadEleccion();
        }
        this.columnas = columnas;
    }

    public Lobo(Lobo otro) {
        this.querys = otro.querys;
        this.tiempoEjecucion = otro.tiempoEjecucion;
        this.espacio = otro.espacio;
        this.frecuenciaTotal = otro.frecuenciaTotal;
        this.fitness = otro.fitness;

        List<Columna> copyList = new ArrayList<>();
        for (Columna col : otro.getColumnas()){
            copyList.add(new Columna(col));
        }
        this.columnas = copyList;
        List<Columna> copyListSeleccionadas = new ArrayList<>();
        for (Columna col : otro.getColumnasSeleccionadas()){
            copyListSeleccionadas.add(new Columna(col));
        }
        this.columnasSeleccionadas = copyListSeleccionadas;
    }

    public String getQuerys() {
        return querys;
    }

    public void setQuerys(String querys) {
        this.querys = querys;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(List<Tabla> tablas) {
        String connectionUrl = "jdbc:mysql://localhost:3306/lobogris?serverTimezone=UTC", createIndexSyntax = "", dropIndexSyntax = "";
        double startTime, endTime = 0;
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "Lobogris22-1")) {
            Statement stmt = conn.createStatement();
            this.createIndex(tablas, createIndexSyntax, dropIndexSyntax);
            stmt.execute(createIndexSyntax);
            System.out.println("Índice creado");
            startTime = System.nanoTime();
            stmt.execute(this.querys);
            endTime = (System.nanoTime() - startTime)/1000000000;
            System.out.println("Querys ejecutados");
            stmt.execute(dropIndexSyntax);
            System.out.println("Índice dropeado");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        this.tiempoEjecucion = endTime;
    }

    public double getEspacio() {
        return espacio;
    }

    public void setEspacio(List<Tabla> tablas) {
        double sum = 0;
        double espTab;
        for (int i=0; i<tablas.size(); i++){
            espTab = 0;
            for(Columna col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    espTab += col.getCantidadBits();
                }
            }
            if(espTab != 0){
                espTab += 11; //Factor de overhead
                espTab *= tablas.get(i).getCantidadFilas();
                sum += espTab;
            }
        }
        sum = sum * 2 / 1000000000;
        this.espacio = sum;
    }

    public double getFrecuenciaTotal() {
        return frecuenciaTotal;
    }

    public void setFrecuenciaTotal() {
        double sum = 0;
        for(Columna col : this.columnasSeleccionadas){
            sum += col.getFrecuenciaUso();
        }
        this.frecuenciaTotal = sum;
    }

    public List<Columna> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnasSeleccionadas() {
        return columnasSeleccionadas;
    }

    public void setColumnasSeleccionadas() {
        List<Columna> colSelect = new ArrayList<>();
        for(Columna col : this.columnas){
            if(col.getProbabilidadEleccion() >= 0.75){
                colSelect.add(col);
            }
        }
        this.columnasSeleccionadas = colSelect;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(List<Tabla> tablas, double alpha, double beta, double eDisp) {
        this.setColumnasSeleccionadas();
        if (this.columnasSeleccionadas.size() == 0){
            this.fitness = 1000000000;
        }
        else {
            this.setEspacio(tablas);
            this.setFrecuenciaTotal();
            this.setTiempoEjecucion(tablas);

            double fit = this.tiempoEjecucion * (1 + alpha * 1 / this.frecuenciaTotal) * (1 + beta * this.espacio / eDisp);
            this.fitness = fit;
        }
    }

    public void createIndex(List<Tabla> tablas, String createIndexSyntax, String dropIndexSyntax) {
        String indexName, indexColumns;
        List<Columna> colSelect = new ArrayList<>();
        createIndexSyntax = "";
        dropIndexSyntax = "";
        for (int i=0; i<tablas.size(); i++){
            indexName = "IX_";
            indexColumns = "";
            for(Columna col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    indexName += col.getNombreColumna() + "_";
                    indexColumns += col.getNombreColumna() + ", ";
                }
            }
            indexName.substring(0, indexName.length() - 2);
            indexColumns.substring(0, indexColumns.length() - 3);
            createIndexSyntax += "CREATE INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + " (" + indexColumns + ");";
            dropIndexSyntax += "DROP INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + ";";
        }
    }

    public void updatePosicion (double[] probabilidades){
        for (int i=0; i<this.columnas.size(); i++) {
            this.columnas.get(i).setProbabilidadEleccion(probabilidades[i]);
        }
        this.frecuenciaTotal = 0;
        this.espacio = 0;
        this.tiempoEjecucion = 0;
        this.fitness = 0;
    }

    public Boolean esValido (List<Lobo> poblacion, double alpha, double beta, double eDisp, List<Tabla> tablas){
        Boolean flagValido = true;
        this.setColumnasSeleccionadas();

        //Cantidad de índices por tabla
        int[] indiceTablas = new int[tablas.size()];
        int count;
        for (int i=0; i<tablas.size(); i++){
            count = 0;
            for(Columna col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    count ++;
                    break;
                }
            }
            indiceTablas[i] = count;
        }
        for(int cantTabla : indiceTablas){
            if (cantTabla >= 5){
                flagValido = false;
                return flagValido;
            }
        }

        //Cantidad de columnas por índice
        int[] indiceColumnas = new int[tablas.size()];
        for (int i=0; i<tablas.size(); i++){
            count = 0;
            for(Columna col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    count ++;
                }
            }
            indiceColumnas[i] = count;
        }
        for(int cantColumnas : indiceColumnas){
            if (cantColumnas > 4){
                flagValido = false;
                return flagValido;
            }
        }

        List<Columna> colsSelAgente;
        //Columnas no repetidas
        for(Lobo agente : poblacion){
            colsSelAgente = agente.getColumnasSeleccionadas();
            if (colsSelAgente.containsAll(this.columnasSeleccionadas) || this.columnasSeleccionadas.containsAll(colsSelAgente)){
                flagValido = false;
                return flagValido;
            }
        }

        this.setFitness(tablas, alpha, beta, eDisp);

        //Espacio total
        if (this.espacio >= eDisp){
            flagValido = false;
            return flagValido;
        }

        return flagValido;
    }

    @Override
    public int compareTo(Lobo o) {
        return (int)(this.getFitness() - o.getFitness());
    }

}

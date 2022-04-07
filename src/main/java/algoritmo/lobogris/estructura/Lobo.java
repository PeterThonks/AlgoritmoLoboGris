package algoritmo.lobogris.estructura;

import algoritmo.lobogris.estructura.Columna;
import algoritmo.lobogris.estructura.Tabla;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Lobo {
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

    public String getQuerys() {
        return querys;
    }

    public void setQuerys(String querys) {
        this.querys = querys;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(String query, List<Tabla> tablas) {
        String connectionUrl = "jdbc:mysql://localhost:3306/lobogris?serverTimezone=UTC";
        double startTime, endTime = 0;
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "Lobogris22-1")) {
            Statement stmt = conn.createStatement();
            String indexStmt = this.createIndex(tablas);
            stmt.execute(indexStmt);
            System.out.println("Índice creado");
            startTime = System.nanoTime();
            stmt.execute(query);
            endTime = (System.nanoTime() - startTime)/1000000000;
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
                if(i == col.getTuplaTabla()){
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

    public void setFitness(String query, List<Tabla> tablas, int alpha, int beta, double eDisp) {
        this.setColumnasSeleccionadas();
        if (this.columnasSeleccionadas.size() == 0){
            this.fitness = 1000000000;
        }
        else {
            this.setEspacio(tablas);
            this.setFrecuenciaTotal();
            this.setTiempoEjecucion(query, tablas);

            double fit = this.tiempoEjecucion * (1 + alpha * 1 / this.frecuenciaTotal) * (1 + beta * this.espacio / eDisp);
            this.fitness = fit;
        }
    }

    public String createIndex(List<Tabla> tablas) {
        String indexSyntax = "", indexName, indexColumns;
        List<Columna> colSelect = new ArrayList<>();
        for (int i=0; i<tablas.size(); i++){
            indexName = "IX_";
            indexColumns = "";
            for(Columna col : this.columnasSeleccionadas){
                if(i == col.getTuplaTabla()){
                    indexName += col.getNombreColumna() + "_";
                    indexColumns += col.getNombreColumna() + ", ";
                }
            }
            indexName.substring(0, indexName.length() - 2);
            indexColumns.substring(0, indexColumns.length() - 3);
            indexSyntax += "CREATE INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + " (" + indexColumns + ");";
        }
        return indexSyntax;
    }

    public void updatePosicion (List<Columna> columnas){
        this.columnas = columnas;
        this.frecuenciaTotal = 0;
        this.espacio = 0;
        this.tiempoEjecucion = 0;
        this.fitness = 0;
    }
}

package algoritmo.lobogris.estructura;

import algoritmo.shared.util.Constante;
import org.javatuples.Pair;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Lobo implements Comparable<Lobo> {
    private String querys;
    private double tiempoEjecucion;
    private double espacio;
    private double frecuenciaTotal;
    private double penalidadTotal;
    private List<Columna> columnas;
    private List<Columna> columnasSeleccionadas;
    private double fitness;
    private String createIndexSyntax;

    public Lobo(String querys, List<Columna> columnasSeleccionadas) {
        if (querys == null || columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (columnasSeleccionadas.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);

        this.querys = querys;
        for(int i=0; i<columnasSeleccionadas.size(); i++){
            columnasSeleccionadas.get(i).setProbabilidadEleccion();
        }
        this.columnas = columnasSeleccionadas;
        this.fitness = 1000000000;
    }

    public Lobo(Lobo otro) {
        this.querys = otro.querys;
        this.tiempoEjecucion = otro.tiempoEjecucion;
        this.espacio = otro.espacio;
        this.frecuenciaTotal = otro.frecuenciaTotal;
        this.penalidadTotal = otro.penalidadTotal;
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

    public String getCreateIndexSyntax() {
        return createIndexSyntax;
    }

    public void setTiempoEjecucion(List<Tabla> tablas) {
        if (this.columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        String connectionUrl = "jdbc:mysql://localhost:3306/northwind?serverTimezone=UTC", createIndexSyntax = "", dropIndexSyntax = "", checkExistance = "";
        double startTime, endTime = 0;
        //Crear sintaxis de índice
        String indexName, indexColumns, queryOriginal = this.querys;
        for (int i=0; i<tablas.size(); i++){
            indexName = "IX_";
            indexColumns = "";
            for(Columna col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    indexName += col.getNombreColumna() + "_";
                    indexColumns += col.getNombreColumna() + ", ";
                }
            }
            if (indexName != "IX_"){
                indexName = indexName.substring(0, indexName.length() - 1);
                indexColumns = indexColumns.substring(0, indexColumns.length() - 2);
                createIndexSyntax += "CREATE INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + " (" + indexColumns + ");";
                dropIndexSyntax += "DROP INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + ";";
                checkExistance += "SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'northwind' " +
                        "AND TABLE_NAME='" + tablas.get(i).getNombreTabla().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "' " +
                        "AND INDEX_NAME='" + indexName + "';";
                //Forzar usar index en query
                String[] querys = this.querys.split(";");
                for (int j = 0; j<querys.length; j++){
                    String q = querys[j].trim().toLowerCase();
                    String tableName = tablas.get(i).getNombreTabla().toLowerCase();
                    if (q.contains(tableName)){
                        tableName = " " + tableName + " ";
                        int index = q.indexOf(tableName);
                        index += tableName.length();
                        q = new StringBuilder(q).insert(index, " use index (" + indexName + ") ").toString();
                        querys[j] = q;
                    }
                }
                this.querys = String.join(";", querys);
            }
        }
        this.createIndexSyntax = createIndexSyntax;
        //Correr querys
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "Lobogris22-1")) {
            Statement stmt = conn.createStatement();
            String[] checkIndex = checkExistance.split(";");
            String[] createIndex = createIndexSyntax.split(";");
            for (int i = 0; i<createIndex.length; i++){
                ResultSet rs = stmt.executeQuery(checkIndex[i].trim());
                rs.last();
                if (rs.getRow()==0){
                    stmt.execute(createIndex[i].trim());
//                    System.out.println("Índice creado");
                }
            }
            String[] query = this.querys.split(";");
            for (int i = 0; i<query.length; i++){
                //Solo se cuenta el tiempo de la tercera ejecución
//                System.out.println("Primera ejecución");
                stmt.execute(query[i]);
//                System.out.println("Segunda ejecución");
                stmt.execute(query[i]);
//                System.out.println("Tercera ejecución");
                startTime = System.nanoTime();
                stmt.execute(query[i]);
                endTime += (System.nanoTime() - startTime)/1000000000;
            }
            this.tiempoEjecucion = endTime;
            this.querys = queryOriginal;
//            System.out.println("Querys ejecutados");
            String[] dropIndex = dropIndexSyntax.split(";");
            for (int i = 0; i<dropIndex.length; i++){
                stmt.execute(dropIndex[i]);
//                System.out.println("Índice dropeado");
            }
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("needed in a foreign key constraint")){
                System.out.println(ex.getMessage());
            }
        }
    }

    public double getEspacio() {
        return espacio;
    }

    public void setEspacio(List<Tabla> tablas) {
        if (tablas == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (tablas.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);
        if (this.columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        double sum = 0;
        double espTab;
        for (int i=0; i<tablas.size(); i++){
            espTab = 0;
            for(Columna col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    espTab += col.getCantidadBytes();
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
        if (this.columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        double sum = 0;
        for(Columna col : this.columnasSeleccionadas){
            sum += col.getFrecuenciaUso();
        }
        this.frecuenciaTotal = sum;
    }

    public double getPenalidadTotal() {
        return penalidadTotal;
    }

    public void setPenalidadTotal() {
        if (this.columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        double sum = 1;
        for(Columna col : this.columnasSeleccionadas){
            sum *= col.getPenalidad();
        }
        this.penalidadTotal = sum;
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
        if (this.columnas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        List<Columna> colSelect = new ArrayList<>();
        for(Columna col : this.columnas){
            if(col.getProbabilidadEleccion() >= 0.7){
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
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);
        }
        else {
            this.setEspacio(tablas);
            this.setFrecuenciaTotal();
            this.setPenalidadTotal();
            this.setTiempoEjecucion(tablas);

            double factorFrecuencia, factorEspacio = (1 + beta * this.espacio / eDisp);
            if (this.frecuenciaTotal == 0)
                factorFrecuencia = 1;
            else
                factorFrecuencia = (1 + alpha * 1 / this.frecuenciaTotal);
            double fit = this.tiempoEjecucion * factorFrecuencia * factorEspacio * this.penalidadTotal;
            this.fitness = fit;
        }
    }

    public void updatePosicion (double[] probabilidades){
        if (probabilidades == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (probabilidades.length == 0)
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);
        if (probabilidades.length != this.columnas.size())
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        for (int i=0; i<this.columnas.size(); i++) {
            this.columnas.get(i).setProbabilidadEleccion(probabilidades[i]);
        }
        this.penalidadTotal = 1;
        this.frecuenciaTotal = 0;
        this.espacio = 0;
        this.tiempoEjecucion = 0;
        this.fitness = 1000000000;
    }

    public Boolean esValido (List<Lobo> poblacion, double eDisp, List<Tabla> tablas){
        Boolean flagValido = true;
        this.setColumnasSeleccionadas();

        if (this.columnasSeleccionadas.size() == 0){
            flagValido = false;
            return flagValido;
        }

        //Cantidad de índices por tabla
        int[] indiceTablas = new int[tablas.size()];
        int count;
        for (int i=0; i<tablas.size(); i++){
            count = 0;
            for(Columna col : this.columnasSeleccionadas){
                if(tablas.get(i).getNumeroTabla() == col.getTuplaTabla()){
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
                if(tablas.get(i).getNumeroTabla() == col.getTuplaTabla()){
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

        //Columnas no repetidas
        for(Lobo agente : poblacion){
            if (agente.mismasColumnasSeleccionadas(this) || this.mismasColumnasSeleccionadas(agente)){
//                System.out.println("Mismas columnas");
                flagValido = false;
                return flagValido;
            }
        }

        this.setEspacio(tablas);

        //Espacio total
        if (this.espacio >= eDisp){
            flagValido = false;
            return flagValido;
        }

        //Solo PK
        int total;
        for (int i=0; i<tablas.size(); i++){
            total = 0;
            for(Columna c : this.columnasSeleccionadas){
                if (tablas.get(i).getNumeroTabla() == c.getTuplaTabla() && c.isEsPkFk())
                    total++;
            }
            if (indiceColumnas[i] != 0 && total == indiceColumnas[i]){
//                System.out.println("Solo pk");
                flagValido = false;
                return flagValido;
            }
        }

        return flagValido;
    }

    public boolean mismasColumnasSeleccionadas (Lobo otro){
        if (this.columnasSeleccionadas.size() != otro.getColumnasSeleccionadas().size())
            return false;
        List<Pair<Integer, Integer>> cols = new ArrayList<>(), colsOtro = new ArrayList<>();
        for (int i=0; i<this.columnasSeleccionadas.size(); i++){
            cols.add(this.columnasSeleccionadas.get(i).getTupla());
            colsOtro.add(otro.getColumnasSeleccionadas().get(i).getTupla());
        }
        return cols.equals(colsOtro);
    }

    public boolean contieneColumnasSeleccionadas (Lobo otro){
        List<Pair<Integer, Integer>> cols = new ArrayList<>(), colsOtro = new ArrayList<>();
        for (int i=0; i<this.columnasSeleccionadas.size(); i++){
            cols.add(this.columnasSeleccionadas.get(i).getTupla());
        }
        for (int i=0; i<otro.getColumnasSeleccionadas().size(); i++){
            colsOtro.add(otro.getColumnasSeleccionadas().get(i).getTupla());
        }
        return cols.containsAll(colsOtro);
    }

    @Override
    public int compareTo(Lobo o) {
        if (this.getFitness() > o.getFitness()){
            return 1;
        }
        else if (this.getFitness() < o.getFitness()){
            return -1;
        }
        else {
            return 0;
        }
    }

}

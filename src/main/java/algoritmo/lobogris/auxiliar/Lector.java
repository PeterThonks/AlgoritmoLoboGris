package algoritmo.lobogris.auxiliar;

import algoritmo.lobogris.estructura.Columna;
import algoritmo.lobogris.estructura.Tabla;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lector {
    private String rutaArchivoTablas;
    private String rutaArchivoColumnas;
    private Path rutaArchivoQuery;
    private List<Tabla> tablas;
    private List<Columna> columnas;
    private List<Columna> columnasQuery;
    private String querys;

    public Lector(String rutaArchivoTablas, String rutaArchivoColumnas, Path rutaArchivoQuery) {
        this.rutaArchivoTablas = rutaArchivoTablas;
        this.rutaArchivoColumnas = rutaArchivoColumnas;
        this.rutaArchivoQuery = rutaArchivoQuery;
        this.tablas = new ArrayList<>();
        this.columnas = new ArrayList<>();
    }

    public List<Tabla> getTablas() {
        return tablas;
    }

    public List<Columna> getColumnas() {
        return columnas;
    }

    public List<Columna> getColumnasQuery() {
        return columnasQuery;
    }

    public String getQuerys() {
        return querys;
    }

    public void setQuerys() {
        String file = "";
        try{
            file = Files.readString(this.rutaArchivoQuery);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        this.querys = file.trim();
    }

    public void leerArchivos(){
        this.leerTablas();
        this.leerColumnas();
        this.setQuerys();
        this.setColumnasQuery();
//        System.out.println("Tablas");
//        for (Tabla t : this.tablas){
//            t.printTabla();
//        }
//        System.out.println("Columnas");
//        for (Columna c : this.columnas){
//            c.printColumna();
//        }
//        System.out.println("Query: ");
//        System.out.println(this.querys);
//        System.out.println("Columnas del query");
//        for (Columna c : this.columnasQuery){
//            c.printColumna();
//        }
    }

    public void leerTablas(){
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(this.rutaArchivoTablas));
            br.readLine(); //skipea la cabecera
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] tabla = line.split(splitBy);
                //use comma as separator
                Tabla nuevaT = new Tabla(tabla[0], Integer.parseInt(tabla[1]), Integer.parseInt(tabla[2]));
                this.tablas.add(nuevaT);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void leerColumnas(){
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(this.rutaArchivoColumnas));
            br.readLine(); //skipea la cabecera
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] columna = line.split(splitBy);
                //use comma as separator
                Columna nuevaC = new Columna(columna[0], Integer.parseInt(columna[1]), Integer.parseInt(columna[2]),
                        Double.parseDouble(columna[3]), Long.parseLong(columna[4]), Integer.parseInt(columna[5]) == 1 ? true : false);
                this.columnas.add(nuevaC);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setColumnasQuery(){
        List<Columna> columnasQuery = new ArrayList<>();
        String[] querys = this.querys.split(";");
        for (int i = 0; i<querys.length; i++){
//            System.out.println(querys[i]);
            querys[i] = querys[i].trim().toLowerCase();
            String fromStatement = querys[i].substring(querys[i].indexOf("from"));
            try {
                Statement statement = CCJSqlParserUtil.parse(querys[i]);
                Select selectStatement = (Select) statement;
                TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
                for (String tableName : tableList) {
//                    System.out.println(tableName);
                    Tabla tb = this.tablas.stream()
                            .filter(tabla -> tableName.equals(tabla.getNombreTabla().toLowerCase()))
                            .findAny()
                            .orElse(null);
                    int numeroTabla = tb.getNumeroTabla();
                    List<Columna> columnasAux = this.columnas.stream()
                            .filter(columna -> columna.getTuplaTabla() == numeroTabla)
                            .collect(Collectors.toList());
                    for (Columna col : columnasAux){
                        String nombreCol = tableName + "." + col.getNombreColumna().toLowerCase();
                        if (querys[i].contains(nombreCol)){
//                            System.out.println(nombreCol);
                            Columna newCol = new Columna(col);
                            if (fromStatement.contains(nombreCol))
                                newCol.setPenalidad(1f);
                            else
                                newCol.setPenalidad(12f);
                            columnasQuery.add(newCol);
                        }
                    }

                }
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
        }
        this.columnasQuery = columnasQuery;
    }
}

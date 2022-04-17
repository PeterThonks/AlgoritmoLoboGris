package algoritmo.lobogris.estructura;

import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.shared.util.Constante;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoboTest {
    private Lobo l;
    private List<Columna> cols;

    @Test
    public void existenciaLobo(){
        cols = new ArrayList<>();
        cols.add(new Columna("idAlumno", 1, 1, 0.04, 4, true));
        cols.add(new Columna("nombre", 1, 2, 0.05, 150, false));
        cols.add(new Columna("apellido", 1, 3, 0.1, 150, false));
        cols.add(new Columna("codigoPUCP", 1, 4, 0.02, 4, false));
        cols.add(new Columna("idAlumno", 7, 2, 0.025, 4, true));
        cols.add(new Columna("nota", 7, 5, 0.05, 8, false));
        l = new Lobo("SELECT \n" +
                "    Products.ProductID,\n" +
                "    Products.ProductName \n" +
                "FROM Products \n" +
                "WHERE Products.Discontinued=0;", cols);
        assertEquals(1000000000, l.getFitness());
        assertEquals(0, l.getEspacio());
        assertEquals(0, l.getFrecuenciaTotal());
        assertEquals(0.5, l.getFrecuenciaTotal(), 0.5);
        assertEquals(0, l.getPenalidadTotal());
        assertEquals(0, l.getTiempoEjecucion());
        assertEquals(null, l.getCreateIndexSyntax());
        assertEquals(cols, l.getColumnas());
        assertEquals(null, l.getColumnasSeleccionadas());
    }

    @Test
    public void existenciaConformidadLobo(){
        l = new Lobo("SELECT \n" +
                "    Products.ProductID,\n" +
                "    Products.ProductName \n" +
                "FROM Products \n" +
                "WHERE Products.Discontinued=0;", null);
    }

    @Test
    public void existenciaMúltiplesEntradasEspacio(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        l.setEspacio(lector.getTablas());
        System.out.println(l.getEspacio());
    }

    @Test
    public void existenciaConformidadEspacio(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        l.setEspacio(lector.getTablas());
    }

    @Test
    public void existenciaMúltiplesEntradasFrecuencia(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        l.setFrecuenciaTotal();
        System.out.println(l.getFrecuenciaTotal());
    }

    @Test
    public void existenciaConformidadFrecuencia(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        l.setFrecuenciaTotal();
    }

    @Test
    public void existenciaMúltiplesEntradasPenalidad(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        l.setPenalidadTotal();
        System.out.println(l.getPenalidadTotal());
    }

    @Test
    public void existenciaConformidadPenalidad(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        l.setPenalidadTotal();
    }

    @Test
    public void existenciaMúltiplesEntradasTiempo(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        l.setTiempoEjecucion(lector.getTablas());
        System.out.println(l.getTiempoEjecucion());
    }

    @Test
    public void existenciaConformidadTiempo(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        l.setTiempoEjecucion(lector.getTablas());
    }

    @Test
    public void existenciaSeleccionarColumnas(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        l.setColumnasSeleccionadas();
        for (Columna c : l.getColumnasSeleccionadas()){
            c.printColumna();
        }
    }

    @Test
    public void existenciaMúltiplesEntradasFitness(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        l.setFitness(lector.getTablas(),0.5, 0.5, 1000);
        System.out.println(l.getFitness());
    }

    @Test
    public void existenciaConformidadFitness(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        l.setFitness(lector.getTablas(),0.5, 0.5, 1000);
    }

    @Test
    public void existenciaMúltiplesEntradasUpdatePosicion(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        double[] arr = new double[]{0.1, 0.05, 0.8, 0.2, 0.6, 0.35, 0.84, 0.64, 0.47, 0.92, 0.64, 0.57, 0.94};
        l.updatePosicion(arr);
    }

    @Test
    public void existenciaConformidadUpdatePosicion(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        List<Lobo> p = new ArrayList<>();
        do {
            l = new Lobo(lector.getQuerys(), lector.getColumnasQuery());
        } while (l.esValido(p, 1000, lector.getTablas()));
        double[] arr = new double[]{0.1, 0.05, 0.8, 0.2, 0.6, 0.35, 0.84, 0.64, 0.47, 0.92, 0.64, 0.57};
        l.updatePosicion(arr);
    }
}

package algoritmo.lobogris.principal;

import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.lobogris.estructura.Columna;
import algoritmo.lobogris.estructura.Lobo;
import algoritmo.shared.util.Constante;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PoblacionTest {
    private Poblacion p;

    @Test
    public void existenciaPoblacion(){
        p = new Poblacion(5);
        assertEquals(5, p.getTamanoPoblacion());
    }

    @Test
    public void existenciaConformidadPoblacion(){
        p = new Poblacion(-1);
    }

    @Test
    public void existenciaMúltiplesEntradasPoblacionInicial(){
        p = new Poblacion(5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacion(lector, 10000, 0.75f);
    }

    @Test
    public void existenciaConformidadPoblacionInicial(){
        p = new Poblacion(-1);
        p.crearPoblacion(null, 10000, 0.75f);
    }

    @Test
    public void existenciaMúltiplesEntradasSeleccionarTresMejoresSoluciones(){
        p = new Poblacion(5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacion(lector, 10000, 0.75f);
        p.seleccionarTresMejoresSoluciones(lector, 0.5, 0.5, 10000);
    }

    @Test
    public void existenciaConformidadSeleccionarTresMejoresSoluciones(){
        p = new Poblacion(5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacion(lector, 10000, 0.75f);
        p.seleccionarTresMejoresSoluciones(lector, 0.5, 0.5, -10000);
    }

    @Test
    public void existenciaMúltiplesEntradasActualizarPosicion(){
        p = new Poblacion(5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacion(lector, 10000, 0.75f);
        p.seleccionarTresMejoresSoluciones(lector, 0.5, 0.5, 10000);
        p.actualizarPosicion(1.6, lector, 0.5, 0.5, 10000, 0.75f);
    }

    @Test
    public void existenciaConformidadActualizarPosicion(){
        p = new Poblacion(5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacion(lector, 10000, 0.75f);
        p.seleccionarTresMejoresSoluciones(lector, 0.5, 0.5, 10000);
        p.actualizarPosicion(-1.6, lector, 0.5, 0.5, 10000, 0.75f);
    }
}

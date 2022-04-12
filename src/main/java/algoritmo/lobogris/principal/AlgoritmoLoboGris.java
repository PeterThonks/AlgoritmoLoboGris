package algoritmo.lobogris.principal;


import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.shared.util.Constante;

import java.nio.file.Path;

public class AlgoritmoLoboGris {
    public static void main(String[] args) {
        String filenameTablas = "tablas_mysql.csv", filenameColumnas = "columnas_mysql.csv", filenameQuery = "Query.sql";

        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
                Constante.PATH_INPUT_CSV + filenameColumnas,
                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
        lector.leerArchivos();

        int tamanoPoblacion = 20, t = 1, maxIter = 500;
        double alpha = 1, beta = 0.5, eDisp = 1000000, a, startTime, endTime;
        startTime = System.nanoTime();
        Poblacion poblacion = new Poblacion(tamanoPoblacion);
        poblacion.crearPoblacion(lector, eDisp);
        poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
        while (t <= maxIter){
            System.out.println("Iteración n°"+t);
            a = 2 * (1 - t/maxIter);
            poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp);
            poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
            t++;
        }
        poblacion.printMejorSolucion();
        endTime = (System.nanoTime() - startTime)/1000000000;
        System.out.println("Bueeeena");
        System.out.println("Tiempo total: " + endTime + " segundos.");

    }
}

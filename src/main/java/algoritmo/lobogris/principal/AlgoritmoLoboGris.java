package algoritmo.lobogris.principal;


import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.lobogris.estructura.Lobo;
import algoritmo.shared.util.Constante;

import java.nio.file.Path;

public class AlgoritmoLoboGris {
    public static void main(String[] args) {
        String filenameTablas = "tablas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameColumnas = "columnas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameQuery = "query_"+Constante.DATABASE_SELECTED+".sql";

        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
                Constante.PATH_INPUT_CSV + filenameColumnas,
                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
        lector.leerArchivos();

        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, maxIter = 500, sinMejora = 0;
        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a, startTime, endTime;
        Lobo alphaWolf;
        startTime = System.nanoTime();
        Poblacion poblacion = new Poblacion(tamanoPoblacion);
        poblacion.crearPoblacion(lector, eDisp);
        poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
        while (t <= maxIter){
            if (t == maxIter){
                System.out.println("maxIter");
            }
            System.out.println("Iteración n°"+t);
            System.out.println(sinMejora);
            if (sinMejora == Math.max(30, (int) (maxIter*0.3))){
                break;
            }
            a = 2 * (1 - t/maxIter);
            alphaWolf = new Lobo(poblacion.getAlphaWolf());
            poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp);
            poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
            if (alphaWolf.mismasColumnasSeleccionadas(poblacion.getAlphaWolf())){
                sinMejora++;
            }
            else {
                sinMejora = 0;
            }
            t++;
        }
        poblacion.printMejorSolucion();
        endTime = (System.nanoTime() - startTime)/1000000000;
        System.out.println("Bueeeena");
        System.out.println("Cantidad de iteraciones: " + t);
        System.out.println("Tiempo total: " + endTime + " segundos.");
    }
}

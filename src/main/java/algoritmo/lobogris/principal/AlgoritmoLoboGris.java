package algoritmo.lobogris.principal;


import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.lobogris.estructura.Lobo;
import algoritmo.shared.util.Constante;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

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
        float porcentajeAceptacion = 0.85f;
        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a;
        long startTime, endTime;
        Lobo alphaWolf;
        startTime = System.nanoTime();
        Poblacion poblacion = new Poblacion(tamanoPoblacion);
        poblacion.crearPoblacion(lector, eDisp, porcentajeAceptacion);
        poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
        while (t < maxIter){
            System.out.println("Iteración n° "+t);
            System.out.println("Sin mejora: "+sinMejora);
            if (sinMejora == 100)
                break;
            a = 2 * (1 - t/maxIter);
            alphaWolf = new Lobo(poblacion.getAlphaWolf());
            poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp, porcentajeAceptacion);
            poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
            if (alphaWolf.mismasColumnasSeleccionadas(poblacion.getAlphaWolf()))
                sinMejora++;
            else
                sinMejora = 0;
            t++;
        }
        poblacion.printMejorSolucion();
        endTime = (System.nanoTime() - startTime);
        System.out.println("Cantidad de iteraciones: " + t);
        System.out.println("Tiempo total: " + TimeUnit.MINUTES.convert(endTime, TimeUnit.NANOSECONDS) + " minutos.");
    }

    public void experimentacionNumeroIteraciones() {
        String filenameTablas = "tablas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameColumnas = "columnas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameQuery = "query_"+Constante.DATABASE_SELECTED+".sql";

        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
                Constante.PATH_INPUT_CSV + filenameColumnas,
                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
        lector.leerArchivos();

        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, cantPruebas = 43;
        float porcentajeAceptacion = 0.75f;
        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a;
        int[] calibracionMaxIter = new int[]{500};

        for (int j=0; j<calibracionMaxIter.length; j++){
            for (int i=0; i<cantPruebas; i++){
                System.out.println("Vuelta n° "+i);
                Poblacion poblacion = new Poblacion(tamanoPoblacion);
                poblacion.crearPoblacion(lector, eDisp, porcentajeAceptacion);
                poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                t = 1;
                while (t < calibracionMaxIter[j]){
                    System.out.println("Iteración n° "+t);
                    a = 2 * (1 - t/calibracionMaxIter[j]);
                    poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp, porcentajeAceptacion);
                    poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                }
                try {
                    FileWriter pw = new FileWriter(Constante.PATH_OUTPUT_CSV + "calibracionMaxIter" + calibracionMaxIter[j] + ".csv",true);
                    pw.append(Double.toString(poblacion.getAlphaWolf().getFitness()));
                    pw.append(",");
                    pw.append(Double.toString(poblacion.getBetaWolf().getFitness()));
                    pw.append(",");
                    pw.append(Double.toString(poblacion.getGammaWolf().getFitness()));
                    pw.append("\n");
                    pw.flush();
                    pw.close();
                }
                catch (IOException e){

                }
            }
        }
    }

    public void experimentacionNumeroSinMejora() {
        String filenameTablas = "tablas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameColumnas = "columnas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameQuery = "query_"+Constante.DATABASE_SELECTED+".sql";

        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
                Constante.PATH_INPUT_CSV + filenameColumnas,
                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
        lector.leerArchivos();

        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, maxIter = 500, sinMejora = 0, cantPruebas = 13;
        float porcentajeAceptacion = 0.75f;
        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a;
        Lobo alphaWolf;
        int[] calibracionSinMejora = new int[]{250};

        for (int j=0; j<calibracionSinMejora.length; j++){
            for (int i=0; i<cantPruebas; i++){
                System.out.println("Vuelta n° "+i);
                Poblacion poblacion = new Poblacion(tamanoPoblacion);
                poblacion.crearPoblacion(lector, eDisp, porcentajeAceptacion);
                poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                t = 1;
                sinMejora = 0;
                while (t < maxIter){
                    System.out.println("Iteración n° "+t);
                    System.out.println("Sin mejora: "+sinMejora);
                    if (sinMejora == calibracionSinMejora[j])
                        break;
                    a = 2 * (1 - t/maxIter);
                    alphaWolf = new Lobo(poblacion.getAlphaWolf());
                    poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp, porcentajeAceptacion);
                    poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                    if (alphaWolf.mismasColumnasSeleccionadas(poblacion.getAlphaWolf()))
                        sinMejora++;
                    else
                        sinMejora = 0;
                    t++;
                }
                try {
                    FileWriter pw = new FileWriter(Constante.PATH_OUTPUT_CSV + "calibracionSinMejora" + calibracionSinMejora[j] + ".csv",true);
                    pw.append(Double.toString(poblacion.getAlphaWolf().getFitness()));
                    pw.append(",");
                    pw.append(Double.toString(poblacion.getBetaWolf().getFitness()));
                    pw.append(",");
                    pw.append(Double.toString(poblacion.getGammaWolf().getFitness()));
                    pw.append("\n");
                    pw.flush();
                    pw.close();
                }
                catch (IOException e){

                }
            }
        }
    }

    public void experimentacionPorcentajeAceptacion() {
        String filenameTablas = "tablas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameColumnas = "columnas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameQuery = "query_"+Constante.DATABASE_SELECTED+".sql";

        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
                Constante.PATH_INPUT_CSV + filenameColumnas,
                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
        lector.leerArchivos();

        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, maxIter = 500, sinMejora, cantPruebas = 50;
        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a;
        Lobo alphaWolf;
        float[] calibracionPorcentajeAceptacion = new float[]{0.85f};

        for (int j=0; j<calibracionPorcentajeAceptacion.length; j++){
            for (int i=0; i<cantPruebas; i++){
                System.out.println("Vuelta n° "+i);
                Poblacion poblacion = new Poblacion(tamanoPoblacion);
                poblacion.crearPoblacion(lector, eDisp, calibracionPorcentajeAceptacion[j]);
                poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                t = 1;
                sinMejora = 0;
                while (t < maxIter){
                    System.out.println("Iteración n° "+t);
                    System.out.println("Sin mejora: "+sinMejora);
                    if (sinMejora == 200)
                        break;
                    a = 2 * (1 - t/maxIter);
                    alphaWolf = new Lobo(poblacion.getAlphaWolf());
                    poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp, calibracionPorcentajeAceptacion[j]);
                    poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                    if (alphaWolf.mismasColumnasSeleccionadas(poblacion.getAlphaWolf()))
                        sinMejora++;
                    else
                        sinMejora = 0;
                    t++;
                }
                try {
                    FileWriter pw = new FileWriter(Constante.PATH_OUTPUT_CSV + "calibracionPorcentajeAceptacion" + calibracionPorcentajeAceptacion[j] + ".csv",true);
                    pw.append(Double.toString(poblacion.getAlphaWolf().getFitness()));
                    pw.append(",");
                    pw.append(Double.toString(poblacion.getBetaWolf().getFitness()));
                    pw.append(",");
                    pw.append(Double.toString(poblacion.getGammaWolf().getFitness()));
                    pw.append("\n");
                    pw.flush();
                    pw.close();
                }
                catch (IOException e){

                }
            }
        }
    }
}

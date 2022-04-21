package algoritmo.lobogris.principal;


import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.lobogris.estructura.Lobo;
import algoritmo.shared.util.Constante;

import java.io.BufferedWriter;
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

        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, maxIter = 2, sinMejora = 0, cantPruebas = 50;
        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a;
        long startTime, endTime;
        Lobo alphaWolf;
        double[] fitnessAlpha = new double[cantPruebas], fitnessBeta = new double[cantPruebas], fitnessGamma = new double[cantPruebas];
        int[] calibracionMaxIter = new int[]{400};
        for (int j=0; j<calibracionMaxIter.length; j++){
            for (int i=0; i<cantPruebas; i++){
                System.out.println("Vuelta n° "+i);
                startTime = System.nanoTime();
                Poblacion poblacion = new Poblacion(tamanoPoblacion);
                poblacion.crearPoblacion(lector, eDisp);
                poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                t = 1;
                while (t < calibracionMaxIter[j]){
                    System.out.println("Iteración n° "+t);
//                System.out.println("Sin mejora: "+sinMejora);
//                if (sinMejora == Math.max(30, (int) (maxIter*0.3)))
//                    break;
                    a = 2 * (1 - t/calibracionMaxIter[j]);
                    alphaWolf = new Lobo(poblacion.getAlphaWolf());
                    poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp);
                    poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
                    if (alphaWolf.mismasColumnasSeleccionadas(poblacion.getAlphaWolf()))
                        sinMejora++;
                    else
                        sinMejora = 0;
                    t++;
                }
//            poblacion.printMejorSolucion();
                fitnessAlpha[i] = poblacion.getAlphaWolf().getFitness();
                fitnessBeta[i] = poblacion.getBetaWolf().getFitness();
                fitnessGamma[i] = poblacion.getGammaWolf().getFitness();
                endTime = (System.nanoTime() - startTime);
//            System.out.println("Cantidad de iteraciones: " + t);
//            System.out.println("Tiempo total: " + TimeUnit.MINUTES.convert(endTime, TimeUnit.NANOSECONDS) + " minutos.");
            }
            try {
                BufferedWriter br = new BufferedWriter(new FileWriter(Constante.PATH_OUTPUT_CSV + "calibracionMaxIter"+calibracionMaxIter[j]+".csv"));
                StringBuilder sb = new StringBuilder();

                // Append strings from array
                for (int i=0; i<cantPruebas; i++){
                    sb.append(fitnessAlpha[i]);
                    sb.append(",");
                    sb.append(fitnessBeta[i]);
                    sb.append(",");
                    sb.append(fitnessGamma[i]);
                    sb.append("\n");
                }

                br.write(sb.toString());
                br.close();
            }
            catch (IOException e){

            }
        }


//        System.out.println("AlphaWolf");
//        for (int i=0; i<cantPruebas; i++){
//            System.out.println(fitnessAlpha[i]);
//        }
//        System.out.println("BetaWolf");
//        for (int i=0; i<cantPruebas; i++){
//            System.out.println(fitnessBeta[i]);
//        }
//        System.out.println("GammaWolf");
//        for (int i=0; i<cantPruebas; i++){
//            System.out.println(fitnessGamma[i]);
//        }
//        String filenameTablas = "tablas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
//                filenameColumnas = "columnas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
//                filenameQuery = "query_"+Constante.DATABASE_SELECTED+".sql";
//
//        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
//                Constante.PATH_INPUT_CSV + filenameColumnas,
//                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
//        lector.leerArchivos();
//
//        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, maxIter = 10, sinMejora = 0;
//        double alpha = 0.5, beta = 0.5, eDisp = 1000000, a;
//        long startTime, endTime;
//        Lobo alphaWolf;
//        startTime = System.nanoTime();
//        Poblacion poblacion = new Poblacion(tamanoPoblacion);
//        poblacion.crearPoblacion(lector, eDisp);
//        poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
//        while (t < maxIter){
//            System.out.println("Iteración n° "+t);
//            System.out.println("Sin mejora: "+sinMejora);
////            if (sinMejora == Math.max(30, (int) (maxIter*0.3)))
////                break;
//            a = 2 * (1 - t/maxIter);
//            alphaWolf = new Lobo(poblacion.getAlphaWolf());
//            poblacion.actualizarPosicion(a, lector, alpha, beta, eDisp);
//            poblacion.seleccionarTresMejoresSoluciones(lector, alpha, beta, eDisp);
//            if (alphaWolf.mismasColumnasSeleccionadas(poblacion.getAlphaWolf()))
//                sinMejora++;
//            else
//                sinMejora = 0;
//            t++;
//        }
//        poblacion.printMejorSolucion();
//        endTime = (System.nanoTime() - startTime);
//        System.out.println("Cantidad de iteraciones: " + t);
//        System.out.println("Tiempo total: " + TimeUnit.MINUTES.convert(endTime, TimeUnit.NANOSECONDS) + " minutos.");
    }
}

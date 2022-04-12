package algoritmo.lobogris.principal;

import algoritmo.lobogris.auxiliar.Lector;
import algoritmo.lobogris.estructura.Columna;
import algoritmo.lobogris.estructura.Lobo;
import algoritmo.lobogris.estructura.Tabla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poblacion {
    private List<Lobo> poblacion;
    private int tamanoPoblacion;
    private Lobo alphaWolf;
    private Lobo betaWolf;
    private Lobo gammaWolf;

    public Poblacion(int tamanoPoblacion) {
        this.tamanoPoblacion = tamanoPoblacion;
    }

    public void crearPoblacion(Lector lector, double eDisp){
        List<Columna> columnasSel = lector.obtenerColumnas();
        int cont = 0;
        List<Lobo> p = new ArrayList<>();
        while (cont < this.tamanoPoblacion){
            Lobo nuevoLobo = new Lobo(lector.getQuerys(), columnasSel);
            if (nuevoLobo.esValido(p, eDisp, lector.getTablas())){
                p.add(new Lobo(nuevoLobo));
                cont++;
            }
        }
        this.poblacion = p;
    }

    public void seleccionarTresMejoresSoluciones(Lector lector, double alpha, double beta, double eDisp){
        for (int i = 0; i<this.poblacion.size(); i++){
            this.poblacion.get(i).setFitness(lector.getTablas(), alpha, beta, eDisp);
        }

        Collections.sort(this.poblacion);
        this.alphaWolf = this.poblacion.get(0);
        this.betaWolf = this.poblacion.get(1);
        this.gammaWolf = this.poblacion.get(2);
    }

    public void actualizarPosicion(double a, Lector lector, double alpha, double beta, double eDisp){
        int cantCols = this.alphaWolf.getColumnas().size();
        Boolean valido;
        Lobo nuevoLobo;
        for (int i = 0; i<this.poblacion.size(); i++){
            System.out.println("Lobo n°" + i);
            do {
                double A1 = a * (2 * Math.random() - 1), A2 = a * (2 * Math.random() - 1), A3 = a * (2 * Math.random() - 1);
                double C1 = 2 * Math.random(), C2 = 2 * Math.random(), C3 = 2 * Math.random();

                double[] X1 = new double[cantCols], X2 = new double[cantCols], X3 = new double[cantCols], XNuevo = new double[cantCols];
                for (int j = 0; j<cantCols; j++){
                    X1[j] = this.alphaWolf.getColumnas().get(j).getProbabilidadEleccion() -
                            A1 * Math.abs(C1 * this.alphaWolf.getColumnas().get(j).getProbabilidadEleccion() -
                                    this.poblacion.get(i).getColumnas().get(j).getProbabilidadEleccion());
                    X2[j] = this.betaWolf.getColumnas().get(j).getProbabilidadEleccion() -
                            A2 * Math.abs(C2 * this.betaWolf.getColumnas().get(j).getProbabilidadEleccion() -
                                    this.poblacion.get(i).getColumnas().get(j).getProbabilidadEleccion());
                    X3[j] = this.gammaWolf.getColumnas().get(j).getProbabilidadEleccion() -
                            A3 * Math.abs(C3 * this.gammaWolf.getColumnas().get(j).getProbabilidadEleccion() -
                                    this.poblacion.get(i).getColumnas().get(j).getProbabilidadEleccion());
                    X1[j] = Math.min(Math.max(X1[j], 0), 1);
                    X2[j] = Math.min(Math.max(X2[j], 0), 1);
                    X3[j] = Math.min(Math.max(X3[j], 0), 1);
                    XNuevo[j] += X1[j] + X2[j] + X3[j];
                }
                for (int j = 0; j<cantCols; j++){
                    XNuevo[j] = Math.min(Math.max(XNuevo[j]/3, 0), 1);
                }
                nuevoLobo = new Lobo(this.poblacion.get(i));
                nuevoLobo.updatePosicion(XNuevo);

                valido = nuevoLobo.esValido(this.poblacion, eDisp, lector.getTablas());
            } while (!valido);

            nuevoLobo.setFitness(lector.getTablas(), alpha, beta, eDisp);
            if (nuevoLobo.getFitness() < this.poblacion.get(i).getFitness() ){
                this.poblacion.set(i, nuevoLobo);
            }
        }
    }

    public void printMejorSolucion() {
        System.out.println("La mejor solución es:");
        System.out.println(this.alphaWolf.getCreateIndexSyntax());
    }

}

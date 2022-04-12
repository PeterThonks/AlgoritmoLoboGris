package algoritmo.lobogris.estructura;

public class Tabla {
    private String nombreTabla;
    private int numeroTabla;
    private int cantidadFilas;

    public Tabla(String nombreTabla, int numeroTabla, int cantidadFilas) {
        this.nombreTabla = nombreTabla;
        this.numeroTabla = numeroTabla;
        this.cantidadFilas = cantidadFilas;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public int getNumeroTabla() {
        return numeroTabla;
    }

    public void setNumeroTabla(int numeroTabla) {
        this.numeroTabla = numeroTabla;
    }

    public int getCantidadFilas() {
        return cantidadFilas;
    }

    public void setCantidadFilas(int cantidadFilas) {
        this.cantidadFilas = cantidadFilas;
    }

    public void printTabla(){
        System.out.println("Tabla " + this.nombreTabla + " n° " + this.numeroTabla + " con " + this.cantidadFilas + " filas.");
    }
}

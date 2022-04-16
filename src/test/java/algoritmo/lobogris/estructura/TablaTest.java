package algoritmo.lobogris.estructura;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TablaTest {
    private Tabla t;

    @Test
    public void existenciaTabla(){
        t = new Tabla("usuario", 5, 8000);
        assertEquals("usuario", t.getNombreTabla());
        assertEquals(5, t.getNumeroTabla());
        assertEquals(8000, t.getCantidadFilas());
    }

    @Test
    public void existenciaConformidadTabla(){
        t = new Tabla(null, -1, -8000);
    }
}

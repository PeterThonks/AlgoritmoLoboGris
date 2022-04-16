package algoritmo.lobogris.estructura;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColumnaTest {
    private Columna c;

    @Test
    public void existenciaTabla(){
        c = new Columna("apellido", 1, 3, 0.1, 150, true);
        assertEquals("apellido", c.getNombreColumna());
        assertEquals(new Pair<Integer, Integer>(1, 3), c.getTupla());
        assertEquals(0.1, c.getFrecuenciaUso());
        assertEquals(150, c.getCantidadBytes());
        assertEquals(true, c.isEsPkFk());
        assertEquals((1)/2.0, c.getProbabilidadEleccion(), (1)/2.0);
        assertEquals(1, c.getPenalidad());
    }

    @Test
    public void existenciaConformidadTabla(){
        c = new Columna(null, -1, -3, -0.1, -150, true);
    }
}

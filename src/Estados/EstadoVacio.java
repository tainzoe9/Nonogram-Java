package Estados;

import Nonogram.Celda;

public class EstadoVacio implements EstadoCelda {

    @Override
    public void marcar(Celda celda) {
        celda.setEstado(new EstadoVacio());
    }

    @Override
    public String ObtenerEstado() {
        return "Vacio";
    }
}

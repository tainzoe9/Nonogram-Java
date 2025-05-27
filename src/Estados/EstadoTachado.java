package Estados;

import Nonogram.Celda;

public class EstadoTachado implements EstadoCelda{

    @Override
    public void marcar(Celda celda) {
        celda.setEstado(new EstadoTachado());
    }

    @Override
    public String ObtenerEstado() {
        return "Tachado";
    }
}

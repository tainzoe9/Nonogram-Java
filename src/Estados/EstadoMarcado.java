package Estados;

import Nonogram.Celda;

public class EstadoMarcado implements EstadoCelda {

    @Override
    public void marcar(Celda celda) {
        celda.setEstado(new EstadoMarcado());
    }

    @Override
    public String ObtenerEstado() {
        return "Marcado";
    }

}

package Comandos;

import Estados.EstadoTachado;
import Nonogram.Celda;

public class ComandoTacharCelda implements Comando {
    private Celda celda;

    public void ejecutar() {
        celda.setEstado(new EstadoTachado());
    }

    public ComandoTacharCelda(Celda celda) {
        this.celda = celda;
    }

}

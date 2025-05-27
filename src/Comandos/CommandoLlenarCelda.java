package Comandos;
import Nonogram.Celda;
import Estados.EstadoMarcado;

public class CommandoLlenarCelda implements Comando {
   Celda celda;

    public CommandoLlenarCelda(Celda celda) {
         this.celda = celda;
    }

    public void ejecutar() {
        celda.setEstado(new EstadoMarcado());
    }

    public Celda getCelda() {
        return celda;
    }
}


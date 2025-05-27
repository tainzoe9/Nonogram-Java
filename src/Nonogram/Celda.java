package Nonogram;
import Estados.*;
public class Celda {
    private EstadoCelda estado;

    public Celda() {
        estado = new EstadoVacio();
    }

    public String obtenerEstado() {
        return estado.ObtenerEstado();
    }
    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public String toString(){
        switch (this.obtenerEstado()) {
           case "Vacio":
               return "□";
           case "Marcado":
                return "■";
            case "Tachado":
                return "☒";
            default:
                return "error";
        }
    }

}

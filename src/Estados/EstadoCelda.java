package Estados;
import Nonogram.Celda;

public interface EstadoCelda {
    public void marcar(Celda celda);
    public String ObtenerEstado();
}
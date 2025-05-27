package Fabricas;
import Nonogram.Celda;

public class FabricaNonogramMedio implements FabricaDeNonogram {
    @Override
    public Celda[][] crearNonogram() {
        return new Celda[10][10];
    }
}

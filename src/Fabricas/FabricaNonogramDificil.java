package Fabricas;
import Nonogram.Celda;

public class FabricaNonogramDificil implements FabricaDeNonogram {

    @Override
    public Celda[][] crearNonogram() {
        return new Celda[15][15];
    }
}

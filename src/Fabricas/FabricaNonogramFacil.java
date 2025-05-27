package Fabricas;

import Nonogram.Celda;

public class FabricaNonogramFacil implements   FabricaDeNonogram{

    @Override
    public Celda[][] crearNonogram() {
        return new Celda[5][5];
    }
}

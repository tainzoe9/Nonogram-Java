package Nonogram;
import Fabricas.FabricaDeNonogram;
import Comandos.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Jugador {
    private int vidas;
    private int numpistas;
    private Nonogram partida;
    private Main mainInterface;

    public Jugador(FabricaDeNonogram fab) {
        this.vidas = 3;
        this.numpistas = 3;
        this.partida = new Nonogram(fab);
        partida.inicializarTablero();
    }

    public void setMainInterface(Main mainInterface) {
        this.mainInterface = mainInterface;
    }

    //Metodos de acceso y modificacion de las vidas y pistas del jugador
    public void restarVida() {
        this.vidas--;
        if (mainInterface != null) {
            mainInterface.updateVidas();
        }
    }

    private void restarPista() {
        this.numpistas--;
    }

    public int getVidas() {
        return this.vidas;
    }

    private int getPistas() {
        return this.numpistas;
    }

    public Nonogram getPartida() {
        return this.partida;
    }

    //Metodo para marcar una celda usando Command
    public void marcar (Comando c) {
            c.ejecutar();
    }

    /**
     * Metodo que permite al jugador realizar un turno, llenando o tachando una celda o pidiendo una pista
     * @return true si el jugador gana, false si aun no ha ganado
     */
    public boolean turno() {
        if (partida.tableroCompleto()) {
            System.out.println("¡Ganaste! Completaste el tablero.");
            return true;
        }
        partida.mostrarTablero();
        System.out.println("Vidas: " + getVidas());
        System.out.println("Pistas: " + getPistas());
        System.out.println("Ingresa: \n1 para rellenar una celda\n2 para tacharla\n3 para obtener una pista");
        Scanner en = new Scanner(System.in);

        try {
            int opcion = en.nextInt();
            switch (opcion) {
                case 1:
                    int[] cord1 = obtenerCoordenadas("Ingresa las coordenadas de la celda que quieres llenar:");
                    Celda cel1 = partida.getCeldaJuego(cord1[0], cord1[1]);
                    if (!cel1.getEstado().ObtenerEstado().equals("Vacio")) {
                        System.out.println("La celda ya está marcada. Intenta con otra celda.");
                        break;
                    }
                    if (partida.getCeldaResuelta(cord1[0], cord1[1]).getEstado().ObtenerEstado().equals("Marcado")) {
                        CommandoLlenarCelda comandoLlenar = new CommandoLlenarCelda(cel1);
                        marcar(comandoLlenar);
                        System.out.println("Celda llenada correctamente.");
                        partida.incrementarCeldasCompletadas();
                    } else {
                        System.out.println("Incorrecto, perdiste una vida.");
                        restarVida();
                    }
                    break;

                case 2:
                    int[] cord2 = obtenerCoordenadas("Ingresa las coordenadas de la celda que quieres tachar:");
                    Celda cel2 = partida.getCeldaJuego(cord2[0], cord2[1]);
                    if (!cel2.getEstado().ObtenerEstado().equals("Vacio")) {
                        System.out.println("La celda ya está marcada. Intenta con otra celda.");
                        break;
                    }
                    if (partida.getCeldaResuelta(cord2[0], cord2[1]).getEstado().ObtenerEstado().equals("Vacio")) {
                        ComandoTacharCelda comandoTachar = new ComandoTacharCelda(cel2);
                        marcar(comandoTachar);
                        System.out.println("Celda tachada correctamente.");
                        partida.incrementarCeldasCompletadas();
                    } else {
                        System.out.println("Incorrecto, perdiste una vida.");
                        restarVida();
                    }
                    break;
                case 3:
                    pista();
                    break;
                default:
                    System.out.println("Opción inválida. Intenta nuevamente.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingresa un número válido.");
            en.nextLine();
        }
        return false;
    }



    /**
     * Metodo que permite al jugador obtener las coordenadas de una celda
     * @param mensaje mensaje que se le muestra al jugador
     * @return arreglo de enteros con las coordenadas
     */
    private int[] obtenerCoordenadas(String mensaje) {
        Scanner en = new Scanner(System.in);
        int[] coordenadas = new int[2];

        while (true) {
            try {
                System.out.println(mensaje);
                int x = en.nextInt() - 1;
                int y = en.nextInt() - 1;

                if (x >= 0 && x < partida.getTableroJuego().length &&
                        y >= 0 && y < partida.getTableroJuego()[0].length) {
                    coordenadas[0] = x;
                    coordenadas[1] = y;
                    return coordenadas;
                } else {
                    System.out.println("Coordenadas fuera de rango. Intenta nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingresa valores numéricos válidos para las coordenadas.");
                en.nextLine();
            }
        }
    }


    /**
     * Método que permite al jugador obtener una pista, le da la informacion y marca la pista en el tablero
     */
    private void pista() {
        if (numpistas > 0) {
            int[] coordenadas = obtenerCoordenadas("Ingresa las coordenadas de la celda que quieres revelar (fila columna):");
            int x = coordenadas[0];
            int y = coordenadas[1];
            if(partida.getCeldaResuelta(x, y).getEstado().ObtenerEstado().equals("Marcado")) {
                System.out.println("La celda va marcada.");
                CommandoLlenarCelda comandoLlenar = new CommandoLlenarCelda(partida.getCeldaJuego(x, y)); // LLena por ti la celda con la pista
                marcar(comandoLlenar);
                partida.incrementarCeldasCompletadas();
            } else {
                System.out.println("La celda va tachada.");
                ComandoTacharCelda comandoTachar = new ComandoTacharCelda(partida.getCeldaJuego(x, y));
                marcar(comandoTachar);
                partida.incrementarCeldasCompletadas();
            }
            restarPista();
        } else {
            System.out.println("No tienes pistas disponibles.");
        }
    }



}

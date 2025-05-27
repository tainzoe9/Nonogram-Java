package Nonogram;
import Fabricas.*;

import java.util.Scanner;

public class MainTerminal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        while (opcion!= 2){
            System.out.println("Bienvenido a Nonogram");
            System.out.println("1. Jugar");
            System.out.println("2. Salir");
            try {
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        System.out.println("Selecciona la dificultad:");
                        System.out.println("1. Facil");
                        System.out.println("2. Medio");
                        System.out.println("3. Dificil");
                        System.out.println("Bonus");
                        System.out.println("4.Principiante");
                        System.out.println("5.Intermedio");
                        int dificultad = scanner.nextInt();
                        FabricaDeNonogram fabrica = null;
                        switch (dificultad) {
                            case 1, 4:
                                fabrica = new FabricaNonogramFacil();
                                break;
                            case 2, 5:
                                fabrica = new FabricaNonogramMedio();
                                break;
                            case 3:
                                fabrica = new FabricaNonogramDificil();
                                break;
                            default:
                                System.out.println("Opcion no valida");
                                break;
                        }
                        Jugador jugador = new Jugador(fabrica);
                        jugador.getPartida().crearJuego();
                        if(dificultad == 4 || dificultad == 5){
                            jugador.getPartida().generarPistasExtra();
                        }
                        boolean ganador = false;
                        while (jugador.getVidas() > 0 && !ganador) {
                           ganador = jugador.turno();
                        }
                        if(jugador.getVidas() == 0){
                            System.out.println("Perdiste");
                        }else{
                            System.out.println("Ganaste");
                        }
                        break;
                    case 2:
                        System.out.println("Gracias por jugar");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Opcion no valida");
                scanner.next();
            }
        }
    }
}
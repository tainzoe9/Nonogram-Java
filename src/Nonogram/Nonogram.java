package Nonogram;
import Estados.EstadoMarcado;
import Estados.EstadoTachado;
import Fabricas.*;
import java.util.Random;

public class Nonogram {
    private Celda[][] respuesta;
    private Celda[][] juego;
    private int[][] pistasfila;
    private int[][] pistascolumna;
    private int celdasCompletadas;



   public Nonogram(FabricaDeNonogram fab){
       respuesta = fab.crearNonogram();
       juego = fab.crearNonogram();
       celdasCompletadas = 0;
   }

    public String getPistasFila(int fila) {
        StringBuilder pistas = new StringBuilder();
        for (int pista : pistasfila[fila]) {
            pistas.append(pista).append(" ");
        }
        return pistas.toString().trim();
    }

    public String getPistasColumna(int col) {
        StringBuilder pistas = new StringBuilder();
        for (int pista : pistascolumna[col]) {
            pistas.append(pista).append(" ");
        }
        return pistas.toString().trim();
    }

    //Metodoa para obtener una celda
   public Celda getCeldaResuelta(int x, int y){
       return respuesta[x][y];
   }

   public Celda getCeldaJuego(int x, int y){
       return juego[x][y];
   }

    public Celda[][] getTableroJuego(){
         return juego;
    }

    //Metodo para incrementar el contador de celdas completadas
    public void incrementarCeldasCompletadas() {
        celdasCompletadas++;
    }

    // Método para verificar si el tablero está completo
    public boolean tableroCompleto() {
        return celdasCompletadas == calcularTotalCeldas();
    }

    // Método para calcular el total de celdas que deben ser marcadas para ganar
    private int calcularTotalCeldas() {
        return juego.length * juego[0].length;
    }

   /*
    * Crea un juego de Nonogram con sus pistas
    */
   public void crearJuego(){
         int n = respuesta.length;
         pistasfila = new int[n][];
         pistascolumna = new int[n][];
            Random al = new Random();
            for(int i = 0; i < n; i++){
               //Elejimos con probabilidad 0.5 si generamos pistas inmediatamente resolubles o no
                if(al.nextBoolean()){
                     pistasfila[i] = generarPistasInmediatas(n);
                }else{
                    pistasfila[i] = generarPistaNormal(n);
                }
            }
            inicializarTablero();
            llenarTableroDesdePistasFila();
            generaPistasColumna(n);

   }

   /*
    * Genera las pistas inmediatamente resolubles de un tablero de Nonogram
    * @param n tamaño del tablero
    * @return un arreglo de enteros con las pistas
    */
    private int[] generarPistasInmediatas(int n){
        Random al = new Random();
        int numbloques = al.nextInt((int) Math.ceil(n/2))+1;
        int[] bloques = new int[numbloques];
        int sum = n-numbloques+1;
        int sumaTotal = 0;
        for(int i = 0; i < numbloques-1; i++){
            int maxTamañoBloque = sum - (numbloques - i - 1);
            bloques[i] = al.nextInt(maxTamañoBloque) + 1;
            sumaTotal += bloques[i];
            sum -= bloques[i];
        }
        bloques[numbloques-1] = sum;
        return bloques;
    }

    /*
     * Genera las pistas de un tablero de Nonogram aleatoriamete, sin que sean inmeditatamente resolubles
     * @param n tamaño del tablero
     * @return un arreglo de enteros con las pistas
     */
    private int[] generarPistaNormal(int n){
        Random al = new Random();
        int numbloques = al.nextInt((int) Math.ceil(n/2))+1;
        int[] bloques = new int[numbloques];
        int sum = n-numbloques+1;
        int sumaTotal = 0;
        for(int i = 0; i < numbloques; i++){
            int maxTamañoBloque = sum - (numbloques - i - 1);
            bloques[i] = al.nextInt(maxTamañoBloque) + 1;
            sumaTotal += bloques[i];
            sum -= bloques[i];
        }
        return bloques;
    }

    /*
     * Inicializa el tablero del Nonogram
     * @param n tamaño del tablero
     */
    public void inicializarTablero() {
        int n = respuesta.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                respuesta[i][j] = new Celda();
                juego[i][j] = new Celda();
            }
        }
    }


    /*
        * Llena el tablero con las pistas de las filas
        * Si la pista es 0, la celda se marca como vacia
     */
    private void llenarTableroDesdePistasFila() {
        for (int i = 0; i < pistasfila.length; i++) {
            int col = 0;
            for (int bloque : pistasfila[i]) {
                for (int k = 0; k < bloque; k++) {
                    respuesta[i][col].setEstado(new EstadoMarcado());
                    col++;
                }
                if (col < respuesta[i].length) {
                    col++;
                }
            }
        }
    }

    private void generaPistasColumna(int n) {
        pistascolumna = new int[n][];
        for (int i = 0; i < n; i++) {
            int[] pistas = new int[n];
            int j = 0;
            int bloque = 0;

            for (int k = 0; k < n; k++) {
                if (respuesta[k][i].getEstado().ObtenerEstado().equals("Marcado")) {
                    bloque++;
                } else if (bloque > 0) {
                    pistas[j++] = bloque;
                    bloque = 0;
                }
            }

            if (bloque > 0) {
                pistas[j++] = bloque;
            }
            pistascolumna[i] = new int[j];
            System.arraycopy(pistas, 0, pistascolumna[i], 0, j);
        }
    }

    /*
      * Muestra el tablero del Nonogram
      * (me complique un poco con el codigo intentando que las pistas se alineen mas para que no se vea tan feo)
     */
    public void mostrarTablero() {
        int maxPistasFilaLength = 0;
        int maxPistasColumnaLength = 0;

        for (int i = 0; i < pistasfila.length; i++) {
            if (pistasfila[i] != null) {
                maxPistasFilaLength = Math.max(maxPistasFilaLength, pistasfila[i].length);
            }
        }
        for (int i = 0; i < pistascolumna.length; i++) {
            if (pistascolumna[i] != null) {
                maxPistasColumnaLength = Math.max(maxPistasColumnaLength, pistascolumna[i].length);
            }
        }
        for (int i = maxPistasColumnaLength - 1; i >= 0; i--) {
            for (int j = 0; j < maxPistasFilaLength; j++) {
                System.out.print("  ");
            }
            System.out.print("  ");

            for (int j = 0; j < respuesta[0].length; j++) {
                if (pistascolumna[j] != null && pistascolumna[j].length > i) {
                    System.out.print(pistascolumna[j][pistascolumna[j].length - i - 1] + " ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
        for (int i = 0; i < respuesta.length; i++) {
            if (pistasfila[i] != null) {
                for (int j = 0; j < maxPistasFilaLength; j++) {
                    if (j < pistasfila[i].length) {
                        System.out.print(pistasfila[i][j] + " ");
                    } else {
                        System.out.print("0 ");
                    }
                }
            } else {
                for (int j = 0; j < maxPistasFilaLength; j++) {
                    System.out.print("0 ");
                }
            }
            System.out.print("| ");
            for (int j = 0; j < juego[i].length; j++) {
                System.out.print(juego[i][j] + " ");
            }
            System.out.println();
        }
    }

    /*
     * Genera pistas extra para un tablero de Nonogram
     */
    public void generarPistasExtra(){
        Random al = new Random();
        int n = respuesta.length;
        for(int i = 0; i < n; i++){
            int j = al.nextInt(n);
            Celda correcta = respuesta[i][j];
            if(correcta.getEstado().ObtenerEstado().equals("Marcado")){
                juego[i][j].setEstado(new EstadoMarcado());
                incrementarCeldasCompletadas();
            }else {
                juego[i][j].setEstado(new EstadoTachado());
                incrementarCeldasCompletadas();
            }
        }

    }


}

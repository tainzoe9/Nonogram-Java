package Nonogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import Fabricas.*;
import Comandos.*;

public class Main extends JFrame {
    private JPanel gridPanel;
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JPanel topNumbersPanel;
    private JPanel leftNumbersPanel;
    private JToggleButton[][] cells;
    private int GRID_SIZE;
    private final int CELL_SIZE = 30;
    private final Color FILLED_COLOR = Color.BLACK;
    private final Color EMPTY_COLOR = Color.WHITE;
    private final Color MARKED_COLOR = Color.LIGHT_GRAY;
    private Jugador jugador;
    private JLabel vidasLabel;

    public Main(int gridSize, Jugador jugador) {
        this.GRID_SIZE = gridSize;
        this.jugador = jugador;
        this.jugador.setMainInterface(this);
        setTitle("Nonogram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createGridWithNumbers();
        createControlPanel();

        mainPanel.add(topNumbersPanel, BorderLayout.NORTH);
        mainPanel.add(leftNumbersPanel, BorderLayout.WEST);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void createGridWithNumbers() {
        topNumbersPanel = new JPanel(new GridLayout(1, GRID_SIZE));
        leftNumbersPanel = new JPanel(new GridLayout(GRID_SIZE, 1));

        // Obtener las pistas de las columnas
        for (int col = 0; col < GRID_SIZE; col++) {
            JPanel columnHintPanel = new JPanel(new GridLayout(0, 1));
            String[] pistasColumna = jugador.getPartida().getPistasColumna(col).split(" ");
            for (String pista : pistasColumna) {
                JLabel label = new JLabel(pista, SwingConstants.CENTER);
                columnHintPanel.add(label);
            }
            topNumbersPanel.add(columnHintPanel);
        }

        // Obtener las pistas de las filas
        for (int row = 0; row < GRID_SIZE; row++) {
            String pistasFila = jugador.getPartida().getPistasFila(row);
            JLabel label = new JLabel(pistasFila, SwingConstants.RIGHT);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            leftNumbersPanel.add(label);
        }

        gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        cells = new JToggleButton[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = createCell();
                gridPanel.add(cells[row][col]);
            }
        }
    }

    private JToggleButton createCell() {
        JToggleButton cell = new JToggleButton();
        cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        cell.setBackground(EMPTY_COLOR);
        cell.setBorderPainted(true);
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCellClick(e, cell);
            }
        });
        return cell;
    }

    private void handleCellClick(MouseEvent e, JToggleButton cell) {
        int row = -1, col = -1;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (cells[i][j] == cell) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) break;
        }

        if (row == -1 || col == -1) return;

        // Check if the cell is already correctly marked
        if (cell.getBackground().equals(FILLED_COLOR) || cell.getBackground().equals(MARKED_COLOR)) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (jugador.getPartida().getCeldaResuelta(row, col).getEstado().ObtenerEstado().equals("Marcado")) {
                CommandoLlenarCelda comandoLlenar = new CommandoLlenarCelda(jugador.getPartida().getCeldaJuego(row, col));
                jugador.marcar(comandoLlenar);
                cell.setBackground(FILLED_COLOR);
                cell.setSelected(false); // Prevent default selection behavior
                jugador.getPartida().incrementarCeldasCompletadas();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrecto, perdiste una vida.");
                jugador.restarVida();
                cell.setSelected(false);
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (jugador.getPartida().getCeldaResuelta(row, col).getEstado().ObtenerEstado().equals("Vacio")) {
                ComandoTacharCelda comandoTachar = new ComandoTacharCelda(jugador.getPartida().getCeldaJuego(row, col));
                jugador.marcar(comandoTachar);
                cell.setBackground(MARKED_COLOR);
                cell.setSelected(false); // Prevent default selection behavior
                jugador.getPartida().incrementarCeldasCompletadas();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrecto, perdiste una vida.");
                jugador.restarVida();
                cell.setSelected(false);
            }
        }

        updateVidas();

        if (jugador.getVidas() <= 0) {
            JOptionPane.showMessageDialog(this, "Perdiste todas tus vidas. Fin del juego.");
            disableAllCells();
        } else if (jugador.getPartida().tableroCompleto()) {
            JOptionPane.showMessageDialog(this, "¡Ganaste! Completaste el tablero.");
            disableAllCells();
        }
    }

    private void disableAllCells() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells[i][j].setEnabled(false);
            }
        }
    }

    private void createControlPanel() {
        controlPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        vidasLabel = new JLabel("Vidas: " + jugador.getVidas());
        resetButton.addActionListener(e -> resetGrid());

        controlPanel.add(resetButton);
        controlPanel.add(vidasLabel);
    }

    private void resetGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setSelected(false);
                cells[row][col].setBackground(EMPTY_COLOR);
            }
        }
    }

    public void updateVidas() {
        vidasLabel.setText("Vidas: " + jugador.getVidas());
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Elige un nivel: facil, medio, dificil");
        String level = in.nextLine().trim().toLowerCase();
        int gridSize;
        FabricaDeNonogram fabrica = null;

        switch (level) {
            case "facil":
                gridSize = 5;
                fabrica = new FabricaNonogramFacil();
                break;
            case "medio":
                gridSize = 10;
                fabrica = new FabricaNonogramMedio();
                break;
            case "dificil":
                gridSize = 15;
                fabrica = new FabricaNonogramDificil();
                break;
            default:
                System.out.println("Nivel no válido. Se usará el tamaño por defecto (10).");
                gridSize = 10;
                fabrica = new FabricaNonogramMedio();
                break;
        }
        System.out.println("Bienvenido a Nonogram, presiona click derecho para tachar y click izquierdo para marcar.");
        Jugador jugador = new Jugador(fabrica);
        jugador.getPartida().crearJuego();
        SwingUtilities.invokeLater(() -> {
            Main game = new Main(gridSize, jugador);
            game.setVisible(true);
        });
    }
}
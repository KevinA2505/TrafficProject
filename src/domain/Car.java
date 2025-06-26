package domain;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Car implements Runnable {
    private final GridPane gridPane;
    private final int gridSize;
    private final int n; // tamaño del patrón de carreteras
    private int currentRow, currentCol;
    private volatile boolean running = true;
    private final Random rand = new Random();
    private Button currentButton;
    private static int carCounter = 0;
    private final int carId;
    private Button previousButton;

    /**
     * Crea un Car que se mueve solo por las carreteras del RoadsGrid.
     * @param gridPane El GridPane que contiene las carreteras
     * @param n El parámetro usado para generar el grid (determina el patrón de carreteras)
     */
    public Car(GridPane gridPane, int n) {
        this.gridPane = gridPane;
        this.n = n;
        this.gridSize = n * n + n + 1;
        this.carId = ++carCounter;
        
        // Encuentra una posición inicial válida (en una carretera)
        findInitialPosition();
        highlightCurrentPosition();
    }

    /**
     * Encuentra una posición inicial aleatoria que sea una carretera válida.
     */
    private void findInitialPosition() {
        List<int[]> validPositions = new ArrayList<>();
        
        // Busca todas las posiciones que tienen botones (carreteras)
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (isRoad(row, col) && getButtonAt(row, col) != null) {
                    validPositions.add(new int[]{row, col});
                }
            }
        }
        
        if (!validPositions.isEmpty()) {
            int[] initialPos = validPositions.get(rand.nextInt(validPositions.size()));
            currentRow = initialPos[0];
            currentCol = initialPos[1];
            currentButton = getButtonAt(currentRow, currentCol);
        }
    }

    /**
     * Verifica si una posición es una carretera según el patrón del RoadsGrid.
     */
    private boolean isRoad(int row, int col) {
        return isH(row) || isV(col);
    }

    private boolean isH(int row) {
        return row % (n + 1) == 0;
    }

    private boolean isV(int col) {
        return col % (n + 1) == 0;
    }

    /**
     * Obtiene el botón en la posición especificada del GridPane.
     */
    private Button getButtonAt(int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                    return (Button) node;
                }
            }
        }
        return null;
    }

    /**
     * Encuentra los vecinos válidos (carreteras adyacentes) de la posición actual.
     */
    private List<int[]> getValidNeighbors() {
        List<int[]> neighbors = new ArrayList<>();
        
        // Direcciones: arriba, abajo, izquierda, derecha
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int newRow = currentRow + dir[0];
            int newCol = currentCol + dir[1];
            
            // Verifica límites del grid
            if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
                // Verifica si es una carretera y tiene un botón
                if (isRoad(newRow, newCol) && getButtonAt(newRow, newCol) != null) {
                    neighbors.add(new int[]{newRow, newCol});
                }
            }
        }
        
        return neighbors;
    }

    /** Detiene el movimiento del Car. */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            List<int[]> validNeighbors = getValidNeighbors();
            
            if (validNeighbors.isEmpty()) {
                // Si no hay vecinos válidos, busca una nueva posición
                findInitialPosition();
                continue;
            }

            // Elige un vecino al azar
            int[] nextPos = validNeighbors.get(rand.nextInt(validNeighbors.size()));

            // Limpia el estilo de la posición actual
            clearCurrentPosition();

            // Actualiza posición
            currentRow = nextPos[0];
            currentCol = nextPos[1];
            currentButton = getButtonAt(currentRow, currentCol);

            // Resalta la nueva posición
            highlightCurrentPosition();

            // Espera antes del próximo movimiento
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // Limpia el estilo al finalizar
        clearCurrentPosition();
    }

    /**
     * Resalta la posición actual del carro.
     */
    private void highlightCurrentPosition() {
        if (currentButton != null) {
            Platform.runLater(() -> {
                String carProperty = "car-" + carId;
                currentButton.getProperties().put(carProperty, true);

                // Guarda el botón actual como el anterior para la próxima iteración
                if (previousButton != null && previousButton != currentButton) {
                    clearPreviousPosition();  // limpia antes de actualizar
                }

                // Aplica el nuevo estilo
                if (shouldCreateVertex(currentRow, currentCol)) {
                    currentButton.setStyle("-fx-background-color: #bbb; -fx-text-fill: white; -fx-border-color: red; -fx-border-width: 3px;");
                } else {
                    currentButton.setStyle("-fx-background-color: red;");
                }

                // Actualiza la referencia
                previousButton = currentButton;
            });
        }
    }

    
    private void clearPreviousPosition() {
        if (previousButton != null) {
            previousButton.getProperties().remove("car-" + carId);

            // Restaura el estilo original
            previousButton.setStyle("");  // ← Limpia cualquier estilo en línea y aplica el del CSS
        }
    }


    /**
     * Limpia el estilo de la posición actual.
     */
    private void clearCurrentPosition() {
        if (currentButton != null) {
            Platform.runLater(() -> {
                String carProperty = "car-" + carId;
                
                // Remueve la marca de este carro
                currentButton.getProperties().remove(carProperty);
                
                // Solo restaura el estilo si no hay otros carros en esta posición
                boolean hasOtherCars = currentButton.getProperties().keySet().stream()
                    .anyMatch(key -> key.toString().startsWith("car-") && !key.equals(carProperty));
                
                if (!hasOtherCars) {
                    // No hay otros carros, restaura el estilo original
                    if (shouldCreateVertex(currentRow, currentCol)) {
                        currentButton.setStyle("-fx-background-color: #bbb; -fx-text-fill: white;");
                    } else {
                        currentButton.setStyle("");
                    }
                }
            });
        }
    }

    /**
     * Verifica si una posición debería ser un vértice del grafo.
     */
    private boolean shouldCreateVertex(int row, int col) {
        return isH(row) && isV(col);
    }

    /**
     * Obtiene la posición actual del carro.
     * @return Array con [fila, columna]
     */
    public int[] getCurrentPosition() {
        return new int[]{currentRow, currentCol};
    }

    /**
     * Verifica si el carro está en ejecución.
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Obtiene el ID único del carro.
     */
    public int getCarId() {
        return carId;
    }
}
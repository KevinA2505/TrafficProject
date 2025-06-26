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

	public Car(GridPane gridPane, int n) {
		this.gridPane = gridPane;
		this.n = n;
		this.gridSize = n * n + n + 1;
		this.carId = ++carCounter;

		findInitialPosition();
		highlightCurrentPosition();
	}

	private void findInitialPosition() {
		List<int[]> validPositions = new ArrayList<>();

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				if (isRoad(row, col) && getButtonAt(row, col) != null) {
					validPositions.add(new int[] { row, col });
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

	private boolean isRoad(int row, int col) {
		return isH(row) || isV(col);
	}

	private boolean isH(int row) {
		return row % (n + 1) == 0;
	}

	private boolean isV(int col) {
		return col % (n + 1) == 0;
	}

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

	private List<int[]> getValidNeighbors() {
		List<int[]> neighbors = new ArrayList<>();

		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		for (int[] dir : directions) {
			int newRow = currentRow + dir[0];
			int newCol = currentCol + dir[1];

			if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
				if (isRoad(newRow, newCol) && getButtonAt(newRow, newCol) != null) {
					neighbors.add(new int[] { newRow, newCol });
				}
			}
		}

		return neighbors;
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		while (running) {
			List<int[]> validNeighbors = getValidNeighbors();

			if (validNeighbors.isEmpty()) {
				findInitialPosition();
				continue;
			}

			int[] nextPos = validNeighbors.get(rand.nextInt(validNeighbors.size()));

			clearCurrentPosition();

			currentRow = nextPos[0];
			currentCol = nextPos[1];
			currentButton = getButtonAt(currentRow, currentCol);

			highlightCurrentPosition();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}

		clearCurrentPosition();
	}

	private void highlightCurrentPosition() {
		if (currentButton != null) {
			Platform.runLater(() -> {
				String carProperty = "car-" + carId;
				currentButton.getProperties().put(carProperty, true);

				if (previousButton != null && previousButton != currentButton) {
					clearPreviousPosition();
				}

				if (shouldCreateVertex(currentRow, currentCol)) {
					currentButton.setStyle(
							"-fx-background-color: #bbb; -fx-text-fill: white; -fx-border-color: red; -fx-border-width: 3px;");
				} else {
					currentButton.setStyle("-fx-background-color: red;");
				}

				previousButton = currentButton;
			});
		}
	}

	private void clearPreviousPosition() {
		if (previousButton != null) {
			previousButton.getProperties().remove("car-" + carId);

			previousButton.setStyle("");
		}
	}

	private void clearCurrentPosition() {
		if (currentButton != null) {
			Platform.runLater(() -> {
				String carProperty = "car-" + carId;

				currentButton.getProperties().remove(carProperty);

				boolean hasOtherCars = currentButton.getProperties().keySet().stream()
						.anyMatch(key -> key.toString().startsWith("car-") && !key.equals(carProperty));

				if (!hasOtherCars) {
					if (shouldCreateVertex(currentRow, currentCol)) {
						currentButton.setStyle("-fx-background-color: #bbb; -fx-text-fill: white;");
					} else {
						currentButton.setStyle("");
					}
				}
			});
		}
	}

	private boolean shouldCreateVertex(int row, int col) {
		return isH(row) && isV(col);
	}

	public int[] getCurrentPosition() {
		return new int[] { currentRow, currentCol };
	}

	public boolean isRunning() {
		return running;
	}

	public int getCarId() {
		return carId;
	}
}
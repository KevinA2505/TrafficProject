package domain;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.Random;
import Structures.RoadList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Structures.Graph;
import LogicStructures.LogicRoadList;
import LogicStructures.LogicQueue;
import domain.GraphRoad;

public class Car implements Runnable {
    private final GridPane gridPane;
    private final int gridSize;
    private final int n; // tamaño del patrón de carreteras
    private int currentRow, currentCol;
    private int prevRow, prevCol;
    private final Graph graph;
	private volatile boolean running = true;
	private final Random rand = new Random();
	private Button currentButton;
	private static int carCounter = 0;
	private final int carId;
    private Button previousButton;

    public Car(GridPane gridPane, int n, Graph graph) {
        this.gridPane = gridPane;
        this.n = n;
        this.gridSize = n * n + n + 1;
        this.graph = graph;
        this.carId = ++carCounter;

        findInitialPosition();
        highlightCurrentPosition();
    }

        private void findInitialPosition() {
                RoadList validPositions = new RoadList();

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
                                if (isRoad(row, col) && getButtonAt(row, col) != null) {
                                        LogicRoadList.add(row, col, validPositions);
                                }
			}
		}

                if (!LogicRoadList.isEmpty(validPositions)) {
                        int index = rand.nextInt(LogicRoadList.size(validPositions));
                        NodeRoad initialPos = LogicRoadList.getAt(validPositions, index);
                        currentRow = initialPos.getI();
                        currentCol = initialPos.getJ();
                        prevRow = currentRow;
                        prevCol = currentCol;
                        currentButton = getButtonAt(currentRow, currentCol);

                        NodeV node = GraphRoad.getNodeAt(currentRow, currentCol);
                        if (node != null) {
                                LogicQueue.add(carId, node.getCars());
                        }
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

        private RoadList getValidNeighbors() {
                RoadList neighbors = new RoadList();

                NodeV node = GraphRoad.getNodeAt(currentRow, currentCol);
                if (node != null) {
                        NodeRoad x = node.getxRoads().getFirst();
                        if (x != null) {
                                LogicRoadList.add(x.getI(), x.getJ(), neighbors);
                        }
                        NodeRoad y = node.getyRoads().getFirst();
                        if (y != null) {
                                LogicRoadList.add(y.getI(), y.getJ(), neighbors);
                        }
                } else {
                        NodeRoad next = getNextStraight();
                        if (next != null) {
                                LogicRoadList.add(next.getI(), next.getJ(), neighbors);
                        }
                }

                return neighbors;
        }

        private NodeRoad getNextStraight() {
                int dRow = currentRow - prevRow;
                int dCol = currentCol - prevCol;
                int newRow = currentRow + dRow;
                int newCol = currentCol + dCol;

                if (dRow == 0 && dCol == 0) {
                        return null;
                }

                if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
                        if (isRoad(newRow, newCol) && getButtonAt(newRow, newCol) != null) {
                                return new NodeRoad(newRow, newCol);
                        }
                }

                return null;
        }

	public void stop() {
		running = false;
	}

        @Override
        public void run() {
                while (running) {
                        RoadList validNeighbors = getValidNeighbors();

                        if (LogicRoadList.isEmpty(validNeighbors)) {
                                findInitialPosition();
                                continue;
                        }

                        int index = rand.nextInt(LogicRoadList.size(validNeighbors));
                        NodeRoad nextPos = LogicRoadList.getAt(validNeighbors, index);

                        NodeV prevVertex = GraphRoad.getNodeAt(currentRow, currentCol);
                        NodeV nextVertex = GraphRoad.getNodeAt(nextPos.getI(), nextPos.getJ());

                        if (prevVertex != null) {
                                LogicQueue.pop(prevVertex.getCars());
                        }

                        clearCurrentPosition();

                        prevRow = currentRow;
                        prevCol = currentCol;
                        currentRow = nextPos.getI();
                        currentCol = nextPos.getJ();
                        currentButton = getButtonAt(currentRow, currentCol);

                        if (nextVertex != null) {
                                LogicQueue.add(carId, nextVertex.getCars());
                        }

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
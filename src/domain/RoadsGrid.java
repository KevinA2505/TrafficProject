package domain;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import utils.Graph;
import utils.LogicRoadList;
import utils.LogicVerticesList;
import utils.NodeV;
import utils.NodeVertex;
import utils.RoadList;

public class RoadsGrid {

	public static GridPane generateGrid(int n) {
		GraphRoad.resetGraph();
		GridPane g = new GridPane();
		int gridSize = n * n + n + 1;
		double p = 100.0 / (double) gridSize;

		for (int i = 0; i < gridSize; i++) {
			RowConstraints r = new RowConstraints();
			r.setPercentHeight(p);
			g.getRowConstraints().add(r);
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(p);
			g.getColumnConstraints().add(c);
		}

		Graph roadGraph = GraphRoad.getGraph();

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				boolean isRoad = isH(row, n) || isV(col, n);

				if (isRoad) {
					Button b = new Button(row + "," + col);
					b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
					if (n >= 6) {
						b.setPrefSize(2, 2);
					} else {
						b.setPrefSize(30, 30);
					}
					g.add(b, col, row);

					// Verificar si este botón debe ser un Nodo del gráfo
					if (shouldCreateVertex(row, col, n, gridSize)) {
						int vertexId = generateVertexId(row, col);
						LogicVerticesList.add(vertexId, roadGraph.getVertices());

						/*
						 * Para darle color a las intersecciones.
						 */
						// b.setStyle("-fx-background-color: #bbb; -fx-text-fill: white;");
					}
				}
			}
		}

		//assignRoadsToVertices(n, roadGraph);

		TrafficPatternGenerator.generateTrafficPattern(n, roadGraph);
		return g;
	}

	private static boolean shouldCreateVertex(int row, int col, int n, int gridSize) {
		boolean isHorizontalRoad = isH(row, n);
		boolean isVerticalRoad = isV(col, n);

		if (isHorizontalRoad && isVerticalRoad) {
			return true;
		}

		return false;
	}

	private static int countAdjacentButtons(int row, int col, int n, int gridSize) {
		int count = 0;

		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };

		for (int[] dir : directions) {
			int newRow = row + dir[0];
			int newCol = col + dir[1];

			if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
				if (isH(newRow, n) || isV(newCol, n)) {
					count++;
				}
			}
		}

		return count;
	}

	private static void assignRoadsToVertices(int n, Graph roadGraph) {
		int gridSize = n * n + n + 1;

		// Recorrer todos los vértices del grafo
		NodeVertex currentVertex = roadGraph.getVertices().getFirst();

		while (currentVertex != null) {
			NodeV vertex = currentVertex.getNodeV();
			int vertexId = vertex.getData();
			int[] coords = vertexIdToCoordinates(vertexId);
			int row = coords[0];
			int col = coords[1];

			// Explorar en las 4 direcciones desde este vértice
			int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } }; // derecha, izquierda, abajo, arriba

			for (int[] dir : directions) {
				exploreDirection(row, col, dir[0], dir[1], n, gridSize, vertex.getRoads(), roadGraph);
			}

			currentVertex = currentVertex.getNext();
		}
	}

	private static void exploreDirection(int startRow, int startCol, int deltaRow, int deltaCol, int n, int gridSize,
			RoadList roadList, Graph roadGraph) {

		int currentRow = startRow + deltaRow;
		int currentCol = startCol + deltaCol;

// Continuar en la dirección hasta encontrar otro vértice o salir del grid
		while (currentRow >= 0 && currentRow < gridSize && currentCol >= 0 && currentCol < gridSize) {

// Verificar si esta posición es un camino válido
			boolean isRoad = isH(currentRow, n) || isV(currentCol, n);

			if (!isRoad) {
				break; // No es un camino válido, parar
			}

// Verificar si esta posición es un vértice del grafo
			if (shouldCreateVertex(currentRow, currentCol, n, gridSize)) {
// Es otro vértice, parar sin agregarlo
				break;
			}

// Agregar esta posición a la lista de caminos del vértice origen
			LogicRoadList.add(currentRow, currentCol, roadList);

// Avanzar en la misma dirección
			currentRow += deltaRow;
			currentCol += deltaCol;
		}
	}

	private static int[] vertexIdToCoordinates(int vertexId) {
		int row = vertexId / 1000;
		int col = vertexId % 1000;
		return new int[] { row, col };
	}

	private static int generateVertexId(int row, int col) {
		return row * 1000 + col; // Simple scheme: row*1000 + col
	}

	private static boolean isH(int row, int n) {
		return row % (n + 1) == 0;
	}

	private static boolean isV(int col, int n) {
		return col % (n + 1) == 0;
	}
}
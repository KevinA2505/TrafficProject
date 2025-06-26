package domain;

import LogicStructures.LogicRoadList;
import LogicStructures.LogicVerticesList;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

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

		// assignRoadsToVertices(n, roadGraph);

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

	private static int generateVertexId(int row, int col) {
		return row * 1000 + col;
	}

	private static boolean isH(int row, int n) {
		return row % (n + 1) == 0;
	}

	private static boolean isV(int col, int n) {
		return col % (n + 1) == 0;
	}
}
package domain;

import LogicStructures.LogicEdgeList;
import LogicStructures.LogicVerticesList;
import Nodes.Node;
import Nodes.NodeE;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.EdgeList;
import Structures.Graph;

public class GraphRoad {

	private static Graph graph;

	private GraphRoad() {

	}

	public static synchronized Graph getGraph() {
		if (graph == null) {
			graph = new Graph();
		}
		return graph;
	}

        public static synchronized void resetGraph() {
                graph = new Graph();
        }

        /**
         * Returns the {@link NodeV} located at the given coordinates of the
         * grid, or {@code null} if there is no vertex for that position.
         * The search is performed using the identifier {@code row*1000 + col}.
         */
        public static NodeV getNodeAt(int row, int col) {
                Graph g = getGraph();
                if (g == null || LogicVerticesList.isEmpty(g.getVertices())) {
                        return null;
                }

                int id = row * 1000 + col;
                NodeVertex current = g.getVertices().getFirst();
                while (current != null) {
                        if (current.getNodeV().getData() == id) {
                                return current.getNodeV();
                        }
                        current = current.getNext();
                }
                return null;
        }

	public static void displayGraph() {
		if (graph == null || LogicVerticesList.isEmpty(graph.getVertices())) {
			return;
		}

		System.out.println("-----");

		NodeVertex currentVertex = graph.getVertices().getFirst();

		while (currentVertex != null) {
			displayVertexConnections(currentVertex.getNodeV());
			currentVertex = currentVertex.getNext();
		}

		System.out.println("-----");
	}

	private static void displayVertexConnections(NodeV vertex) {
		int[] originCoords = vertexIdToCoordinates(vertex.getData());
		String originStr = formatCoordinates(originCoords);

		EdgeList edges = vertex.getEdges();

		if (!LogicEdgeList.isEmpty(edges)) {
			Node edgeNode = edges.getFirst();

			while (edgeNode != null) {
				NodeE edge = edgeNode.getNodeE();
				int[] destCoords = vertexIdToCoordinates(edge.getDestination().getData());
				String destStr = formatCoordinates(destCoords);

				System.out.println(originStr + " -> " + destStr + " weight: " + edge.getWeight());

				edgeNode = edgeNode.getNext();
			}
		} else {
			System.out.println(originStr + " -> Sin conexiones");
		}
	}

	private static int[] vertexIdToCoordinates(int vertexId) {
		int row = vertexId / 1000;
		int col = vertexId % 1000;
		return new int[] { row, col };
	}

	private static String formatCoordinates(int[] coordinates) {
		return "(" + coordinates[0] + "," + coordinates[1] + ")";
	}
}
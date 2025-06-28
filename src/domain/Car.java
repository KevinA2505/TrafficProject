package domain;

import LogicStructures.LogicQueue;
import LogicStructures.LogicRoadList;
import LogicStructures.LogicVerticesList;
import Nodes.Node;
import Nodes.NodeE;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;
import Structures.VerticesList;
import business.MainController;
import java.util.Random;

public class Car implements Runnable {

	private static int counter = 0;
	private static final int VELOCITY_STANDARD = 1000;
	private final int id;
	private NodeV origin;
	private NodeV destination;
	private MainController controller;
	private int lastRow = -1;
	private int lastCol = -1;

	public Car(NodeV origin, NodeV destination, MainController controller) {
		this.id = ++counter;
		this.origin = origin;
		this.destination = destination;
		this.controller = controller;
	}

	@Override
	public void run() {
		Graph g = GraphRoad.getGraph();

		while (true) {
			int[] path = Dijkstra.buildPath(origin.getData(), destination.getData(), g);

			/*
			 * Esto es para saber la ruta del car desde un inicio
			 */
			System.out.print("Ruta Dijkstra: ");
			for (int i = 0; i < path.length; i++) {
				System.out.print(path[i]);
				if (i < path.length - 1)
					System.out.print(" -> ");
			}
			System.out.println();

			for (int i = 0; i < path.length; i++) {
				NodeV node = findNode(path[i], g);

				if (node == null)
					continue;

				System.out.println(this + " -> " + toCoord(node.getData()));
				LogicQueue.add(this, node.getCars());

				int totalDelay = 0;
				RoadList rList = null;
				NodeV next = null;

				if (i < path.length - 1) {
					next = findNode(path[i + 1], g);
					NodeE edge = findEdge(node, next);
					if (edge != null) {
						/*
						 * calculo para incluir la costante de velocidad
						 */
						totalDelay = (int) edge.getWeight() * VELOCITY_STANDARD;
					}
					rList = selectRoadList(node, next);
				}

				/*
				 * Aqui dividimos el step para establecer un recorrido diferente en tramos Se
				 * calcula basandose en el weight de la arista Divide por el tamaaño de la
				 * lista.
				 */
				if (rList != null && !LogicRoadList.isEmpty(rList)) {
					int steps = LogicRoadList.size(rList) + 1;
					int stepDelay = (steps > 0) ? totalDelay / steps : totalDelay;
					NodeRoad cursor = rList.getFirst();
					while (cursor != null) {
						if (controller != null) {
							controller.updateCarPosition(lastRow, lastCol, cursor.getI(), cursor.getJ());
							lastRow = cursor.getI();
							lastCol = cursor.getJ();
						}
						try {
							Thread.sleep(stepDelay);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							return;
						}
						System.out.println(this + " -> (" + cursor.getI() + "," + cursor.getJ() + ")");
						cursor = cursor.getNext();
					}

					if (next != null && controller != null) {
						int nrow = next.getData() / 1000;
						int ncol = next.getData() % 1000;
						controller.updateCarPosition(lastRow, lastCol, nrow, ncol);
						lastRow = nrow;
						lastCol = ncol;
					}
					try {
						Thread.sleep(stepDelay);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				} else {

					if (i < path.length - 1 && next != null && controller != null) {
						int nrow = next.getData() / 1000;
						int ncol = next.getData() % 1000;
						controller.updateCarPosition(lastRow, lastCol, nrow, ncol);
						lastRow = nrow;
						lastCol = ncol;
					}
					try {
						Thread.sleep(totalDelay);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}

				LogicQueue.pop(node.getCars());
			}

			System.out.println("Ruta terminada");

			origin = destination;
			destination = getRandomDestination(origin, g);
		}
	}

	private RoadList selectRoadList(NodeV originV, NodeV destinationV) {
		int oRow = originV.getData() / 1000;
		int oCol = originV.getData() % 1000;
		int dDow = destinationV.getData() / 1000;
		int dCol = destinationV.getData() % 1000;
		if (oRow == dDow)
			return originV.getxRoads();
		if (oCol == dCol)
			return originV.getyRoads();
		return null;
	}

	private NodeV findNode(int data, Graph g) {
		if (g.getVertices() == null)
			return null;
		NodeVertex curr = g.getVertices().getFirst();
		while (curr != null) {
			if (curr.getNodeV().getData() == data)
				return curr.getNodeV();
			curr = curr.getNext();
		}
		return null;
	}

	private NodeE findEdge(NodeV origin, NodeV destination) {
		if (origin == null || origin.getEdges() == null)
			return null;
		Node current = origin.getEdges().getFirst();
		while (current != null) {
			NodeE e = current.getNodeE();
			if (e.getDestination() == destination)
				return e;
			current = current.getNext();
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public NodeV getOrigin() {
		return origin;
	}

	public NodeV getDestination() {
		return destination;
	}

	@Override
	public String toString() {
		return "Car " + id;
	}

	private static String toCoord(int id) {
		int row = id / 1000;
		int col = id % 1000;
		return "(" + row + "," + col + ")";
	}

	private NodeV getRandomDestination(NodeV current, Graph g) {
		VerticesList vList = g.getVertices();
		if (vList == null)
			return current;

		int size = LogicVerticesList.size(vList);
		if (size <= 1)
			return current;

		Random rand = new Random();
		NodeV dest = current;
		while (dest == current) {
			int index = rand.nextInt(size);
			NodeVertex node = vList.getFirst();
			for (int i = 0; i < index && node != null; i++) {
				node = node.getNext();
			}
			if (node != null)
				dest = node.getNodeV();
		}
		return dest;
	}
}
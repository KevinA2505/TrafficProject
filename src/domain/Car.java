package domain;

import LogicStructures.LogicQueue;
import LogicStructures.LogicRoadList;
import Nodes.Node;
import Nodes.NodeE;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;
import business.MainController;

public class Car implements Runnable {

	private static int counter = 0;
	private static final int VELOCITY_STANDARD = 1000;
	private final int id;
	private NodeV origin;
	private NodeV destination;
	private MainController controller;

	public Car(NodeV origin, NodeV destination, MainController controller) {
		this.id = ++counter;
		this.origin = origin;
		this.destination = destination;
		this.controller = controller;
	}

	@Override
	public void run() {
		Graph g = GraphRoad.getGraph();
		if (g == null)
			return;

		while (true) {
			int[] path = Dijkstra.buildPath(origin.getData(), destination.getData(), g);

			for (int i = 0; i < path.length; i++) {
				NodeV node = findNode(path[i], g);
				if (node == null)
					continue;

				System.out.println(this + " -> " + toCoord(node.getData()));
				LogicQueue.add(this, node.getCars());

				long totalDelay = 0;
				RoadList rList = null;
				if (i < path.length - 1) {
					NodeV next = findNode(path[i + 1], g);
					NodeE edge = findEdge(node, next);
					if (edge != null) {
						totalDelay = (long) edge.getWeight() * VELOCITY_STANDARD;
					}
					rList = selectRoadList(node, next);
				}

				if (rList != null && !LogicRoadList.isEmpty(rList)) {
					int steps = LogicRoadList.size(rList) + 1;
					long stepDelay = (steps > 0) ? totalDelay / steps : totalDelay;
					NodeRoad cursor = rList.getFirst();
					while (cursor != null) {
						try {
							Thread.sleep(stepDelay);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							return;
						}
						System.out.println(this + " -> (" + cursor.getI() + "," + cursor.getJ() + ")");
						cursor = cursor.getNext();
					}
					try {
						Thread.sleep(stepDelay);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				} else {
					try {
						Thread.sleep(totalDelay);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
				long totalDelay = 0;
				RoadList rList = null;
				NodeV next = null;
				if (i < path.length - 1) {
					next = findNode(path[i + 1], g);
					NodeE edge = findEdge(node, next);
					if (edge != null) {
						totalDelay = (long) edge.getWeight() * VELOCITY_STANDARD;
					}
					rList = selectRoadList(node, next);
				}

				if (rList != null && !LogicRoadList.isEmpty(rList)) {
					int steps = LogicRoadList.size(rList) + 1;
					long stepDelay = (steps > 0) ? totalDelay / steps : totalDelay;
					NodeRoad cursor = rList.getFirst();
					while (cursor != null) {
						if (controller != null) {
							controller.updateCarPosition(cursor.getI(), cursor.getJ());
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
						controller.updateCarPosition(nrow, ncol);
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
						controller.updateCarPosition(nrow, ncol);
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

			NodeV temp = origin;
			origin = destination;
			destination = temp;
		}
	}

	private RoadList selectRoadList(NodeV originV, NodeV destinationV) {
		int orow = originV.getData() / 1000;
		int ocol = originV.getData() % 1000;
		int drow = destinationV.getData() / 1000;
		int dcol = destinationV.getData() % 1000;
		if (orow == drow)
			return originV.getxRoads();
		if (ocol == dcol)
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
}

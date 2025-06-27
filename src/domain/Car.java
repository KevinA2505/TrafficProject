package domain;

import LogicStructures.LogicQueue;
import Nodes.Node;
import Nodes.NodeE;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;

public class Car implements Runnable {

	private static int counter = 0;
	private static final int VELOCITY_STANDARD = 1000;
	private final int id;
	private NodeV origin;
	private NodeV destination;

	public Car(NodeV origin, NodeV destination) {
		this.id = ++counter;
		this.origin = origin;
		this.destination = destination;
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

				long delay = 0;
				if (i < path.length - 1) {
					NodeV next = findNode(path[i + 1], g);
					NodeE edge = findEdge(node, next);
					if (edge != null) {
						delay = (long) edge.getWeight() * VELOCITY_STANDARD;
					}
				}

				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}

				LogicQueue.pop(node.getCars());
			}

			System.out.println("Ruta terminada");

			NodeV temp = origin;
			origin = destination;
			destination = temp;
		}
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

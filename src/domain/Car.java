package domain;

import LogicStructures.LogicQueue;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;

public class Car implements Runnable {

	private static int counter = 0;
	private final int id;
	private final NodeV origin;
	private final NodeV destination;

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

		int[] path = Dijkstra.buildPath(origin.getData(), destination.getData(), g);
                for (int vId : path) {
                        NodeV node = findNode(vId, g);
                        if (node == null)
                                continue;

                        System.out.println(this + " -> " + toCoord(node.getData()));
                        LogicQueue.add(this, node.getCars());
                        try {
                                Thread.sleep(2000);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                        }
                        LogicQueue.pop(node.getCars());
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

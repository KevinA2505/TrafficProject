package utils;

public final class Dijkstra {

	private Dijkstra() {
	}

	private static final int INF = Integer.MAX_VALUE / 4;

	public static int[] shortestPaths(int srcData, Graph g) {
		VerticesList vList = g.getVertices();
		int n = LogicVerticesList.size(vList);
		NodeV[] nodes = new NodeV[n];

		NodeVertex cursor = vList.getFirst();
		for (int i = 0; i < n; i++) {
			nodes[i] = cursor.getNodeV();
			cursor = cursor.getNext();
		}

		int[] dist = new int[n];
		int[] prev = new int[n];
		boolean[] seen = new boolean[n];

		for (int i = 0; i < n; i++) {
			dist[i] = INF;
			prev[i] = -1;
		}

		int src = indexOf(srcData, nodes);
		if (src == -1) {
			throw new IllegalArgumentException("Origen inexistente");
		}
		dist[src] = 0;

		for (int iter = 0; iter < n; iter++) {
			int u = selectMin(dist, seen);
			if (u == -1)
				break;

			seen[u] = true;

			EdgeList eList = nodes[u].getEdges();
			if (eList == null)
				continue;

			Node edge = eList.getFirst();
			while (edge != null) {
				NodeE e = edge.getNodeE();
				int v = indexOf(e.getDestination().getData(), nodes);
				int alt = dist[u] + e.getWeight();

				if (alt < dist[v]) {
					dist[v] = alt;
					prev[v] = u;
				}
				edge = edge.getNext();
			}
		}

		return dist;
	}

	public static int[] buildPath(int srcData, int dstData, Graph g) {
	    VerticesList vList = g.getVertices();
	    int n = LogicVerticesList.size(vList);
	    NodeV[] nodes = new NodeV[n];

	    NodeVertex cursor = vList.getFirst();
	    for (int i = 0; i < n; i++) {
	        nodes[i] = cursor.getNodeV();
	        cursor = cursor.getNext();
	    }

	    final int INF = Integer.MAX_VALUE / 4;
	    int[] dist = new int[n];
	    int[] prev = new int[n];
	    boolean[] seen = new boolean[n];

	    for (int i = 0; i < n; i++) {
	        dist[i] = INF;
	        prev[i] = -1;
	    }

	    int src = indexOf(srcData, nodes);
	    int dst = indexOf(dstData, nodes);

	    if (src == -1 || dst == -1) {
	        throw new IllegalArgumentException("Origen o destino inexistente");
	    }

	    dist[src] = 0;

	    for (int iter = 0; iter < n; iter++) {
	        int u = selectMin(dist, seen);
	        if (u == -1)
	            break;

	        seen[u] = true;
	        EdgeList eList = nodes[u].getEdges();
	        if (eList == null) continue;

	        Node edge = eList.getFirst();
	        while (edge != null) {
	            NodeE e = edge.getNodeE();
	            int v = indexOf(e.getDestination().getData(), nodes);
	            int alt = dist[u] + e.getWeight();

	            if (alt < dist[v]) {
	                dist[v] = alt;
	                prev[v] = u;
	            }
	            edge = edge.getNext();
	        }
	    }

	    if (dist[dst] == INF) {
	        return new int[0]; // No hay camino
	    }

	    // Crear la ruta invertida con una lista enlazada
	    PathNode head = null;
	    int current = dst;

	    while (current != -1) {
	        PathNode newNode = new PathNode(nodes[current].getData());
	        newNode.next = head;
	        head = newNode;
	        current = prev[current];
	    }

	    // Contar elementos
	    int count = 0;
	    PathNode temp = head;
	    while (temp != null) {
	        count++;
	        temp = temp.next;
	    }

	    // Convertir a arreglo
	    int[] path = new int[count];
	    temp = head;
	    int i = 0;
	    while (temp != null) {
	        path[i++] = temp.value;
	        temp = temp.next;
	    }

	    return path;
	}


	private static int indexOf(int data, NodeV[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].getData() == data)
				return i;
		}
		return -1;
	}

	private static int selectMin(int[] dist, boolean[] seen) {
		int best = INF;
		int idx = -1;

		for (int i = 0; i < dist.length; i++) {
			if (!seen[i] && dist[i] < best) {
				best = dist[i];
				idx = i;
			}
		}
		return idx;
	}
}
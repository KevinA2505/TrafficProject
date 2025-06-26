package domain;

import LogicStructures.LogicRoadList;
import Nodes.NodeRoad;
import Nodes.NodeV;
import Nodes.NodeVertex;
import Structures.Graph;
import Structures.RoadList;

/**
 * Utility class that prints, to the console, the X and Y road lists of every
 * vertex in the road graph. Intended for quick debugging from controllers or
 * unit tests.
 */
public final class RoadLister {

    private RoadLister() { /* util class */ }

    /**
     * Iterates over every vertex in {@code graph} and prints its road lists in
     * human‑readable form:
     *
     * Nodo 0,0
     *   X → (0,1) (0,2) (0,3)
     *   Y → (1,0) (2,0) (3,0)
     *
     * If a vertex has an empty list on an axis, it prints “(vacío)”.
     */
    public static void print(Graph graph) {
        if (graph == null || graph.getVertices() == null) {
            System.out.println("[RoadLister] Grafo vacío o nulo.");
            return;
        }

        NodeVertex current = graph.getVertices().getFirst();
        while (current != null) {
            NodeV node = current.getNodeV();
            System.out.println("Nodo " + toCoord(node.getData()));

            System.out.print("  X → ");
            printRoadList(node.getxRoads());

            System.out.print("  Y → ");
            printRoadList(node.getyRoads());
            System.out.println();

            current = current.getNext();
        }
    }

    /* --------------------------------------------------------------------- */

    private static void printRoadList(RoadList list) {
        if (list == null || LogicRoadList.isEmpty(list)) {
            System.out.println("(vacío)");
            return;
        }
        NodeRoad curr = list.getFirst();
        while (curr != null) {
            System.out.print("(" + curr.getI() + "," + curr.getJ() + ") ");
            curr = curr.getNext();
        }
        System.out.println();
    }

    /** Decodifica id → "fila,col" suponiendo que id = fila*1000 + col. */
    private static String toCoord(int id) {
        int row = id / 1000;
        int col = id % 1000;
        return row + "," + col;
    }
}

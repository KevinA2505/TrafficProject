package domain;

import Nodes.NodeV;

/**
 * Simple representation of a vehicle in the simulation. The car only stores
 * its current node and destination node and can be executed in a separate
 * thread. The movement logic is intentionally minimal for now.
 */
public class Car implements Runnable {

    private final NodeV start;
    private final NodeV destination;

    public Car(NodeV start, NodeV destination) {
        this.start = start;
        this.destination = destination;
    }

    public NodeV getStart() {
        return start;
    }

    public NodeV getDestination() {
        return destination;
    }

    @Override
    public void run() {
        // Placeholder behaviour; real movement logic would be implemented here
        System.out.println(
            "Car running from " + start.getData() + " to " + destination.getData());
    }
}

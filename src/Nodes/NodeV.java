package Nodes;

import Structures.EdgeList;
import Structures.Queue;
import Structures.RoadList;

public class NodeV {

	private int data;
	private Queue cars;
	private RoadList xRoads;
	private RoadList yRoads;
	private EdgeList edges;

	public NodeV(int data) {
		this.data = data;
		this.cars = new Queue();
		this.xRoads = new RoadList(data);
		this.yRoads = new RoadList(data);
		this.edges = new EdgeList();
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public Queue getCars() {
		return cars;
	}

	public void setCars(Queue cars) {
		this.cars = cars;
	}

	public RoadList getxRoads() {
		return xRoads;
	}

	public void setxRoads(RoadList xRoads) {
		this.xRoads = xRoads;
	}

	public RoadList getyRoads() {
		return yRoads;
	}

	public void setyRoads(RoadList yRoads) {
		this.yRoads = yRoads;
	}

	public EdgeList getEdges() {
		return edges;
	}

	public void setEdges(EdgeList edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return data + ""; // PrintList maybe
	}
}

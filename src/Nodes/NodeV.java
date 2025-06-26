package Nodes;

import Structures.EdgeList;
import Structures.Queue;
import Structures.RoadList;

public class NodeV {

	private int data;
	private Queue cars;
	private RoadList roads;
	private EdgeList edges;

	public NodeV(int data) {
		this.data = data;
		this.cars = new Queue();
		this.roads = new RoadList();
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

	public RoadList getRoads() {
		return roads;
	}

	public void setRoads(RoadList roads) {
		this.roads = roads;
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

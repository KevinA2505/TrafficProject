package utils;

public class NodeV {

	private int data;
	private Queue cars;
	private EdgeList edges;

	public NodeV(int data) {
		this.data = data;
		this.cars = new Queue();
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

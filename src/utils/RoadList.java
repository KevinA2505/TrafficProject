package utils;

public class RoadList {

	private NodeRoad first;
	private NodeRoad last;
	
	public RoadList() {
		this.first = null;
		this.last = null;
	}

	public NodeRoad getFirst() {
		return first;
	}

	public void setFirst(NodeRoad first) {
		this.first = first;
	}

	public NodeRoad getLast() {
		return last;
	}

	public void setLast(NodeRoad last) {
		this.last = last;
	}
}

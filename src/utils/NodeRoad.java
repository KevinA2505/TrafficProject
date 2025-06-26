package utils;

import javafx.scene.control.Button;

public class NodeRoad {

	private Button road;
	private NodeRoad next;
	
	public NodeRoad(Button road) {
		super();
		this.road = road;
		this.next = null;
	}
	public Button getRoad() {
		return road;
	}
	public void setRoad(Button road) {
		this.road = road;
	}
	public NodeRoad getNext() {
		return next;
	}
	public void setNext(NodeRoad next) {
		this.next = next;
	}
	@Override
	public String toString() {
		return road.getText();
	}
}

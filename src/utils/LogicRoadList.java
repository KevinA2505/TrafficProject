package utils;

import javafx.scene.control.Button;

public class LogicRoadList {

	public static boolean isEmpty(RoadList roadList) {
		return roadList.getFirst() == null;
	}

	public static void add(int i, int j, RoadList roadList) {
		if (isEmpty(roadList)) {
			NodeRoad newNode = new NodeRoad(i, j);
			roadList.setFirst(newNode);
			roadList.setLast(newNode);
		} else {
			NodeRoad newNode = new NodeRoad(i, j);
			roadList.getLast().setNext(newNode);
			roadList.setLast(newNode);
		}
	}

	public static void removeFirst(RoadList roadList) {
		if (isEmpty(roadList)) {
			return;
		}

		NodeRoad temp = roadList.getFirst();
		if (temp.getNext() == null) {
			roadList.setFirst(null);
			roadList.setLast(null);
		} else {
			roadList.setFirst(temp.getNext());
		}
		temp.setNext(null);
	}

	public static void removeLast(RoadList roadList) {
		if (isEmpty(roadList)) {
			return;
		}

		NodeRoad temp = roadList.getFirst();

		// Si solo hay un elemento
		if (temp.getNext() == null) {
			roadList.setFirst(null);
			roadList.setLast(null);
			return;
		}

		// Buscar el penúltimo nodo
		while (temp.getNext() != null && temp.getNext() != roadList.getLast()) {
			temp = temp.getNext();
		}

		if (temp.getNext() == roadList.getLast()) {
			roadList.setLast(temp);
			temp.setNext(null);
		}
	}

	public static String printList(RoadList roadList) {
		if (isEmpty(roadList)) {
			return "";
		}

		NodeRoad temp = roadList.getFirst();
		StringBuilder text = new StringBuilder();

		while (temp != null) {
			text.append(temp.toString() + "-");
			if (temp.getNext() != null) {
				text.append(", ");
			}
			temp = temp.getNext();
		}

		return text.toString();
	}
}
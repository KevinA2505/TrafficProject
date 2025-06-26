package LogicStructures;

import Nodes.QueueNode;
import Structures.Queue;

public class LogicQueue {

	public static boolean isEmpty(Queue queue) {
		return queue.getFirst() == null;
	}
	
	public static void add(int i, Queue queue) {
		if(isEmpty(queue)) {
			QueueNode newNode = new QueueNode(i);
			queue.setFirst(newNode);
		} else {
			QueueNode newNode = new QueueNode(i);
			QueueNode temp = queue.getFirst();
			
			while(temp.getNext() != null) {
				temp = temp.getNext();
			}
			temp.setNext(newNode);
		}
	}
	
	public static void pop(Queue queue) {
		if(isEmpty(queue)) {
			return;
		}
		if(queue.getFirst().getNext() == null) {
			queue.setFirst(null);
			return;
		}
		queue.setFirst(queue.getFirst().getNext());
	}
	
	public static String print(Queue queue) {
		if(isEmpty(queue)) {
			return null;
		}
		String text = "";
		QueueNode temp = queue.getFirst();
		
		do {
			text += temp.getI();
			if(temp.getNext() != null) {
				text += " ,";
			}
			temp = temp.getNext();
		} while (temp != null);
		return text;
	}
}

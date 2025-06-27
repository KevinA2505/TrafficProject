package LogicStructures;

import Nodes.QueueNode;
import Structures.Queue;
import domain.Car;

public class LogicQueue {

	public static boolean isEmpty(Queue queue) {
		return queue.getFirst() == null;
	}
	
        public static void add(Car car, Queue queue) {
                if(isEmpty(queue)) {
                        QueueNode newNode = new QueueNode(car);
                        queue.setFirst(newNode);
                } else {
                        QueueNode newNode = new QueueNode(car);
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
                        if (temp.getCar() != null) {
                                text += temp.getCar().getCarId();
                        } else {
                                text += "null";
                        }
                        if(temp.getNext() != null) {
                                text += " ,";
                        }
                        temp = temp.getNext();
                } while (temp != null);
                return text;
        }

        public static int size(Queue queue) {
                int count = 0;
                QueueNode temp = queue.getFirst();
                while (temp != null) {
                        count++;
                        temp = temp.getNext();
                }
                return count;
        }
}

package Nodes;

public class QueueNode {

	private int i;
	private QueueNode next;

	public QueueNode() {
		this.i = ' ';
		this.next = null;
	}

	public QueueNode(int i) {
		this.i = i;
		this.next = null;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public QueueNode getNext() {
		return next;
	}

	public void setNext(QueueNode next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return i + "";
	}
}

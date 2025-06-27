package utils;

import javafx.scene.control.Button;

public class VertexButtonNode {
    private int vertexId;
    private Button button;
    private VertexButtonNode next;

    public VertexButtonNode(int vertexId, Button button) {
        this.vertexId = vertexId;
        this.button = button;
        this.next = null;
    }

    public int getVertexId() {
        return vertexId;
    }

    public Button getButton() {
        return button;
    }

    public VertexButtonNode getNext() {
        return next;
    }

    public void setNext(VertexButtonNode next) {
        this.next = next;
    }
}

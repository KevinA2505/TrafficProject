package utils;

import javafx.scene.control.Button;

public class VertexButtonList {
    private VertexButtonNode first;

    public VertexButtonList() {
        this.first = null;
    }

    public void add(int vertexId, Button button) {
        VertexButtonNode newNode = new VertexButtonNode(vertexId, button);
        if (first == null) {
            first = newNode;
        } else {
            VertexButtonNode current = first;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public Button getButton(int vertexId) {
        VertexButtonNode current = first;
        while (current != null) {
            if (current.getVertexId() == vertexId) {
                return current.getButton();
            }
            current = current.getNext();
        }
        return null;
    }
}

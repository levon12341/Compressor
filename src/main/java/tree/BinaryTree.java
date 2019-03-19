package tree;
import java.io.Serializable;

public class BinaryTree implements Serializable {
    private Node root;

    public BinaryTree() {
        root = new Node();
    }

    public BinaryTree(Node root) {
        this.root = root;
    }

    public long getFrequence() {
        return root.getFrequence();
    }

    public Node getRoot() {
        return root;
    }
}
package ed.lab;

import java.util.Comparator;

public class E02AVLTree<T> {

    private final Comparator<T> comparator;
    private Node<T> root;
    private int size;

    public E02AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public void insert(T value) {
        root = insert(root, value);
    }

    public void delete(T value) {
        root = delete(root, value);
    }

    public T search(T value) {
        Node<T> current = root;
        while (current != null) {
            int cmp = comparator.compare(value, current.value);
            if (cmp == 0) return current.value;
            if (cmp < 0) current = current.left;
            else current = current.right;
        }
        return null;
    }
    public int height() {
        return height(root);
    }

    public int size() {
        return size;
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            size++;
            return new Node<>(value);
        }

        int cmp = comparator.compare(value, node.value);
        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            return node; // no duplicados
        }

        updateHeight(node);
        return rebalance(node);
    }

    private Node<T> delete(Node<T> node, T value) {
        if (node == null) return null;

        int cmp = comparator.compare(value, node.value);
        if (cmp < 0) {
            node.left = delete(node.left, value);
        } else if (cmp > 0) {
            node.right = delete(node.right, value);
        } else {
            // nodo encontrado
            if (node.left == null || node.right == null) {
                Node<T> temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            } else {
                Node<T> successor = minNode(node.right);
                node.value = successor.value;
                node.right = delete(node.right, successor.value);
            }
        }

        if (node == null) return null;

        updateHeight(node);
        return rebalance(node);
    }

    private Node<T> minNode(Node<T> node) {
        Node<T> current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    private Node<T> rebalance(Node<T> node) {
        int balance = balanceFactor(node);

        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> t2 = x.right;

        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        Node<T> t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private int height(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node<T> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balanceFactor(Node<T> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        int height;

        Node(T value) {
            this.value = value;
            this.height = 1;
        }
    }
}
package oop.ex4.data_structures;

/**
 * This class represents one node on the AVL Tree. It has a value, a height and references
 * to the left and right children nodes.
 * @author Idan Refaeli
 *
 */
public class Node {
	private int value;
	private Node left;
	private Node right;
	private Node parent;
	private int height;
	
	/**
	 * Initiates a new Node with the given value
	 * @param value the value of the Node.
	 */
	public Node (int value) {
		this.value = value;
		height = 0;
	}
	
	/**
	 * @return the left child Node.
	 */
	public Node getLeft() {
		return left;
	}
	
	/**
	 * @return the right child Node.
	 */
	public Node getRight() {
		return right;
	}
	
	/**
	 * @return the parent Node.
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * @return the value of the Node.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @return the height of the Node.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Set the left child of the node to the given node.
	 * @param left the new left child node.
	 */
	public void setLeft(Node left) {
		this.left = left;
	}
	
	/**
	 * Set the right child of the node to the given node.
	 * @param rigth the new right child node.
	 */
	public void setRight(Node right) {
		this.right = right;
	}
	
	/**
	 * Set the parent of the node to the given node.
	 * @param parent the new parent node.
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	/**
	 * Set the value of the node to the given number.
	 * @param value the new value of the node.
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Set the height of the node to the given number.
	 * @param height the new height of the node.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}

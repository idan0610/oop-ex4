package oop.ex4.data_structures;

import java.util.Iterator;

public class AvlIterator implements Iterator<Integer> {

	private AvlTree avlTree;
	private Node cursor;
	
	/**
	 * Initiates a new Iterator object to the given AvlTree
	 * @param avlTree The AvlTree object
	 */
	public AvlIterator(AvlTree avlTree) {
		this.avlTree = avlTree;
		this.cursor = this.avlTree.findMin(this.avlTree.getRoot());
	}
	
	/**
	 * @return true if cursor is not null
	 */
	public boolean hasNext() {
		if(cursor != null) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the cursor is not null and returns its value if so. Update the cursor to be its successor.
	 * @return the value of the current cursor, null if cursor is null.
	 */
	public Integer next() {
		if(this.hasNext()) {
			Node current = cursor;
			cursor = avlTree.findSuccessor(cursor);
			return current.getValue();
		}
		return null;
	}

	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}

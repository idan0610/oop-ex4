package oop.ex4.data_structures;

import java.util.Iterator;

public class AvlTree implements Iterable<Integer> {
	
	private final int HEIGHT_UPDATE = 1;
	private final int NULL_HEIGHT = -1;
	private final int UNBALANCED_DIFFERENCE = 2;
	private final int RR_VIOLATION = 1;
	private final int RL_VIOLATION = 2;
	private final int LL_VIOLATION = 3;
	private final int LR_VIOLATION = 4;
	private final int NUMBER_NOT_FOUND = -1;
	private final int ROOT_DEPTH = 0;
	private final static double GOLDEN_RATIO_PLUS = (1 + Math.sqrt(5))/2;
	private final static double GOLDEN_RATIO_MINUS = (1 - Math.sqrt(5))/2;
	private final static double NUM_CONSTANT = 1/Math.sqrt(5);
	
	private Node root;
	private int size;
	
	/**
	 * The default constructor.
	 */
	public AvlTree() {
		size = 0;
	}
	
	/**
	 * A constructor that builds the tree by adding the elements in the input array one by
	 * one. If a value appears more the once in the list, only the first appearance is added.
	 * 
	 * @param data the values to add to tree.
	 */
	public AvlTree(int[] data) {
		size = 0;
		for(int newValue : data) {
			add(newValue);
		}
		
	}
	
	/**
	 * A copy constructor that creates a deep copy of the given AvlTree. This means that for
	 * every node or any other internal object of the given tree, a new, identical object, is
	 * instantiated for the new tree (the internal object is not simply referenced from it). The
	 * new tree must contain all the values of the given tree, but not necessarily in the same
	 * structure.
	 * 
	 * @param avlTree an AVL tree.
	 */
	public AvlTree(AvlTree avlTree) {
		size = 0;
		for(int newValue : avlTree) {
			add(newValue);
		}
	}

	/**
	 * Add a new node with the given key to the tree
	 * 
	 * @param newValue the value of the new node to add.
	 * @return true if the value to add is not already in the tree and it was successfully added,
	 * false otherwise.
	 */
	public boolean add(int newValue) {
		// Every node changed during the rotation is updated by its left, right and parent node, if necessary.
		
		if (root == null) {
			// If the AVL Tree is empty, create new Node as the root with newValue
			root = new Node(newValue);
		}
		else {
			Node newNode = root;
			Node parentNode = null;
			boolean isLeftChild = false; // Used to determine if the new added node is left or right child of
										 // its parent.
			
			while (newNode != null) {
				// newNode will be added as a leaf, so keep search for the right place for newNode until it
				// is null, there it will be added to the tree as a leaf.
				if (newNode.getValue() == newValue) {
					// If newValue already exist on the tree, do nothing to change the tree and return false.
					return false;
				}
				
				parentNode = newNode; // Keep the parent of newNode to link between them at the end.
				
				// If newValue is smaller then the current newNode value, go left. Otherwise, go right.
				if (newNode.getValue() > newValue) {
					newNode = parentNode.getLeft();
					isLeftChild = true;
				}
				else {
					newNode = parentNode.getRight();
					isLeftChild = false;
				}
			}
			
			// Found the right place for newValue, add new Node with newValue and link between the new Node
			// with its parent Node
			newNode = new Node(newValue);
			newNode.setParent(parentNode);
			if (isLeftChild == true) {
				parentNode.setLeft(newNode);
			}
			else {
				parentNode.setRight(newNode);
			}
			
			// Update heights of the nodes on tree from the new leaf to the root. If during the updating,
			// a violation Node was found, fix the tree
			Node violationNode = parentNode;
			while(violationNode != null) {
				violationNode = updateTree(violationNode);
				if(violationNode != null) {
					fixAvlTree(violationNode);
				}
			}
			
			
		}
		size++;
		return true;
	}
	
	/**
	 * Check whether the tree contains the given input value.
	 * 
	 * @param searchVal the value to search for.
	 * @return the depth of the node (0 for the root) with the given value if it was found in
	 * the tree, -1 otherwise.
	 */
	public int contains(int searchVal) {
		int depth = ROOT_DEPTH;
		Node current = root;
		
		while(current != null) {
			
			if(searchVal == current.getValue()) {
				// searchVal was found. Return its depth.
				return depth;
			}
			
			if(searchVal < current.getValue()) {
				// searchVal is smaller then value of current, go left.
				current = current.getLeft();
			}
			else {
				// searchVal is bigger then value of current, go right.
				current = current.getRight();
			}
			depth++;
		}
		return NUMBER_NOT_FOUND;
	}
	
	/**
	 * Removes the node with the given value from the tree, if it exists.
	 * 
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete) {
		// Every node changed during the rotation is updated by its left, right and parent node, if necessary.
		
		Node current = root;
		boolean isLeftChild = false;
		
		while(current != null) {
			if(current.getValue() == toDelete) {
				// The node was found, take its left, right children nodes and parent node, and check in
				// which case the node is in.
				
				// Save the left, right children and parent of current (the node to be deleted).
				Node left = current.getLeft();
				Node right = current.getRight();
				Node parent = current.getParent();
				int height = current.getHeight();
				
				if(left != null && right != null) {
					// The node has 2 children nodes. Find the successor and replace it with the node. Then
					// the node will have 1 right child. 
					
					// Find the successor and its parent and right child. 
					// The successor has no left child.
					Node successor = findSuccessor(current);
					Node successorParent = successor.getParent();
					Node successorRight = successor.getRight();
					int successorHeight = successor.getHeight();
					
					// Set the parent of successor to be the parent of current. If current was the root, set
					// the successor as the new root. Otherwise, find if current was a left or right child of
					// its parent and set the successor the same.
					successor.setParent(parent);
					if(root == current) {
						root = successor;
					}
					else {
						if (isLeftChild == true) {
							parent.setLeft(successor);
						}
						else {
							parent.setRight(successor);
						}
					}
					
					// Set the left child of successor to be the left of current.
					successor.setLeft(left);
					left.setParent(successor);
					
					// Set the left child of current to be null and the right child to be the successor's.
					current.setLeft(null);
					current.setRight(successorRight);
					if(successorRight != null) {
						successorRight.setParent(current);
					}
					
					// Check if the successor was the right child of current. If so, set the right child of
					// successor as current and the parent of current as successor. Otherwise, set the right
					// child of successor as right, and the parent of successor as successorParent.
					if(successorParent == current) {
						successor.setRight(current);
						current.setParent(successor);
						isLeftChild = false;
					}
					else {
						successor.setRight(right);
						right.setParent(successor);
						current.setParent(successorParent);
						successorParent.setLeft(current);
						isLeftChild = true;
					}
					
					successor.setHeight(height);
					current.setHeight(successorHeight);
					
					// Update left, right and parent to be the new left, right and parent of current.
					left = null;
					right = successorRight;
					parent = current.getParent();
				}
				
				if(left == null && right == null) {
					// The node has no children. So the only thing needs to be done is delete it.
					
					// If the root is current (The tree has only 1 node - the node to be deleted), just set
					// the root as null. Otherwise, check if current is left or right child of its parent,
					// and change it to null.
					if(root == current) {
						root = null;
					}
					else {
						current.setParent(null);
						if(isLeftChild == true) {
							parent.setLeft(null);
						}
						else {
							parent.setRight(null);
						}
					}
				}
				else if(left == null && right != null) {
					// The node has only right child. So link the the right child to its parent.
					
					// If current is the root, set the right child as the root. Otherwise, set the right
					// child as the child of the current's parent.
					if(root == current) {
						current.setRight(null);
						right.setParent(null);
						root = right;
					}
					else {
						right.setParent(parent);
						if(isLeftChild == true) {
							parent.setLeft(right);
						}
						else {
							parent.setRight(right);
						}
						current.setRight(null);
						current.setParent(null);
					}
				}
				else {
					// The node has only left child. So link the the left child to its parent.
					
					// If current is the root, set the left child as the root. Otherwise, set the left
					// child as the child of the current's parent.
					if(root == current) {
						current.setLeft(null);
						left.setParent(null);
						root = left;
					}
					else {
						left.setParent(parent);
						if(isLeftChild == true) {
							parent.setLeft(left);
						}
						else {
							parent.setRight(left);
						}
						current.setLeft(null);
						current.setParent(null);
					}
				}
				
				// Update heights of the nodes on tree from the deleted node to the root. If during the 
				// updating, a violation node was found, fix the tree.
				Node violationNode = parent;
				while(violationNode != null) {
					violationNode = updateTree(violationNode);
					if(violationNode != null) {
						fixAvlTree(violationNode);
					}
				}
				
				size--;
				return true;
			}
			
			if(current.getValue() > toDelete) {
				current = current.getLeft();
				isLeftChild = true;
			}
			else {
				current = current.getRight();
				isLeftChild = false;
			}
		}
		
		return false;
	}
	
	/**
	 * @return the number of nodes in the tree.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * @return an iterator on the AVL Tree. The returned iterator iterates over the tree nodes
	 * in an ascending order, and does NOT implement the remove() method.
	 */
	public Iterator<Integer> iterator() {
		return new AvlIterator(this);
	}
	
	/**
	 * Calculates the minimum number of nodes in an AVL tree of height h.
	 * 
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given height.
	 */
	public static int findMinNodes(int h) {
		// The calculation is done by the explicit formula for minimum number of nodes in an AVL tree proved
		// on DaSt course.
		return (int) (NUM_CONSTANT*(Math.pow(GOLDEN_RATIO_PLUS, h+3) - 
				Math.pow(GOLDEN_RATIO_MINUS, h+3))) - 1;
	}
	
	private Node updateTree(Node node) {
		// Used to update the tree after adding or removing a node. If a violation node is found, it will
		// returned and the update will stop. Otherwise, null is returned.
		
		Node current = node;
		
		while(current != null) {
			// Keep update the heights until the root (or if no more update is needed)
			
			boolean violation = updateHeight(current);
			
			if(violation == true) {
				return current;
			}
			
			current = current.getParent();
		}
		
		return null;
	}
	
	private void fixAvlTree(Node violationNode) {
		// Used to fix a violation created on the given violation node. This method checks the type of the
		// violation and make rotations according to it.
		
		Node left = violationNode.getLeft();
		Node right = violationNode.getRight();
		
		int height = violationNode.getHeight();
		int leftHeight = NULL_HEIGHT;
		int rightHeight = NULL_HEIGHT;
		int rightRightHeight = NULL_HEIGHT;
		int leftLeftHeight = NULL_HEIGHT;
		
		if(left != null) {
			leftHeight = left.getHeight();
			if(left.getLeft() != null) {
				leftLeftHeight = left.getLeft().getHeight();
			}
		}
		if(right != null) {
			rightHeight = right.getHeight();
			if(right.getRight() != null) {
				rightRightHeight = right.getRight().getHeight();
			}
		}
		
		int violationType;
		
		// Check the type of the violation. The type is checked according to the first two nodes on the
		// longest path from the violation node to a leaf.
		if(height == rightHeight + 1) {
			if(rightHeight == rightRightHeight + 1) {
				violationType = RR_VIOLATION;
			}
			else {
				violationType = RL_VIOLATION;
			}
		}
		else {
			if(leftHeight == leftLeftHeight + 1) {
				violationType = LL_VIOLATION;
			}
			else {
				violationType = LR_VIOLATION;
			}
		}
		
		// Make the right rotations according to the type of the violation.
		switch(violationType) {
		case RR_VIOLATION:
			rotate(violationNode, true);
			break;
		case RL_VIOLATION:
			rotate(right, false);
			rotate(violationNode, true);
			break;
		case LL_VIOLATION:
			rotate(violationNode, false);
			break;
		case LR_VIOLATION:
			rotate(left, true);
			rotate(violationNode, false);
			break;
		}
	}
	
	private void rotate(Node node, boolean toLeft) {
		// Used to make a rotation on the given node, according to toLeft. If toLeft is true, the rotation
		// is to left, otherwise the rotation is to right.
		// Every node changed during the rotation is updated by its left, right and parent node, if necessary.
		
		// The algorithm of rotation is based on the algorithm learned on DaSt.
 		Node parent = node.getParent();
		if(toLeft == true) {
			// The rotation on node is to the left.
			Node right = node.getRight();
			Node temp = right.getLeft();
			right.setParent(parent);
			// Check if node is the root, if so, change the root to right. Otherwise, set the parent of node
			// to be the parent of right.
			if(root == node) {
				root = right;
			}
			else if(parent.getLeft() == node) {
				parent.setLeft(right);
			}
			else {
				parent.setRight(right);
			}
			right.setLeft(node);
			node.setParent(right);
			node.setRight(temp);
			if(temp != null) {
				temp.setParent(node);
			}
		}
		else {
			// The rotation on node is to the right.
			Node left = node.getLeft();
			Node temp = left.getRight();
			left.setParent(parent);
			// Check if node is the root, if so, change the root to left. Otherwise, set the parent of node
			// to be the parent of right.
			if(root == node) {
				root = left;
			}
			else if(parent.getLeft() == node) {
				parent.setLeft(left);
			}
			else {
				parent.setRight(left);
			}
			left.setRight(node);
			node.setParent(left);
			node.setLeft(temp);
			if(temp != null) {
				temp.setParent(node);
			}
		}
		
		updateHeight(node);
		if(root != node) {
			updateHeight(node.getParent());
		}
	}
	
	private boolean updateHeight(Node node) {
		// Get heights of left and right children of node. If one of them is not exist, the height
		// will be -1.

		int leftHeight = NULL_HEIGHT;
		int rightHeight = NULL_HEIGHT;
		
		if(node.getLeft() != null) {
			leftHeight = node.getLeft().getHeight();
		}
		
		if(node.getRight() != null) {
			rightHeight = node.getRight().getHeight();
		}
		
		// The new height will be the maximum of the heights of the two children + 1.
		node.setHeight(Math.max(leftHeight, rightHeight) + HEIGHT_UPDATE);
		
		if(Math.max(leftHeight, rightHeight) >= Math.min(leftHeight, rightHeight) 
				+ UNBALANCED_DIFFERENCE) {
			// If the height of one of the children is bigger then the other by 2, then node is
			// a violation node, return true.
			return true;
		}
		
		return false;
	}
	
	Node findSuccessor(Node node) {
		// Used to find the successor of the given node. If no successor is found (node is the maximum on the
		// tree) null is returned.
		
		Node right = node.getRight();
		Node successor;
		
		// If node has right child, the successor is the minimum of the right sub-tree.
		// Otherwise, the successor is the first so node is located on its left sub-tree.
		if(right != null) {
			successor = findMin(right);
		}
		else {
			successor = node.getParent();
			Node child = node;
			while(successor != null && successor.getRight() == child) {
				child = successor;
				successor = successor.getParent();
			}
		}
		
		return successor;
	}
	
	Node findMin(Node node) {
		// Used to find the minimum node on the sub-tree where the given node is its root.
		
		Node min = node;
		while(min.getLeft() != null) {
			min = min.getLeft();
		}
		
		return min;
	}
	
	Node getRoot() {
		// Returns the root of the AVL Tree.
		return root;
	}

}

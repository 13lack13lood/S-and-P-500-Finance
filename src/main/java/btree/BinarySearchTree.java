package btree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
/**
 * The binary search tree that sorts nodes based on a comparator
 *
 * @param <E> the type of objects that may be used with this binary search tree
 */
public class BinarySearchTree<E> {
	
	private BinaryTreeNode<E> root;
	private Comparator<E> comparator;
	/**
	 * A binary search tree is created where all values are compared based on <b>comparator</b>
	 * @param comparator
	 */
	public BinarySearchTree(Comparator<E> comparator) {
		this.comparator = comparator;
		root = null;
	}

	private BinaryTreeNode<E> addNode(BinaryTreeNode<E> parent, E key) {
		if (parent == null)
			return new BinaryTreeNode<E>(key);

		if (comparator.compare(key, parent.getValue()) > 0)
			parent.setRight(addNode(parent.getRight(), key));
		else if (comparator.compare(key, parent.getValue()) < 0)
			parent.setLeft(addNode(parent.getLeft(), key));

		return parent;

	}

	private void DFS(List<E> list, BinaryTreeNode<E> parent) {
		if (parent == null)
			return;

		DFS(list, parent.getLeft());
		list.add(parent.getValue());
		DFS(list, parent.getRight());
	}

	private BinaryTreeNode<E> deleteNode(BinaryTreeNode<E> parent, E key) {
		if (parent == null)
			return parent;

		if (comparator.compare(key, parent.getValue()) < 0)
			parent.setLeft(deleteNode(parent.getLeft(), key));
		else if (comparator.compare(key, parent.getValue()) > 0)
			parent.setRight(deleteNode(parent.getRight(), key));
		else {
			if (parent.getLeft() == null)
				return parent.getRight();

			if (parent.getRight() == null)
				return parent.getLeft();

			parent.setValue(minValue(parent.getRight()));
			parent.setRight(deleteNode(parent.getRight(), parent.getValue()));
		}

		return parent;
	}
	
	private E minValue(BinaryTreeNode<E> parent) {
		E min = parent.getValue();

		while (parent.getLeft() != null) {
			min = parent.getLeft().getValue();
			parent = parent.getLeft();
		}

		return min;
	}
	/**
	 * Deletes the key from the binary search tree
	 * @param key
	 */
	public void delete(E key) {
		root = deleteNode(root, key);
	}
	/**
	 * Adds the key into the binary search tree
	 * @param key
	 */
	public void add(E key) {
		root = addNode(root, key);
	}
	
	/**
	 * Performs in-order traversal and adds all nodes to a LinkedList
	 */
	public List<E> toList() {
		List<E> list = new LinkedList<E>();
		DFS(list, root);
		return list;
	}

}

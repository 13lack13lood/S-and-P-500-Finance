package btree;

/**
 * 
 * The class used to store data for BinarySearchTree
 *
 * @param <E> the type of data stored
 */
public class BinaryTreeNode<E> {

	private E value;

	private BinaryTreeNode<E> left;
	private BinaryTreeNode<E> right;

	/**
	 * @param value
	 */
	public BinaryTreeNode(E value) {
		this.value = value;
		left = right = null;
	}

	/**
	 * @return the left
	 */
	public BinaryTreeNode<E> getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(BinaryTreeNode<E> left) {
		this.left = left;
	}

	/**
	 * @return the right node
	 */
	public BinaryTreeNode<E> getRight() {
		return right;
	}

	/**
	 * @param right set the right node
	 */
	public void setRight(BinaryTreeNode<E> right) {
		this.right = right;
	}

	/**
	 * @return the value
	 */
	public E getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(E value) {
		this.value = value;
	}

}

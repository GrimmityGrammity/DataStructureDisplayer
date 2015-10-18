package comp2402TreeEditor;

import java.awt.Point;

public class RBTreeNode extends BSTreeNode{

	private boolean isBlack = false;
	private boolean isDoubleBlack = false;
	
	static enum orientation {
		NOT_LINE, 
		LEFT_LINE, 
		RIGHT_LINE,
		LEFT_CURVE,
		RIGHT_CURVE
	}
	

	// CONSTRUCTORS ========================================================
	public RBTreeNode() {
	}


	public RBTreeNode(DataADT data) {
		super(data);
		setLeftChild(null);
		setRightChild(null);
	}	


	public boolean getIsBlack() { return isBlack; }
	public void setIsBlack(boolean black) { isBlack = black; }

	public boolean getIsDoubleBlack() { return isDoubleBlack; }
	public void setIsDoubleBlack(boolean doubleBlack) { isDoubleBlack = doubleBlack; }

	public RBTreeNode getRBParent() { return (RBTreeNode) getParent(); }
	public RBTreeNode getRBLeftChild() { return (RBTreeNode) leftChild(); }
	public RBTreeNode getRBRightChild() { return (RBTreeNode) rightChild(); }


	/*
	 * 	Gets this node's parent's sibling
	 */
	public RBTreeNode getUncle() { 
		if(getGrandparent().leftChild() != null && 
				getGrandparent().leftChild() != getRBParent()) {
			return (RBTreeNode) getGrandparent().leftChild();
		} 
		else if(getGrandparent().rightChild() != null && 
				getGrandparent().rightChild() != getRBParent()) {
			return (RBTreeNode) getGrandparent().rightChild();
		}
		else {
			//RBTreeNode nullNode = new RBTreeNode();
			//nullNode.setIsBlack(true);
			return null;
		}
	}
	
	
	/*
	 * 	Gets this node's parent's parent
	 */
	public RBTreeNode getGrandparent() { 
		if(getRBParent().getRBParent() != null) {
			return (RBTreeNode) getRBParent().getRBParent();
		} else {
			//System.out.println("ERROR: Grandparent is null.");
			return null;
		}
	}

	
	/*
	 * 	Returns the orientation of the tree for inserting
	 */
	public orientation getOrientation() { 
		if(getRBParent() == null || getGrandparent() == null) {
			return orientation.NOT_LINE;
		}
		if(getRBParent().leftChild() == this &&
				getGrandparent().leftChild() == getRBParent()) {
			return orientation.LEFT_LINE;
		}
		else if(getRBParent().rightChild() == this &&
				getGrandparent().leftChild() == getRBParent()) {
			return orientation.LEFT_CURVE;
		}
		else if(getRBParent().rightChild() == this &&
				getGrandparent().rightChild() == getRBParent()) {
			return orientation.RIGHT_LINE;
		}
		else if(getRBParent().leftChild() == this &&
				getGrandparent().rightChild() == getRBParent()) {
			return orientation.RIGHT_CURVE;
		}
		return orientation.NOT_LINE;
	}

	
	/*
	 * 	Basic binary insert
	 */
	@Override
	public void insertNode(TreeNode aNode){
		//This insert method of a Red Black Tree inserts aNode
		//in search tree order.
		//O(tree height)

		if(!(aNode instanceof RBTreeNode)) return;

		if(aNode.getData().compare(getData()) < 0){
			//aNode should go in left subtree
			if(leftChild == null) {
				//left child is free
				leftChild = (RBTreeNode) aNode;
				aNode.setParent(this);
				aNode.setLocation(this.getLocation()); //starting location for animation
			}
			else{
				leftChild.insertNode(aNode);
			}
		}
		else if(aNode.getData().compare(getData()) > 0){
			//aNode should go in right subtree
			if(rightChild == null) {
				//right child is free
				rightChild = (RBTreeNode) aNode;
				aNode.setParent(this);
				aNode.setLocation(this.getLocation()); //starting location for animation
			}
			else {
				rightChild.insertNode(aNode);
			}
		}
		else {
			return;
		}
	}


	/*
	 * 	Basic binary search tree remove plus extra
	 * 	I'm not going to sit here and type out all the cases lol
	 */
	public RBTreeNode remove(String aDataString, RBTree tree) {
		System.out.println("\nRemove");


		DataADT temp = new Data(aDataString); //turn the string into a data object for comparisons

		// Tree is null
		if(tree == null) {
			System.out.println("ERROR: Tree passed as null.");
			return null;
		}
		
		//compare this node's key using a temporary data object 
		int comparision = temp.compare(this.getData());

		if(comparision < 0 && leftChild != null) {
			//search the left subtree
			return getRBLeftChild().remove(aDataString, tree);
		}

		else if(comparision > 0 && rightChild != null) {
			//search the right subtree
			return getRBRightChild().remove(aDataString, tree);
		}
		else if(comparision == 0){
			//found a node to remove so remove this node

			// Node to remove is the last node in the tree
			if(isRoot() && isLeaf()) {
				System.out.println("Root");

				tree.setRoot(null);
				return null;
			}
			
			// Node to remove is a red leaf
			if(isLeaf() && !getIsBlack()) {
				System.out.println("Red Leaf");

				RBTreeNode retNode = getRBParent();
				retNode.removeChildNode(this);
				return retNode;
			}
			// Node to remove is a black leaf
			else if(isLeaf() && getIsBlack()) {
			//	System.out.println("Black Leaf");

				// Create a temporary node to represent a double black null node
				RBTreeNode tempDoubleBlack = new RBTreeNode(new Data("temp"));
			//	System.out.println(this.toString());
			//	System.out.println(this.getParent().toString());
				tempDoubleBlack.setIsBlack(true);
				tempDoubleBlack.setIsDoubleBlack(true);
				// Set the temporary node in smallest's place
				if(getRBParent().getRBLeftChild() == this) {
					getRBParent().setLeftChild(tempDoubleBlack);
				}
				else {
					getRBParent().setRightChild(tempDoubleBlack);
				}
				tempDoubleBlack.setParent(getRBParent());
				//System.out.println("tempParent: " + tempDoubleBlack.getParent().toString());

				tempDoubleBlack.removeSort(tree);
				// Remove the temporary node
				if(getRBParent().getRBLeftChild() == this) {
					tempDoubleBlack.getRBParent().setLeftChild(null);
				}
				else {
					tempDoubleBlack.getRBParent().setRightChild(null);
				}
				//System.out.println("Final");

				return tempDoubleBlack.getRBParent();
			}

			else if(isRoot() && rightChild == null) {
				System.out.println("Root left");

				//hijack the leftChild as the new root of the tree
				setData(leftChild.getData());
				leftChild.setParent(null);
				rightChild = (RBTreeNode) leftChild.rightChild();
				if(rightChild != null) rightChild.setParent(this);
				leftChild = (RBTreeNode) leftChild.leftChild();
				if(leftChild != null) leftChild.setParent(this);
				// Set the root black
				setIsBlack(true);
				return this;
			}
			else if(isRoot() && leftChild == null) {
				System.out.println("Root Right");

				//hijack the right as the new root of the tree
				setData(rightChild.getData());
				rightChild.setParent(null);
				leftChild = (RBTreeNode) rightChild.leftChild();
				if(leftChild != null) leftChild.setParent(this);
				rightChild = (RBTreeNode) rightChild.rightChild();
				if(rightChild != null) rightChild.setParent(this);
				// Set the root black
				setIsBlack(true);
				return this;
			}
			else if(leftChild == null && rightChild != null) {
				System.out.println("Replace right");

				//promote rightchild as new child of parent
				return getRBParent().replaceChildNode(this, rightChild);
			}
			else if(rightChild == null && leftChild != null) {
				System.out.println("Replace left");

				//promote left child as new child of parent
				return getRBParent().replaceChildNode(this, leftChild);
			}
			else{
				//we need to remove an internal node with two current children
				//find the smallest next node via the right subtree and use it as a
				//replacement for this node

				RBTreeNode smallest = getRBRightChild().findSmallestNode();
				this.setData(smallest.getData()); //steal the data object of the smallest node
				if(smallest.rightChild() != null) {
					System.out.println("One red smallest");

					// Smallest or smallest's right child is red
					if(!smallest.getIsBlack() || 
							!smallest.getRBRightChild().getIsBlack()) {
						// Put smallest's right child in its place
						if(smallest.getRBParent() == this) {
							smallest.getRBParent().setRightChild(smallest.getRBRightChild());
							smallest.getRBParent().getRBRightChild().setIsBlack(true);
							smallest.getRBRightChild().setParent(smallest.getRBParent());
							return smallest.getRBParent().getRBRightChild();
						}
						else {
							smallest.getRBParent().setLeftChild(smallest.getRBRightChild());
							smallest.getRBParent().getRBLeftChild().setIsBlack(true);
						}
						smallest.getRBRightChild().setParent(smallest.getRBParent());
						// Set smallest's right child to black


						return smallest.getRBParent().getRBLeftChild();
					}
					// Both nodes are black, create double black
					else {
						System.out.println("Black Double smallest");

						// Put smallest's right child in its place
						if(smallest.getRBParent() == this) {
							smallest.getRBParent().setRightChild(smallest.getRBRightChild());
							// Set smallest's right child to double black
							smallest.getRBParent().getRBRightChild().setIsBlack(true);
							smallest.getRBParent().getRBRightChild().setIsDoubleBlack(true);
							smallest.getRBRightChild().setParent(smallest.getRBParent());
							return smallest.getRBParent().getRBRightChild();
						}
						else {
							smallest.getRBParent().setLeftChild(smallest.getRBRightChild());
							// Set smallest's right child to double black
							smallest.getRBParent().getRBLeftChild().setIsBlack(true);
							smallest.getRBParent().getRBLeftChild().setIsDoubleBlack(true);
						}
						smallest.getRBRightChild().setParent(smallest.getRBParent());


						return smallest.getRBParent().getRBLeftChild();
					}
				}
				// Smallest has no children
				else {
					// Smallest is red
					if(!smallest.getIsBlack()) {
						// Smallest is the child of node to be removed
						if(smallest.getRBParent() == this) {
							setRightChild(null);
							smallest.setParent(null);
							return this;
						}
						// Smallest is not the child of node to be removed
						smallest.getRBParent().setLeftChild(null);
						System.out.println("Red Leaf smallest");

						return smallest.getRBParent();
					}
					// Smallest is black
					else {
						// Create a temporary node to represent a double black null node
						RBTreeNode tempDoubleBlack = new RBTreeNode(new Data("temp"));
						tempDoubleBlack.setIsBlack(true);
						tempDoubleBlack.setIsDoubleBlack(true);
						// Set the temporary node in smallest's place
						if(smallest.getRBParent() == this) {
							// Smallest is the child of node to be removed
							smallest.getRBParent().setRightChild(tempDoubleBlack);
						}
						else {
							smallest.getRBParent().setLeftChild(tempDoubleBlack);
						}
						System.out.println("Black Leaf smallest");
						tempDoubleBlack.setParent(smallest.getRBParent());
						tempDoubleBlack.removeSort(tree);
						// Remove the temporary node
						if(tempDoubleBlack.getRBParent() == this) {
							tempDoubleBlack.getRBParent().setRightChild(null);	
							tempDoubleBlack.setParent(null);
							return this;
						}
						tempDoubleBlack.getRBParent().setLeftChild(null);
						return tempDoubleBlack.getRBParent();
					}
				}
				//System.out.println("RBTreeNode::remove: smallest to remove= " + smallest.key());
			}
		}
		System.out.println("Node does not exist.");
		return null;
	}
	
	
	/*
	 * 	Sort called after a node is removed
	 * 	Again, not listing all cases here
	 * 
	 * 	There is problem with this method, one or two 
	 * 	specific cases are still not working properly,
	 * 	but the vast majority of cases work
	 */
	public void removeSort(RBTree tree) {
		//System.out.println("This: " + this.toString() + " -- Parent: " + this.getParent().toString());

		// Tree is null
		if(tree == null) {
			System.out.println("ERROR: Tree passed as null.");
			return;
		}
		//System.out.println("LeftChild: " + tree.getRBRoot().leftChild().toString());

		// Double black has been taken care of
		if(!getIsDoubleBlack()) {
			return;
		}
		// We have reached the root, absorb the double black
		if(isRoot()) {
			setIsDoubleBlack(false);
			setIsBlack(true);
			return;
		}
		// Sibling is null, push double black up the tree
		if(getSibling() == null) {
			setIsDoubleBlack(false);
			if(getRBParent().getIsBlack()) {
				getRBParent().setIsDoubleBlack(true);
				getRBParent().setIsBlack(true);
			}
			else {
				getRBParent().setIsBlack(true);
			}
		//	System.out.println("lol: " + this.toString());

			getRBParent().removeSort(tree);
		}
		
		//System.out.println("Sibling: " + getSibling().toString());

		
		/*
		 * 	CASE 1: Sibling is red
		 */
		
		if(!getSibling().getIsBlack()) {
			RBTreeNode sibling = getSibling();
			// This is a left child
			if(getRBParent().getRBLeftChild() == this) {
				System.out.println("Case 1:1");
				
				// Sibling has a left child, preserve it
				if(sibling.getRBLeftChild() != null) {
					sibling.getRBLeftChild().setParent(getRBParent());
					getRBParent().setRightChild(sibling.getRBLeftChild());
				}
				// Sibling has no left child
				else {
					getRBParent().setRightChild(null);
				}
				// Make sibling a child of grandparent
				if(getGrandparent() != null) {
					sibling.setParent(getGrandparent());
					// Parent was a left child
					if(getGrandparent().getRBLeftChild() != null && getGrandparent().getRBLeftChild() == getParent()) {
						getGrandparent().setLeftChild(sibling);
					}
					// Parent was a right child
					else {
						getGrandparent().setRightChild(sibling);
					}
				}
				// Make sibling the new root
				else if(getParent().isRoot()) {
					sibling.setParent(null);
					tree.setRoot(sibling);
				}
				// Make parent a child of sibling
				sibling.setLeftChild(getRBParent());
				getRBParent().setParent(sibling);
				
				// Recursively sort from this
				removeSort(tree);
			}
			
			/***********************/
			
			// This is a right child
			else {
				System.out.println("Case 1:2");
				// Sibling has a right child, preserve it
				if(sibling.getRBRightChild() != null) {
					sibling.getRBRightChild().setParent(getRBParent());
					getRBParent().setLeftChild(sibling.getRBRightChild());
				}
				// Sibling has no right child
				else {
					getRBParent().setLeftChild(null);
				}
				// Make sibling a child of grandparent
				if(getGrandparent() != null) {
					sibling.setParent(getGrandparent());
					// Parent was a left child
					if(getGrandparent().getRBLeftChild() != null && getGrandparent().getRBLeftChild() == getParent()) {
						getGrandparent().setLeftChild(sibling);
					}
					// Parent was a right child
					else {
						getGrandparent().setRightChild(sibling);
					}
				}
				// If parent is the root, make sibling the new root
				else if(getParent().isRoot()) {
					sibling.setParent(null);
					tree.setRoot(sibling);
				}
				// Make parent a child of sibling
				sibling.setRightChild(getRBParent());
				getRBParent().setParent(sibling);
				
				// Recursively sort from this
				removeSort(tree);
			}
		}
		
		
		/*
		 * 	CASE 2: Sibling and its children are null or black
		 */
		
		else if(getSibling() != null && getSibling().getIsBlack() && 
				((getSibling().getRBLeftChild() == null || getSibling().getRBLeftChild().getIsBlack()) &&
						(getSibling().getRBRightChild() == null || getSibling().getRBRightChild().getIsBlack()))) {

			System.out.println("Case 2:1");
			// Set sibling to be red
			getSibling().setIsBlack(false);
			// Parent is black, pass on the double black
			if(getRBParent().getIsBlack()) {
				getRBParent().setIsDoubleBlack(true);
			}
			// Parent is red, absorb the double black
			else {
				getRBParent().setIsBlack(true);
			}
			// This node is no longer double black
			setIsDoubleBlack(false);
			//Recursively sort from parent
			getRBParent().removeSort(tree);
		}
		
		
		/*
		 * 	CASE 3: Sibling is black and one of its children is red
		 */
		
		else if(getSibling() != null && getSibling().getIsBlack() && 
				((getSibling().getRBLeftChild() != null && getSibling().getRBLeftChild().getIsBlack()) ||
						(getSibling().getRBRightChild() != null && getSibling().getRBRightChild().getIsBlack()))) {

			RBTreeNode sibling = getSibling();
			
			if(getRBParent().getRBLeftChild() == this) {

				// This is a left child and sibling's right child is red
				if(!sibling.getRBRightChild().getIsBlack()) {
					System.out.println("Case 3:1");

					// Sibling has a left child, preserve it
					if(sibling.getRBLeftChild() != null) {
						sibling.getRBLeftChild().setParent(getRBParent());
						getRBParent().setRightChild(sibling.getRBLeftChild());
					}
					// Sibling has no left child
					else {
						getRBParent().setRightChild(null);
					}
					// Make sibling a child of grandparent
					if(getGrandparent() != null) {
						sibling.setParent(getGrandparent());
						// Parent was a left child
						if(getGrandparent().getRBLeftChild() != null && getGrandparent().getRBLeftChild() == getParent()) {
							getGrandparent().setLeftChild(sibling);
						}
						// Parent was a right child
						else {
							getGrandparent().setRightChild(sibling);
						}
					}
					// Make sibling the new root
					else if(getParent().isRoot()) {
						sibling.setParent(null);
						tree.setRoot(sibling);
					}
					// Make parent a child of sibling
					sibling.setLeftChild(getRBParent());
					getRBParent().setParent(sibling);
					// Colour sibling colour of former parent
					sibling.setIsBlack(sibling.getRBLeftChild().getIsDoubleBlack());
					// Set sibling's children to be black
					sibling.getRBRightChild().setIsBlack(true);
					sibling.getRBLeftChild().setIsBlack(true);
					// Double black is gone
					setIsDoubleBlack(false);
					// Recursively sort from parent
					getRBParent().removeSort(tree);
				}
				
				/**********************/
				
				// This is a left child and sibling's left child is red
				if(!sibling.getRBLeftChild().getIsBlack()) {
					
					RBTreeNode siblingLeft = sibling.getRBLeftChild();
					System.out.println("Case 3:2");
					// Sibling's left has a left child, preserve it
					if(siblingLeft.getRBLeftChild() != null) {
						siblingLeft.getRBLeftChild().setParent(getRBParent());
						getRBParent().setRightChild(siblingLeft.getRBLeftChild());
					}
					// Sibling has no left child
					else {
						getRBParent().setRightChild(null);
					}
					// Sibling's left has a right child, preserve it
					if(siblingLeft.getRBRightChild() != null) {
						siblingLeft.getRBRightChild().setParent(sibling);
						sibling.setLeftChild(siblingLeft.getRBRightChild());
					}
					// Sibling's left has no right child
					else {
						sibling.setLeftChild(null);
					}
					// Make sibling's left a child of grandparent
					if(getGrandparent() != null) {
						siblingLeft.setParent(getGrandparent());
						// Parent was a left child
						if(getGrandparent().getRBLeftChild() != null && getGrandparent().getRBLeftChild() == getParent()) {
							getGrandparent().setLeftChild(siblingLeft);
						}
						// Parent was a right child
						else {
							getGrandparent().setRightChild(siblingLeft);
						}
					}
					// Make sibling the new root
					else if(getParent().isRoot()) {
						siblingLeft.setParent(null);
						tree.setRoot(siblingLeft);
					}
					// Make parent a child of sibling's left
					siblingLeft.setLeftChild(getRBParent());
					getRBParent().setParent(sibling);
					// Colour sibling colour of former parent
					sibling.setIsBlack(sibling.getRBLeftChild().getIsDoubleBlack());
					// Make sibling a child of sibling's left
					siblingLeft.setRightChild(sibling);
					sibling.setParent(siblingLeft);
					// Set sibling's right child to be black
					siblingLeft.setIsBlack(true);
					// Double black is gone
					setIsDoubleBlack(false);
					// Recursively sort from parent
					getRBParent().removeSort(tree);
				
				}

			}
			

			/**********************/
			/**********************/
			
			
			else {

				// This is a right child and sibling's left child is red
				if(!sibling.getRBLeftChild().getIsBlack()) {
					System.out.println("Case 3:3");
					// Sibling has a right child, preserve it
					if(sibling.getRBRightChild() != null) {
						sibling.getRBRightChild().setParent(getRBParent());
						getRBParent().setLeftChild(sibling.getRBRightChild());
					}
					// Sibling has no left child
					else {
						getRBParent().setLeftChild(null);
					}
					// Make sibling a child of grandparent
					if(getGrandparent() != null) {
						sibling.setParent(getGrandparent());
						// Parent was a left child
						if(getGrandparent().getRBLeftChild() != null && getGrandparent().getRBLeftChild() == getParent()) {
							getGrandparent().setLeftChild(sibling);
						}
						// Parent was a right child
						else {
							getGrandparent().setRightChild(sibling);
						}
					}
					// Make sibling the new root
					else if(getParent().isRoot()) {
						sibling.setParent(null);
						tree.setRoot(sibling);
					}
					// Make parent a child of sibling
					sibling.setRightChild(getRBParent());
					getRBParent().setParent(sibling);
					// Colour sibling colour of former parent
					sibling.setIsBlack(sibling.getRBLeftChild().getIsDoubleBlack());
					// Set sibling's right child to be black
					sibling.getRBLeftChild().setIsBlack(true);
					// Recursively sort from parent
					getRBParent().removeSort(tree);
				}
				
				/**********************/
			
			// This is a right child and sibling's right child is red
				if(!sibling.getRBRightChild().getIsBlack()) {
					RBTreeNode siblingRight = sibling.getRBRightChild();
					System.out.println("Case 3:4");
					// Sibling's right has a right child, preserve it
					if(siblingRight.getRBRightChild() != null) {
						siblingRight.getRBRightChild().setParent(getRBParent());
						getRBParent().setLeftChild(siblingRight.getRBRightChild());
					}
					// Sibling has no left child
					else {
						getRBParent().setLeftChild(null);
					}
					// Sibling's left has a left child, preserve it
					if(siblingRight.getRBLeftChild() != null) {
						siblingRight.getRBLeftChild().setParent(sibling);
						sibling.setRightChild(siblingRight.getRBLeftChild());
					}
					// Sibling's left has no right child
					else {
						sibling.setRightChild(null);
					}
					// Make sibling's left a child of grandparent
					if(getGrandparent() != null) {
						siblingRight.setParent(getGrandparent());
						// Parent was a left child
						if(getGrandparent().getRBLeftChild() != null && getGrandparent().getRBLeftChild() == getParent()) {
							getGrandparent().setLeftChild(siblingRight);
						}
						// Parent was a right child
						else {
							getGrandparent().setRightChild(siblingRight);
						}
					}
					// Make sibling the new root
					else if(getParent().isRoot()) {
						siblingRight.setParent(null);
						tree.setRoot(siblingRight);
					}
					// Make parent a child of sibling's left
					siblingRight.setRightChild(getRBParent());
					getRBParent().setParent(sibling);
					// Colour sibling colour of former parent
					sibling.setIsBlack(sibling.getRBLeftChild().getIsDoubleBlack());
					// Make sibling a child of sibling's left
					siblingRight.setLeftChild(sibling);
					sibling.setParent(siblingRight);
					// Set sibling's right child to be black
					siblingRight.setIsBlack(true);
					// Double black is gone
					setIsDoubleBlack(false);
					// Recursively sort from parent
					getRBParent().removeSort(tree);
				}
			}	
		}
	}


	/*
	 * 	Find a black node in the tree
	 */
	public DataADT find(String aKeyString){

		System.out.println("RBTreeNode::find(String)");

		DataADT temp = new Data(aKeyString); //turn the string into a data object for comparisons

		//compare this node's key using a temporary data object 
		int comparision = temp.compare(this.getData());

		if(comparision < 0 && leftChild != null) {
			//search the left subtree
			return leftChild.find(aKeyString);
		}

		else if(comparision > 0 && rightChild != null) {
			//search the right subtree
			return rightChild.find(aKeyString);
		}
		else if(comparision == 0){
			setSelection(true); //select the found node
			return getData();

		}

		return null;
	}

	
	/*
	 *	Gets this node's sibling 
	 */
	public RBTreeNode getSibling() {
		// No sibling exists
		if(getRBParent() == null) {
			return null;
		}
		// Sibling is a right child
		if(getRBParent().getRBLeftChild() == this && getRBParent().getRBRightChild() != null) {
			return getRBParent().getRBRightChild();
		}
		// Sibling is a left child
		else if(getRBParent().getRBRightChild() == this && getRBParent().getRBLeftChild() != null) {
			return getRBParent().getRBLeftChild();
		}
		// No sibling exists
		else {
			return null;
		}
	}
	
	/*
	 * 	Finds the smallest node by traveling left
	 */
	private RBTreeNode findSmallestNode(){
		//find the smallest node searching from this node along the left subtrees
		//in a binary search tree the node with the smallest key will be found by
		//going left as far as possible.
		
		if(leftChild == null) return this;
		else return ((RBTreeNode) leftChild).findSmallestNode();
		
	}
	

	/*
	 * 	Replaces a node to be removed with its child node
	 */
	private RBTreeNode replaceChildNode(TreeNode currentChild, TreeNode newChild){
		//replace the currentChild of this node with the newChild node
		if(leftChild == currentChild){
			removeChildNode(currentChild);
			leftChild = (RBTreeNode) newChild;
			newChild.setParent(this);
			// One node was red, no change in height
			if(!((RBTreeNode) currentChild).getIsBlack() || !((RBTreeNode) newChild).getIsBlack()) {
				getRBLeftChild().setIsBlack(true);
			}
			// Both nodes were black, create double black
			else {
				getRBLeftChild().setIsBlack(true);
				getRBLeftChild().setIsDoubleBlack(true);
			}
			return getRBLeftChild();
		}
		else if(rightChild == currentChild){
			removeChildNode(currentChild);
			rightChild = (RBTreeNode) newChild;
			newChild.setParent(this);
			// One node was red, no change in height
			if(!((RBTreeNode) currentChild).getIsBlack() || !((RBTreeNode) newChild).getIsBlack()) {
				getRBRightChild().setIsBlack(true);
			}
			// Both nodes were black, create double black
			else {
				getRBRightChild().setIsBlack(true);
				getRBRightChild().setIsDoubleBlack(true);
			}
			return getRBRightChild();
		}
		System.out.println("ERROR: No child to replace.");
		return null;
	}
	
	
	/*
	 * 	Removes the specified child 
	 */
	public void removeChildNode(TreeNode aChildNode){
		//remove the child aChildNode from this node 
    	if(leftChild == aChildNode) leftChild = null;
    	else if(rightChild == aChildNode) rightChild = null;
    	
    	aChildNode.setParent(null);
    }

}

	
	

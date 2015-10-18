package comp2402TreeEditor;

import java.awt.Point;

import javax.swing.JOptionPane;


public class RBTree extends BSTree{
	//       =====
	// This class represents a Red Black Tree
	
    
	//CONSTRUCTORS==================================================================
	public RBTree() {
    }
	
	public RBTreeNode getRBRoot() { return (RBTreeNode) getRoot(); } 
	

    public void insert(String dataString){
    	  	  
   	
    	System.out.println("\nRBNode::insert(String)");
    	Data data = new Data(dataString);

    	TreeNode newChildNode = new RBTreeNode(data);

    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    		getRBRoot().setIsBlack(true);
    	} 
    	else {
    		getRBRoot().insertNode(newChildNode);
    		insertSort(newChildNode);
    	}
    }

    
    
    /*
	 * O = black
	 * X = red
	 * 
	 * g = grandparent
	 * u = uncle
	 * p = parent
	 * t = this
	 * 
	 */
	public void insertSort(TreeNode aNode) {

		if(!(aNode instanceof RBTreeNode)) {
			return;
		}
		
		
		RBTreeNode node = (RBTreeNode) aNode;
		
		if(node.getParent() == null || node.getGrandparent() == null) {
			return;
		}
		
		// While this and its parent are both red
		if(!node.getIsBlack() && !node.getRBParent().getIsBlack()) {
			// If parent's sibling is black
			if(node.getUncle() == null || node.getUncle().getIsBlack()) {
				RBTreeNode tempGrandparent = node.getGrandparent();
				// Restructure
				switch(node.getOrientation().name()) {
				case "LEFT_LINE":
					
					
					/*	
					 *     g O          p O
					 *      / \          / \
					 *   p X u O -->  t X g X
					 *	  /                  \
					 * t X                  u O
					 * 
					 */
					
					
					// Preserve parent's right child
					if(node.getRBParent().rightChild() != null) {
						tempGrandparent.setLeftChild(node.getRBParent().getRBRightChild());
						node.getRBParent().rightChild().setParent(tempGrandparent);
					} 
					else {
						tempGrandparent.setLeftChild(null);
					}
					// Make parent a child of grandparent's parent
					if(tempGrandparent.isRoot()) {
						node.getRBParent().setParent(null);
						setRoot(node.getParent());
					}
					else if(tempGrandparent.getRBParent().rightChild() == tempGrandparent) {
						tempGrandparent.getRBParent().setRightChild(node.getRBParent());
						node.getRBParent().setParent(tempGrandparent.getRBParent());
					}
					else {
						tempGrandparent.getRBParent().setLeftChild(node.getRBParent());
						node.getRBParent().setParent(tempGrandparent.getRBParent());
					}
					// Make grandparent a child of parent
					node.getRBParent().setRightChild(tempGrandparent);
					tempGrandparent.setParent(node.getRBParent());

					// Set parent to black and grandparent to red
					node.getRBParent().setIsBlack(true);
					tempGrandparent.setIsBlack(false);
					System.out.println("Left Line");

					// Recursively sort
					insertSort(node.getRBParent());
					break;
				case "LEFT_CURVE":
					
			
					/*	 
					 *    g O          g O
					 *     / \          / \
					 *  p X u O -->  t X u O
					 * 	   \          /
					 * 	  t X      p X
					 *
					 */
					
					
					// Preserve this left child
					if(node.getRBLeftChild() != null) {
						node.getRBParent().setRightChild(node.getRBLeftChild());
						node.getRBLeftChild().setParent(node.getRBParent());
					}
					else {
						node.getRBParent().setRightChild(null);
					}
					// Swap this with its parent
					node.setLeftChild(node.getRBParent());
					node.getRBParent().setParent(node);
					// Make this its grandparent's child
					node.setParent(tempGrandparent);
					tempGrandparent.setLeftChild(node);
					System.out.println("Left Curve");

					// Recursively sort
					insertSort(node.getRBLeftChild());
					break;
				case "RIGHT_LINE":
					
					System.out.println("Right Line");

					
					/*	
					 *     g O          p O
					 *      / \          / \
					 *   u O p X -->  g X t X
					 *	        \      /
					 *         t X  u O
					 * 
					 */
					
					
					// Preserve parent's right child
					if(node.getRBParent().leftChild() != null) {
						tempGrandparent.setRightChild(node.getRBParent().getRBLeftChild());
						node.getRBParent().leftChild().setParent(tempGrandparent);
						System.out.println("1");

					}
					else {
						tempGrandparent.setRightChild(null);	
					}
					// Make parent a child of grandparent's parent
					if(tempGrandparent.isRoot()) {
						node.getRBParent().setParent(null);
						setRoot(node.getParent());
						System.out.println("2");

					}
					else if(tempGrandparent.getRBParent().rightChild() == tempGrandparent) {
						tempGrandparent.getRBParent().setRightChild(node.getRBParent());
						node.getRBParent().setParent(tempGrandparent.getRBParent());
						System.out.println("3");

					}
					else {
						tempGrandparent.getRBParent().setLeftChild(node.getRBParent());
						node.getRBParent().setParent(tempGrandparent.getRBParent());
						System.out.println("4");

					}
					// Make grandparent a child of parent
					node.getRBParent().setLeftChild(tempGrandparent);
					tempGrandparent.setParent(node.getRBParent());
					// Set parent to black and grandparent to red
					node.getRBParent().setIsBlack(true);
					tempGrandparent.setIsBlack(false);
					// Recursively sort
					insertSort(node.getRBParent());


					break;
				case "RIGHT_CURVE":
					
						
					/*	  
					 *    g O          g O
					 *     / \          / \
					 *  u O p X -->  u O t X
					 * 	     /              \
					 * 	  t X      		   p X
					 *
					 */
					
					
					// Preserve this right child
					if(node.getRBRightChild() != null) {
						node.getRBParent().setLeftChild(node.getRBRightChild());
						node.getRBRightChild().setParent(node.getRBParent());
					}
					else {
						node.getRBParent().setLeftChild(null);
					}
					// Swap this with its parent
					node.setRightChild(node.getRBParent());
					node.getRBParent().setParent(node);
					// Make this its grandparent's child
					node.setParent(tempGrandparent);
					tempGrandparent.setRightChild(node);
					System.out.println("Right Curve");

					// Recursively sort
					insertSort(node.getRBRightChild());
					break;
				case "NOT_LINE":
					insertSort(node.getRBParent());
				default:
					insertSort(node.getRBParent());
				}
			}
			// If parent's sibling is red
			else {
				
				/*	 
				 *     g O          g X
				 *      / \          / \
				 *   p X u X -->  p O u O
				 *    /            /
				 * t X          t X
				 *
				 * Not limited to this specific location of t
				 *
				 */
				
				// Recolour
				node.getRBParent().setIsBlack(true);
				node.getGrandparent().setIsBlack(false);
				if(node.getUncle() != null) {
					node.getUncle().setIsBlack(true);
				}
				System.out.println("Recolour");

				// Recursively sort up the tree
				insertSort(node.getRBParent());
			}
			System.out.println("Loop");
		}
		// If no error is found, check parent
		else {
			insertSort(node.getRBParent());
		}
		getRBRoot().setIsBlack(true);
	}

	
	/*
	 * 	Removes a node with a specified key from the tree
	 */
	public void remove(String aKeyString) {
		// Tree is empty
		if(isEmpty()) return;
		// Remove the node with the specified string using BSTree remove
		RBTreeNode sortNode = getRBRoot().remove(aKeyString, this);
		if(sortNode != null) {
			// Sort the tree starting from the removed node
			sortNode.removeSort(this);
		}
		else {
			System.out.println("Tree is empty or node is not found.");
		}
	}
	
	  
	    public void createChildForNode(TreeNode aNode, Point aLocation){
	    	/*Graphical creating of nodes is not allowed for a red black tree
	    	 *since the tree
	    	 *wants to control where Nodes are placed.
	    	*/
	         
	        JOptionPane.showMessageDialog(getOwner(), 
	        "MUST use ADT Insert to add nodes to Binary Heap", 
	        "Not allowed for Binary Heap", 
	        JOptionPane.ERROR_MESSAGE);
	  
	    	
	    }

	    public boolean allowsGraphicalDeletion(){ 
	    //Red Bloack trees  do not allow the deletion of arbitrary nodes since the tree
	    //must get a chance to restore itself. The TreeADT remove method should be used
	    //to delete nodes
	      return false;
	    }


}


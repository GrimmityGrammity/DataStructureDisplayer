package comp2402TreeEditor;

import java.util.*;
import java.awt.*;
import java.io.*;

import javax.swing.JOptionPane;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions or defects that are
//for illustration or assignment purposes

//This code is based on hierarchy that still requires lots of casting
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class BinaryHeap extends BTree implements BTreeADT{
	//       =====
    //This class represents a binary heap
	
	private static final int ADDED_NODE = 0;
	private static final int REMOVED_NODE = 1;
	
	private BinaryHeapNode lastNode = null;
    
	//CONSTRUCTORS==================================================================
	public BinaryHeap() {
    }
	
	public BinaryHeapNode getLastNode() { return lastNode; }
	public void setLastNode(TreeNode newLastNode) { lastNode = (BinaryHeapNode) newLastNode; } 
	
	
	public DataADT top(){
		//Answer the top (root) data item in the heap which should be the smallest
		//This operation allows us to inspect the top of the heap
		
		if(this.isEmpty()) return null;
		else return root().getData();
	}
	
	public DataADT removeTop(){
		//This is the main operation that a heap is designed for.
		//Remove the top (root) node from heap and return its data item
		//which should be the one with the smallest key value
		//The heap should re-adjust in O(log(n)) time.
		
	   if(this.isEmpty()) return null;
	   
	   DataADT rootData = root().getData();
	   removeNode((BinaryHeapNode) root());
	   return rootData;
	}


    //BTreeADT inteface methods =========================================================
     public void insert(String dataString){
    	
    	/*A binary heap tree inserts nodes based on the key value such that for any internal
    	 * node it's key value is smaller, or a at least no bigger, then those of its two children
    	 * This heap condition is recursively true for any internal node.
    	 */
    	//heap is at most O(log(n)) high for a heap of n nodes
   	  
   	    
    	
   	    System.out.println("BHeapNode::insert(String)");
    	Data data = new Data(dataString);
    	    	
    	TreeNode newChildNode = new BinaryHeapNode(data);
    	
    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    		setLastNode(newChildNode);
    	} else {
    		insertNode(newChildNode);	
    	} 	   	
    }
     
     private void insertNode(TreeNode aNode){
    	 
 		if(!(aNode instanceof BinaryHeapNode)) return;
		
 		// Branch is full, search for the next free spot
 		if(getLastNode().leftChild != null && getLastNode().rightChild != null) {
 			setLastNode(getLastNode().findLastNode(ADDED_NODE));
 		}
 		// Insert node and sort up
 		getLastNode().insertChildNode((BinaryHeapNode) aNode);
 		upHeap();
     }
     
    
    public void remove(String aKeyString){
    	
    	//Remove a node whose key value is aKeyString
    	//It will take O(n) to find the node to remove but from that point
    	//the removal  of the node will take O(log(n)) time

   	    System.out.println("BHeapNode::remove(String)");
    	BinaryHeapNode nodeToRemove = null;	

    	//O(N) to find a  node to remove
    	//Heaps are not designed for searching so this is an O(n)
    	//operation
    	for(TreeNodeADT t : this.nodes()){
    		if(t.getData().key().equals(aKeyString)) {
    			nodeToRemove = (BinaryHeapNode) t;
    			break;
    		}

    	}
    	if(nodeToRemove == null) return;

    	//remove the nodeToRemove from the heap and restore heap property
    	//by bubbling bigger key values down.
    	//The removal takes no more than O(log(n)) time
    	removeNode(nodeToRemove);

    }
    
 
    /*	Removes a BinaryHeapNode from the heap by swapping the node
	 * 	to be removed's data with its parent's data. This is done until the 
	 * 	root node is reached. The last node's data is then placed in the root
	 * 	node position and the last node is removed. The heap is then sorted down.
	 * 
	 *  Runtime: O(height)
	 */
    private void removeNode(BinaryHeapNode nodeToRemove){

    	// Node to contain the location of the root node
		BinaryHeapNode rootNode = (BinaryHeapNode) getRoot();

    	// lastNode contains a pointer to the parent of the actual
    	// last node for heap travel convenience. The following is a 
    	// check to discover which of the children are the actual last node.

    	// If the nodeToRemove is the only remaining node, remove it
    	if(nodeToRemove.isRoot() && nodeToRemove.leftChild() == null && 
    			nodeToRemove.rightChild() == null){
    		setRoot(null);
    	} 
    	else {
	    		// Last node is the right child
	    		if(getLastNode().rightChild() != null) {
	    			// Set searchNode to the nodeToRemove
	    			BinaryHeapNode searchNode = nodeToRemove;
	
	    			// Swap the data of seachNode with its parent's data, and then
	    			// travel up the tree to the parent. Repeat until root node is 
	    			// reached.
	    			while(!searchNode.isRoot()){
	    				searchNode.setData(searchNode.getParent().getData());
	    				searchNode = (BinaryHeapNode) searchNode.getParent();
	    			}

	    			// Swap the data of the last node with the root node
	    			searchNode.setData(getLastNode().rightChild().getData());
	    			// Remove the last node
	    			getLastNode().removeChildNode(getLastNode().rightChild());
	
	
	
	    			// Last node is the left child
	    		} else if(getLastNode().leftChild() != null) {
	    			BinaryHeapNode searchNode = nodeToRemove;
	    			// Swap child data with parent's until root node
	    			while(!searchNode.isRoot()){
	    				searchNode.setData(searchNode.getParent().getData());
	    				searchNode = (BinaryHeapNode) searchNode.getParent();
	    			}

	    			// Swap the data of the last node with the last node
	    			searchNode.setData(getLastNode().leftChild().getData());
	    			// Remove the last node
	    			getLastNode().removeChildNode(getLastNode().leftChild());
	    			// Because the last node was a left child, a new last node must
	    			// be found, so find it
	     			setLastNode(getLastNode().findLastNode(REMOVED_NODE));
	
	    		} else {
	    			System.out.println("ERROR: No data swapped, something went wrong.");
	    			return;
	    		}

	    	// Sort the heap
	    	// O(log(n)) time
	    	downHeap(rootNode);
    	}
    }
    
    
    /*	Sort the heap starting from the root
     * 
     * 	Runtime: O(height)
     */
 	private void downHeap(BinaryHeapNode rootNode){
		
		// searchNode and swapNode for sorting data in while loop
		BinaryHeapNode searchNode = rootNode;
		BinaryHeapNode swapNode = new BinaryHeapNode();
		

		// Sorts the heap data from the root down
		while(!searchNode.isLeaf()){

			// If the searchNode data is greater than the left child and the right child is null
			// or
			// if the searchNode data is greater than the left child and the left child is less 
			// than or equal to the right child, 
			// then swap the searchNode and the left child
			if((searchNode.getData().compare(searchNode.leftChild().getData()) > 0 && searchNode.rightChild() == null) || 
					(searchNode.getData().compare(searchNode.leftChild().getData()) > 0 && searchNode.rightChild() != null && 
					(searchNode.leftChild().getData().compare(searchNode.rightChild().getData()) < 0 ||
							searchNode.leftChild().getData().compare(searchNode.rightChild().getData()) == 0))){
				// Swap searchNode and left child data
				swapNode.setData(searchNode.leftChild().getData());
				searchNode.leftChild().setData(searchNode.getData());
				searchNode.setData(swapNode.getData());
				
				// Travel down the heap through the left child
				searchNode = (BinaryHeapNode) searchNode.leftChild();
			}
			
			// Same as above but with the right child
			else if(searchNode.rightChild() != null && searchNode.getData().compare(searchNode.rightChild().getData()) > 0 && 
					searchNode.rightChild().getData().compare(searchNode.leftChild().getData()) < 0){
				// Swap searchNode and right child data
				swapNode.setData(searchNode.rightChild().getData());
				searchNode.rightChild().setData(searchNode.getData());
				searchNode.setData(swapNode.getData());
				
				// Travel down the heap through the right child
				searchNode = (BinaryHeapNode) searchNode.rightChild();
			}
			else break;
			
		}
 	}

 	
 	 /*	Sort up the heap starting from the lastNode
     * 
     * 	Runtime: O(log(n))
     */
 	private void upHeap(){
		
		// searchNode and swapNode for sorting data in while loop
		BinaryHeapNode searchNode = getLastNode();
		BinaryHeapNode swapNode = new BinaryHeapNode();
		
		// If the parent is the root, then there is no reason to
		// sort as it would have been sorted upon insertion
		if(searchNode.isRoot()){
			return;
		}
		// Sorts the heap data from the lastNode up
		while(!searchNode.isRoot()){

			// If the searchNode data is less than the parent, swap the
			// searchNode and the parent
			if(searchNode.getData().compare(searchNode.getParent().getData()) < 0){
				// Swap searchNode and left child data
				swapNode.setData(searchNode.getParent().getData());
				searchNode.getParent().setData(searchNode.getData());
				searchNode.setData(swapNode.getData());
				
				// Travel down the heap through the left child
				searchNode = (BinaryHeapNode) searchNode.getParent();
			}
			else break;
			
		}
 	}
    

    //===================================================================================
    
    public void createNewRoot(Point aLocation){
    	//create a new root for the tree    	
    	setRoot(new BinaryHeapNode(aLocation));
    }    
    public void createChildForNode(TreeNode aNode, Point aLocation){
    	/*Graphical creating of nodes is not allowed for a binary heap
    	 *since the heap
    	 *wants to control where Nodes are placed.
    	*/
         
        JOptionPane.showMessageDialog(getOwner(), 
        "MUST use ADT Insert to add nodes to Binary Heap", 
        "Not allowed for Binary Heap", 
        JOptionPane.ERROR_MESSAGE);
  
    	
    }

    public boolean allowsGraphicalDeletion(){ 
    //Binary Heaps  do not allow the deletion of arbitrary nodes since the heap
    //must get a chance to restore itself. The TreeADT remove method should be used
    //to delete nodes
      return false;
    }

}

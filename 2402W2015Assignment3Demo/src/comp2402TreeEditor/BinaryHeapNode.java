package comp2402TreeEditor;

import java.util.*;
import java.awt.*;
import java.io.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions or defects that are
//for illustration or assignment purposes
//
//This code is based on a hierarchy that still requires lots of casting

//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class BinaryHeapNode extends BTreeNode implements BTreeNodeADT{
	//This class represents a node in a Binary Heap
	

	// CONSTRUCTORS ========================================================
	public BinaryHeapNode() {
	}

	public BinaryHeapNode(Point aPoint) {
		super(aPoint);
	}
	
	public BinaryHeapNode(Data data) {
		super(data);
	}
	
	
    //===================================================================================

	/*	Insert a child node.
	 * 	If the node to be inserted is greater than this node, swap them.
	 * 
	 * 	Runtime: O(1)
	 */
	public void insertChildNode(BinaryHeapNode aNode){
		//This insert method of a Binary Heap inserts aNode
		//in heap order.

		// If aNode's value is less than this node's value, turn this node into a leaf of aNode
		if(aNode.getData().compare(getData()) < 0){
			
			//check if left child is free
			if(leftChild == null) {
				//left child is free
				//put aNode in this node's place and set left child to this
				aNode.setParent(this);
				leftChild = aNode;
				aNode.setLocation(this.getLocation()); //starting location for animation
				DataADT temp = aNode.getData();
				aNode.setData(this.getData());
				this.setData(temp);
			}

			//Since left child exists, check if right child is free
			else if(rightChild == null) {
				//right child is free
				//put aNode in this node's place and set right child to this
				aNode.setParent(this);
				rightChild = aNode;
				aNode.setLocation(this.getLocation()); //starting location for animation
				DataADT temp = aNode.getData();
				aNode.setData(this.getData());
				this.setData(temp);
			}			
		}
		// If aNode's value is greater than this node's value, turn aNode into a leaf of this node
		else{
			
			//check if left child is free
			if(leftChild == null) {
				//left child is free
				//set aNode as this node's left child
				aNode.setParent(this);
				leftChild = aNode;
				aNode.setLocation(this.getLocation()); //starting location for animation
			}

			//Since left child exists, check if right child is free
			else if(rightChild == null) {
				//right child is free
				//set aNode as this node's right child
				aNode.setParent(this);
				rightChild = aNode;
				aNode.setLocation(this.getLocation()); //starting location for animation
			}
		}
	}
	
	
	    /*	Finds the new last node by traveling up the heap.
	     * 	Check if the right node is the node we came from, if not, travel
	     * 	down that path.
	     * 	If we reach the root before be find a path and we came from the 
	     * 	right, go to the far left, else, go right once, then far left.
	     * 
	     * 	Runtime: O(height)
	     */
	    public BinaryHeapNode findLastNode(int comparison){
	    	if(this.isRoot() && this.leftChild() != null){
				// Set current last node to be new last node
				return (BinaryHeapNode) this.leftChild();
	    	}
	    	else if(this.isRoot() && this.isLeaf()){
	    		return this;
	    	}
			// Set pointers to the node to search and the previously searched node
			BinaryHeapNode searchNode = (BinaryHeapNode) this.getParent();
			BinaryHeapNode previousNode = this;
	    	
			// Finding new lastNode after inserting a node
			if(comparison == 0){
				// Travel up until a path to the right is found
				while(searchNode.rightChild() == previousNode){
					
					// If we reach the root and have come from the right, break
					if(searchNode.isRoot()){
						break;
					}
					
					// Go up once
					previousNode = searchNode;
					searchNode =  (BinaryHeapNode) previousNode.getParent();
				}
				
				// If we have come from the left, go right once
				if(searchNode.leftChild() == previousNode){
					searchNode = (BinaryHeapNode) searchNode.rightChild();
				}
				
				// Travel left until a leaf is reached
				while(!searchNode.isLeaf()){
					searchNode = (BinaryHeapNode) searchNode.leftChild();
				}
				
				return searchNode;
			}
			
			// Finding new lastNode after removing a node
			if(comparison == 1){
				
				// Travel up until a path to the left is found
				while(searchNode.leftChild() == previousNode){
					
					// If we reach the root and have come from the left, break
					if(searchNode.isRoot()){
						break;
					}
					
					// Go up once
					previousNode = searchNode;
					searchNode =  (BinaryHeapNode) previousNode.getParent();
				}
				
				// If we have come from the right, go left once
				if(searchNode.rightChild() == previousNode && !searchNode.leftChild().isLeaf()){
					searchNode = (BinaryHeapNode) searchNode.leftChild();
				}
				
				// Travel right until a leaf is reached
				while(!searchNode.rightChild().isLeaf()){
					searchNode = (BinaryHeapNode) searchNode.rightChild();
				}
				
				return searchNode;
			}
			
			System.out.println("ERROR: No new lastNode could be found.");
			return this;
			
	    	
	    }
	    
	    
	    /*	Searches heap for value
	     * 
	     * 	RuntimeL O(n)
	     */
	    public DataADT find(String aKeyString){

	    	System.out.println("BHeapNode::find(String)");

	    	DataADT temp = new Data(aKeyString); //turn the string into a data object for comparisons
	    	DataADT data = getData();

	    	//compare this node's key using a temporary data object 
	    	int comparision = temp.compare(this.getData());
	    	

	    	if(comparision >= 0 && leftChild != null) {
	    		//search the left subtree
		    	DataADT tempData = leftChild.find(aKeyString);
		    	
		    	if(tempData != null)
		    		data = tempData;
	    	}

	    	if(comparision >= 0 && rightChild != null) {
	    		//search the right subtree
		    	DataADT tempData = rightChild.find(aKeyString);
		    	
		    	if(tempData != null)
		    		data = tempData;
	    	}

	    	if(comparision == 0){
	    		setSelection(true); //select the found node
	    		data = getData();
	    	}
	    	
	    	if(data != null){
	    		// return that we found a node
	    		return data;
	    	}

	    	return null;
	    }
}
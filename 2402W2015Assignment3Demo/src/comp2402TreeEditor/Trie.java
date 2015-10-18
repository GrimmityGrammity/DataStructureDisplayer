package comp2402TreeEditor;

import java.awt.Point;

import javax.swing.JOptionPane;


public class Trie extends Tree{

	//CONSTRUCTORS==================================================================
	public Trie() {
    }
	
	
	// Convert the root to a TrieNode
	public TrieNode getTrieRoot() { return (TrieNode) getRoot(); }
	
	
	/*	Insert a word into the trie
	 * 	Runtime: O(height)
	 */
	public void insert(String dataString){
		
   	    System.out.println("BHeapNode::insert(String)");
    	
   	    // Add root if needed
    	if(isEmpty()){
    		TreeNode root = new TrieNode(new Data(" "));
    		setRoot(root);
    		root.setLocation(getOwner().getDefaultRootLocation());
       	    System.out.println("Add Root");
    	}
    	
    	// Create node for traversing the trie
    	TrieNode searchNode = getTrieRoot();
		int i;
		
		// Parse the input string into characters
		for(i = 0; i < dataString.length(); i++){
			// Convert the character into an index
			int index = dataString.charAt(i) - 'a';
			// Check if there is a node for the character
			if(!searchNode.hasCharAt(index)){
				// Create a node for the character
		    	Data data = new Data(Character.toString((char)(index + 'a')));
				searchNode.addChildNode(new TrieNode(data), index);
		   	    System.out.println("Add new node");

			}
			// Check if the character is valid
			if(searchNode.isValidChar(index)){
				// Move down the trie to the next node
				searchNode = searchNode.getTrieChildren()[index];
			}
			else {
				System.out.println("ERROR: Not valid input. (a-z)");
				return;
			}

		}
		// Once a word has been entered
		// Set the last node to be a word node
		searchNode.setIsWord(true);
		// Set the word to be the input string
		searchNode.setWord(dataString);
   	    System.out.println("Set Word True");
   	    System.out.println("Word: " + searchNode.getWord());
	}
	
	
	/*	Insert a word into the trie
	 * 	Runtime: O(height)
	 */
	public void remove(String dataString){
   	    System.out.println("BHeapNode::remove(String)");
   	    
    	// Create node for traversing the trie
    	TrieNode searchNode = getTrieRoot();
		int i;
		
		// Parse the input string into characters
		for(i = 0; i < dataString.length(); i++){
			// Convert the character into an index
			int index = dataString.charAt(i) - 'a';
			// Check if there is a node for the character
			if(searchNode.hasCharAt(index)){
				// Travel down the tree
    			searchNode = searchNode.getTrieChildren()[index];
			}

			else {
				System.out.println("Index: " + index);
				System.out.println("ERROR: Not valid input. (a-z)");
				return;
			}
		}
		// Word has been found
		// If the word has children, change the node from a word node
		// to a regular node
		if(searchNode.getChildren().size() > 0){
			searchNode.setIsWord(false);
			searchNode.setWord(null);
		}
		// If the word is a leaf, travel up the trie until a branch,
		// the root, or another word is reached
		else {
			while(!searchNode.getTrieParent().isRoot() && searchNode.getTrieParent().getChildren().size() < 2 &&
					!searchNode.getTrieParent().getIsWord()){
				searchNode = searchNode.getTrieParent();
			}
			// Remove the nodes unneeded below the branch/root/word
			searchNode.removeNode(searchNode.getAsciiValue() - 'a');
		}
		
		// In case something is missed, set it to a regular node
		searchNode.setIsWord(false);
		searchNode.setWord(null);
   	    System.out.println("Set Word False");

	}
	
	
	
	
	
    public void createChildForNode(TreeNode aNode, Point aLocation){
	    	/*Graphical creating of nodes is not allowed for a Trie
	    	 *since it must control where Nodes are placed.
	    	*/
	         
	        JOptionPane.showMessageDialog(getOwner(), 
	        "MUST use ADT Insert to add nodes to Trie", 
	        "Operation Not allowed for Trie", 
	        JOptionPane.ERROR_MESSAGE);
	  
	    	
	}

	public boolean allowsGraphicalDeletion(){ 
	    //Tries  do not allow the deletion of arbitrary nodes since the Trie
	    //must get a chance to restore itself. The TreeADT "remove" method should be used
	    //to delete nodes
	      return false;
	}
}


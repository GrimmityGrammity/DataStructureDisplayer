package comp2402TreeEditor;

public class TrieNode extends TreeNode{
	
	private static final int NUM_CHILDREN = 26;
	private boolean isWord;
	private String word;
	
	// Array of valid letters (a-z)
	private TrieNode[] children;

	public TrieNode() {
		children = new TrieNode[NUM_CHILDREN];
		setIsWord(false);
	}
	
	public TrieNode(Data data){
		super(data);
		children = new TrieNode[NUM_CHILDREN];
		setIsWord(false);
	}
	
	public TrieNode getTrieParent() {return (TrieNode) getParent(); }
	public boolean getIsWord() { return isWord; }
	public TrieNode[] getTrieChildren() { return children; }
	public String getWord() { return word; }
	
	public void setWord(String aWord) { word = aWord; }
	public void setIsWord(boolean value) { isWord = value; }
	
	
	/*	Inserts a child node on the graph and 
	 * 
	 * 	Runtime: O(1*)
	 */
	public void addChildNode(TrieNode aNode, int index){
		if(!isValidChar(index)){
			System.out.println("\nERROR: Invalid character. (a-z)\n");
			return;
		}
		aNode.setLocation(this.getLocation());
		aNode.setParent(this);
		this.insertNode(aNode);
		children[index] = aNode;
	}
	
	
	/*	Returns if a character child exists
	 * 
	 * 	Runtime: O(1*)
	 */
	public void removeNode(int index){
		if(!isValidChar(index)){
			System.out.println("\nERROR: Invalid character. (a-z)\n");
			return;
		}
		this.getTrieParent().getTrieChildren()[index] = null;
		this.getTrieParent().removeChildNode(this);
		setParent(null);
	}
	
	
	/*	Returns if a character child exists
	 * 
	 * 	Runtime: O(1)
	 */
	public boolean hasCharAt(int index){
		// Check if character is from a-z
		if(!isValidChar(index) || getTrieChildren()[index] == null){
			System.out.println("Ret Char False");

			return false;
		}
		else{
			System.out.println("Ret Char True");

			return true;
		}
	}
	
	
	/*	Returns if a character is valid (a-z)
	 * 
	 * 	Runtime: O(1)
	 */
	public boolean isValidChar(int index){
		if(index < NUM_CHILDREN && index >= 0){
			System.out.println("Ret True");

			return true;
		}
		else{
			System.out.println("Ret false");

			return false;
		}
	}
	
	
	/*	Searches the trie for a word, if found, travels up
	 * 	the trie and highlights every node until the root
	 * 
	 * 	Runtime: O(height)
	 */
	public DataADT find(String aKeyString){
    	System.out.println("TrieNode::find(String)");
    	
    	TrieNode searchNode = this;
    	
		// Parse the input string into characters
    	for(int i = 0; i < aKeyString.length(); i ++){
			// Convert the character into an index
    		int index = aKeyString.charAt(i) - 'a';
    		if(searchNode.hasCharAt(index)){
    			searchNode = searchNode.getTrieChildren()[index];
    		}
    		else {
    			return null;
    		}
    	}
    	if(searchNode.getIsWord() && searchNode.getWord().equalsIgnoreCase(aKeyString)){
    		DataADT temp = searchNode.getData();
    		/*
    		 * Highlight all nodes leading down to found word
    		while(!searchNode.isRoot()){
    			searchNode.setSelection(true);
    			searchNode = searchNode.getTrieParent();
    		}
    		*/
    		// Highlight single found word
			searchNode.setSelection(true);
    		return temp;
    	}
    	else {
    		return null;
    	}
	}
	
}
	


/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

	private IAVLNode root; // the tree's field 
	private IAVLNode max;
	private IAVLNode min;

	/**
	 *public AVLTree()
	 *creates empty tree
	 *contains only root - an external leaf
	 *complexity O(1)
	 */

	public AVLTree() { // the tree's constructor
		this.root = new AVLNode();
	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *complexity O(1)
	 */
	public boolean empty() {
		return (this.root.getValue() == null); // return true if root is null
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * complexity O(log(n))
	 */
	public String search(int k)
	{
		IAVLNode node=searchNode(k,0);//:we get the node with the key k if it exists
		if( node == null) {
			return null;
		}
		if (node.getKey()==k)
			return node.getValue();
		return null;	  
	}
	/**
	 * private IAVLNode searchNode(int k, int addToSize)
	 *
	 * returns the node with key k if it exists in the tree
	 * otherwise, returns the node it should be inserted below
	 * updates the size of the nodes if needed
	 * complexity O(log(n))
	 */ 
	private IAVLNode searchNode(int k, int addToSize) {
		if(this.root.getValue() == null) {// empty tree
			return null;
		}
		IAVLNode currNode = this.root;
		while((currNode.getKey()!= -1) ){
			currNode.setSize(currNode.getSize() + addToSize);

			if( currNode.getKey() == k) {
				return currNode;
			}
			else {
				if(k<currNode.getKey()) {
					currNode=currNode.getLeft();
				}
				else {
					currNode=currNode.getRight();  
				}
			}	  
		}	  
		return currNode.getParent(); 
	}

	/**
	 *
	 * private void rightRotation(IAVLNode x, IAVLNode y)
	 *
	 * right rotation between the nodes x and y
	 * complexity O(1)
	 *
	 */
	private void rightRotation(IAVLNode x, IAVLNode y) {// x is the parent , y is the son
		// rotation
		IAVLNode parent = x.getParent();
		y.setParent(parent);
		x.setLeft(y.getRight());
		y.getRight().setParent(x);
		y.setRight(x);
		x.setParent(y);

		if(y.getParent() == null) {
			this.root = y;
		}
		else {
			if(parent.getKey() > x.getKey()) {
				parent.setLeft(y);
		}
			else {
				parent.setRight(y);
			}
		// updating the size
		}
		x.setSize(x.getLeft().getSize() + x.getRight().getSize() +1);
		y.setSize(x.getSize() + y.getLeft().getSize() +1);	
	}
	/**
	 * private void leftRotation(IAVLNode y, IAVLNode x)
	 *
	 * left rotation between the nodes y and x
	 * complexity O(1)
	 *
	 */
	private void leftRotation(IAVLNode y, IAVLNode x) {//y is the parent , x is the son
		// rotation
		IAVLNode parent = y.getParent();
		x.setParent(parent);
		y.setRight(x.getLeft());
		x.getLeft().setParent(y);
		x.setLeft(y);
		y.setParent(x);

		if(x.getParent() == null) {
			this.root = x;
		}
		else {
			if(parent.getKey() < x.getKey()) {	
			parent.setRight(x);
		}
			else {
				parent.setLeft(x);
			}
		// update the size
		}
		y.setSize(y.getLeft().getSize() + y.getRight().getSize() + 1);
		x.setSize(y.getSize() + x.getRight().getSize() + 1);
	}
	
	/**
	 *
	 * private void promote(IAVLNode node)
	 *
	 * promote the node's rank by 1
	 * complexity O(1)
	 *
	 */
	private void promote(IAVLNode node) {
		node.setHeight(node.getHeight()+1);
	}

	/**
	 *
	 * private void demote(IAVLNode node)
	 *
	 * decrease the node's rank by 1
	 * complexity O(1)
	 *
	 */
	private void demote(IAVLNode node) {
		node.setHeight(node.getHeight()-1);
	}

	/**
	 * private int rebalanceInsertion(IAVLNode node)
	 *
	 * Rebalance the tree after insertion according to the cases we saw in class
	 * complexity O(log(n))
	 *
	 */
	private int rebalanceInsertion(IAVLNode node) {
		int cnt =0;
		while(node != null) {
			int deltaRankRight = node.getHeight()-node.getRight().getHeight();// delta rank between node and its right son
			int deltaRankLeft = node.getHeight()-node.getLeft().getHeight(); // delta rank between node and its left son
			// the node is 0-1 or 1-0 node
			if (((deltaRankRight==0)&&(deltaRankLeft==1))||(((deltaRankRight==1)&&(deltaRankLeft==0)))) {//
				promote(node);
				cnt++;
			}
			// case 0,2
			if ((deltaRankRight==2)&&(deltaRankLeft==0)){
				int deltaLeftSonRight=node.getLeft().getHeight()-node.getLeft().getRight().getHeight();// delta rank between left son and his right son
				int deltaLeftSonLeft=node.getLeft().getHeight()-node.getLeft().getLeft().getHeight();// delta rank between left son and his left son
				// 0-2 node and it's son is 1-2 node
				if(((deltaLeftSonRight==2)&&(deltaLeftSonLeft==1))) {
					case02And12Rebalance(node);
					cnt += 2 ;// rotation and demote
				}
				// 0-2 node and it's son is 2-1 node
				if((deltaLeftSonRight==1)&&(deltaLeftSonLeft==2)) {
					case02And21Rebalance(node);
					cnt+=5; // double rotation , 2 demotes,  1 promote
				}
				if((deltaLeftSonRight==1)&&(deltaLeftSonLeft==1)) {
					case02And11Rebalance(node);
					cnt += 2;					
				}
			}
			//case 2,0
			if((deltaRankRight==0)&&(deltaRankLeft==2)) {
				int deltaRightSonleft=node.getRight().getHeight()-node.getRight().getLeft().getHeight();// delta rank between right son and his left son
				int deltaRightSonRight=node.getRight().getHeight()-node.getRight().getRight().getHeight(); // delta rank between right son and his right son
				// 2-0 node and his son is 2-1 node
				if(( deltaRightSonleft==1)&& (deltaRightSonRight==2)) {
					case20And21Rebalance(node);
					cnt += 5;	//: 2*1rotataion + 2*1demote + 1promote
				}
				// 2-0 node and it's son is 1-2 node
				if(( deltaRightSonleft==2)&& (deltaRightSonRight==1)) {
					case20And12Rebalance(node);
					cnt += 2; //: 2*2rotataions + 1*2demotes + 1*1promotion			  
				}
				if(( deltaRightSonleft==1)&& (deltaRightSonRight==1)) {
					case20And11Rebalance(node);
					cnt += 2;
				}
			}
			node = node.getParent();
		} 
		return cnt;
	}
	
	/**
	 * private void case02And11Rebalance(IAVLNode node) 
	 *
	 * rebalance the case: 0-2 node and it's son is 1-1 node.
	 * complexity O(1).
	 */
	private void case02And11Rebalance(IAVLNode node) {
		IAVLNode tempLeftNode = node.getLeft();
		rightRotation(node, node.getLeft());
		promote(tempLeftNode);		
	}
	/**
	 * private void case20And11Rebalance(IAVLNode node) 
	 * 
	 * rebalance the case: 2-0 node and it's son is 1-1 node.
	 * complexity O(1).
	 */
	private void case20And11Rebalance(IAVLNode node) {
		IAVLNode tempLeftNode = node.getRight();
		leftRotation(node, node.getRight());
		promote(tempLeftNode);		
	}
	
	/**
	 * private void case02And12Rebalance(IAVLNode node) 
	 * 
	 * rebalance the case: 0-2 node and it's son is 1-2 node.
	 * complexity O(1).
	 */
	private void case02And12Rebalance(IAVLNode node) {
		rightRotation(node,node.getLeft());
		demote(node);
	}
	
	/**
	 * private void case02And21Rebalance(IAVLNode node) 
	 * 
	 * rebalance the case: 0-2 node and it's son is 2-1 node.
	 * complexity O(1).
	 */
	private void case02And21Rebalance(IAVLNode node) {
		leftRotation(node.getLeft(),node.getLeft().getRight());
		rightRotation(node,node.getLeft());
		demote(node);
		demote(node.getParent().getLeft());
		promote(node.getParent());
	}

	/**
	 * private void case20And21Rebalance(IAVLNode node) 
	 * 
	 * rebalance the case: 2-0 node and it's son is 2-1 node.
	 * complexity O(1).
	 */
	private void case20And21Rebalance(IAVLNode node) {
		rightRotation(node.getRight(),node.getRight().getLeft());
		leftRotation(node,node.getRight());
		demote(node);
		demote(node.getParent().getRight());
		promote(node.getParent());
	}
	/**
	 * private void case20And12Rebalance(IAVLNode node) 
	 * 
	 * rebalance the case: 2-0 node and it's son is 1-2 node.
	 * complexity O(1).
	 */
	private void case20And12Rebalance(IAVLNode node) {
		leftRotation(node,node.getRight());
		demote(node);
	}


	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 * complexity O(log(n)).
	 */
	public int insert(int k, String i) {
		int numOfRebalanceOpp= 0 ;
		IAVLNode node = createLeaf(k,i);
		if(this.root.getValue() == null) {// empty tree
			this.root = node;
			this.max = node;
			this.min = node;
			return 0;
		}
		else {// the tree was not empty
			if ((this.min!=null) && (k<this.min.getKey()) ){// update the min pointer
				this.min = node;
			}
			if((this.max!=null)&&(k>this.max.getKey())) { // update the max pointer
				this.max = node;
			}
			if( search(k) == null) { // tree is not empty and node does not exist
				IAVLNode parentNode = searchNode(k,1);
				if(parentNode.getKey()> k) {		
					parentNode.setLeft(node);	
					node.setParent(parentNode);	
					numOfRebalanceOpp = rebalanceInsertion(parentNode);		  	
				}else {
					parentNode.setRight(node);		 
					node.setParent(parentNode);
					numOfRebalanceOpp = rebalanceInsertion(parentNode);
				}
				return numOfRebalanceOpp;
			}
			else {
				return -1;
			} 
		} 
	}
	/**
	 * private IAVLNode createLeaf(int k, String val) 
	 * 
	 * create a new node and set it's right an left sons to be an external leaf.
	 * complexity O(1).
	 */
	private IAVLNode createLeaf(int k, String val) {// create a leaf node
	
		AVLNode rightExternalLeaf = new AVLNode();
		AVLNode leftExternalLeaf = new AVLNode();
		IAVLNode node= new AVLNode(k,val);
		node.setLeft(leftExternalLeaf);
		rightExternalLeaf.setParent(node);
		node.setRight(rightExternalLeaf);
		leftExternalLeaf.setParent(node);
		return node;
	}


	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 * updates the min and max fields 
	 * complexity O(log(n))
	 */
	public int delete(int k)
	{
		int numOfRebalance = 0;
		if(this.root.getValue() == null) {
			return -1;
		}		
		if(search(k) != null) {// node exsit in tree;
			IAVLNode deleteNode = searchNode(k,-1);// return the node and changes nodes' size
			IAVLNode deleteParent = deleteNode.getParent();		
			if(isLeaf(deleteNode)) {// node is leaf				
				deleteLeaf(deleteNode);
				numOfRebalance = rebalanceDeletion(deleteParent);
			}
			else {				
				if(isUnary(deleteNode)) {// node is unary
					deleteUnary(deleteNode);
					numOfRebalance = rebalanceDeletion(deleteParent);
			}
				else {// node has two sons
					IAVLNode deleteSuccessor = successor(deleteNode, -1);// find successor and update the nodes' size
					switchSuccessor(deleteNode, deleteSuccessor);
					deleteSuccessor.setSize(deleteSuccessor.getLeft().getSize() + deleteSuccessor.getRight().getSize() + 1);
					deleteParent = deleteNode.getParent();
					if(isLeaf(deleteNode)) {
						deleteLeaf(deleteNode);
						numOfRebalance = rebalanceDeletion(deleteParent);
					}
					else {
						deleteUnary(deleteNode);
						numOfRebalance = rebalanceDeletion(deleteParent);
					}
				}
			}
		}
		if(this.root==null) {
			this.root=new AVLNode();
		}
		this.min = this.findMin();
		this.max = this.findMax();
		return numOfRebalance;
	}
	/**
	 * private void switchSuccessorNotRightSon(IAVLNode node, IAVLNode successor) 
		
	 * switchig between a node and it's successor, when the node.getRight() != successor
	 * complexity O(1).
	 */
	private void switchSuccessorNotRightSon(IAVLNode node, IAVLNode successor) {
		int tempHeight = node.getHeight();
		IAVLNode tempParent = successor.getParent();// parent of the successor node
		IAVLNode tempSuccessorRight1 = successor.getRight();
		IAVLNode tempSuccessorLeft1 = successor.getLeft();
		if(node.getParent() == null) {//  node is root
			this.root = successor;// update the tree root
			successor.setParent(node.getParent());	
	}
		else {// node is not root
			successor.setParent(node.getParent());
			if (node.getParent().getKey() > node.getKey()) {// node is left son
				node.getParent().setLeft(successor);		
			}
			else {
				node.getParent().setRight(successor);
			}
		}// updating the pointers of the successor's left and right son:
		successor.setRight(node.getRight());
		successor.setLeft(node.getLeft());
		successor.getLeft().setParent(successor);
		successor.getRight().setParent(successor);
		tempParent.setLeft(node);// updating successor's left son pointer
		// updating the pointers of the node's left son, right son and parent:
		node.setRight(tempSuccessorRight1);
		node.setLeft(tempSuccessorLeft1);
		node.getRight().setParent(node);
		node.getLeft().setParent(node);
		node.setParent(tempParent);
		successor.setHeight(tempHeight);
	}
	
	
	/**
	 * private void switchSuccessorNotRightSon(IAVLNode node, IAVLNode successor) 
		
	 * switchig between a node and it's successor, when the node.getRight() == successor
	 * complexity O(1).
	 */
		private void switchSuccessorRightSon(IAVLNode node, IAVLNode successor) {
			int tempHeight = node.getHeight();
			IAVLNode parent = node.getParent();
			IAVLNode tempLeft = node.getLeft();
			if(node.getParent() == null) {// node is the root
				successor.setParent(parent);
				this.root = successor;
			}		
			else {// node is not root
				successor.setParent(parent);
				if (parent.getKey() > node.getKey()) {// node is left son
					parent.setLeft(successor);// updating the parent's left son		
				}
				else {
					parent.setRight(successor);// updating the parent's right son
				}
			}// updating the pointers of the node's left son, right son and parent:
				node.setLeft(successor.getLeft());
				node.setRight(successor.getRight());
				node.getRight().setParent(node);
				node.getLeft().setParent(node);
				node.setParent(successor);
				// updating the pointers of the successor's left son, right son:
				successor.setLeft(tempLeft);
				tempLeft.setParent(successor);
				successor.setRight(node);
				successor.setHeight(tempHeight);		
	}		
		/**
		 * private void switchSuccessor(IAVLNode node, IAVLNode successor)
			
		 * switchig between a node and it's successor
		 * complexity O(1).
		 *
		 */
		private void switchSuccessor(IAVLNode node, IAVLNode successor) {// switch between node and it's successor
			if (node.getRight().getKey() == successor.getKey()) {// successor is node's right son
				 switchSuccessorRightSon( node,successor);
			}
			else {
				 switchSuccessorNotRightSon( node, successor);
			}
		}
	
	/**
	 * private boolean isLeaf(IAVLNode node)
		
	 * return true if node is leaf
	 * complexity O(1).
	 *
	 */
	
	private boolean isLeaf(IAVLNode node) {// return true if node is leaf
		return ((node.getLeft().getKey() == -1) && (node.getRight().getKey() == -1));
	}	
	/**
	 * private boolean isUnary(IAVLNode node)
		
	 * return true if node is unary
	 * complexity O(1).
	 *
	 */
	private boolean isUnary(IAVLNode node) { // return true if node is unary
		return (!(isLeaf(node)) && ((node.getLeft().getKey() == -1) || (node.getRight().getKey() == -1)));
		
	}
		
	/**
	 * private void deleteLeaf(IAVLNode node)
		
	 * @ pre isLeaf(node) == true
	 * delete node from the tree
	 * complexity O(1).
	 *
	 */
	private void deleteLeaf(IAVLNode node) {// delete a leaf
		if(node.getParent() == null) {//the node is root
			this.root = new AVLNode();
		}
		else {
			if(node.getParent().getLeft().getKey() == node.getKey()) {// the node is left's son of its parent
				IAVLNode newExt = new AVLNode();
				IAVLNode newParent = node.getParent();
				newParent.setLeft(newExt);
				newExt.setParent(newParent);		
		}
			else {// the node is right's son of its parent
				IAVLNode newExt = new AVLNode();
				IAVLNode newParent = node.getParent();
				newParent.setRight(newExt);
				newExt.setParent(newParent);
		}
			node.setParent(null);
		}
	}
	
	/**
	 * private void deleteUnary(IAVLNode node)
		
	 * @ pre isUnary(node) == true
	 * delete node from the tree
	 * complexity O(1).
	 *
	 */
	private void deleteUnary(IAVLNode node) {
		IAVLNode newSon;
		if(node.getRight().getKey() != -1) {
			newSon = node.getRight();
		}
		else {
			newSon = node.getLeft();
		}
		if(node.getParent() == null) { // node is root
			this.root = newSon;
			newSon.setParent(null);
		}
		else { // node is not root
			if(node.getKey() < node.getParent().getKey()) {// the node is left's son of its parent
				node.getParent().setLeft(newSon);
				newSon.setParent(node.getParent());
				node.setParent(null);			
			}
			else {// the node is right's son of its parent
				node.getParent().setRight(newSon);
				newSon.setParent(node.getParent());
				node.setParent(null);
			}
		}
	}
	
	/**
	 * private IAVLNode successor(IAVLNode node, int increaseSize)	
	 *finding node's successor and changing the size of the nodes in the route to the successor by increaseSize
	 * 
	 * complexity O(log(n)).
	 *
	 */
	private IAVLNode successor(IAVLNode node, int increaseSize) {
		if (node.getRight().getKey() != -1) {// the node has a right child
			node = node.getRight();
			while(node.getKey() != -1) {
				node.setSize(node.getSize() + increaseSize);
				node = node.getLeft();
			}
			return node.getParent();
		}
		else {// the node does not have a right son
			IAVLNode parent = node.getParent();
			while((parent != null) && (node.getKey() == parent.getRight().getKey()) ) {
				node = parent;
				parent = node.getParent();
			}
			return parent;
		}		
	}
		
	/**
	 * private void case31And11Rebalance(IAVLNode node)
	 * 
	 * rebalance the case : 3-1 node and it's right son is 1-1 node.
	 * complexity O(1)
	 */
	private void case31And11Rebalance(IAVLNode node) {//node is z and node.right is y
		leftRotation(node,node.getRight());
		demote(node);
		promote(node.getParent());
	}
	
	/**
	 * private void case13And11Rebalance(IAVLNode node)
	 * rebalance the case : 1-3 node and it's left son is 1-1 node.
	 * complexity O(1)
	 */
	private void case13And11Rebalance(IAVLNode node) {
		rightRotation(node,node.getLeft());
		demote(node);
		promote(node.getParent());		
	}
	/**
	 * private void case31And21Rebalance(IAVLNode node)
	 * rebalance the case : 3-1 node and it's right son is 2-1 node.
	 * complexity O(1)
	 */
	private void case31And21Rebalance(IAVLNode node) {// 
		leftRotation(node,node.getRight());
		demote(node);
		demote(node);
	}
	/**
	 * private void case13And12Rebalance(IAVLNode node)
	 * rebalance the case : 1-3 node and it's right son is 1-2 node.
	 * complexity O(1)
	 */
	private void case13And12Rebalance(IAVLNode node) {//lior changed name
		rightRotation(node,node.getLeft());
		demote(node);
		demote(node);
	}
	/**
	 * private void case31And12Rebalance(IAVLNode node)
	 * 
	 * rebalance the case : 3-1 node and it's right son is 1-2 node.
	 * complexity O(1)
	 */
	private void case31And12Rebalance(IAVLNode node) {// 
		rightRotation(node.getRight(),node.getRight().getLeft());
		leftRotation(node,node.getRight());
		demote(node);
		demote(node);//demote z by two
		promote(node.getParent());//promote a by 1
		demote(node.getParent().getRight());//demote y by 1	
	}
	
	/**
	 * private void case13And21Rebalance(IAVLNode node)
	 * 
	 * rebalance the case : 1-3 node and it's right son is 2-1 node.
	 * complexity O(1)
	 */
	private void case13And21Rebalance(IAVLNode node) {//
		leftRotation(node.getLeft(),node.getLeft().getRight());
		rightRotation(node,node.getLeft());
		demote(node);
		demote(node);//demote z by two
		promote(node.getParent());//promote a by 1
		demote(node.getParent().getLeft());//demote y by 1	
	}
	
	/**
	 * private int rebalanceDeletion(IAVLNode node)
	 * 
	 * rebalance the case : 1-3 node and it's right son is 2-1 node.
	 * complexity O(1)
	 */
	private int rebalanceDeletion(IAVLNode node) {
		if(node==null)
			return 0;
		int cnt =0;
		while(node != null) {
			int deltaRankRight = node.getHeight()-node.getRight().getHeight();// delta rank between node and its right son
			int deltaRankLeft = node.getHeight()-node.getLeft().getHeight(); // delta rank between node and its left son
			// the tree is rebalanced
			if ((deltaRankRight == 1 && deltaRankLeft==1)||(deltaRankRight == 2 && deltaRankLeft == 1)||(deltaRankRight == 1 && deltaRankLeft==2)) {// the tree is rebalanced
				//break;
				node = node.getParent();
				continue;
			}
			else {
			// the node is 2-2 
			if (deltaRankRight== 2 && deltaRankLeft== 2){
				demote(node);
				cnt++;
				node = node.getParent();
				continue;			
			}
			else {
			// case 3,1
			if ((deltaRankRight==1)&&(deltaRankLeft==3)){
				int deltaRightSonRight = node.getRight().getHeight()-node.getRight().getRight().getHeight();// delta rank between left son and his right son
				int deltaRightSonLeft = node.getRight().getHeight()-node.getRight().getLeft().getHeight();// delta rank between left son and his left son
				// 3-1 node and it's right son is 1-1 node
				if(((deltaRightSonRight==1)&&(deltaRightSonLeft==1))) {
					case31And11Rebalance(node);
					cnt += 3 ;// rotation, demote, promote - one of each
					node = node.getParent().getParent();
					continue;					
				}
				// 3-1 node and it's right son is 1-2 node
				if((deltaRightSonRight==2)&&(deltaRightSonLeft==1)) {
					case31And12Rebalance(node);
					cnt += 6; // 2*rotation, 4*demote
					node = node.getParent().getParent();
					continue;
				}
				// 3-1 node and it's right son is 2-1 node
				if((deltaRightSonRight==1)&&(deltaRightSonLeft==2)) {
					case31And21Rebalance(node);
					cnt += 3;//1*rotation, 2*demote
					node = node.getParent().getParent();
					continue;
				}
			}
			else {
			//case 1,3
			if((deltaRankRight==3)&&(deltaRankLeft==1)) {
				int deltaLeftSonleft=node.getLeft().getHeight()-node.getLeft().getLeft().getHeight();// delta rank between right son and his left son
				int deltaLeftSonRight=node.getLeft().getHeight()-node.getLeft().getRight().getHeight(); // delta rank between right son and his right son
				// 1-3 node and it's left son is 1-1 node
				if(( deltaLeftSonleft==1)&& (deltaLeftSonRight==1)) {
					case13And11Rebalance(node);
					cnt += 3;	//:rotation, demote, promote - one of each
					node = node.getParent().getParent();
					continue;
				}
				// 1-3 node and it's  left son is 2-1 node
				if(( deltaLeftSonleft==2)&& (deltaLeftSonRight==1)) {
					case13And21Rebalance(node);
					cnt += 6; //:2*rotation, 4*demote 
					node = node.getParent().getParent();
					continue;
				}
				// 1-3 node and it's  left son is 1-2 node
				if(( deltaLeftSonleft==1)&& (deltaLeftSonRight==2)) {
					case13And12Rebalance(node);
					cnt += 3; //:1*rotation, 2*demote 	
					node = node.getParent().getParent();
					continue;
				}
			}
			}
		node = node.getParent();
			}
			} 
		}
		return cnt;
	}	

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 * complexity O(1)
	 */
	public String min()
	{
		if(this.min != null) {
			return this.min.getValue();
		}
		return null; 
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 * complexity O(1)
	 */
	public String max() {
		if(this.max != null) {
			return this.max.getValue();
		}
		return null;

	}
	
	/**
	 * private IAVLNode findMin()
	 *
	 * Returns the node with the smallest key in the tree,
	 * or null if the tree is empty
	 * complexity O(log(n))
	 */
	private IAVLNode findMin() {
		if(this.root.getValue() == null) {
			return null;
		}
		IAVLNode node = this.root;
		while(node.getLeft().getKey() != -1){
			
			node = node.getLeft();
		}
		return node;
	}
		
	/**
	 * private IAVLNode findMax()
	 *
	 * Returns the node with the greatest key in the tree,
	 * or null if the tree is empty
	 * complexity O(log(n))
	 */
	private IAVLNode findMax() {
		if(this.root.getValue() == null) {
			return null;
		}
		IAVLNode node = this.root;
		while(node.getRight().getKey() != -1){
			node = node.getRight();
		}
		return node;	
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 * complexity O(n)
	 */
	public int[] keysToArray()
	{
		int[] arr = new int[this.size()]; 
		int[] index = new int[1];
		index[0]=0;
		keysToArrayRec(this.root, arr,index );
		return arr;  
	}
	
	/**
	 * private void keysToArrayRec(IAVLNode node, int[] keysArray, int[] index)
	 *
	 * updates the array which contains all keys, sorted
	 * recursively
	 * complexity O(n)
	 */
	private void keysToArrayRec(IAVLNode node, int[] keysArray, int[] index) {
		if((node.getKey() == -1)||(index[0]==keysArray.length)) {
			return ;
		}
		keysToArrayRec(node.getLeft(),keysArray, index);
		keysArray[index[0]] = node.getKey();
		index[0]+=1;
		keysToArrayRec(node.getRight(),keysArray, index);
		
		
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 * complexity O(n)
	 */
	public String[] infoToArray()
	{
		String[] arr = new String[this.size()]; 
		int[] index = new int[1];
		index[0]=0;
		infoToArrayRec(this.root, arr,index );
		return arr;  
	}
	
	/**
	 * private void infoToArrayRec(IAVLNode node, String[] infoArray, int[] index)
	 * 
	 * updates the array which contains all info in the tree 
	 * sorted by their respective keys,
	 * recursively
	 * complexity O(n)
	 */
	private void infoToArrayRec(IAVLNode node, String[] infoArray, int[] index) {
		if((node.getKey() == -1)||(index[0]==infoArray.length)) {
			return ;
		}
		infoToArrayRec(node.getLeft(),infoArray, index);
		infoArray[index[0]] = node.getValue();
		index[0]+=1;
		infoToArrayRec(node.getRight(),infoArray, index);			
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 * 
	 * prcondition: none
	 * postcondition: none
	 * 
	 * complexity O(1)
	 */
	public int size()
	{
		return this.root.getSize(); // to be replaced by student code
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 * 
	 * precondition: none
	 * postcondition: none
	 * 
	 * complexity O(1)
	 * 
	 */
	public IAVLNode getRoot()
	{
		if(this.root.getKey() == -1) {
			return null;
		}
		return this.root;
	}
	/**
	 * public string split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. 
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null
	 * postcondition: none
	 * 
	 * complexity O(log(n))
	 * 
	 */   
	public AVLTree[] split(int x)
	{
		IAVLNode root = this.root;
		
		AVLTree[] treeArray = new AVLTree[2];
		IAVLNode node=this.searchNode(x,0); //the node that splits
		AVLTree smaller=new AVLTree();
		if(node.getLeft().getKey() != -1) {// this tree is the left sub tree of the node
			smaller.root=node.getLeft(); 
			
		}
	
		AVLTree bigger=new AVLTree();
		
		if(node.getRight().getKey() != -1){//// this tree is the right sub tree of the node
			bigger.root=node.getRight();
		}
		IAVLNode tempParent= node.getParent();//parent of the node so we can access

		//start splitting
		while(node.getKey()!= root.getKey()) {
			 if(tempParent.getRight().getKey() == node.getKey()) {// arriving the parent from right
				AVLTree tempTree = new AVLTree(); //this is the tree we are adding
				 if(tempParent.getLeft().getKey() != -1) {//left son isnt external leaf
					 tempTree.root = tempParent.getLeft(); 
					 tempTree.root.setParent(null);
					 tempParent.setRight(null);
					 tempParent.setLeft(null);				 
				 }			
				 node = tempParent;
				 tempParent = tempParent.getParent();
				 node.setParent(null);
				 smaller.joinSetMinMax(node, tempTree,false); //join the parnt left sub tree with parent and smaller
			 }
			 
			 else {// arriving the parent from the left
				 AVLTree tempTree = new AVLTree();
				 if(tempParent.getRight().getKey() != -1) {//the right son isnt externaleaf
					 tempTree.root = tempParent.getRight(); 
					 tempTree.root.setParent(null);
					 tempParent.setRight(null);
					 tempParent.setLeft(null);
				 }
				 node = tempParent;
				 tempParent = tempParent.getParent();
				 node.setParent(null);
				 bigger.joinSetMinMax(node, tempTree,false); //join the parnt left sub tree with parent and smaller
			 }	
		}
		smaller.min = smaller.findMin();
		smaller.max = smaller.findMax();
		bigger.min = bigger.findMin();
		bigger.max = bigger.findMax();
		 treeArray[0] = smaller;
		 treeArray[1] = bigger;		 
		return treeArray; 
	}
	
	/**
	 * public join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. 	
	 * Returns the complexity of the operation (rank difference between the tree and t + 1)
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	 * x isnt empty - no null no external leaf
	 * postcondition: none
	 * complexity O(log(n))
	 */  
	public int join(IAVLNode x, AVLTree t) {
		return joinSetMinMax(x, t, true);
	}
	/**
	 * public joinSetMinMax(IAVLNode x, AVLTree t, boolean updateMinMax)
	 *
	 * joins t and x with the tree. 	
	 * Returns the complexity of the operation (rank difference between the tree and t + 1)
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	 * x isnt empty - no null no external leaf
	 * postcondition: none
	 * complexity O(log(n))
	 */   
	public int joinSetMinMax(IAVLNode x, AVLTree t, boolean updateMinMax)
	{
		if((t.getRoot()==null)&&(this.getRoot()==null)) {// both trees are empty
				IAVLNode exteralRight= new AVLNode();
				IAVLNode exteralLeft= new AVLNode();
				x.setLeft(exteralLeft);
				x.setRight(exteralRight);
				exteralRight.setParent(x);
				exteralLeft.setParent(x);
			    this.root=x;
			    this.min=x;
				this.max=x;
				x.setSize(1);
				x.setHeight(0);
				return 1;			
		}
		// x isnt empty either 		
		boolean myTreeIsLeft=false;
		if((this.root.getKey()!=-1)&&(t.root.getKey()!=-1)) {//if both not empty
			 myTreeIsLeft = this.root.getKey() < x.getKey(); 
		}
		//one is empty the other one isnt
		else {
			if(this.root.getKey()==-1) {//my tree is empty
				if(t.root.getKey()>x.getKey()) {//t isnt empty
					myTreeIsLeft=true;
				}
			}
			else {
				if(this.root.getKey()<x.getKey()) {
					myTreeIsLeft=true;
			}
		}
		}	
		int heightOfMyTree= this.root.getHeight();
		int heightOfT=t.root.getHeight();
		//start joining
		if (myTreeIsLeft) {
			if( heightOfMyTree > heightOfT) {//my tree is left and bigger
			joinBiggerTreeIsLeft(t,x,this,false);
			if(updateMinMax) {
			this.min = this.findMin();
			this.max = this.findMax();
			}
			return heightOfMyTree - heightOfT + 1;
			}
			else {
				if (heightOfMyTree == heightOfT) {//my tree is left and heights are equal
					joinSameHeights(this,x,t);
					this.min = this.findMin();
					this.max = this.findMax();
					return 1;				
				}
				else{//my tree is left and smaller		
					joinBiggerTreeIsRight(this,x,t,true);
					if(updateMinMax) {
						this.min = this.findMin();
						this.max = this.findMax();
						}
					this.min = this.findMin();
					this.max = this.findMax();
					return heightOfT - heightOfMyTree + 1;
				}		
			}
		}			
		//my tree goes right
		else {	
			if(heightOfMyTree>heightOfT) {//my tree is right and bigger
				joinBiggerTreeIsRight(t ,x ,this,false);
				if(updateMinMax) {
					this.min = this.findMin();
					this.max = this.findMax();
					}
				return heightOfMyTree - heightOfT + 1;
			}
			else {
				if(heightOfMyTree == heightOfT) {
					joinSameHeights(t,x,this);
					if(updateMinMax) {
						this.min = this.findMin();
						this.max = this.findMax();
						}
					return 1;
				}
				else {//my tree is right and smaller
					joinBiggerTreeIsLeft(this,x,t,true);
					if(updateMinMax) {
						this.min = this.findMin();
						this.max = this.findMax();
						}
					
					return heightOfT - heightOfMyTree + 1;		
				}			
			}
		}
	}
	
	/**
	 * private void joinSameHeights(AVLTree leftSon,IAVLNode x,AVLTree rightSon)
	 * joins leftSon and x and rightSon	  
	 * pre:leftSon and rightSon are in the same height
	 * post: the tree this is balanced
	 * 
	 * complexity O(1) 
	 */   
	private void joinSameHeights(AVLTree leftSon,IAVLNode x,AVLTree rightSon) {
		x.setRight(rightSon.root);
		rightSon.root.setParent(x);
		x.setLeft(leftSon.root);
		leftSon.root.setParent(x);
		x.setSize(leftSon.root.getSize() + rightSon.root.getSize()+1);
		x.setHeight(rightSon.root.getHeight()+1);
		this.root=x;
		x.setParent(null);
	}
	/**
	 * private void joinBiggerTreeIsLeft(AVLTree smaller,IAVLNode x,AVLTree bigger, boolean myTreeIsSmaller)
	 * joins smaller and x and bigger	  
	 * pre: bigger goes left, smaller goes right 
	 * post: the tree this is balanced - calls rebalanceInsertion
	 *
	 * complexity O(log(n))
	 */   
	private void joinBiggerTreeIsLeft(AVLTree smaller,IAVLNode x,AVLTree bigger, boolean myTreeIsSmaller) {
		IAVLNode newLeftSon=searchRankNodeLeftJoin(bigger.root,smaller.root.getHeight(),smaller.root.getSize()+1);
		IAVLNode parentOfNewLeftSon = newLeftSon.getParent();
		x.setLeft(newLeftSon);
		newLeftSon.setParent(x);
		x.setRight(smaller.root);
		smaller.root.setParent(x);
		parentOfNewLeftSon.setRight(x);
		x.setParent(parentOfNewLeftSon);
		x.setHeight(smaller.root.getHeight()+1);
		x.setSize(smaller.root.getSize()+newLeftSon.getSize()+1);
		if(myTreeIsSmaller) {
			smaller.root=bigger.root;
		}
		rebalanceInsertion(x.getParent());			
	}
	
	/**
	 * private void joinBiggerTreeIsRight(AVLTree smaller,IAVLNode x,AVLTree bigger, boolean myTreeIsSmaller)
	 * joins smaller and x and bigger	  
	 * pre: bigger goes right, smaller goes left 
	 * post: the tree this is balanced - calls rebalanceInsertion
	 * 
	 * complexity O(log(n))
	 */   
	private void joinBiggerTreeIsRight(AVLTree smaller,IAVLNode x,AVLTree bigger,boolean myTreeIsSmaller ) {
		IAVLNode newRightSon = searchRankNodeRightJoin(bigger.root, smaller.root.getHeight(), smaller.root.getSize()+1);
		IAVLNode parentOfNewRightSon = newRightSon.getParent();
		x.setRight(newRightSon);
		newRightSon.setParent(x);
		x.setLeft(smaller.root);
		smaller.root.setParent(x);
		parentOfNewRightSon.setLeft(x);
		x.setParent(parentOfNewRightSon);
		x.setHeight(smaller.root.getHeight()+1);
		x.setSize(smaller.root.getSize()+newRightSon.getSize()+1);
		if(myTreeIsSmaller) {
			smaller.root=bigger.root;
		}
		rebalanceInsertion(x.getParent());	
	}

	/**
	 * private IAVLNode searchRankNodeRightJoin(IAVLNode node, int rank, int newSize)
	 * search the new right son of x on the left spine of the tree of node	  
	 * returns it
	 * complexity O(log(n))
	 */   
	private IAVLNode searchRankNodeRightJoin(IAVLNode node, int rank, int newSize) {// the keys of the tallest tree are greater then the node
		if(rank+1 == node.getHeight()){
			node.setSize(newSize + node.getSize());
		}
		while((node.getHeight() > rank +1) && (node.getLeft().getKey() != -1)) {
			node.setSize(newSize + node.getSize());
			node = node.getLeft();
		}
		return node.getLeft() ;
	}
	/**
	 * private IAVLNode searchRankNodeLeftJoin(IAVLNode node, int rank, int newSize)
	 * search the new left son of x on the right spine of the tree of node	  
	 * returns it
	 * complexity O(log(n))
	 */   
	private IAVLNode searchRankNodeLeftJoin(IAVLNode node, int rank, int newSize) {// the keys of the taller tree are smaller then the node
		if(rank+1 == node.getHeight()){
			node.setSize(newSize + node.getSize());

		}
		while((node.getHeight() > rank + 1) && (node.getRight().getKey() != -1)) {
			node.setSize(newSize + node.getSize());
			node = node.getRight();
		}
		return node.getRight();
	}
	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
		public void setSize(int newSize); // sets the node's size
		public int getSize();// return the node's size
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in 
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode{
		//:Fields of class AVLNode
		private int key;
		private String value;
		private int rank;
		private AVLNode left;
		private AVLNode right;
		private AVLNode parent;
		private int size; //:each node holds the number of nodes in the subtree that this node is the root of

		/**
		 * public AVLNode(int newKey, String newValue)
		 *
		 * constructor
		 * complexity O(1)
		 */
		public AVLNode(int newKey, String newValue) {
			this.key=newKey;
			this.value=newValue;
			this.rank=0;
			this.left=null;
			this.right=null;
			this.parent=null;
			this.size=1;	    	
		}
		
		/**
		 * public AVLNode()
		 *
		 * constructor of external leaf
		 * complexity O(1)
		 */
		public AVLNode() {//:this is the constructor of external leaf
			this.key = - 1;
			this.value = null;
			this.rank = -1;
		}
		
		/**
		 * public int getKey()
		 *
		 * getter of field key
		 * complexity O(1)
		 */
		public int getKey()
		{
			return this.key; 
		}

		/**
		 * public String getValue()
		 *
		 * getter of field value
		 * complexity O(1)
		 */
		public String getValue()
		{
			return this.value; 
		}

		/**
		 * public void setLeft(IAVLNode node)
		 *
		 * setter of field left
		 * complexity O(1)
		 */
		public void setLeft(IAVLNode node)
		{	
			AVLNode leftNode = (AVLNode)node;//:casting from IAVLNode to AVLNode	
			this.left = leftNode;		//:define the left son
		}

		/**
		 * public void getLeft()
		 *
		 * getter of field left
		 * complexity O(1)
		 */
		public IAVLNode getLeft()
		{

			return this.left; 
		}

		/**
		 * public void setRight(IAVLNode node)
		 *
		 * setter of field right
		 * complexity O(1)
		 */
		public void setRight(IAVLNode node)
		{
			AVLNode rightNode = (AVLNode)node;// casting from IAVLNode to AVLNode
			this.right = rightNode;		// define the right son
		}
		
		/**
		 * public void getRight()
		 *
		 * getter of field right
		 * complexity O(1)
		 */
		public IAVLNode getRight()
		{
			return this.right; 
		}
		
		/**
		 * public void setParent(IAVLNode node)
		 *
		 * setter of field parent
		 * complexity O(1)
		 */
		public void setParent(IAVLNode node)
		{
			if(node == null) {
				this.parent = null;
				return;
			}
			AVLNode parentNode = (AVLNode)node; //:casting
			this.parent=parentNode;
		}

		/**
		 * public void getParent()
		 *
		 * getter of field parent
		 * complexity O(1)
		 */
		public IAVLNode getParent()
		{
			return this.parent; 
		}


		/**
		 * public boolean isRealNode()
		 *
		 * returns true if the node is real and not external
		 * complexity O(1)
		 */
		public boolean isRealNode()
		{
			return (this.key!=-1); //:only if key=-1 than this is not real node
		}

		/**
		 * public void setHeight(int height)
		 *
		 * setter of field height
		 * complexity O(1)
		 */
		public void setHeight(int height)//: In AVL tree rank = height 
		{
			this.rank=height ; 
		}
		
		/**
		 * public void getHeight()
		 *
		 * getter of field height
		 * complexity O(1)
		 */
		public int getHeight()
		{
			return this.rank; 
		}

		/**
		 * public void setSize(int newSize)
		 *
		 * setter of field size
		 * complexity O(1)
		 */
		public void setSize(int newSize)
		{
			this.size=newSize ; 
		}
		
		/**
		 * public void getSize()
		 *
		 * getter of field size
		 * complexity O(1)
		 */
		public int getSize()
		{
			return this.size; 
		}
	}
}
	
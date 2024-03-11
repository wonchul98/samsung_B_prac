package GiftCardDistribute;

import java.util.ArrayList;
import java.util.HashMap;

class UserSolution {
	public static HashMap<Integer, Node> Nodes;
	
	public void init(int N, int mId[], int mNum[]) {
		Nodes = new HashMap<Integer, Node>();
		for(int i = 0;i < N;i++) {
			Nodes.put(mId[i], new Node(mId[i], mId[i], mNum[i]));
		}
		return;
	}

	public int add(int mId, int mNum, int mParent) {
		
		Node p = Nodes.get(mParent);
		if(p.children.size()==3) return -1;
		Nodes.put(mId, new Node(mParent, mId, mNum));
		
		return 0;
	}

	public int remove(int mId) {
		return 0;
	}

	public int distribute(int K) {
		return 0;
	}
	public class Node{
		Node parent;
		ArrayList<Node> children;
		int mId, mNum;
		public Node(int pId, int mId, int mNum) {
			this.parent = Nodes.get(pId);
			this.mId = mId;
			this.mNum = mNum;
			this.parent.children.add(this); //이거 돌아가나?
		}
		
	}
}
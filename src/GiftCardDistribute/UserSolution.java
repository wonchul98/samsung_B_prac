package GiftCardDistribute;

import java.util.HashMap;
import java.util.LinkedList;

class UserSolution {
	public static HashMap<Integer, Node> Nodes;
	public static HashMap<Integer, Node> Roots;

	public void init(int N, int mId[], int mNum[]) {
		Nodes = new HashMap<Integer, Node>();
		for (int i = 0; i < N; i++) {
			Node cur = new Node(mId[i], mId[i], mNum[i]);
			Nodes.put(mId[i], cur);
			Roots.put(mId[i], cur);
		}
		return;
	}

	public int add(int mId, int mNum, int mParent) {
		Node p = Nodes.get(mParent);
		if (p.children.size() == 3)
			return -1;
		Node cur = new Node(mParent, mId, mNum);
		Nodes.put(mId, cur);
		p.children.add(cur);
		Node up = p;
		while (up.parent.mId != up.mId) {
			up.mNum += mNum;
			up = up.parent;
		}
		return p.mNum;
	}

	public int remove(int mId) {
		if (!Nodes.containsKey(mId))
			return -1;
		Node cur = Nodes.get(mId);
		Node p = cur.parent;
		Nodes.remove(cur.mId);
		p.mNum -= cur.mNum;
		for (Node n : p.children) { // 부모 노드에서 자기 삭제
			if (n.mId == mId)
				p.children.remove(n);
		}
		return removeNode(cur);
	}

	public int removeNode(Node n) {
		int cnt = 0;
		for (Node c : n.children) {
			cnt += removeNode(c);
		}
		cnt += n.mNum;
		Nodes.remove(n.mId);
		return cnt;
	}

	public int distribute(int K) {
		int max = 0;
		int sum = 0;
		int Lmax = 0;
		for(int k : Roots.keySet()) {
			int num = Roots.get(k).mNum;
			sum += num;
			Lmax = Math.max(Lmax, num);
		}
		if(K >= sum) return Lmax;
		binarySearch(1, Lmax, K);
		
		return 0;
	}
	public int binarySearch(int start,int end, int K) {
		int max = 0;
		while(start <= end) {
			int mid = ( start + end ) / 2;
			int result = calc(mid);
			if(result <= K) {
				start = mid + 1;
				max = mid;
			} else{
				end = mid - 1;
			}
		}
		return max;
	}
	public int calc(int L) {
		int sum = 0;
		for(int k : Roots.keySet()) {
			if(Roots.get(k).mNum < L) sum += Roots.get(k).mNum;
			else sum+=L;
		}
		return sum;
	}

	public class Node {
		Node parent;
		LinkedList<Node> children;
		int mId, mNum;

		public Node(int pId, int mId, int mNum) {
			this.parent = Nodes.get(pId);
			this.mId = mId;
			this.mNum = mNum;
			children = new LinkedList<>();
		}

	}
}
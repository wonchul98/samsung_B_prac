package CountryAdmin;

import java.util.PriorityQueue;

class UserSolution
{
	public int[] towns;
	public int[] lines;
	PriorityQueue<line> pq = new PriorityQueue<>();
	void init(int N, int mPopulation[])
	{
		towns = new int[N];
		lines = new int[N];
		towns[0] = mPopulation[0];
		for(int i = 1; i < N;i++) {
			towns[i] = mPopulation[i];
			int length = (towns[i]-towns[i-1])/2;
			lines[i-1] = length;
			pq.add(new line(i-1, length, 1));
		}
		return;
	}

	int expand(int M)
	{
		int rst = 0;
		for(int i = 0; i < M;i++) {
			line curLine = pq.poll();
			int nextLength = (towns[curLine.left] +towns[curLine.left+1]) / (curLine.length + 1); 
			line nextLine = new line(curLine.left, nextLength, curLine.size+1);
			pq.add(nextLine);
			rst = nextLength;
			lines[i] = nextLength;
		}
		return rst;
	}
	
	int calculate(int mFrom, int mTo)
	{
		int cnt = 0;
		for(int i = mFrom;i < mTo;i++) {
			cnt += lines[i];
		}
		return cnt;
	}
	
	int divide(int mFrom, int mTo, int K)
	{
		int sum = 0, max = 0;
		for(int i = mFrom; i < mTo;i++) {
			sum += towns[i];
			max = Math.max(towns[i], max);
		}
		int start = max;
		int end = sum;
		while(start <= end) {
			int mid = (start + end)/2;
			int dm = K-1;
			int cur = 0;
			int curMax = 0;
			boolean rst;
			for(int i = mFrom; i < mTo;i++) {
				cur += towns[i];
				if(cur > mid && dm > 0) {
					dm--;
					curMax = Math.max(cur - towns[i], curMax);
					cur = towns[i];
				}
				if(i == mTo-1 && dm > 0) rst = false; // 더 좁게 해야함
			}
			
		}
		return 0;
	}
	public class line implements Comparable<line>{
		int left;
		int length;
		int size;
		public line(int left, int length, int size) {
			this.left = left;
			this.length = length;
			this.size = size;
		}		
		@Override 
		public int compareTo(line o) { //내림차순 
			return Integer.compare(o.length, this.length);
		}
	}
}
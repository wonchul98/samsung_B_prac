package CountryAdmin;

import java.util.PriorityQueue;

class UserSolution
{
	public int[] towns;
	public int[] lines;
	PriorityQueue<line> pq;
	void init(int N, int mPopulation[])
	{
		towns = new int[N];
		lines = new int[N];
		towns[0] = mPopulation[0];
		pq = new PriorityQueue<>();
		for(int i = 1; i < N;i++) {
			towns[i] = mPopulation[i];
			int length = (towns[i]+towns[i-1]);
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
			int nextLength = (towns[curLine.left] +towns[curLine.left+1]) / (curLine.size + 1); 
			line nextLine = new line(curLine.left, nextLength, curLine.size+1);
			pq.add(nextLine);
			rst = nextLength;
			lines[curLine.left] = nextLength;
		}
		return rst;
	}
	
	int calculate(int mFrom, int mTo)
	{
		if(mTo < mFrom) {
			int temp = mFrom;
			mFrom = mTo;
			mTo = temp;			
		}
		int cnt = 0;
		for(int i = mFrom;i < mTo;i++) {
			cnt += lines[i];
		}
		return cnt;
	}
	
	
	
	
	int divide(int mFrom, int mTo, int K)
	{
		if(mTo < mFrom) {
			int temp = mFrom;
			mFrom = mTo;
			mTo = temp;			
		}
		int sum = 0, max = 0;
		for(int i = mFrom; i < mTo;i++) {
			sum += towns[i];
			max = Math.max(towns[i], max);
		}
		int start = max;
		int end = sum;
		while (start < end) {
			int mid = (start + end) / 2; //선거구 내 인구 상한선
			int cnt = 0; // 선거구 수
			for (int i = mFrom; i <= mTo && cnt <= K; cnt++) {
				sum = 0;
				int j = i;
				while (j <= mTo && sum + towns[j] <= mid) {
					sum += towns[j++];
				}
				i = j;
			}
			if (cnt <= K) { // 덜 나뉘는 경우 -> mid를 너무 크게 잡음 or 적당
				end = mid;
			}
			else {
				start = mid + 1; // 더 나뉘는 경우 -> mid를 너무 작게 잡음 
			}
		}
		return end;
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
			if(o.length == this.length) return Integer.compare(this.left, o.left);
			return Integer.compare(o.length, this.length);
		}
	}
}
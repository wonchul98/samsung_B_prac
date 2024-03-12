package findPair;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSolution {
	public void playGame(int N) {
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
		for (int i = 1; i < 2 * N; i++) {
			int diff = findDiff(0, i, N);
			//System.out.println("diff: " + diff);
			if (map.containsKey(diff))
				map.get(diff).add(i);
			else {
				ArrayList<Integer> list = new ArrayList<>();
				list.add(i);
				map.put(diff, list);
			}
		}
		for (int k : map.keySet()) {
			//System.out.println("diff : " + k);
			//System.out.println("size : " + map.get(k).size());
			if (map.get(k).size() == 2) {
				Solution.checkCards(map.get(k).get(0), map.get(k).get(1), 0);
			} else if (map.get(k).size() == 1) {
				Solution.checkCards(map.get(k).get(0), 0, 0);
			} else {
				if (Solution.checkCards(map.get(k).get(0), map.get(k).get(1), 0)) { // 01 23
					Solution.checkCards(map.get(k).get(2), map.get(k).get(3), 0);
				} else if(Solution.checkCards(map.get(k).get(0), map.get(k).get(2), 0)){ // 02 13
					Solution.checkCards(map.get(k).get(1), map.get(k).get(3), 0);
				} else { //03 12
					Solution.checkCards(map.get(k).get(0), map.get(k).get(3), 0);
					Solution.checkCards(map.get(k).get(1), map.get(k).get(2), 0);
				}
			}
		}
	}

	public int findDiff(int a, int b, int N) {
    	//System.out.printf("findDiff(%d,%d)\n", a, b);
    	int start = 0;
    	int end = N-1;
    	while(start <= end) {
    		//System.out.println("start: " + start + " end: " + end);
    		if(end-start==1) {
    			if(!Solution.checkCards(a, b, start) && Solution.checkCards(a, b, end)) return end; // FT
    			else if(!Solution.checkCards(a, b, start) && !Solution.checkCards(a, b, end)) return end + 1; // FF
    			else return start; //TT
    		}
    		int mid = (start + end) / 2;
    		if(Solution.checkCards(a, b, mid)) {
    			
    			end = mid;
    		}
    		else {
    			
    			start = mid;
    		}
    		
    	}
    	return 0;
    	
    }

}

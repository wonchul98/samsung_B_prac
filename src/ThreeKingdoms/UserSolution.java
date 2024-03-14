package ThreeKingdoms;

import java.util.ArrayList;
import java.util.HashMap;

class UserSolution {
	public int bound;
	public String[][] map;
	HashMap<Integer, Country> hm;
	HashMap<String, Integer> nameToIdx;
	int idx = 1;
	int[] p;
	int[] dx = {-1,-1,-1,0,0,1,1,1};
	int[] dy = {-1,0,1,-1,1,-1,0,1};
	boolean inRange(int x, int y) {
		if(x < 0 || x >= bound || y < 0 || y >= bound) return false;
		return true;
	}
	
    void init(int N, int mSoldier[][], char mMonarch[][][])
    {
    	idx++;
    	p = new int[N*N];
    	bound = N;
    	map = new String[N][N];
    	hm = new HashMap<>();
    	nameToIdx = new HashMap<>();
    	for(int i = 0; i < N;i++) {
    		for(int j = 0; j < N;j++) {
    			String mName = new String(mMonarch[i][j]);
    			map[i][j] = mName;
    			Country country = new Country(mName, mSoldier[i][j], N*i+j);
    			nameToIdx.put(mName, N*i+j);
    			hm.put(N*i+j, country);
    			p[N*i+j] = N*i+j;
    		}
    	}
    }
    void union(int a, int b) {
    	int pa = find(a);
    	int pb = find(b);
    	System.out.println("union " + hm.get(pa).monarch + " " + hm.get(pb).monarch);
    	p[pa] = pb;
    	if (hm.get(pa).hostiles.size()!=0) {
    		for(Country h : hm.get(pa).hostiles) {
    			hm.get(pb).hostiles.add(h);
    		}
    	}
    	System.out.println("host: " + hm.get(pb).hostiles.toString());
    }
    int find(int a) {
    	if(p[a] == a) {
    		return a;
    	}
    	return p[a] = find(p[a]);
    }
    void destroy()
    {

    }
    int ally(char mMonarchA[], char mMonarchB[])
    {
    	System.out.println("###idx: " + idx++);
    	System.out.println("ally " + new String(mMonarchA) + " " + new String(mMonarchB));
    	Country a = hm.get(nameToIdx.get(new String(mMonarchA)));
    	Country b = hm.get(nameToIdx.get(new String(mMonarchB)));
    	Country pa = hm.get(find(a.idx));
    	Country pb = hm.get(find(b.idx));
    	System.out.println("pa host: " + pa.hostiles.toString());
    	System.out.println("pb: " + pb);
    	if(find(a.idx) == find(b.idx)) {
    		System.out.println("-1");
    		return -1;
    	}
    	else if(hm.get(find(a.idx)).hostiles.contains(pb)) {
    		System.out.println("-2");
    		return -2;
    	}
    	else {
    		union(a.idx, b.idx);
    		System.out.println("1");
    		return 1;
    	}
    }
    int attack(char mMonarchA[], char mMonarchB[], char mGeneral[])
    {
    	System.out.println("###idx: " + idx++);
    	System.out.println("attack");
    	Country a = hm.get(nameToIdx.get(new String(mMonarchA)));
    	Country b = hm.get(nameToIdx.get(new String(mMonarchB)));
    	if(find(a.idx) == find(b.idx)) { //같은 동맹
    		System.out.println("-1");
    		return -1;
    	}
    	ArrayList<Country> nearHost = new ArrayList<>();
    	ArrayList<Country> nearAlly = new ArrayList<>();
    	int bx = b.idx / bound;
    	int by = b.idx % bound;
    	for(int i = 0; i < 8;i++) {
    		int nx = bx + dx[i];
    		int ny = by + dy[i];
    		if(!inRange(nx, ny)) continue;
    		int nextIdx = nx * bound + ny;
    		Country next = hm.get(nextIdx);
    		if(find(next.idx) == find(a.idx)) nearHost.add(next);
    		if(find(next.idx) == find(b.idx)) nearAlly.add(next);
    	}
    	System.out.println("동맹: " + nearAlly.toString());
    	System.out.println("적대: " + nearHost.toString());
    	if(nearHost.size() == 0) {
    		System.out.println("-2");
    		return -2; // b주변에 a의 동맹이 없는 경우
    	}
    	// a , b의 parent에 적대관계 추가.
    	hm.get(find(a.idx)).hostiles.add(b);
    	hm.get(find(b.idx)).hostiles.add(a);
    	int attackerSoldiers = 0;
    	for(int i =0; i < nearHost.size();i++) {
    		int nSoldiers = nearHost.get(i).soldiers/2;
    		nearHost.get(i).soldiers -= nSoldiers;
    		attackerSoldiers += nSoldiers;
    	}
    	int defenseSoldiers = b.soldiers;
    	for(int i =0; i < nearAlly.size();i++) {
    		int nSoldiers = nearAlly.get(i).soldiers/2;
    		nearAlly.get(i).soldiers -= nSoldiers;
    		defenseSoldiers += nSoldiers;
    	}
    	System.out.println("동맹: " + nearAlly.toString());
    	System.out.println("적대: " + nearHost.toString());
    	System.out.println("공격수: " + attackerSoldiers+ " 방어수: " + defenseSoldiers);
    	if(attackerSoldiers > defenseSoldiers) {
    		int newRootIdx = -1;
    		if(find(b.idx) == b.idx) { //root로 있는 나라가 점령당하는 경우
    			for(int i = 0 ; i < bound*bound;i++) {
    				if(find(hm.get(i).idx) == b.idx) {
    					if(newRootIdx == -1) {
    						newRootIdx = i; // 새로운 root
    						p[hm.get(i).idx] = newRootIdx;
    					}else {
    						p[hm.get(i).idx] = newRootIdx;
    					}
    				}
    			}
    		}
    		hm.remove(b.idx);
    		Country newCountry = new Country(new String(mGeneral), attackerSoldiers - defenseSoldiers, b.idx);
    		hm.put(b.idx, newCountry);
    		nameToIdx.remove(new String(mMonarchB));
    		nameToIdx.put(new String(mGeneral), b.idx);
    		p[b.idx] = find(a.idx); //새로운 동맹관계 추가
    		System.out.println("1");
    		return 1;
    	} else { // 공격 실패
    		for(int i = 0; i < nearHost.size();i++) {
    			nearHost.get(i).soldiers = 0;
    		}
    		b.soldiers = defenseSoldiers - attackerSoldiers;
    		System.out.println("0");
    		return 0;
    	}
    }
    int recruit(char mMonarch[], int mNum, int mOption)
    {
    	System.out.println("###idx: " + idx++);
    	System.out.println("recruit");
    	if(mOption == 0) {
    		hm.get(nameToIdx.get(new String(mMonarch))).soldiers += mNum;
    		System.out.println(hm.get(nameToIdx.get(new String(mMonarch))).soldiers);
    		return hm.get(nameToIdx.get(new String(mMonarch))).soldiers;
    	}else {
    		int mRoot = find(nameToIdx.get(new String(mMonarch)));
    		int cnt = 0;
    		for(int i = 0; i < bound * bound;i++) {
    			if(p[i] == mRoot) {
    				hm.get(i).soldiers += mNum;
    				System.out.println("m: " + hm.get(i).monarch);
    				cnt += hm.get(i).soldiers;
    				System.out.println("cnt: " + hm.get(i).soldiers);
    			}
    		}
    		System.out.println(cnt);
    		return cnt;
    	}
    }
    public class Country {
    	String monarch;
    	int soldiers;
    	int idx;
    	ArrayList<Country> hostiles;
		public Country(String monarch, int soldiers, int idx) {
			this.monarch = monarch;
			this.soldiers = soldiers;
			this.idx = idx;
			this.hostiles = new ArrayList<>();
		}
		@Override
		public String toString() {
			return "Country [monarch=" + monarch + ", soldiers=" + soldiers + "]";
		}
		
    }
}

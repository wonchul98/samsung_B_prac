package swea_b;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Solution {
    private static BufferedReader br;
    private static UserSolution usersolution = new UserSolution();
	
    private final static int INIT = 0;
    private final static int REGI = 1;
    private final static int OFFER = 2;
	private final static int CANCEL = 3;
	private final static int CHECK = 4;
	
    private static int gids[] = new int[30];
	private static int ansids[] = new int[3];
	private static int retids[] = new int[3];
	
    private static boolean run() throws Exception {

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");		
       
        int N, K, cmd, ans, ret;
		int time, num, uid, gid, nid, delay;
		
		int Q = Integer.parseInt(st.nextToken());
        boolean ok = false;

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());

            if (cmd == INIT) {
                N = Integer.parseInt(st.nextToken());
                K = Integer.parseInt(st.nextToken());

                usersolution.init(N, K);
                ok = true;
            } else if (cmd == REGI) {
                time = Integer.parseInt(st.nextToken());
                uid = Integer.parseInt(st.nextToken());
				num = Integer.parseInt(st.nextToken());				
				for (int m = 0; m < num; m++) {
					gids[m] = Integer.parseInt(st.nextToken());
				}

				usersolution.registerUser(time, uid, num, gids);
            } else if (cmd == OFFER) {
                time = Integer.parseInt(st.nextToken());
                nid = Integer.parseInt(st.nextToken());
				delay = Integer.parseInt(st.nextToken());
				gid = Integer.parseInt(st.nextToken());     
				ans = Integer.parseInt(st.nextToken());

				ret = usersolution.offerNews(time, nid, delay, gid);
				if (ans != ret) {
					ok = false;
				}
            }
			else if (cmd == CANCEL) {
				time = Integer.parseInt(st.nextToken());
				nid = Integer.parseInt(st.nextToken());

				usersolution.cancelNews(time, nid);
            }
			else if (cmd == CHECK) {
				time = Integer.parseInt(st.nextToken());
				uid = Integer.parseInt(st.nextToken());
				
				ret = usersolution.checkUser(time, uid, retids);
				
				ans = Integer.parseInt(st.nextToken());
				num = ans;
				if (num > 3) num = 3;
				for (int m = 0; m < num; m++) {
					ansids[m] = Integer.parseInt(st.nextToken());
				}
				if (ans != ret) {
					ok = false;
				}
				else {
					for (int m = 0; m < num; m++) {
						if (ansids[m] != retids[m]) {
							ok = false;
						}
					}
				}
            }
			else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        //System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}

class UserSolution {
	public static HashMap<Integer, Integer> chIdToChIdx;
	public static HashMap<Integer, Integer> newsIdToChId; 
	public static PriorityQueue<Pair> pq;
	public static HashMap<Integer, News> newsIdToNews;
	public static Channel[] channels;
	public static int chIdx = 0;
	void init(int N, int K)
	{
		chIdToChIdx = new HashMap<>(); //채널ID -> 채널 IDX;
		newsIdToChId = new HashMap<>(); // 뉴스 아이디 -> 채널 id 저장
		newsIdToNews = new HashMap<>();
		pq = new PriorityQueue<>();
		channels = new Channel[K];
	}

	void registerUser(int mTime, int mUID, int mNum, int mChannelIDs[])
	{
		
	}

	int offerNews(int mTime, int mNewsID, int mDelay, int mChannelID)
	{
		return -1;
	}

	void cancelNews(int mTime, int mNewsID)
	{
	}

	int checkUser(int mTime, int mUID, int mRetIDs[])
	{
		return -1;
	}
	public static class Pair implements Comparable<Pair>{
		int x, y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Pair o) {
			return this.x - o.y;
		}
		
	}
	public static class News{
		int mTime, mNewsId, mDelay, mChannel;
		boolean isDeleted;

		public News(int mTime, int mNewsId, int mDelay, int mChannel, boolean isDeleted) {
			this.mTime = mTime;
			this.mNewsId = mNewsId;
			this.mDelay = mDelay;
			this.mChannel = mChannel;
			this.isDeleted = isDeleted;
		}
	}
	public static class Channel{
		HashMap<Integer, List<Integer>> timeNews = new HashMap<>(); //시간 , 해당 시간 뉴스 Id 리스트
		ArrayList<User> list = new ArrayList<>();
		public Channel(HashMap<Integer, List<Integer>> timeNews, ArrayList<User> list) {
			this.timeNews = timeNews;
			this.list = list;
		}
		
	}
	public static class User{
		ArrayList<Integer> channelIdList;
		Deque<Integer> curNewsId = new LinkedList<>(); //최근 읽은 신문
		int totalCnt; //총 알람 수 
		public User(ArrayList<Integer> channelIdList, Deque<Integer> curNewsId, int totalCnt) {
			super();
			this.channelIdList = channelIdList;
			this.curNewsId = curNewsId;
			this.totalCnt = totalCnt;
		}
		
	}
}
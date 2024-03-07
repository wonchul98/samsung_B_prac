package swea_b;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
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
	HashMap<Integer, Channel> chIdToCh;
	HashMap<Integer, Channel> newsIdToCh; 
	HashMap<Integer, News> newsIdToNews;
	HashMap<Integer, User> userIdToUser;
	PriorityQueue<Pair> pq;
	
	Channel getChannel(int ChannelId) {
		Channel curChannel;
		if(chIdToCh.containsKey(ChannelId)) curChannel = chIdToCh.get(ChannelId);
		else {
			curChannel = new Channel();
			chIdToCh.put(ChannelId, curChannel);
		}
		return curChannel;
	}
	News getNews(int mTime, int mNewsID, int mDelay, int mChannelID) {
		News news;
		if(newsIdToNews.containsKey(mNewsID)) news = newsIdToNews.get(mNewsID);
		else {
			news = new News(mTime, mDelay, mNewsID);
			newsIdToNews.put(mNewsID, news);
		}
		return news;
	}
	User getUser(int mUID) {
		User user;
		if(userIdToUser.containsKey(mUID)) user = userIdToUser.get(mUID);
		else {
			user = new User();
			userIdToUser.put(mUID, user);
		}
		return user;
	}
	void init(int N, int K)
	{
		chIdToCh = new HashMap<>(); //채널ID -> 채널
		newsIdToNews = new HashMap<>(); //뉴스ID -> 뉴스
		userIdToUser = new HashMap<>(); //유저ID -> 유저
		newsIdToCh = new HashMap<>(); // 뉴스 아이디 -> 채널 id 저장
		pq = new PriorityQueue<>();
	}

	void registerUser(int mTime, int mUID, int mNum, int mChannelIDs[])
	{
		User user = getUser(mUID);
		for(int i = 0;i < mNum;i++) {
			chIdToCh.get(mChannelIDs[i]).userList.add(user);
		}
	}

	int offerNews(int mTime, int mNewsID, int mDelay, int mChannelID)
	{
		Channel curChannel = getChannel(mChannelID);
		News news = getNews(mTime,mNewsID, mDelay, mChannelID);
		newsIdToCh.put(mNewsID, curChannel);
		if(curChannel.timeNews.containsKey(mTime + mDelay)) {
			curChannel.timeNews.get(mTime+mDelay).add(news); // 이미 해당 시간에 추가할 뉴스 리스트 들이 있는 경우
		}else {
			ArrayList<News> list = new ArrayList<>();
			list.add(news);
			curChannel.timeNews.put(mTime+mDelay ,list);
		}
		pq.add(new Pair(mTime + mDelay, curChannel));
		
		return curChannel.userList.size();
	}
	void getNews(int mTime) {
		while(!pq.isEmpty() && pq.peek().time <= mTime) {
			Pair p = pq.poll();
			Channel channel = p.ch;
			ArrayList<News> newsList = channel.timeNews.get(p.time);
			Collections.sort(newsList); // 뉴스 아이디 오름차순으로 정렬 -> 작은게 먼저 들어가면 큰게 나중에 들어가서 최신 뉴스가 삽입됨
			for(News n : newsList) {
				for(User u : channel.userList) {
					u.curNews.addFirst(n);
				}
			}
		}
	}

	void cancelNews(int mTime, int mNewsID)
	{
		newsIdToNews.get(mNewsID).isDeleted = true;
	}

	int checkUser(int mTime, int mUID, int mRetIDs[])
	{
		getUser(mTime);
		User user = getUser(mUID);
		int cnt = 0;
		for(News n :user.curNews) {
			if(n.isDeleted==true) continue;
			if(cnt < 3) {
				mRetIDs[cnt] = n.newsId;
			}
			cnt++;
			
		}
		return cnt;
	}
	class Pair implements Comparable<Pair>{
		int time;
		Channel ch;

		public Pair(int time, Channel ch) {
			this.time = time;
			this.ch = ch;
		}

		@Override
		public int compareTo(Pair o) {
			return this.time - o.time;
		}	
	}
	class News implements Comparable<News>{
		int mTime, mDelay, newsId;
		boolean isDeleted;

		public News(int mTime, int mDelay, int newsId) {
			this.mTime = mTime;
			this.mDelay = mDelay;
			this.newsId = newsId;
			this.isDeleted = false;
		}
		@Override
		public int compareTo(News o){
			return this.newsId - o.newsId;
		}
	}
	class Channel{
		HashMap<Integer, ArrayList<News>> timeNews; //시간 , 해당 시간 뉴스 Id 리스트
		ArrayList<User> userList;
		public Channel() {
			this.timeNews = new HashMap<>();
			this.userList = new ArrayList<>();
		}
	}
	class User{
		Deque<News> curNews; //최근 읽은 신문
		public User() {
			this.curNews = new LinkedList<>();
		}
	}
}
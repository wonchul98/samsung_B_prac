package swea_b;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import ScoreCheck.UserSolution;

class NewsAlarm {
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
        	String Input = br.readLine();
            st = new StringTokenizer(Input, " ");
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
					System.out.println("ans: " + ans);
					System.out.println("ret: " + ret);
					System.out.println(Input);
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
			curChannel = new Channel(ChannelId);
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
			user = new User(mUID);
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
		getSomeNews(mTime);
		User user = getUser(mUID);
		for(int i = 0;i < mNum;i++) {
			getChannel(mChannelIDs[i]).userList.add(user);
		}
	}

	int offerNews(int mTime, int mNewsID, int mDelay, int mChannelID)
	{
		Channel curChannel = chIdToCh.get(mChannelID); //유저가 하나 이상인 채널(이미 등록된 채널)임이 보장
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
		//System.out.println(curChannel.userList.size());
		return curChannel.userList.size();
	}
	void getSomeNews(int mTime) {
	    ArrayList<NewsEvent> events = new ArrayList<>();
	    
	    while (!pq.isEmpty() && pq.peek().time <= mTime) {
	        Pair p = pq.poll();
	        Channel channel = p.ch;
	        for (News n : channel.timeNews.get(p.time)) {
	        	//if(n.isDeleted) continue;
	            for (User u : channel.userList) {
	                events.add(new NewsEvent(p.time, n, u));
	            }
	        }
	    }

	    Collections.sort(events);
	    for (NewsEvent event : events) {
	    	event.user.curNews.addFirst(event.news);
	    }
	}


	void cancelNews(int mTime, int mNewsID)
	{
		newsIdToNews.get(mNewsID).isDeleted = true;
	}

	int checkUser(int mTime, int mUID, int mRetIDs[]) {
	    getSomeNews(mTime); //알람 받기
	    User user = getUser(mUID); //등록이 보장됨
	    int cnt = 0;
	    HashMap<Integer,Integer> hm = new HashMap<>();
	    for (News n : user.curNews) {
	    	if (n.isDeleted || hm.containsKey(n.newsId)) continue; 
	    	hm.put(n.newsId, 1);
	        if (cnt < 3) {
	        	mRetIDs[cnt] = n.newsId;
	        }
	        cnt++;
	    }
	    user.curNews = new LinkedList<>(); //알람 모두 삭제
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

		@Override
		public String toString() {
			return "Pair [time=" + time + ", ch=" + ch + "]";
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
		@Override
		public String toString() {
			return "News [newsId=" + newsId + ", isDeleted=" + isDeleted
					+ "]";
		}
		
	}
	class Channel{
		int ChId;
		HashMap<Integer, ArrayList<News>> timeNews; //시간 , 해당 시간 뉴스 Id 리스트
		ArrayList<User> userList;
		public Channel(int chId) {
			this.ChId = chId;
			this.timeNews = new HashMap<>();
			this.userList = new ArrayList<>();
		}
		@Override
		public String toString() {
			return "Channel [ChannelID=" + ChId + "]";
		}
		
	}
	class User{
		int userId;
		Deque<News> curNews; //최근 읽은 신문
		public User(int userId) {
			this.userId = userId;
			this.curNews = new LinkedList<>();
		}
		@Override
		public String toString() {
			return "User [userID=" + userId + "]";
		}
		
	}
	class NewsEvent implements Comparable<NewsEvent> {
	    int time;
	    News news;
	    User user;

	    NewsEvent(int time, News news, User user) {
	        this.time = time;
	        this.news = news;
	        this.user = user;
	    }

	    @Override
	    public int compareTo(NewsEvent other) {
	        if (this.time != other.time) { //시간 같으면 뉴스 ID순 오름차순
	            return this.time - other.time;
	        } //시간 순 오름차순
	        return this.news.newsId - other.news.newsId;

	    }

		@Override
		public String toString() {
			return "NewsEvent [time=" + time + ", news=" + news + ", user=" + user + "]";
		}
	    
	}

}
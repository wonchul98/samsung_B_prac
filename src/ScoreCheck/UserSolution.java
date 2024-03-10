package ScoreCheck;

import java.util.HashMap;
import java.util.TreeSet;

class UserSolution {
	public static HashMap<Integer, Student> idToSt;
	public static Channel[][] channels; // 성별 / 학년

	public void init() {
        channels = new Channel[2][4]; // 성별 / 학년
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 4; j++)
                channels[i][j] = new Channel();
        }
        idToSt = new HashMap<>();
    }

    public int add(int mId, int mGrade, char mGender[], int mScore) {
        int g = mGender[0] == 'm' ? 0 : 1;
        Student student = new Student(mId, mScore, g, mGrade);
        idToSt.put(mId, student);
        Channel channel = channels[g][mGrade];
        channel.ts.add(student);
        return channel.ts.last().mId;
    }

    public int remove(int mId) {
        if (!idToSt.containsKey(mId))
            return 0; // 등록된 적 없는 mId인 경우
        Student st = idToSt.get(mId);
        idToSt.remove(mId);
        Channel channel = channels[st.mGender][st.mGrade];
        channel.ts.remove(st); //채널에서도 삭제
        if (channel.ts.size() == 0) {
            return 0; // 삭제 후 크기가 0인 경우
        }
        return channel.ts.first().mId;
    }

    public int query(int gradeCount, int[] grades, int genderCount, char[][] genders, int targetScore) {
        Student lowestStudent = new Student(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
        boolean updated = false;

        for (int i = 0; i < gradeCount; i++) {
            int grade = grades[i];
            for (int j = 0; j < genderCount; j++) {
                int g = genders[j][0] == 'm' ? 0 : 1;
                Channel channel = channels[g][grade];
                Student student = channel.ts.ceiling(new Student(0, targetScore, 0, 0));
                if (student != null) {
                    updated = true;
                    lowestStudent = updateLowestStudent(lowestStudent, student);
                }
            }
        }

        return updated ? lowestStudent.mId : 0;
    }

	private Student updateLowestStudent(Student currentLowest, Student student) {
		if (student.mScore < currentLowest.mScore) {
			return student;
		} else if (student.mScore == currentLowest.mScore && student.mId < currentLowest.mId) {
			return student;
		}
		return currentLowest;
	}

	public static class Student implements Comparable<Student> {
		int mId, mScore, mGender, mGrade; // 0은 남자, 1은 여자

		public Student(int mId, int mScore, int mGender, int mGrade) {
			this.mId = mId;
			this.mScore = mScore;
			this.mGender = mGender;
			this.mGrade = mGrade;
		}

		@Override
		public int compareTo(Student o) {
			if (o.mScore == this.mScore) {
				return Integer.compare(this.mId, o.mId);
			}
			return Integer.compare(this.mScore, o.mScore);
		}

		@Override
		public String toString() {
			return "Student [mId=" + mId + ", mScore=" + mScore + ", mGender=" + mGender + ", mGrade=" + mGrade + "]";
		}
		
	}

	public static class Channel {
		TreeSet<Student> ts;

		public Channel() {
			this.ts = new TreeSet<>();
		}
	}
}
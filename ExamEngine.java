
package ct414;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ExamEngine implements ExamServer {

	private static Student[] studs;
	private HashMap<Integer, Session> sessions;
	private static Assessment[] assessm;
	Random rand;

	// Constructor is required
	public ExamEngine() {
		super();
		ExamEngine.studs = students;
		ExamEngine.assessm = ass;
		this.sessions = new HashMap<>();
	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period
	public int login(int studentid, String password) throws UnauthorizedAccess, RemoteException {
		int min = 0;
		int max = 10000000;

		for (Student s : studs) {
			if (s.getId() == studentid && s.getPassword() == password) {

				int randomNum = rand.nextInt((max - min) + 1) + min;
				sessions.put(randomNum, new Session(studentid));
				return randomNum;
			}
		}

		throw new UnauthorizedAccess("Your Username/ Password was Entered incorrectly");
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		checkSessionId(token, studentid);
		List<Assessment> assessments = new ArrayList<Assessment>();

		for (Assessment a : this.assessm) {
			if (a.getAssociatedID() == studentid) {
				assessments.add(a);
			}
		}

		if (assessments.size() == 0) {
			throw new NoMatchingAssessment("No assessments found please check ID");
		}

		return assessments;
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		checkSessionId(token, studentid);

		for (Assessment a : assessm) {
			if (a.getInformation().equals(courseCode) && a.getAssociatedID() == studentid
					&& a.getClosingDate().after(new Date())) {
				return a;
			}
		}

		throw new NoMatchingAssessment("No assessment found");
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

	}

	private void checkSessionId(int token, int userID) throws UnauthorizedAccess, RemoteException {
		Session session = sessions.get(token);

		if (session == null || session.getStudentId() != userID) {
			throw new UnauthorizedAccess("Error are you sure your logged in?");
		}

		if (session.getLogout().before(new Date())) {
			sessions.remove(token);
			throw new UnauthorizedAccess("You have been logged in for more than an hour please Log In again to continue");
		}

	}

	private static Assessment[] generateAssessments(Student stu1) {
		Assessment history = new AssessmentImpl("History", new Date(new Date().getTime() +1000)
				,
			new List<Question>() {
			new QuestionImpl(1, "In what year did TeamSlicedBread create the H.E.L.M.E.T?",
					new String[]{"2013", "2014", "2017"}),
			new QuestionImpl(2, "What year is it now?",
					new String[]{"2012","2018","2019"})}
			, 1, 13500527);
		return new Assessment[]{history};
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			Student s1 = new Student("yellow", 1);
			Assessment[] assessments = generateAssessments(s1);

			ExamEngine engine = new ExamEngine(new Student[] { new Student("yellow", 1) }, assessments);

			int token = engine.login(s1.getId(), "yellow");
			Question History = engine.getAvailableSummary(token, s1.getId()).get(0).getQuestion(1);
			System.out.println(History);

			Assessment HistoryAssessment = engine.getAssessment(token, s1.getId(), "History");
			Question Q1 = HistoryAssessment.getQuestion(1);
			System.out.println(Q1);
			System.out.println(Q1.getAnswerOptions()[HistoryAssessment.getSelectedAnswer(1)]);
			HistoryAssessment.selectAnswer(1, 2);
			System.out.println(Q1.getAnswerOptions()[HistoryAssessment.getSelectedAnswer(1)]);
			engine.submitAssessment(token, s1.getId(), HistoryAssessment);

			String name = "ExamServer";
			ExamServer engine1 = new ExamEngine();
			ExamServer stub = (ExamServer) UnicastRemoteObject.exportObject(engine1, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("ExamEngine bound");
		} catch (Exception e) {
			System.err.println("ExamEngine exception:");
			e.printStackTrace();
		}
	}
}

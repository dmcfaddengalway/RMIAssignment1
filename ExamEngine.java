import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ExamEngine implements ExamServer {

	private static Student[] students;
	private HashMap<Integer, Session> sessions;
	private static Assessment[] assessments;
	Random randomNumber;

	// Constructor is required
	public ExamEngine(Student[] students, Assessment[] assessments) {
		super();
		ExamEngine.students = students;
		ExamEngine.assessments = assessments;
		this.sessions = new HashMap<>();
	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period
	public int login(int studentID, String password) throws UnauthorizedAccess, RemoteException {
		int min = 0;
		int max = 10000000;

		for (Student student : students) {
			if (student.getID() == studentID && student.getPassword() == password) {
				int randomNum = randomNumber.nextInt((max - min) + 1) + min;
				sessions.put(randomNum, new Session(studentID));

				return randomNum;
			}
		}

		throw new UnauthorizedAccess("Your studentID and/or password was incorrect.");
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int studentID, int token) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		checkSessionId(token, studentID);
		ArrayList<String> assessments = new ArrayList<String>();

		for (Assessment a : ExamEngine.assessments) {
			if (a.getAssociatedID() == studentID) {
				assessments.add(a.getInformation());
			}
		}

		if (assessments.size() == 0) {
			throw new NoMatchingAssessment("No assessments found; please check studentID.");
		}

		return assessments;
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentID, String courseCode) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		checkSessionId(token, studentID);

		for (Assessment assessment : assessments) {
			if(assessment.getInformation().equals(courseCode) &&
					assessment.getAssociatedID() == studentID &&
					assessment.getClosingDate().after(new Date())
				) {
				return assessment;
			}
		}

		throw new NoMatchingAssessment("No assessments were found for this course.");
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

	}

	private void checkSessionId(int token, int studentID) throws UnauthorizedAccess, RemoteException {
		Session session = sessions.get(token);

		if (session == null || session.getStudentId() != studentID) {
			throw new UnauthorizedAccess("Error! Are you sure you are logged in?");
		}

		if (session.getLogout().before(new Date())) {
			sessions.remove(token);
			throw new UnauthorizedAccess("You have exceded the session timer. Please log in again to continue.");
		}

	}

	private static Assessment[] generateAssessments(Student stu1) {
		ArrayList<Question> questionList = new ArrayList<Question>();

		Question q1 = new QuestionImp(1, "In what year did TeamSlicedBread create the H.E.L.M.E.T?",
					new String[]{"2013", "2014", "2017"});
		Question q2 = new QuestionImp(2, "What year is it now?",
					new String[]{"2012","2018","2019"});

		questionList.add(q1);
		questionList.add(q2);

		Assessment history = new AssessmentImp("History", new Date(new Date().getTime() + 1000), questionList, 1, 13500527);
		return new Assessment[] { history };
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			Student s1 = new Student(15105456, "desmond");
			Student s2 = new Student(16280010, "chambers");

			Student[] students = {s1, s2};
			Assessment[] assessments = generateAssessments(s1);

			ExamEngine engine = new ExamEngine(students, assessments);

			// ExamEngine engine = new ExamEngine(Student[] {s1}, assessments);

			int token = engine.login(s1.getID(), "desmond");
			// Question History = engine.getAvailableSummary(s1.getId(), token).get(0).getQuestion(1);
			List<String> historyQuestion = engine.getAvailableSummary(s1.getID(), token);
			System.out.println(historyQuestion);

			Assessment HistoryAssessment = engine.getAssessment(token, s1.getID(), "History");
			Question Q1 = HistoryAssessment.getQuestion(1);
			System.out.println(Q1);
			System.out.println(Q1.getAnswerOptions()[HistoryAssessment.getSelectedAnswer(1)]);
			HistoryAssessment.selectAnswer(1, 2);
			System.out.println(Q1.getAnswerOptions()[HistoryAssessment.getSelectedAnswer(1)]);
			engine.submitAssessment(token, s1.getID(), HistoryAssessment);

			String name = "ExamServer";
			// ExamServer engine1 = new ExamEngine();
			ExamServer stub = (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("ExamEngine bound");
		} catch (Exception e) {
			System.err.println("ExamEngine exception:");
			e.printStackTrace();
		}
	}
}

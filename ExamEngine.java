import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamEngine implements ExamServer {

	private List<Student> students;
	private List<Assessment> assessments;
	private List<Session> sessions;
	private List<Question> questions;

	// Constructor is required
	public ExamEngine() {
		super();

		students = new ArrayList<Student>();
    assessments = new ArrayList<Assessment>();
    sessions = new ArrayList<Session>();
    questions = new ArrayList<Question>();

    students.add(new Student(12345678, "GY350"));

		String[] a = {"6", "5", "7"};
		Question q = new QuestionImp(1, "What is 2 + 5", a, "7");
    questions.add(q);

		Assessment assessment = new AssessmentImp("Assessment 1", new Date(new Date().getTime() + (7 * 60 * 60 * 1000)), questions, 7, 12345678);
		assessments.add(assessment);

	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period
	public int login(int studentID, String password) throws UnauthorizedAccess, RemoteException {
		for (Student student : students) {
			System.out.println(student.getID());
			System.out.println(student.getPassword());
			System.out.println(studentID);
			System.out.println(password);
			if (student.getID() == studentID && student.getPassword().equals(password)) {
				sessions.add(new Session(studentID));
				System.out.println("Student: " + studentID + " has logged in.");

				return 1;
			}
		}

		throw new UnauthorizedAccess("Your studentID and/or password was incorrect.");
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int studentID, int token) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		ArrayList<String> availableAssessments = new ArrayList<String>();
		System.out.println("In Servers Assessment");

		System.out.println(sessions);
		for(Session session : sessions) {
			if(session.isActive() == token && session.getStudentID() == studentID) {
				for (Assessment assessment : assessments) {
					if(assessment.getAssociatedID() == studentID) {
						availableAssessments.add(assessment.getInformation());
					}
				}
				break;
			}
		}

		if (assessments.size() == 0) {
			throw new NoMatchingAssessment("No assessments found; please check studentID.");
		}

		return availableAssessments;
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentID, String courseCode) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

		for(Session session : sessions) {
			if(session.isActive() == token && session.getStudentID() == studentID) {
				for (Assessment assessment : assessments) {
					if(assessment.getInformation().equals(courseCode) &&
							assessment.getAssociatedID() == studentID &&
							assessment.getClosingDate().after(new Date())
						) {
						return assessment;
					}
				}
				break;
			}
		}

		throw new NoMatchingAssessment("No assessments were found for this course.");
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentID, Assessment completed) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

		for(Session session : sessions) {
			if(session.isActive() == token && session.getStudentID() == studentID) {
				for(Assessment assessment : assessments) {
					if(assessment.getAssociatedID() == studentID && new Date().before(assessment.getClosingDate())) {
						System.out.println("Success! " + studentID + " has submitted their assignment.");
						return;
					}
				}
				break;
			}
		}

		throw new NoMatchingAssessment("No assessments were found for this course.");
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "ExamServer";
			ExamServer engine = new ExamEngine();

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

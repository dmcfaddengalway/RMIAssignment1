import java.awt.event.ActionEvent;
import java.rmi.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Daniel McFadden (16280010)
 * Joan Rohan (15104654)
 */
public class Client extends JFrame implements ActionListener, ExamServer {

  private static final long serialVersionUID = 1L;
  public static ExamServer server;
  JLabel lblId;
  JLabel lblPass;
  JLabel lblAssessmentNum;
  JLabel lblQuestionNum;
  JLabel lblAnswer;
  JLabel lblSubmission;
  JTextField txtId;
  JTextField txtPass;
  JTextField txtAssessmentNum;
  JTextField desiredQNum;
  JTextField answerInput;
  JButton btnProcess;
  JButton btnSubmit;
  JButton btnSubmitQuestionAnswer;
  JTextArea txtS;
  JTextArea txtQ;
  JTextArea quest;

  public Client() {

    this.setTitle("GUI");
    this.setSize(500, 300);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);

    // Labels and text fields for ID & password
    lblId = new JLabel("Student ID: ");
    lblId.setBounds(10, 10, 90, 21);
    add(lblId);

    txtId = new JTextField();
    txtId.setBounds(105, 10, 90, 21);
    add(txtId);

    lblPass = new JLabel("Password: ");
    lblPass.setBounds(10, 35, 90, 21);
    add(lblPass);

    txtPass = new JTextField();
    txtPass.setBounds(105, 35, 90, 21);
    add(txtPass);

    btnProcess = new JButton("Login");
    btnProcess.setBounds(200, 40, 90, 21);
    btnProcess.addActionListener(this);
    add(btnProcess);

    txtS = new JTextArea();
    txtS.setBounds(10, 85, 290, 120);
    add(txtS);

    lblAssessmentNum = new JLabel("Enter Assessment Number: ");
    lblAssessmentNum.setBounds(10, 250, 170, 21);
    add(lblAssessmentNum);
    lblAssessmentNum.setVisible(false);

    txtAssessmentNum = new JTextField();
    txtAssessmentNum.setBounds(180, 250, 90, 21);
    add(txtAssessmentNum);
    txtAssessmentNum.setVisible(false);

    btnSubmit = new JButton("Submit");
    btnSubmit.setBounds(100, 280, 90, 21);
    btnSubmit.addActionListener(this);
    add(btnSubmit);
    btnSubmit.setVisible(false);

    quest = new JTextArea();
    quest.setBounds(10, 320, 290, 120);
    add(quest);
    quest.setVisible(false);

    lblQuestionNum = new JLabel("Enter Question Number: ");
    lblQuestionNum.setBounds(10, 450, 170, 21);
    add(lblQuestionNum);
    lblQuestionNum.setVisible(false);

    desiredQNum = new JTextField();
    desiredQNum.setBounds(180, 450, 90, 21);
    add(desiredQNum);
    desiredQNum.setVisible(false);

    txtQ = new JTextArea();
    txtQ.setBounds(10, 320, 440, 120);
    add(txtQ);
    txtQ.setVisible(false);

    lblAnswer = new JLabel("Enter Answer Number: ");
    lblAnswer.setBounds(10, 480, 170, 21);
    add(lblAnswer);
    lblAnswer.setVisible(false);

    answerInput = new JTextField();
    answerInput.setBounds(180, 480, 90, 21);
    add(answerInput);
    answerInput.setVisible(false);

    btnSubmitQuestionAnswer = new JButton("Submit");
    btnSubmitQuestionAnswer.setBounds(100, 520, 90, 21);
    btnSubmitQuestionAnswer.addActionListener(this);
    add(btnSubmitQuestionAnswer);
    btnSubmitQuestionAnswer.setVisible(false);

    lblSubmission = new JLabel("Submitted!");
    lblSubmission.setBounds(10, 550, 170, 21);
    add(lblSubmission);
    lblSubmission.setVisible(false);

    this.setVisible(true);
  }

  public static void main(String[] args) throws RemoteException, NotBoundException {
    new Client();
    String name = "ExamServer";
    Registry registry = LocateRegistry.getRegistry();
    server = (ExamServer) registry.lookup(name);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    int ID = Integer.parseInt(txtId.getText());
    String pass = txtPass.getText();
    Assessment completedAssessment = null;
    int token = 0;

    try {
      token = this.login(ID, pass);
    } catch (RemoteException | UnauthorizedAccess e) {
      e.printStackTrace();
    }

    if (event.getSource().equals(btnProcess)) {
      try {
        this.getAvailableSummary(ID, token);
      } catch (IOException | UnauthorizedAccess | NoMatchingAssessment exc1) {
        exc1.printStackTrace();
      }
    } else if (event.getSource().equals((btnSubmit))) {
      try {
        String assessmentID = txtAssessmentNum.getText();
        completedAssessment = this.getAssessment(token, ID, assessmentID);
      } catch (IOException | UnauthorizedAccess | NoMatchingAssessment exc2) {
        exc2.printStackTrace();
      }
    } else if (event.getSource().equals(btnSubmitQuestionAnswer)) {
      try {
        this.submitAssessment(token, ID, completedAssessment);
      } catch (UnauthorizedAccess | NoMatchingAssessment | RemoteException exc4) {
        exc4.printStackTrace();
      }

    }
  }

  @Override
  public int login(int studentid, String password) throws UnauthorizedAccess, RemoteException {
    int loggedIn = server.login(studentid, password);

    if (loggedIn == 1) {

      return 1;
    } else {

      return 0;
    }
  }

  @Override
  public List<String> getAvailableSummary(int studentID, int token)
      throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    List<String> availableAssessments;
    availableAssessments = server.getAvailableSummary(studentID, token);

    txtS.append("Available Assessments: \n");
    for (String assignmentList : availableAssessments) {
      System.out.println(assignmentList);
      txtS.append("Assessment #" + assignmentList.toString());
      txtS.append("\n");
    }
    lblAssessmentNum.setVisible(true);
    txtAssessmentNum.setVisible(true);
    btnSubmit.setVisible(true);

    return availableAssessments;
  }

  @Override
  public Assessment getAssessment(int token, int studentid, String courseCode)
      throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    Assessment assessment = server.getAssessment(token, studentid, courseCode);
    List<Question> questions = assessment.getQuestions();
    txtQ.setVisible(true);
    int answerOptionNum = 1;

    txtQ.append("Available Questions: \n");
    for (Question q : questions) {
      String qNum = Integer.toString(q.getQuestionNumber());
      txtQ.append(qNum + ") " + q.getQuestionDetail() + "\n");
      for (Question qOptions : questions) {
        String[] qOpts = qOptions.getAnswerOptions();
        for (String option : qOpts) {
          txtQ.append("\tOption #" + answerOptionNum++ + ") " + option.toString() + "\n");
        }
      }
    }
    lblQuestionNum.setVisible(true);
    desiredQNum.setVisible(true);
    lblAnswer.setVisible(true);
    answerInput.setVisible(true);
    btnSubmitQuestionAnswer.setVisible(true);

    int desiredQuestion = Integer.parseInt(desiredQNum.getText());
    int chosenAnswer = Integer.parseInt(answerInput.getText());

    try {
      assessment.selectAnswer(desiredQuestion, chosenAnswer);
    } catch (InvalidQuestionNumber | InvalidOptionNumber e) {
      e.printStackTrace();
    }

    return assessment;
  }

  @Override
  public void submitAssessment(int token, int studentid, Assessment completed)
      throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        server.submitAssessment(token, studentid, completed);
        lblSubmission.setVisible(true);

  }

}

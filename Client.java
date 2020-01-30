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

public class Client extends JFrame implements ActionListener, ExamServer {

  private static final long serialVersionUID = 1L;
  public static ExamServer server;
  JLabel lblId;
  JLabel lblPass;
  JTextField txtId;
  JTextField txtPass;
  JButton btnProcess;
  JTextArea txtS;

  public Client() {

    this.setTitle("GUI");
    this.setSize(320, 240);
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

    this.setVisible(true);
  }

  public static void main(String[] args) throws RemoteException, NotBoundException {
    new Client();
    String name = "ExamServer";
    Registry registry = LocateRegistry.getRegistry();
    server = (ExamServer) registry.lookup(name);
    System.out.println("GUI started!");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(btnProcess)) {
      try {
        int ID = Integer.parseInt(txtId.getText());
        String pass = txtPass.getText();
        int token = this.login(ID, pass);
        this.getAvailableSummary(ID, token);
      } catch (IOException | UnauthorizedAccess | NoMatchingAssessment e1) {
        e1.printStackTrace();
      }
    }
  }

  @Override
  public int login(int studentid, String password) throws UnauthorizedAccess, RemoteException {
    int loggedIn = server.login(studentid, password);

    if(loggedIn == 1) {
      System.out.println("Logged in");
      return 1;
    } else {
      System.out.println("Not logged in");
      return 0;
    }
  }

  @Override
  public List<String> getAvailableSummary(int studentID, int token) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    System.out.println("In Client Assessment");
    List<String> availableAssessments;
    availableAssessments = server.getAvailableSummary(studentID, token);

    for(String ass : availableAssessments) {
      System.out.println(ass);
    }

    return availableAssessments;
  }

  @Override
  public Assessment getAssessment(int token, int studentid, String courseCode)
      throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
    return null;
  }

  @Override
  public void submitAssessment(int token, int studentid, Assessment completed)
      throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

  }

}

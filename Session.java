import java.util.Date;

public class Session {

  // Member Variables
  private Date logoutTime;
  private int studentId;

  // Constructor
  public Session(int studentID) {
    this.studentId = studentID;
    this.logoutTime = new Date(new Date().getTime() + 3600000);
  }

  // Methods

  // Accessor Methods

    // Setters

    // Getters
    public int getStudentId() {
      return studentId;
    }

    public Date getLogout() {
      return logoutTime;
    }

}

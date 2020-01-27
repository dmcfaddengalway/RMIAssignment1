import java.util.Date;

public class Session {

  // Member Variables
  private Date logout;
  private int studentId;

  // Constructor
  public Session(int id) {
    this.logout = new Date(new Date().getTime() + 3600000);
    this.studentId = id;
  }

  // Methods

  // Accessor Methods

    // Setters

    // Getters
    public int getStudentId() {
      return studentId;
    }

    public Date getLogout() {
      return logout;
    }

}

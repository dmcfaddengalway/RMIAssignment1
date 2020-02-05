import java.util.Date;

/*
 * Daniel McFadden (16280010)
 * Joan Rohan (15104654)
 */
public class Session {

  // Member Variables
  private Date logoutTime;
  private int isValid;
  private int studentId;

  // Constructor
  public Session(int studentID) {
    this.isValid = 1;
    this.studentId = studentID;
    this.logoutTime = new Date(new Date().getTime() + 3600000);
  }

  // Methods

  // Accessor Methods
  public int isActive() {
    if(new Date().before(logoutTime)) {
			return 1;
		} else {
      return 0;
    }
  }

    // Setters

    // Getters
    public int getStudentID() {
      return studentId;
    }

    public Date getLogout() {
      return logoutTime;
    }

}

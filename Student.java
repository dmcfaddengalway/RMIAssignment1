import java.io.Serializable;

public class Student implements Serializable {

  // Member Variables
  private String password;
  private int studentIDNum;
  private static final long serialVersionUID = 1L;

  // Constructor
  public Student(int studentIDNum, String pass) {
    this.studentIDNum = studentIDNum;
    this.password = pass;
  }

  // Methods

  // Accessor Methods

    // Setters

    // Getters
    public String getPassword() {
      return password;
    }

    public int getID() {
      return studentIDNum;
    }

}

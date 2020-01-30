public class Student {

  // Member Variables
  private String password;
  private int studentIDNum;

  // Constructor
  public Student(int studentIDNum, String password) {
    this.studentIDNum = studentIDNum;
    this.password = password;
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

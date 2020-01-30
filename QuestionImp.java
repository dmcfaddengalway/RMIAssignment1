public class QuestionImp implements Question {

  // Member Variables
  private int questionNum;
  private String questionDetails;
  private String[] options;
  private static final long serialVersionUID = 1L;

  // Constructor
  public QuestionImp(int qNum, String qDetails, String[] options, String answer) {
    this.questionNum = qNum;
    this.questionDetails = qDetails;
    this.options = options;
  }

  // Methods

  // Accessor Methods

    // Setters

    // Getters
    @Override
    public int getQuestionNumber() {
      return this.questionNum;
    }

    @Override
    public String getQuestionDetail() {
      return this.questionDetails;
    }

    @Override
    public String[] getAnswerOptions() {
      return this.options;
    }

}

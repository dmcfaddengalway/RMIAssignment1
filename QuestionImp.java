public class QuestionImp implements Question {

  // Member Variables
  private int questionNo;
  private String questionDe;
  private String[] options;
  private static final long serialVersionUID = 1L;

  // Constructor
  public QuestionImp(int qNo, String qDe, String[] options) {
    this.options = options;
    this.questionDe = qDe;
    this.questionNo = qNo;
  }

  // Methods

  // Accessor Methods

    // Setters

    // Getters
    @Override
    public int getQuestionNumber() {
      return this.questionNo;
    }

    @Override
    public String getQuestionDetail() {
      return this.questionDe;
    }

    @Override
    public String[] getAnswerOptions() {
      return this.options;
    }

}

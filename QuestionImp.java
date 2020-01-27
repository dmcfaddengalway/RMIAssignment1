public class QuestionImpl implements Question {
  private int questionNo;
  private String questionDe;
  private String[] options;

  public QuestionImpl(int qNo, String qDe, String[] options) {
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

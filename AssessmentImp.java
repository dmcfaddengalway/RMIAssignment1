import java.util.Date;
import java.util.List;

public class AssessmentImpl implements Assessment {

  // Member Variables
  private String information;
  private Date ClosingDate;
  private List<Question> questions;
  private int answer;
  private int AssociatedId;

  // Constructor
  public AssessmentImpl(String information, Date Closing, List quest, int answer, int ID) {
    this.information = information;
    this.ClosingDate = Closing;
    this.questions = quest;
    this.answer = answer;
    this.AssociatedId = ID;
  }

  // Methods
  @Override
  public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {
    if (questionNumber < 0 || questionNumber >= questions.size()) {
      throw new InvalidQuestionNumber();
    }

    if (optionNumber < 0 || optionNumber >= getQuestion(questionNumber).getAnswerOptions().length) {
      throw new InvalidOptionNumber();
    }
    this.answer = optionNumber;

  }

  // Accessor Methods

    // Setters

    // Getters
    @Override
    public String getInformation() {
      return this.information;
    }

    @Override
    public Date getClosingDate() {
      return this.ClosingDate;
    }

    @Override
    public List<Question> getQuestions() {
      return this.questions;
    }

    @Override
    public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
      try {
        return questions.get(questionNumber);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new InvalidQuestionNumber();
      }
    }

    @Override
    public int getSelectedAnswer(int questionNumber) {
      return answer;
    }

    @Override
    public int getAssociatedID() {
      return this.AssociatedId;
    }

}

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Daniel McFadden (16280010)
 * Joan Rohan (15104654)
 */
public class AssessmentImp implements Assessment {

  // Member Variables
  private String information;
  private Date ClosingDate;
  private List<Question> questions = new ArrayList<Question>();
  private int answer;
  private int associatedId;
  private static final long serialVersionUID = 1L;

  // Constructor
  public AssessmentImp(String information, Date Closing, List<Question> question, int answer, int ID) {
    this.information = information;
    this.ClosingDate = Closing;
    this.questions = question;
    this.answer = answer;
    this.associatedId = ID;
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
      return this.associatedId;
    }

}

// Question.java
import java.io.Serializable;

/*
 * Daniel McFadden (16280010)
 * Joan Rohan (15104654)
 */
public interface Question extends Serializable {

	// Return the question number
	public int getQuestionNumber();

	// Return the question text
	public String getQuestionDetail();

	// Return the possible answers to select from
	public String[] getAnswerOptions();

}

/*
 * Daniel McFadden (16280010)
 * Joan Rohan (15104654)
 */
public class UnauthorizedAccess extends Exception {

	private static final long serialVersionUID = 1L;

	public UnauthorizedAccess(String reason) {
		super(reason);
	}
}


package executeMulan.exceptions;

/**
 * Exception for required parameter that is missing
 * 
 * @author Jose M. Moyano
 *
 */
public class MissingParameter extends Exception { 
    /** serialVersionUID */
	private static final long serialVersionUID = -5726529074867657287L;

	public MissingParameter(String parameter) {
        super("Required parameter -" + parameter + " is missing.");
    }
}

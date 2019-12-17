package executeMulan.exceptions;

/**
 * Exception for required value of a specified parameter that is missing
 * 
 * @author Jose M. Moyano
 *
 */
public class MissingParameterValue extends Exception { 
	/** serialVersionUID */
	private static final long serialVersionUID = -1202677222354084940L;

	public MissingParameterValue(String parameter) {
    	super("Value for parameter -" + parameter + " was not given.");
    }
}

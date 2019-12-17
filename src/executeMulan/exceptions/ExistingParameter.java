package executeMulan.exceptions;

/**
 * Exception for a parameter already existing
 * 
 * @author Jose M. Moyano
 *
 */
public class ExistingParameter extends Exception { 
    /** serialVersionUID */
	private static final long serialVersionUID = -5726529074867657287L;

	public ExistingParameter(String parameter) {
        super("Parameter -" + parameter + " was previously included.");
    }
}

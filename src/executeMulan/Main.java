package executeMulan;

import parameters.Parameters;

/**
 * Main class to execute the algorithm
 * 
 * @author Jose M. Moyano
 *
 */
public class Main {

	public static void main(String[] args) {
		Parameters params = new Parameters();
		
		params.parseParameters(args);
		
		MulanExecutor mBuilder = new MulanExecutor(params);
		mBuilder.execute();
		
		System.out.println("Finished.");
	}
	
	
}

package parameters;

import java.util.HashMap;
import java.util.LinkedHashMap;

import mulan.classifier.MultiLabelLearner;

/**
 * Class to control the parameters of each of the learners
 * 
 * @author Jose M. Moyano
 *
 */
public abstract class LearnerParameters {
	
	/**
	 * Check if any parameter for the algorithm is not given and fill with its default parameters
	 * 
	 * @param parameters Parameters of the user
	 * @return Parameters including default parameters that were not given by the user
	 */
	public void checkDefaultParameters(Parameters parameters) {
		HashMap<String, String> defParameters = defaultParameters();
		
		try {
			for(String key : defParameters.keySet()) {
				if(!parameters.checkParam(key, false)) {
					parameters.putParameter(key, defParameters.get(key));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print the default parameters of the algorithm
	 */
	public void printDefaultParameters() {
		HashMap<String, String> defParameters = defaultParameters();
		
		System.out.println("Default parameters: ");
		for(String key: defParameters.keySet()) {
			System.out.println(key + ": " + defParameters.get(key));
		}
	}
	
	/**
	 * Print the description of the parameters of the algorithm
	 */
	public void printParametersDescription() {
		LinkedHashMap<String, String> parametersDesc = parameterDescription();
		
		System.out.println("Default parameters: ");
		for(String key: parametersDesc.keySet()) {
			System.out.println(key + ": " + parametersDesc.get(key));
		}
	}
	
	/**
	 * Create the object of the corresponding algorithm given the parameters
	 * 
	 * @param parameters Parameters of the algorithm
	 * @param seed Seed for random numbers
	 * @return MultiLabelLearner object of the corresponding algorithm. Not built.
	 */
	public abstract MultiLabelLearner createObject(Parameters parameters, int seed);
	
	/**
	 * Create the object of the corresponding algorithm given the parameters (without seed)
	 * 
	 * @param parameters Parameters of the algorithm
	 * @return MultiLabelLearner object of the corresponding algorithm. Not built.
	 */
	public MultiLabelLearner createObject(Parameters parameters) {
		return createObject(parameters, 1);
	}
	
	/**
	 * Obtain the default parameters of the algorithm
	 * 
	 * @return Hashmap with the default parameters of the algorithm
	 */
	protected abstract HashMap<String, String> defaultParameters();
	
	/**
	 * Obtain the description of each of the parameters of the algorithm
	 * 
	 * @return HashMap with the description of each parameter of the algorithm
	 */
	protected abstract LinkedHashMap<String, String> parameterDescription();
}

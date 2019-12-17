package parameters;

import java.util.LinkedHashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import executeMulan.exceptions.ExistingParameter;
import executeMulan.exceptions.MissingParameter;
import executeMulan.exceptions.MissingParameterValue;

/**
 * Class to store the parameters of the algorithms to run
 * 
 * @author Jose M. Moyano
 *
 */
public class Parameters {

	/**
	 * Options object to retrieve the parameters
	 */
	protected Options options = null;
	
	/**
	 * Map with all the parameters:
	 * 	- Key: flag of the parameter
	 *  - Value: value of the parameter as String
	 */
	protected LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	
	/**
	 * Empty constructor
	 */
	public Parameters() {
		prepareOptions();
	}
	
	/**
	 * Returns the value of a parameter in the map
	 * 
	 * @param key Parameter key
	 * @return Value of the parameter; null if it is not included
	 */
	public String getParameter(String key) {
		if(parameters.containsKey(key)) {
			return parameters.get(key);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Returns the value of a parameter in the map as a double
	 * 
	 * @param key Parameter key
	 * @return Double value of the parameter
	 */
	public double getDoubleParameter(String key) {
		if(parameters.containsKey(key)) {
			try {
				return Double.parseDouble(parameters.get(key));
			} catch(Exception e) {
				System.out.println("Error trying to obtain parameter [" + key + " -> " + parameters.get(key) + "] as double.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return Double.MIN_VALUE;
	}
	
	/**
	 * Returns the value of a parameter in the map as an int
	 * 
	 * @param key Parameter key
	 * @return Int value of the parameter
	 */
	public int getIntParameter(String key) {
		
		if(parameters.containsKey(key)) {
			try {
				return Integer.parseInt(parameters.get(key));
			} catch(Exception e) {
				System.out.println("Error trying to obtain parameter [" + key + " -> " + parameters.get(key) + "] as integer.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return Integer.MIN_VALUE;
	}
	
	/**
	 * Returns the value of a parameter in the map as a boolean
	 * 
	 * @param key Parameter key
	 * @return Boolean value of the parameter
	 */
	public boolean getBooleanParameter(String key) {
		if(parameters.containsKey(key)) {
			try {
				return Boolean.parseBoolean(parameters.get(key));
			} catch(Exception e) {
				System.out.println("Error trying to obtain parameter [" + key + " -> " + parameters.get(key) + "] as boolean.");
				e.printStackTrace();
				System.exit(1);
			}
		}

		System.out.println("Parameter \"" + key + "\" does not exist.");
		System.exit(1);
		return false;
	}
	
	/**
	 * Parse the parameters given in the args array
	 * 
	 * @param args Arguments
	 */
	public void parseParameters(String [] args) {
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			
			//Parse common parameters
			parseParameter(cmd, "t", true, true, null); //Train data
			parseParameter(cmd, "T", false, true, parameters.get("t")); //Test data
			parseParameter(cmd, "x", true, true, null); //XML file
			parseParameter(cmd, "o", true, true, null); //Output file
			parseParameter(cmd, "i", false, true, "1"); //Number of iterations/different seeds. By default = 1
			parseParameter(cmd, "f", false, true, "0"); //Numbers of folds for CV. By default = 0 (no CV)
			parseParameter(cmd, "l", false, false, "false"); //Show macro measures for all labels? By default = false
			parseParameter(cmd, "a", true, true, null); //Algorithm name
			//Check if the algorithm name is allowed
			if(! allowedAlgorithm(parameters.get("a"))) {
				System.out.println("The algorithm \"" + parameters.get("a") + "\" is not an allowed algorithm name.");
				System.out.println("The allowed algorithms are: ");
				printAllowedAlgorithms();
				System.exit(1);
			}
			
			//Parse algorithm-specific parameters
			parseParameter(cmd, "b", false, true, null);
			parseParameter(cmd, "c", false, true, null);
			parseParameter(cmd, "d", false, false, null);
			parseParameter(cmd, "e", false, true, null);
			parseParameter(cmd, "k", false, true, null);
			parseParameter(cmd, "m", false, true, null);
			parseParameter(cmd, "n", false, true, null);
			parseParameter(cmd, "p", false, true, null);
			parseParameter(cmd, "r", false, true, null);
			parseParameter(cmd, "s", false, true, null);
			parseParameter(cmd, "u", false, false, null);
			parseParameter(cmd, "v", false, true, null);
			parseParameter(cmd, "w", false, false, null);
			parseParameter(cmd, "z", false, true, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print the parameters
	 */
	public void printParameters() {
		for(String key : parameters.keySet()) {
			System.out.println(key + ": " + parameters.get(key));
		}
	}
	
	/**
	 * Print the algorithms included
	 */
	public void printAllowedAlgorithms() {
		LinkedHashMap<String, String> algorithms = includedAlgorithms();
		
		for(String key : algorithms.keySet()) {
			System.out.println(key + ": " + algorithms.get(key));
		}
	}
	
	/**
	 * Check if parameter exist in the map.
	 * 
	 * @param param Parameter to check
	 * @param throwException Indicate if an exception is thrown in case of not existing
	 * @return true if exist (all correct); false otherwise
	 * @throws MissingParameter Exception thrown if indicated and parameter not given
	 */
	public boolean checkParam(String param, boolean throwException) throws MissingParameter {
		if(parameters.containsKey(param)) {
			return true;
		}
		else if(throwException){
			throw new MissingParameter(param);
		}
		return false;
	}
	
	/**
	 * Check if parameter exist in the map.
	 * 
	 * @param param Parameter to check
	 * @return true if exist (all correct); false otherwise
	 */
	public boolean checkParam(String param) {
		try {
			return checkParam(param, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Prepare the options
	 */
	protected void prepareOptions() {
		options = new Options();
		
		//Common parameters
		options.addOption("a", true, "Algorithm name. Call algorithms() method to get the list of available algorithms. Required parameter.");
		options.addOption("f", true, "Number of folds for cross-validation. By default, do not perform CV.");
		options.addOption("i", true, "Number of executions using different random seeds. By default, i=1.");
		options.addOption("l", false, "Report values of macro measures for each label independently. By default l=false.");
		options.addOption("o", true, "Output filename to store the results. Required parameter.");		
		options.addOption("t", true, "Training .arff filename. Used as full data in case of CV. Required parameter.");
		options.addOption("T", true, "Test .arff filename. If not provided, train data is used as test. If CV, test is not used.");
		options.addOption("x", true, "Labels .xml filename. Required parameter.");

		//Specific parameters for each algorithm
		options.addOption("b", true, "Parameter b; different meaning for different algorithms.");
		options.addOption("c", true, "Parameter c; different meaning for different algorithms.");
		options.addOption("d", false, "Parameter d; different meaning for different algorithms.");
		options.addOption("e", true, "Parameter e; different meaning for different algorithms.");
		options.addOption("k", true, "Parameter k; different meaning for different algorithms.");
		options.addOption("m", true, "Parameter m; different meaning for different algorithms.");
		options.addOption("n", true, "Parameter n; different meaning for different algorithms.");
		options.addOption("p", true, "Parameter p; different meaning for different algorithms.");
		options.addOption("r", true, "Parameter r; different meaning for different algorithms.");
		options.addOption("s", true, "Parameter s; different meaning for different algorithms.");
		options.addOption("u", false, "Parameter u; different meaning for different algorithms.");
		options.addOption("v", false, "Parameter V; different meaning for different algorithms.");
		options.addOption("w", false, "Parameter w; different meaning for different algorithms.");
		options.addOption("z", false, "Parameter z; different meaning for different algorithms.");
	}
		
	/**
	 * Parse a parameter
	 * 
	 * @param cmd CommandLine object
	 * @param param String defining the parameter
	 * @param required True if the parameter is required; false if it is optional
	 * @param needsValue True if the parameter needs a value; false otherwise
	 * @param defaultValue Default value for the parameter in case it was not given
	 * @throws MissingParameterValue If the parameter needs a value, and the parameter was given without value, throws an exception
	 * @throws ExistingParameter If the given parameter was already parsed, throws an exception
	 * @throws MissingParameter If the parameter is required and is not given, throws an exception
	 */
	protected void parseParameter(CommandLine cmd, String param, boolean required, boolean needsValue, String defaultValue) throws MissingParameterValue, ExistingParameter, MissingParameter {
		//If parameter needs a value
		if(needsValue) {
			if(cmd.hasOption(param)) {
				//Get value and include in the map
				String value = cmd.getOptionValue(param);				
				if(value != null) {
					putParameter(param, value);
				}
				//Throw error if the value of the parameter was not given
				else throw new MissingParameterValue(param);
			}
			//If required parameter was not given, throw exception
			else if(required) throw new MissingParameter(param);
			//If the parameter is not required and was not given, include the default value if given
			else if (defaultValue != null){
				putParameter(param, defaultValue);
			}
		}
		//If the parameter is boolean
		else {
			if(cmd.hasOption(param)) {
				putParameter(param, "true");
			}
			//If required parameter was not given, throw exception
			else if(required) throw new MissingParameter(param);
			//If the parameter is not required and was not given, include the default value if given
			else if(defaultValue != null){
				putParameter(param, defaultValue);
			}
		}
	}
	
	/**
	 * Check if a given algorithm name is correct or not
	 * 
	 * @param algorithm Algorithm name
	 * @return True if the algorithm name is correct/allowed and false otherwise
	 */
	public boolean allowedAlgorithm(String algorithm) {
		if(includedAlgorithms().containsKey(algorithm)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Get a map with available algorithms.
	 * The map contains the algorithm name as key, and the description as value.
	 * 
	 * @return Map with names and descriptions of available methods.
	 */
	protected LinkedHashMap<String, String> includedAlgorithms(){
		LinkedHashMap<String, String> algorithms = new LinkedHashMap<String, String>(20);
		
		algorithms.putAll(includedClassificationAlgorithms());
		algorithms.putAll(includedRegressionAlgorithms());
		
		return algorithms;
	}
	
	/**
	 * Get a map with available classification algorithms.
	 * The map contains the algorithm name as key, and the description as value.
	 * 
	 * @return Map with names and descriptions of available classification methods.
	 */
	public static LinkedHashMap<String, String> includedClassificationAlgorithms(){
		LinkedHashMap<String, String> algorithms = new LinkedHashMap<String, String>(20);
		
		//Problem Transformation (PTs)
		algorithms.put("BR", "Binary Relevance.");
		algorithms.put("CC", "Classifier Chains.");
		algorithms.put("CLR", "Calibrated Label Ranking.");
		algorithms.put("LP", "Label Powerset.");
		algorithms.put("LPBR", "LPBR.");
		//algorithms.put("PCC", "Parallel Classifier Chains.");
		algorithms.put("PS", "Pruned Sets.");
		
		//Algorithm Adaptation (AAs)
		algorithms.put("AdaBoostMH", "AdaBoost.MH.");
		algorithms.put("BPMLL", "Back-Propagation for Multi-Label Learning.");
		algorithms.put("IBLR", "Instance-Based Logistic Regression.");
		algorithms.put("MLkNN", "Multi-Label k-Nearest Neighbors.");
		
		//Ensembles of Multi-Label Classifiers (EMLCs)
		algorithms.put("CDE", "Chi-Dep Ensemble.");
		algorithms.put("EBR", "Ensemble of Binary Relevances.");
		algorithms.put("ECC", "Ensemble of Classifier Chains.");
		algorithms.put("ELP", "Ensemble of Label Powersets.");
		algorithms.put("EPS", "Ensemble of Pruned Sets.");
		algorithms.put("HOMER", "Hierarchy Of Multilabel classifiERs.");
		algorithms.put("MLS", "Multi-Label Stacking.");
		algorithms.put("RAkEL", "RAndom k-labELsets.");
		algorithms.put("RFPCT", "Random Forest of Predictive Clustering Trees.");
		
		return algorithms;
	}
	
	/**
	 * Get a map with available regression algorithms.
	 * The map contains the algorithm name as key, and the description as value.
	 * 
	 * @return Map with names and descriptions of available regression methods.
	 */
	public static LinkedHashMap<String, String> includedRegressionAlgorithms(){
		LinkedHashMap<String, String> algorithms = new LinkedHashMap<String, String>(20);
		
		algorithms.put("ERC", "Ensemble of Regressor Chains.");
		algorithms.put("RC", "Regressor Chain.");
		algorithms.put("RLC", "Random Linear Combinations (Normalized).");
		algorithms.put("ST", "Single Target.");
		algorithms.put("SST", "Stacked ST.");
		
		return algorithms;
	}
	
	/**
	 * Include a pair parameter-value into the map. Check if it existed.
	 * 
	 * @param key Parameter 
	 * @param value Value of the parameter
	 * @throws ExistingParameter If the parameter already existed in the map, throw an exception
	 */
	public void putParameter(String key, String value) throws ExistingParameter {
		//If the parameter was previously parsed, throw exception
		if(parameters.containsKey(key)) {
			throw new ExistingParameter(key);
		}
		
		//Include parameter and value in the map
		parameters.put(key, value);
	}
	
	/**
	 * Replace the value for a given key into the map. Check if it does not exist.
	 * 
	 * @param key Parameter 
	 * @param value Value of the parameter
	 * @throws MissingParameter If the parameter did not exist in the map, throw an exception
	 */
	public void replaceParameter(String key, String value) throws MissingParameter {
		//If the does not exist, throw exception
		if(! parameters.containsKey(key)) {
			throw new MissingParameter(key);
		}
		
		//Include parameter and value in the map
		parameters.replace(key, value);
	}
}

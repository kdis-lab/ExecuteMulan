package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.MultiLabelStacking;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of MLS
 * 
 * @author Jose M. Moyano
 *
 */
public class MLSParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("n", "10");
		map.put("r", "1.0");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Number of folds for the first level.");
		map.put("r", "Ratio of labels to use in the meta-level.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new MultiLabelStacking(
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				parameters.getIntParameter("n"), //Number of folds for first level
				parameters.getDoubleParameter("r"), //Ratio of labels for meta level
				seed //Seed for random numbers
				); 
		
		return learner;
	}
	
}

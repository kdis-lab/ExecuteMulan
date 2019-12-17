package parameters.mlc.aa;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.AdaBoostMH;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of AdaBoost.MH
 * 
 * @author Jose M. Moyano
 *
 */
public class AdaBoostParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "weka.classifiers.trees.DecisionStump");
		map.put("n", "10");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Max number of resampling iterations.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new AdaBoostMH(
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				parameters.getIntParameter("n"), //Number of models
				seed //Seed for random numbers
				);
		
		return learner;
	}
	
}

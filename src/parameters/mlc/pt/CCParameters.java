package parameters.mlc.pt;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.ClassifierChain;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of CC
 * 
 * @author Jose M. Moyano
 *
 */
public class CCParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new ClassifierChain(
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				seed //Seed for random numbers
				);
		
		return learner;
	}
	
}

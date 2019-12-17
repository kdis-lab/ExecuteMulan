package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.EnsembleOfSubsetLearners;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of CDE
 * 
 * @author Jose M. Moyano
 *
 */
public class CDEParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("n", "10");
		map.put("p", "10000");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Number of members in the ensemble.");
		map.put("p", "Number of random partitions into group of labels.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new EnsembleOfSubsetLearners(
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				parameters.getIntParameter("n"), //Number of models
				parameters.getIntParameter("p"), //useConfidences
				seed //Seed for random numbers
				);
		
		return learner;
	}
	
}

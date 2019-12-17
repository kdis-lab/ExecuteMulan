package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.EnsembleOfLabelPowersets;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of ELP
 * 
 * @author Jose M. Moyano
 *
 */
public class ELPParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("n", "10");
		map.put("r", "67");
		map.put("u", "false");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Number of members in the ensemble.");
		map.put("r", "Sampling percentage [0, 100]. Percentage of instances to use at each LP.");
		map.put("u", "UseConfidences (boolean). True if confidences are used to combine predictions.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new EnsembleOfLabelPowersets(
				parameters.getDoubleParameter("r"), //Percentage of instances
				parameters.getIntParameter("n"), //Number of models
				0.5,
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				parameters.getBooleanParameter("u"), //UseConfidences
				seed //Seed for random numbers
				);
		
//		MultiLabelLearner learner = new EnsembleOfLabelPowersets();
		
		return learner;
	}
	
}

package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.EnsembleOfClassifierChains;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of ECC
 * 
 * @author Jose M. Moyano
 *
 */
public class ECCParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("n", "10");
		map.put("u", "false");
		map.put("w", "true");
		map.put("b", "100");
		map.put("r", "67");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Number of members in the ensemble.");
		map.put("u", "UseConfidences (boolean). True if confidences are used to combine predictions.");
		map.put("w", "SampleWithReplacement (boolean). True if sample with replacement is used to generate the ensemble.");
		map.put("b", "Bag size percent [0, 100]. Percentage of instances to use when sampleWithReplacement = true.");
		map.put("r", "Sampling percentage [0, 100]. Percentage of instances to use when sampleWithReplacement = false.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new EnsembleOfClassifierChains(
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				parameters.getIntParameter("n"), //Number of models
				parameters.getBooleanParameter("u"), //useConfidences
				parameters.getBooleanParameter("w"), //sampleWithReplacement
				parameters.getIntParameter("b"), //bagSizePercent
				parameters.getDoubleParameter("r"), //samplingPercentage
				seed //Seed for random numbers
				);
		
		return learner;
	}
	
}

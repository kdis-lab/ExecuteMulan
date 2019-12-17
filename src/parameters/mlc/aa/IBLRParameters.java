package parameters.mlc.aa;

import java.util.HashMap;
import java.util.LinkedHashMap;

import mulan.classifier.MultiLabelLearner;
import mulan.classifier.lazy.IBLR_ML;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of IBLR-ML
 * 
 * @author Jose M. Moyano
 *
 */
public class IBLRParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("d", "false");
		map.put("k", "10");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("d", "Indicates if features are added in logistic regression, i.e., IBLR-ML+ (boolean).");
		map.put("k", "Number of neighbors.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new IBLR_ML(
				parameters.getIntParameter("k"), //Number of neighbors
				parameters.getBooleanParameter("d") //AddFeatures
				);
		
		return learner;
	}
	
}

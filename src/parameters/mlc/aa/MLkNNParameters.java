package parameters.mlc.aa;

import java.util.HashMap;
import java.util.LinkedHashMap;

import mulan.classifier.MultiLabelLearner;
import mulan.classifier.lazy.MLkNN;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of MLkNN
 * 
 * @author Jose M. Moyano
 *
 */
public class MLkNNParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("k", "10");
		map.put("s", "1.0");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("k", "Number of neighbors.");
		map.put("s", "Smoothing parameter controlling the strength of uniform prior.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new MLkNN(
				parameters.getIntParameter("k"), //Number of neighbors
				parameters.getDoubleParameter("s") //Smoothing
				);
		
		return learner;
	}
	
}

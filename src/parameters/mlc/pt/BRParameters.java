package parameters.mlc.pt;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.transformation.BinaryRelevance;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of BR
 * 
 * @author Jose M. Moyano
 *
 */
public class BRParameters extends LearnerParameters{
	
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
		
		MultiLabelLearner learner = new BinaryRelevance(
				Utils.getBaseLearner(parameters.getParameter("c")) //Classifier
				);
		
		return learner;
	}
	
}

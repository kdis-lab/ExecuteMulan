package parameters.mtr;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.regressor.transformation.SingleTargetRegressor;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of ST
 * 
 * @author Jose M. Moyano
 *
 */
public class STParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "REPTree");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base regressor.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new SingleTargetRegressor(
				Utils.getBaseLearner(parameters.getParameter("c")) //Regressor
				);
		
		return learner;
	}
	
}

package parameters.mtr;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.regressor.transformation.RandomLinearCombinationsNormalize;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of ERC
 * 
 * @author Jose M. Moyano
 *
 */
public class RLCParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "REPTree");
		map.put("n", "100");
		map.put("z", "2");	
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base regressor.");
		map.put("n", "Number of members in the ensemble (different combinations).");
		map.put("z", "Number of non-zero targets.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new RandomLinearCombinationsNormalize(
				parameters.getIntParameter("n"), //Number of different combinations / members
				seed,
				Utils.getBaseLearner(parameters.getParameter("c")), //Regressor
				parameters.getIntParameter("z") //Non-zero
				);
		
		return learner;
	}
	
}

package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.RFPCT;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of RF-PCT
 * 
 * @author Jose M. Moyano
 *
 */
public class RFPCTParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("n", "10");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("n", "Number of trees.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		if(parameters.getIntParameter("f") > 1) {
			System.out.println("RF-PCT cannot be executed with cross-validation");
			System.exit(1);
		}
		
		MultiLabelLearner learner = new RFPCT(
				"libs/Clus.jar", //Directory of the Clus.jar
				parameters.getIntParameter("n"), //Number of trees
				seed //Seed for random numbers
				);
		
		return learner;
	}
	
}

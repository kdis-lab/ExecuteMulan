package parameters.mlc.aa;

import java.util.HashMap;
import java.util.LinkedHashMap;

import mulan.classifier.MultiLabelLearner;
import mulan.classifier.neural.BPMLL;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of BPMLL
 * 
 * @author Jose M. Moyano
 *
 */
public class BPMLLParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("e", "100");
		map.put("r", "0.05");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("e", "Number of epochs.");
		map.put("r", "Learning rate.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new BPMLL(seed);
		((BPMLL)learner).setTrainingEpochs(parameters.getIntParameter("e"));
		((BPMLL)learner).setLearningRate(parameters.getDoubleParameter("r"));
		
		return learner;
	}
	
}

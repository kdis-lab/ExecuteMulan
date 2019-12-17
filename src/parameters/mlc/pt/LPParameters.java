package parameters.mlc.pt;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.transformation.LabelPowerset;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of LP
 * 
 * @author Jose M. Moyano
 *
 */
public class LPParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("u", "false");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("u", "UseConfidences (boolean). True if confidences are used to combine predictions.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new LabelPowerset(
				Utils.getBaseLearner(parameters.getParameter("c")) //Classifier
				);
		((LabelPowerset)learner).setMakePredictionsBasedOnConfidences(parameters.getBooleanParameter("u")); //UseConfidences
		((LabelPowerset)learner).setSeed(seed); //Seed
		
		return learner;
	}
	
}

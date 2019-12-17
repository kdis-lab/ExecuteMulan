package parameters.mlc.pt;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.transformation.PrunedSets;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of PS
 * 
 * @author Jose M. Moyano
 *
 */
public class PSParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("p", "2");
		map.put("b", "3");
		map.put("s", "A");
		map.put("u", "false");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("p", "Parameter p of PS.");
		map.put("b", "Parameter b of PS.");
		map.put("s", "Strategy for PS. Strategy A keeps top b ranked labelsets. Strategy B keeps all labelsets of size greater than b.");
		map.put("u", "UseConfidences (boolean). True if confidences are used to combine predictions.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		PrunedSets.Strategy strategy = null;//= PrunedSets.Strategy.A;
		if(parameters.getParameter("s").equalsIgnoreCase("A")) {
			strategy = PrunedSets.Strategy.A;
		}
		else if(parameters.getParameter("s").equalsIgnoreCase("B")) {
			strategy = PrunedSets.Strategy.B;
		}
		else {
			System.out.println("Strategy \"" + parameters.getParameter("s") + "\"for EPS is not valid.");
			System.exit(1);
		}
		
		MultiLabelLearner learner = new PrunedSets(
				Utils.getBaseLearner(parameters.getParameter("c")), //Classifier
				parameters.getIntParameter("p"), //Parameter p
				strategy, //Strategy
				parameters.getIntParameter("b") //Parameter b
				);
		((PrunedSets)learner).setMakePredictionsBasedOnConfidences(parameters.getBooleanParameter("u"));
		
		return learner;
	}
	
}

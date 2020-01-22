package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mlc.BinaryRelevance;
import algorithms.mlc.EnsembleOfSampling;
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
public class EMLSParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("n", "5");
		map.put("p", "0.3");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Number of members in the ensemble.");
		map.put("p", "Sampling ratio (0, 1].");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new EnsembleOfSampling();
		((EnsembleOfSampling)learner).setBaseMlLearner(new BinaryRelevance(Utils.getBaseLearner(parameters.getParameter("c"))));
		((EnsembleOfSampling)learner).setNumModels(parameters.getIntParameter("n"));
		((EnsembleOfSampling)learner).setP(parameters.getDoubleParameter("p"));
		((EnsembleOfSampling)learner).setSeed(seed);
		
		return learner;
	}
	
}

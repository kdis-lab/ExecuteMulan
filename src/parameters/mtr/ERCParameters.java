package parameters.mtr;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.regressor.transformation.EnsembleOfRegressorChains;
import mulan.regressor.transformation.EnsembleOfRegressorChains.SamplingMethod;
import mulan.regressor.transformation.RegressorChain.metaType;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of ERC
 * 
 * @author Jose M. Moyano
 *
 */
public class ERCParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "REPTree");
		map.put("n", "10");
		map.put("s", "WithReplacement");
		map.put("m", "True");
		map.put("b", "100");
		map.put("r", "67");
		map.put("v", "3");		
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base regressor.");
		map.put("n", "Number of members in the ensemble.");
		map.put("s", "Sampling method.");
		map.put("m", "Method to obtain values of meta features.");
		map.put("b", "Bag size percent [0, 100]. Percentage of instances to use when sample = WithReplacement.");
		map.put("r", "Sampling percentage [0, 100]. Percentage of instances to use when sample = WithoutReplacement.");
		map.put("v", "Number of folds to use when meta features are obtained with CV method.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		SamplingMethod samplingMethod = null;
		if(parameters.getParameter("s").equalsIgnoreCase("None")) {
			samplingMethod = SamplingMethod.None;
		}
		else if(parameters.getParameter("s").equalsIgnoreCase("WithReplacement")) {
			samplingMethod = SamplingMethod.WithReplacement;
		}
		else if(parameters.getParameter("s").equalsIgnoreCase("WithoutReplacement")) {
			samplingMethod = SamplingMethod.WithoutReplacement;
		}
		else {
			System.out.println("Incorrect sampling method.");
			System.exit(-1);
		}
		
		metaType meta = null;
		if(parameters.getParameter("m").equalsIgnoreCase("CV")) {
			meta = metaType.CV;
		}
		else if(parameters.getParameter("m").equalsIgnoreCase("Insample")) {
			meta = metaType.INSAMPLE;
		}
		else if(parameters.getParameter("m").equalsIgnoreCase("True")) {
			meta = metaType.TRUE;
		}
		else if(parameters.getParameter("m").equalsIgnoreCase("Sample")) {
			meta = metaType.SAMPLE;
		}
		else {
			System.out.println("Incorrect meta type: " + parameters.getParameter("m"));
			System.exit(-1);
		}
		
		MultiLabelLearner learner = null;
		try {
			learner = new EnsembleOfRegressorChains(
					Utils.getBaseLearner(parameters.getParameter("c")), //Regressor
					parameters.getIntParameter("n"), //Number of members
					samplingMethod //Sampling method
					);
			
			((EnsembleOfRegressorChains)learner).setSeed(seed);
			((EnsembleOfRegressorChains)learner).setSampleWithReplacementPercent(parameters.getIntParameter("b"));
			((EnsembleOfRegressorChains)learner).setSampleWithoutReplacementPercent(parameters.getIntParameter("r"));
			((EnsembleOfRegressorChains)learner).setNumFolds(parameters.getIntParameter("v"));
			((EnsembleOfRegressorChains)learner).setMeta(meta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return learner;
	}
	
}

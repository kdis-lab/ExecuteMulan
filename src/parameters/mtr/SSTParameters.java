package parameters.mtr;

import java.util.HashMap;
import java.util.LinkedHashMap;

import algorithms.mtr.MultiTargetStacking;
import algorithms.mtr.MultiTargetStacking.metaType;
import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of ERC
 * 
 * @author Jose M. Moyano
 *
 */
public class SSTParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "REPTree");
		map.put("m", "Insample");
		map.put("v", "3");		
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base regressor.");
		map.put("m", "Method to obtain values of meta features.");
		map.put("v", "Number of folds to use when meta features are obtained with CV method.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
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
			learner = new MultiTargetStacking(
					Utils.getBaseLearner(parameters.getParameter("c")) //Regressor
					);

			((MultiTargetStacking)learner).setSeed(seed);
			((MultiTargetStacking)learner).setNumFolds(parameters.getIntParameter("v"));
			((MultiTargetStacking)learner).setMeta(meta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return learner;
	}
	
}

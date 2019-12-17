package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import executeMulan.exceptions.MissingParameter;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.meta.RAkEL;
import mulan.classifier.transformation.LabelPowerset;
import mulan.data.InvalidDataFormatException;
import mulan.data.MultiLabelInstances;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of RAkEL
 * 
 * @author Jose M. Moyano
 *
 */
public class RAkELParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		
		//Default n for RAkEL is n=2q
		//As here we don't have access to q (numer of labels), it will be put then.
		map.put("n", "-1");
		map.put("k", "3");
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("n", "Number of members in the ensemble.");
		map.put("k", "Size of the k-labelsets.");
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		//If n=-1, set it to n=2q
		if(parameters.getIntParameter("n") < 0) {
			int n;
			MultiLabelInstances mlData;
			try {
				mlData = new MultiLabelInstances(parameters.getParameter("t"), parameters.getParameter("x"));
				n = 2 * mlData.getNumLabels();
				parameters.replaceParameter("n", String.valueOf(n));
				
			} catch (InvalidDataFormatException e) {
				e.printStackTrace();
			} catch (MissingParameter e) {
				e.printStackTrace();
			}
		}
		
		MultiLabelLearner learner = new RAkEL(
				new LabelPowerset(Utils.getBaseLearner(parameters.getParameter("c"))), //Use LP with given classifier
				parameters.getIntParameter("n"), //Number of models
				parameters.getIntParameter("k") //Size of k-labelsets
				);
		((RAkEL)learner).setSeed(seed);
		
		return learner;
	}
	
}

package parameters.mlc.emlc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import executeMulan.Utils;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.meta.HOMER;
import mulan.classifier.meta.HierarchyBuilder;
import mulan.classifier.transformation.BinaryRelevance;
import parameters.LearnerParameters;
import parameters.Parameters;

/**
 * Parameters of HOMER
 * 
 * @author Jose M. Moyano
 *
 */
public class HOMERParameters extends LearnerParameters{
	
	@Override
	protected HashMap<String, String> defaultParameters(){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("c", "J48");
		map.put("k", "3");	
		
		return map;
	}
	
	@Override
	protected LinkedHashMap<String, String> parameterDescription(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("c", "Base classifier.");
		map.put("k", "Number of clusters.");
		
		
		return map;
	}

	@Override
	public MultiLabelLearner createObject(Parameters parameters, int seed) {
		this.checkDefaultParameters(parameters);
		
		MultiLabelLearner learner = new HOMER(
				//By default they use BR -> use BR with given base classifier
				new BinaryRelevance(Utils.getBaseLearner(parameters.getParameter("c"))),
				parameters.getIntParameter("k"), //Number of clusters
				HierarchyBuilder.Method.BalancedClustering //Clustering type
				);
		
		return learner;
	}
	
}

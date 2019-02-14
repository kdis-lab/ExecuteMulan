package executeMulan.mlc;
import java.util.Random;

import mulan.classifier.transformation.EnsembleOfPrunedSets;
import mulan.classifier.transformation.PrunedSets;
import weka.classifiers.trees.J48;


public class EPS_1 extends EnsembleOfPrunedSets {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8232684670814787107L;

	private void setRand(Random r)
	{
		this.rand = r;
	}
	
	public void setSeed(long s)
	{
		Random r = new Random(s);
		setRand(r);
	}
	
	EPS_1()
	{
		super(66, 10, 0.5, 1, PrunedSets.Strategy.A, 3, new J48());
	}
	
	EPS_1(int s)
	{
		super(66, 10, 0.5, 1, PrunedSets.Strategy.A, 3, new J48());
		Random r = new Random(s);
		setRand(r);
	}
}

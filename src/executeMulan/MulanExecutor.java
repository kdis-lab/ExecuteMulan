package executeMulan;

import java.util.List;

import algorithms.mlc.RFPCT;
import mulan.classifier.MultiLabelLearner;
import mulan.data.InvalidDataFormatException;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import mulan.evaluation.measure.LabelMatrix;
import mulan.evaluation.measure.Measure;
import parameters.LearnerParameters;
import parameters.Parameters;
import parameters.mlc.aa.AdaBoostParameters;
import parameters.mlc.aa.BPMLLParameters;
import parameters.mlc.aa.IBLRParameters;
import parameters.mlc.aa.MLkNNParameters;
import parameters.mlc.emlc.CDEParameters;
import parameters.mlc.emlc.EBRParameters;
import parameters.mlc.emlc.ECCParameters;
import parameters.mlc.emlc.ELPParameters;
import parameters.mlc.emlc.EMLSParameters;
import parameters.mlc.emlc.EPSParameters;
import parameters.mlc.emlc.HOMERParameters;
import parameters.mlc.emlc.MLSParameters;
import parameters.mlc.emlc.RAkELParameters;
import parameters.mlc.emlc.RFPCTParameters;
import parameters.mlc.pt.BRParameters;
import parameters.mlc.pt.CCParameters;
import parameters.mlc.pt.CLRParameters;
import parameters.mlc.pt.LPBRParameters;
import parameters.mlc.pt.LPParameters;
import parameters.mlc.pt.PSParameters;
import parameters.mtr.ERCParameters;
import parameters.mtr.RCParameters;
import parameters.mtr.RLCParameters;
import parameters.mtr.SSTParameters;
import parameters.mtr.STParameters;

/**
 * Class to guide the execution of any Multi-Label Learning method from Mulan.
 * 
 * @author Jose M. Moyano
 *
 */
public class MulanExecutor {
	
	/**
	 * Parameters to execute the Mulan algorithm
	 */
	Parameters parameters = null;
	
	/**
	 * Constructor
	 * 
	 * @param parameters Parameters to execute the Mulan algorithm
	 */
	public MulanExecutor(Parameters parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Setter for the parameters
	 * 
	 * @param parameters Parameters
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Execute the corresponding learner with the given parameters
	 * The whole process is performed:
	 * 	- Read dataset
	 *  - Build classifier
	 *  - Evaluate
	 */
	public void execute() {
		//Get common parameters
		String trainFilename = parameters.getParameter("t");
		String testFilename = parameters.getParameter("T");
		String xmlFilename = parameters.getParameter("x");
		String algName = parameters.getParameter("a");
		String oFilename = parameters.getParameter("o");
		int nSeeds = parameters.getIntParameter("i");
		int nFolds = parameters.getIntParameter("f");
		boolean reportMacro = parameters.getBooleanParameter("l");
		
		List<Measure> measures = null;
		
		//Read datasets and get time
		long init_time = System.currentTimeMillis(), total_time;
		long readData_time = 0;
		MultiLabelInstances mlTrain=null, mlTest=null;
		try {
			mlTrain = new MultiLabelInstances(trainFilename, xmlFilename);
			mlTest = new MultiLabelInstances(testFilename, xmlFilename);
			readData_time = System.currentTimeMillis() - init_time;
			
			//List of measures to evaluate
			if(Parameters.includedClassificationAlgorithms().containsKey(algName)) {
				measures = Utils.prepareMeasuresClassification(mlTrain);
			}
			else if(Parameters.includedRegressionAlgorithms().containsKey(algName)) {
				measures = Utils.prepareMeasuresRegression(mlTrain, mlTest);
			}
			
			Utils.printHeader(oFilename, measures, mlTrain, reportMacro);
		} catch (InvalidDataFormatException e1) {
			e1.printStackTrace();
		}
		
		MultiLabelLearner learner = null;
		
		//Repeat execution for the specified number of iterations with different seeds
		int seed = 0;
		for(int i=1; i<=nSeeds; i++) {
			//New seed
			seed = 10*i;
			
			//Init time corresponding to the classifier
			init_time = System.currentTimeMillis();
			
			//Create object of the corresponding multi-label learner
			learner = getLearner(parameters, seed);
			
			//If cross-validation is not performed, build over train, evaluate over test
			if(nFolds <= 1) {
				try {
					//Build learner
					learner.build(mlTrain);
					
					//Evaluate
					Evaluation results = null;
					Evaluator eval = new Evaluator();
					
					//RF-PCT needs to be evaluated in other way (by Clus library)
					if(algName.equalsIgnoreCase("RFPCT")) {
						results = eval.evaluate((RFPCT)learner, mlTest, measures);
						
						LabelMatrix lm = Utils.getLabelsClus(mlTest.getNumInstances(), mlTest.getNumLabels());
			    	    results = Utils.evaluate(lm.realLabels, lm.predLabels, measures);
					}
					else {
						//Evaluate rest of methods
						results = eval.evaluate(learner, mlTest, measures);
					}	
							
					
					//Total time is time of building and evaluating the classifier, plus reading the data
					total_time = (System.currentTimeMillis() - init_time) + readData_time;
					
					//Print results
					Utils.printResults(oFilename, results, total_time, testFilename, algName, mlTest, reportMacro);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//Perform cross-validation using the training data
			else {
				Evaluator eval = new Evaluator();
				MultipleEvaluation mResults = eval.crossValidate(learner, mlTrain, measures, nFolds);
				
				//Total time is time of cross-validating divided by the number of folds, plus time of reading the data
				total_time = (int)Math.round((((System.currentTimeMillis() - init_time)*1.0)/nFolds)) + readData_time;
				
				//Print mean results
				Utils.printResultsCV(oFilename, measures, mResults, total_time, trainFilename, algName, mlTrain, reportMacro);
			}
		}
	}
	
	/**
	 * Get the corresponding learner given the parameters
	 * 
	 * @param parameters Parameters of the algorithm
	 * @param seed Seed for random numbers
	 * @return Corresponding Multi-Label Learner (not built)
	 */
	protected MultiLabelLearner getLearner(Parameters parameters, int seed) {
		String algName = parameters.getParameter("a");
		
		MultiLabelLearner learner = null;
		LearnerParameters cParameters = null;
		
		//Get parameters object corresponding to the given algorithm
		switch (algName) {
		
		/** Multi-Label Classification methods */
		case "AdaBoostMH":
			cParameters = new AdaBoostParameters();
			break;
				
		case "BPMLL":
			cParameters = new BPMLLParameters();
			break;
			
		case "BR":
			cParameters = new BRParameters();
			break;
			
		case "CC":
			cParameters = new CCParameters();
			break;
			
		case "CDE":
			cParameters = new CDEParameters();
			break;
			
		case "CLR":
			cParameters = new CLRParameters();
			break;
			
		case "EBR":
			cParameters = new EBRParameters();
			break;
			
		case "ECC":
			cParameters = new ECCParameters();
			break;
			
		case "ELP":
			cParameters = new ELPParameters();
			break;
			
		case "EMLS":
			cParameters = new EMLSParameters();
			break;
			
		case "EPS":
			cParameters = new EPSParameters();
			break;
			
		case "HOMER":
			cParameters = new HOMERParameters();
			break;
			
		case "IBLR":
			cParameters = new IBLRParameters();
			break;
			
		case "LP":
			cParameters = new LPParameters();
			break;
			
		case "LPBR":
			cParameters = new LPBRParameters();
			break;

		case "MLkNN":
			cParameters = new MLkNNParameters();
			break;
			
		case "MLS":
			cParameters = new MLSParameters();
			break;
			
		case "PS":
			cParameters = new PSParameters();
			break;
			
		case "RAkEL":
			cParameters = new RAkELParameters();
			break;
			
		case "RFPCT":
			cParameters = new RFPCTParameters();
			break;
			
		/** Multi-Target Regression methods */
		case "ERC":
			cParameters = new ERCParameters();
			break;
			
		case "RC":
			cParameters = new RCParameters();
			break;
			
		case "RLC":
			cParameters = new RLCParameters();
			break;
		
		case "ST":
			cParameters = new STParameters();
			break;
			
		case "SST":
			cParameters = new SSTParameters();
			break;
				
		default:
			learner = null;
			System.out.println("The specified algorithm is not available.");
			System.exit(1);
			break;
		}
		
		//Create object
		learner = cParameters.createObject(parameters, seed);
		
		return learner;
	}
	
	
}

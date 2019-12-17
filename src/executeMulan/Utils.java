package executeMulan;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mulan.classifier.MultiLabelOutput;
import mulan.core.MulanException;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.GroundTruth;
import mulan.evaluation.MultipleEvaluation;
import mulan.evaluation.measure.AveragePrecision;
import mulan.evaluation.measure.Coverage;
import mulan.evaluation.measure.ErrorSetSize;
import mulan.evaluation.measure.ExampleBasedAccuracy;
import mulan.evaluation.measure.ExampleBasedFMeasure;
import mulan.evaluation.measure.ExampleBasedPrecision;
import mulan.evaluation.measure.ExampleBasedRecall;
import mulan.evaluation.measure.ExampleBasedSpecificity;
import mulan.evaluation.measure.GeometricMeanAveragePrecision;
import mulan.evaluation.measure.HammingLoss;
import mulan.evaluation.measure.IsError;
import mulan.evaluation.measure.LabelMatrix;
import mulan.evaluation.measure.MacroAverageMeasure;
import mulan.evaluation.measure.MacroFMeasure;
import mulan.evaluation.measure.MacroPrecision;
import mulan.evaluation.measure.MacroRecall;
import mulan.evaluation.measure.MacroSpecificity;
import mulan.evaluation.measure.MeanAveragePrecision;
import mulan.evaluation.measure.Measure;
import mulan.evaluation.measure.MicroFMeasure;
import mulan.evaluation.measure.MicroPrecision;
import mulan.evaluation.measure.MicroRecall;
import mulan.evaluation.measure.MicroSpecificity;
import mulan.evaluation.measure.AdjHammingLoss;
import mulan.evaluation.measure.OneError;
import mulan.evaluation.measure.RankingLoss;
import mulan.evaluation.measure.SubsetAccuracy;
import mulan.evaluation.measures.regression.example.ExampleBasedRMaxSE;
import mulan.evaluation.measures.regression.macro.MacroMAE;
import mulan.evaluation.measures.regression.macro.MacroMaxAE;
import mulan.evaluation.measures.regression.macro.MacroRMSE;
import mulan.evaluation.measures.regression.macro.MacroRMaxSE;
import mulan.evaluation.measures.regression.macro.MacroRelMAE;
import mulan.evaluation.measures.regression.macro.MacroRelRMSE;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;

/**
 * Class with different utilities
 * 
 * @author Jose M. Moyano
 *
 */
public class Utils {

	/**
	 * Get the corresponding learner given its name or full className
	 * 
	 * @param baseLearnerType String defining the single-label base learner used. It could be any of predefined learner or full java class name.
	 * @return Classifier object
	 */
	public static Classifier getBaseLearner(String baseLearnerType) {
		Classifier learner = null;
		
		//J48, predefined
		if(baseLearnerType.equalsIgnoreCase("J48")) {
			learner = new J48();
		}
		//Random Tree, predefined
		else if(baseLearnerType.equalsIgnoreCase("RT")) {
			learner = new RandomTree();
		}
		//SMO, predefined
		else if(baseLearnerType.equalsIgnoreCase("SMO")) {
			learner = new SMO();
		}
		else if(baseLearnerType.equalsIgnoreCase("REPTree")) {
			learner = new REPTree();
		}
		//Use class name to create learner
		else {
			Class<?> classDefinition = null;
			try {
				classDefinition = Class.forName(baseLearnerType);
				learner = (Classifier) classDefinition.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return learner;
	}
	
	/**
	 * Prepare the measures to evaluate classification methods
	 * 
	 * @param mlTrainData Multi-label dataset
	 * @return List with measures to evaluate
	 */
	protected static List<Measure> prepareMeasuresClassification(MultiLabelInstances mlTrainData) {
		List<Measure> measures = new ArrayList<Measure>();

        int numOfLabels = mlTrainData.getNumLabels();

        // add example-based measures
        measures.add(new HammingLoss());
        measures.add(new AdjHammingLoss());
        measures.add(new SubsetAccuracy());
        measures.add(new ExampleBasedPrecision());
        measures.add(new ExampleBasedRecall());
        measures.add(new ExampleBasedFMeasure());
        measures.add(new ExampleBasedAccuracy());
        measures.add(new ExampleBasedSpecificity());

        // add label-based measures
        measures.add(new MicroPrecision(numOfLabels));
        measures.add(new MicroRecall(numOfLabels));
        measures.add(new MicroFMeasure(numOfLabels));
        measures.add(new MicroSpecificity(numOfLabels));
        measures.add(new MacroPrecision(numOfLabels));
        measures.add(new MacroRecall(numOfLabels));
        measures.add(new MacroFMeasure(numOfLabels));
        measures.add(new MacroSpecificity(numOfLabels));

        // add ranking based measures
        measures.add(new AveragePrecision());
        measures.add(new Coverage());
        measures.add(new OneError());
        measures.add(new IsError());
        measures.add(new ErrorSetSize());
        measures.add(new RankingLoss());

        // add confidence measures if applicable
        measures.add(new MeanAveragePrecision(numOfLabels));
        measures.add(new GeometricMeanAveragePrecision(numOfLabels));
        //measures.add(new MicroAUC(numOfLabels));
        //measures.add(new MacroAUC(numOfLabels));

        return measures;
	}
	
	/**
	 * Prepare the measures to evaluate regression methods
	 * 
	 * @param mlTrainData Training data
	 * @param mlTestData Test data
	 * @return List of measures for multi-target regression
	 */
	protected static List<Measure> prepareMeasuresRegression(MultiLabelInstances mlTrainData, MultiLabelInstances mlTestData) {
        List<Measure> measures = new ArrayList<Measure>();

        int numOfLabels = mlTrainData.getNumLabels();
        measures.add(new MacroMAE(numOfLabels));
        measures.add(new MacroRMSE(numOfLabels));
        measures.add(new MacroRelMAE(mlTrainData, mlTestData));
        measures.add(new MacroRelRMSE(mlTrainData, mlTestData));
        
        measures.add(new MacroMaxAE(numOfLabels));
        measures.add(new MacroRMaxSE(numOfLabels));
        
        measures.add(new ExampleBasedRMaxSE());

        return measures;
    }
	
	/**
	 * Print header for CSV results file
	 * 
	 * @param oFilename Output filename
	 * @param measures List of measures
	 * @param mlData Multi-label dataset (to obtain labelNames if corresponds)
	 * @param reportMacro Flag to determine if macro measures for each label are reported
	 */
	protected static void printHeader(String oFilename, List<Measure> measures, MultiLabelInstances mlData, boolean reportMacro){
		PrintWriter pw = null;
		try {
			//Create PrintWriter
			pw = new PrintWriter(new FileWriter(oFilename, true));

			//Data name
			pw.print("Dataset" + ";");

			//For each measure
			for(Measure m : measures)
	        {
	        	//Print name
	        	pw.print(m.getName() + ";");

	        	//If it is a macro measure, and reportMacro flag is true, print them
	        	if((reportMacro) && (m.getClass().getName().contains("Macro")))
	        	{
	        		for(int l=0; l<mlData.getNumLabels(); l++)
	        		{
	        			pw.print(m.getName() + " - " + mlData.getLabelNames()[l] + ";");
	                }
	            }
	        }

	        //Execution time
	        pw.print("Execution time (ms): ");
	        pw.println();

	        //Close pw
	        pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print the results in CSV file
	 * 
	 * @param oFilename Output filename
	 * @param results Results over test set
	 * @param total_time Runtime
	 * @param dataset Dataset name
	 * @param algorithm Algorithm name
	 * @param mlData Multi-label dataset (to obtain labelNames if corresponds)
	 * @param reportMacro Flag to determine if macro measures for each label are reported
	 */
	protected static void printResults(String oFilename, Evaluation results, long total_time, String dataset, String algorithm, MultiLabelInstances mlData, boolean reportMacro) {
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(new FileWriter(oFilename, true));

			//Get dataset name and print it
			String [] p = dataset.split("\\/");
			String datasetName = p[p.length-1].split("\\.")[0];                   
			pw.print(algorithm + "_" + datasetName + ";");

	        //Print measure values
	        for(Measure m : results.getMeasures())
	        {
	        	pw.print(m.getValue() + ";");
	        	if((reportMacro) && (m.getClass().getName().contains("Macro")))
	     	   	{
	     	   		for(int l=0; l<mlData.getNumLabels(); l++)
	     	   		{
	     	   			pw.print(((MacroAverageMeasure) m).getValue(l) + ";");
	     	   		}
	            }
	        }

	        //Print runtime
	        pw.print(total_time + ";");
	        pw.println();

	        //Close pw
	        pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}         
	}
	
	/**
	 * Print the results (cross-validation means) in CSV file
	 * 
	 * @param oFilename Output filename
	 * @param measures List of measures
	 * @param mResults Results of cross-validation
	 * @param total_time Runtime
	 * @param dataset Dataset name
	 * @param algorithm Algorithm name
	 * @param mlData Multi-label dataset (to obtain labelNames if corresponds)
	 * @param reportMacro Flag to determine if macro measures for each label are reported
	 */
	protected static void printResultsCV(String oFilename, List<Measure> measures, MultipleEvaluation mResults, long total_time, String dataset, String algorithm, MultiLabelInstances mlData, boolean reportMacro) {
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(new FileWriter(oFilename, true));

			//Get dataset name and print it
			String [] p = dataset.split("\\/");
			String datasetName = p[p.length-1].split("\\.")[0];                   
			pw.print(algorithm + "_" + datasetName + ";");

			//Print measure means
			for(Measure m : measures) {
	        	pw.print(mResults.getMean(m.getName()) + ";");

	        	if((reportMacro) && (m.getClass().getName().contains("Macro")))
	        	{
	        		for(int l=0; l<mlData.getNumLabels(); l++)
	     	   		{
	     	   			try {
	     	   				pw.print(mResults.getMean(m.getName(), l) + ";");
	     	   			} catch (MulanException e) {
							e.printStackTrace();
						}
	     	   		}
	            }
	        }

	        //Print runtime
	        pw.print(total_time + ";");
	        pw.println();
	        
	        //Close pw
	        pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}   
	}
	
	/**
	 * Get the labels predicted by Clus
	 * 
	 * @param nInstances Number of instances
	 * @param nLabels Number of labels
	 * @return LabelMatrix object with predicted labels
	 * @throws FileNotFoundException Exception
	 * @throws IOException Exception
	 */
	public static LabelMatrix getLabelsClus(int nInstances, int nLabels) throws FileNotFoundException, IOException{
		LabelMatrix lm = new LabelMatrix(nInstances, nLabels);
		
		Instances inst = new Instances(new FileReader("libs/Clus.jardata-train.test.pred.arff"));
		
		for(int i=0; i<nInstances; i++){
			for(int l=0; l<nLabels; l++){
				int v = (int)inst.get(i).value(l);
				
				if(v == 1){
					lm.realLabels[i][l] = 0;
				}
				else{
					lm.realLabels[i][l] = 1;
				}
				
				v = (int)inst.get(i).value(l+nLabels);
				
				if(v == 1){
					lm.predLabels[i][l] = 0;
				}
				else{
					lm.predLabels[i][l] = 1;
				}
			}
		}
		
		return lm;
	}
	
	/**
	 * Evaluate predictions 
	 * 
	 * @param groundTruth True labels
	 * @param predictedLabels Predicted labels
	 * @param measures List of measures
	 * @return Evaluation object
	 * @throws Exception Exception
	 */
	public static Evaluation evaluate(int [][] groundTruth, int [][] predictedLabels, List<Measure> measures) throws Exception {
		
	        // reset measures
	        for (Measure m : measures) {
	            m.reset();
	        }

	        int numLabels = groundTruth[0].length;
	        Set<Measure> failed = new HashSet<Measure>();
	        int numInstances = groundTruth.length;
	        for (int i = 0; i < numInstances; i++) {
	            
	            boolean [] predBool = new boolean[numLabels];
	            boolean [] realBool = new boolean[numLabels];
	            
	            for(int j=0; j<numLabels; j++){
	            	if(predictedLabels[i][j] == 1){
	            		predBool[j] = true;
	            	}
	            	else{
	            		predBool[j] = false;
	            	}
	            	
	            	if(groundTruth[i][j] == 1){
	            		realBool[j] = true;
	            	}
	            	else{
	            		realBool[j] = false;
	            	}
	            }
	            
	            MultiLabelOutput output2 = new MultiLabelOutput(predBool);
	            GroundTruth truth2 = new GroundTruth(realBool);

	            Iterator<Measure> it = measures.iterator();
	            while (it.hasNext()) {
	                Measure m = it.next();
	                if (!failed.contains(m)) {
	                    try {
	                        m.update(output2, truth2);
	                    } catch (Exception ex) {
	                        failed.add(m);
	                    }
	                }
	            }
	        }

	        return new Evaluation(measures, null);
	    }
}

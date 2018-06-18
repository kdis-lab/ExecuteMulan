package executeMulan.mlc;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import executeMulan.ExecuteMulanAlgorithm;
import mulan.classifier.neural.BPMLL;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import mulan.evaluation.measure.*;


public class ExecuteBPMLL extends ExecuteMulanAlgorithm {
	
	public void execute (String tvalue, String Tvalue, String xvalue, String ovalue, boolean lvalue, int nIter)
	{
		 PrintWriter pw = null;
		 MultiLabelInstances trainingSet = null;
		 MultiLabelInstances testSet = null;
		
		 try{			
			trainingSet = new MultiLabelInstances(tvalue, xvalue);
			testSet = new MultiLabelInstances(Tvalue, xvalue);
			
			Evaluator eval = new Evaluator();
            Evaluation results;
            List<Measure> measures = new ArrayList<Measure>();
            
            pw = new PrintWriter(new FileWriter(ovalue, true));
            
            BPMLL learner = null;   
            
            long time_in, time_fin, total_time;
            
            for(int i=1; i<=nIter; i++)
            {            	
        	   time_in = System.currentTimeMillis();
        	   
        	   //Seeds are 10, 20, 30, ...
        	   learner = new BPMLL(i*10);
   	        
    	       learner.build(trainingSet);
    	       
    	       measures = prepareMeasuresClassification(trainingSet, learner);
    	       
    	       results = eval.evaluate(learner, testSet, measures);
    	       
    	       time_fin = System.currentTimeMillis();
    	       
    	       total_time = time_fin - time_in;

    	       System.out.println("Execution time (ms): " + total_time);
            	
    	       //First iteration --> print header
    	       if(i==1)
    	       {
	           		pw.print("Dataset" + ";");
	                for(Measure m : measures)
	                {
	                   pw.print(m.getName() + ";");
	                   if((lvalue) && (m.getClass().getName().contains("Macro")))
	                   {
	                   	   for(int l=0; l<trainingSet.getNumLabels(); l++)
	                   	   {
	                   		   pw.print(m.getName() + " - " + trainingSet.getLabelNames()[l] + ";");
	                	   }
	                   }
	                }
	                pw.print("Execution time (ms): ");
	                pw.println();
    	       }
    	       
    	       String [] p = tvalue.split("\\/");
    	       String datasetName = p[p.length-1].split("\\.")[0];                   
               pw.print("BPMLL_" + datasetName + ";");
               
               for(Measure m : results.getMeasures())
               {
            	   pw.print(m.getValue() + ";");
            	   if((lvalue) && (m.getClass().getName().contains("Macro")))
                   {
                	   for(int l=0; l<trainingSet.getNumLabels(); l++)
                	   {
                		   pw.print(((MacroAverageMeasure) m).getValue(l) + ";");
               		   }
               	   }
               }
               pw.print(total_time + ";");
               pw.println();    
               
            }           
		}
        catch(Exception e1)
    	{
    		e1.printStackTrace();
    	}
    	finally{
    		if(pw != null)
    		{
    			pw.close();
    		}
    	}      
	}
	
}

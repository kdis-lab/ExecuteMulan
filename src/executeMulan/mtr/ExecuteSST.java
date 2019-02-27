package executeMulan.mtr;
import executeMulan.ExecuteMulanAlgorithm;
import mulan.regressor.transformation.MultiTargetStacking;
import mulan.regressor.transformation.MultiTargetStacking.metaType;
import weka.classifiers.trees.REPTree;

public class ExecuteSST extends ExecuteMulanAlgorithm {
	
	public void execute(String tvalue, String Tvalue, String xvalue, String ovalue, boolean lvalue, int fvalue)
	{		
		 try{
			prepareExecution(tvalue, Tvalue, xvalue, ovalue, fvalue);
			 
			MultiTargetStacking learner = null;
            
            /* Only one execution (does not use random numbers) */
			
        	time_in = System.currentTimeMillis();
        	   
        	learner = new MultiTargetStacking(new REPTree());
        	learner.setMeta(metaType.INSAMPLE);
    	    
        	if(nFolds > 0) {
        		mResults = eval.crossValidate(learner, trainingSet, nFolds);
        		for(int m=0; m<mResults.getEvaluations().get(0).getMeasures().size(); m++) {
        			measures.add(mResults.getEvaluations().get(0).getMeasures().get(m));
        		}
        	}
        	else {
        		learner.build(trainingSet);
        		measures = prepareMeasuresRegression(trainingSet, testSet);
        	    results = eval.evaluate(learner, testSet, measures);
        	}
    	       
    	    time_fin = System.currentTimeMillis();
    	      
    	    total_time = time_fin - time_in;

    	    System.out.println("Execution time (ms): " + total_time);

    	    printHeader(lvalue);
    	    if(nFolds <= 0) {
    	    	printResults(Tvalue, lvalue, "SST");
    	    }
    	    else {
    	    	printResultsCV(tvalue, lvalue, "SST");
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

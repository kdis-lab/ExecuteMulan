package executeMulan.mtr;
import executeMulan.ExecuteMulanAlgorithm;
import mulan.regressor.transformation.SingleTargetRegressor;
import weka.classifiers.trees.REPTree;

public class ExecuteST extends ExecuteMulanAlgorithm {
	
	public void execute(String tvalue, String Tvalue, String xvalue, String ovalue, boolean lvalue, int fvalue)
	{		
		 try{
			prepareExecution(tvalue, Tvalue, xvalue, ovalue, fvalue);
			 
			SingleTargetRegressor learner = null;
            
            /* Only one execution (does not use random numbers) */
			
        	time_in = System.currentTimeMillis();
        	   
        	learner = new SingleTargetRegressor(new REPTree());
    	    learner.build(trainingSet);
    	       
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
    	    	printResults(Tvalue, lvalue, "ST");
    	    }
    	    else {
    	    	printResultsCV(tvalue, lvalue, "ST");
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

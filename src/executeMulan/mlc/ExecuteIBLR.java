package executeMulan.mlc;
import executeMulan.ExecuteMulanAlgorithm;
import mulan.classifier.lazy.IBLR_ML;

public class ExecuteIBLR extends ExecuteMulanAlgorithm {
	
	public void execute (String tvalue, String Tvalue, String xvalue, String ovalue, boolean lvalue, int fvalue)
	{		
		 try{
			 prepareExecution(tvalue, Tvalue, xvalue, ovalue, fvalue);
			 
			 IBLR_ML learner = null;
            
            /* Only one execution (does not use random numbers) */
			
        	time_in = System.currentTimeMillis();
        	   
        	learner = new IBLR_ML();
    	    
        	measures = prepareMeasuresClassification(trainingSet);    	
        	
        	if(nFolds > 0) {
        		mResults = eval.crossValidate(learner, trainingSet, measures, nFolds);
        	}
        	else {
        		learner.build(trainingSet);  
        	    results = eval.evaluate(learner, testSet, measures);
        	}
    	       
    	    time_fin = System.currentTimeMillis();
    	      
    	    total_time = time_fin - time_in;

    	    System.out.println("Execution time (ms): " + total_time);

    	    printHeader(lvalue);
    	    if(nFolds <= 0) {
    	    	printResults(Tvalue, lvalue, "IBLR");
    	    }
    	    else {
    	    	printResultsCV(tvalue, lvalue, "IBLR");
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

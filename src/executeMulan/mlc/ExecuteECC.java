package executeMulan.mlc;
import executeMulan.ExecuteMulanAlgorithm;

public class ExecuteECC extends ExecuteMulanAlgorithm {
	
	public void execute (String tvalue, String Tvalue, String xvalue, String ovalue, boolean lvalue, int nIter, int fvalue)
	{		
		 try{
			 prepareExecution(tvalue, Tvalue, xvalue, ovalue, fvalue);
			 
			 ECC learner = null;
            
			 /* The seeds are 10, 20, 30, ... */        	   
			 for(int i=1; i<=nIter; i++)
			 {
				time_in = System.currentTimeMillis();
				 
          	   	learner = new ECC(i*10);

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

	    	    //Print header only in first iteration
	    	    if(i == 1) {
	    	    	printHeader(lvalue);
	    	    }
	    	    
	    	    if(nFolds <= 0) {
	    	    	printResults(Tvalue, lvalue, "ECC");
	    	    }
	    	    else {
	    	    	printResultsCV(tvalue, lvalue, "ECC");
	    	    }
	    	    
			 }//End for
			 
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

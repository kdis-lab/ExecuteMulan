/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package algorithms.mlc;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.transformation.LabelPowerset;
import mulan.classifier.transformation.TransformationBasedMultiLabelLearner;
import mulan.data.MultiLabelInstances;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 * Implementation of the Ensemble of Label Powersets (ELP) algorithm.
 * 
 * @author Jose M. Moyano
 */
public class EnsembleOfLabelPowersets extends TransformationBasedMultiLabelLearner {

    /** serialVersionUID */
	private static final long serialVersionUID = 2599017854711277922L;
	/**
     * Parameter for the threshold of discretization of prediction output
     */
    protected double threshold;
    /**
     * Parameter for the number of models that constitute the ensemble
     */
    protected int numOfModels;
    /**
     * Percentage of data
     */
    protected double percentage;
    /**
     * The models in the ensemble
     */
    protected LabelPowerset[] ensemble;
    /**
     * Random number generator
     */
    protected Random rand;
    

    /**
     * Creates a new instance with default values
     */
    public EnsembleOfLabelPowersets() {
        this(66, 10, 0.5, new J48(), false, 1);
    }
    
    /**
     * Creates a new instance with default values
     */
    public EnsembleOfLabelPowersets(Classifier baselearner, int nClassifiers, int seed) {
        this(66, nClassifiers, 0.5, new J48(), false, seed);
    }
    
    /**
     * @param aNumOfModels the number of models in the ensemble
     * @param aStrategy pruned sets strategy
     * @param aPercentage percentage of data to sample
     * @param aP pruned sets parameter p
     * @param aB pruned sets parameter b
     * @param baselearner the base learner
     * @param aThreshold the threshold for producing bipartitions
     */
    public EnsembleOfLabelPowersets(double aPercentage, int aNumOfModels, double aThreshold, Classifier baselearner, boolean useConfidences, int seed) {
        super(baselearner);
        numOfModels = aNumOfModels;
        threshold = aThreshold;
        percentage = aPercentage;
        ensemble = new LabelPowerset[numOfModels];
        for (int i = 0; i < numOfModels; i++) {
            try {
                ensemble[i] = new LabelPowerset(AbstractClassifier.makeCopy(baselearner));
                ensemble[i].setMakePredictionsBasedOnConfidences(useConfidences);
            } catch (Exception ex) {
                Logger.getLogger(EnsembleOfLabelPowersets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        rand = new Random(seed);
    }

    @Override
    protected void buildInternal(MultiLabelInstances trainingSet)
            throws Exception {
        Instances dataSet = new Instances(trainingSet.getDataSet());

        for (int i = 0; i < numOfModels; i++) {
            dataSet.randomize(rand);
            RemovePercentage rmvp = new RemovePercentage();
            rmvp.setInputFormat(dataSet);
            rmvp.setPercentage(percentage);
            rmvp.setInvertSelection(true);
            Instances trainDataSet = Filter.useFilter(dataSet, rmvp);
            MultiLabelInstances train = new MultiLabelInstances(trainDataSet, trainingSet.getLabelsMetaData());
            ensemble[i].build(train);
        }
    }

    @Override
    protected MultiLabelOutput makePredictionInternal(Instance instance)
            throws Exception, InvalidDataException {

        int[] sumVotes = new int[numLabels];

        for (int i = 0; i < numOfModels; i++) {
            MultiLabelOutput ensembleMLO = ensemble[i].makePrediction(instance);
            boolean[] bip = ensembleMLO.getBipartition();

            for (int j = 0; j < sumVotes.length; j++) {
                sumVotes[j] += bip[j] == true ? 1 : 0;
            }
        }
        double[] confidence = new double[numLabels];

        for (int j = 0; j < sumVotes.length; j++) {
            confidence[j] = (double) sumVotes[j] / (double) numOfModels;
        }

        MultiLabelOutput mlo = new MultiLabelOutput(confidence, threshold);
        return mlo;
    }
}
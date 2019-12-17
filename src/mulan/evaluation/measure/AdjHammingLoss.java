package mulan.evaluation.measure;

import mulan.evaluation.measure.ExampleBasedBipartitionMeasureBase;

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

/*
 *    ExampleBasedAccuracy.java
 *    Copyright (C) 2009-2012 Aristotle University of Thessaloniki, Greece
 */


/**
 * Implementation of the Adjusted Hamming loss (AHL)
 * 
 * 	More information about AHL in:
 * 		F. Charte, A. J. Rivera, M. J. del Jesus, and F. Herrera. (2014). 
 * 		MLeNN: a first approach to heuristic multilabel undersampling.
 * 		In International Conference on Intelligent Data Engineering and Automated Learning (pp. 1-9).
 *
 * @author Jose M. Moyano
 */
public class AdjHammingLoss extends ExampleBasedBipartitionMeasureBase {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2879257844937816754L;
	
	private final double forgivenessRate;

    /**
     * Constructs a new object
     */
    public AdjHammingLoss() {
        this(1.0);
    }

    /**
     * Constructs a new object
     *
     * @param aForgivenessRate the forgiveness rate
     */
    public AdjHammingLoss(double aForgivenessRate) {
        forgivenessRate = aForgivenessRate;
    }

    public String getName() {
        return "Adjusted Hamming Loss";
    }

    public double getIdealValue() {
        return 0;
    }

    @Override
    protected void updateBipartition(boolean[] bipartition, boolean[] truth) {
        double intersection = 0;
        double union = 0;
        for (int i = 0; i < truth.length; i++) {
            if ((bipartition[i] && !truth[i]) || (!bipartition[i] && truth[i]) ) {
                intersection++;
            }
            if (bipartition[i] || truth[i]) {
                union++;
            }
        }

        if (union == 0) {
            sum += Math.pow(0, forgivenessRate);
        } else {
            sum += Math.pow(intersection / union, forgivenessRate);
        }
        count++;
    }
}
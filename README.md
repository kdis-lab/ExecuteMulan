# Executing Multi-Label Learning methods from Mulan.

[Mulan](http://mulan.sourceforge.net/) is an open-source Java library for learning from multi-label datasets; including both Multi-Label Classification (MLC) and Multi-Target Regression (MTR) methods [[Tso11]](#Tso11). The Mulan library only provides a Java API, but no any GUI or command-line interface to execute its methods.

In this repository, we created an executable .jar file to execute MLC and MTR methods from the Mulan library. The aim of this project was to have a personal way to execute experiments with the different methods as simple as possible. The .jar file is available to download at the [latest release](https://github.com/kdis-lab/ExecuteMulan/releases/latest). This code was implemented for its use in the article [[Moy18]](#Moy18).

To execute it, the following command have to be executed in a console:
```sh
java -jar ExecuteMulan_1.3.jar [parameters]
```

The jar file allows several parameters to indicate the method to execute, the dataset, and so on. Some of the parameters are common to all or almost all algorithms, and are the following:
* With the ```-t``` parameter, we define the .arff filename of the training dataset. In case of performing CV procedure, it defines the full set.
* With the ```-T``` parameter, we define the .arff filename of the test dataset. It should not be used if CV procedure is used. If not CV is performed and test data is not given, train data is used as default for testing too.
* With the ```-x``` parameter, we define the .xml filename of the dataset [(know more about the format of the datasets)](http://www.uco.es/kdis/mllresources/).
* With the ```-a``` parameter, the MLC or MTR method to be executed is indicated. Below is provided a list with all the available methods in this jar.
* With the ```-f``` parameter, we define the number of folds for the cross-validation procedure. If it is not set, the CV procedure is not performed, and both train and test are used.
* With the ```-o``` parameter, we define the filename of the output file where the results are stored as a .csv file.
* With the ```-i``` parameter, the number of different seeds for random numbers (for those algorithms that need) is indicated; i.e., the number of executions for those algorithms that use random numbers. If this parameter is passed to a method that does not use random numbers, it is omitted. By default, its value is 1.
* If the ```-l``` flag is included, the values of the macro-averaged measures for each label are reported. By default, they are not reported.

Each method also accept a number of different parameters to tune them. The methods _printDefaultParameters()_ and _printParametersDescription()_ of the _LearnerParameters_ class are very helpful to understand which parameters need each of the method. However, we present some of the most common here:
* With the ```-c``` parameter, we indicate the base learner to use in the given method. Allowed values for this parameter are _J48_ (J48/C4.5 decision tree), _RT_ (Random Tree), _SMO_ (SMO Support Vector) for multi-label classification algorithms, and also _REPTree_ for multi-target regression algorithms. However, if another base learner is needed to be used, the full java classname for the desired base learner can be given, as for example: ```-c weka.classifiers.trees.DecisionStump```.
* For ensemble-based methods, with the parameter ```-n``` we indicate the number of members to use in the ensemble.

The full list of methods availables in this jar is provided below. It is shown the value of the ```-a``` parameter and the full name of the method:
* Multi-label classification. Problem Transformation methods:
  * ```BR```: Binary Relevance
  * ```CC```: Classifier Chains
  * ```CLR```: Calibrated Label Ranking
  * ```LP```: Label Powerset
  * ```LPBR```: LP-BR (a.k.a. Chi-Dep)
  * ```PS```: Pruned Sets
* Multi-label classification. Algorithm Adaptation methods:
  * ```AdaBoostMH```: AdaBoost.MH
  * ```BPMLL```: Back-Propagation for Multi-Label Learning
  * ```IBLR```: Instance-Based Logistic Regression for Multi-Label Learning
  * ```MLkNN```: Multi-Label k-Nearest Neighbors
* Multi-label classification. Ensembles of Multi-Label Classifiers:
  * ```CDE```: Chi-Dep Ensemble
  * ```EBR```: Ensemble of Binary Relevances
  * ```ECC```: Ensemble of Classifier Chains
  * ```ELP```: Ensemble of Label Powersets
  * ```EMLS```: Ensemble of Multi-Label Sampling
  * ```EPS```: Ensemble of Pruned Sets
  * ```HOMER```: Hierarchy Of Multilabel classifiERs
  * ```MLS```: Multi-Label Stacking
  * ```RAkEL```: RAndom k-labELsets
  * ```RFPCT```: Random Forest of Predictive Clustering Trees
* Multi-target regression methods:
  * ```ERC```: Ensemble of Regressor Chains
  * ```RC```: Regressor Chain
  * ```RLC```: Random Linear Combinations (Normalized)
  * ```ST```: Single-Target
  * ```SST```: Stacked ST

Two datasets have been included in the repository as example: _Emotions_ [[Tso08]](#Tso08) (for multi-label classification), and _andro_ [[Hat08]](#Hat08) (for multi-target regression); a wide variety of dataset are available at the [KDIS Research Group Repository](http://www.uco.es/kdis/mllresources/) and the [Mulan webpage](http://mulan.sourceforge.net/datasets.html). 

### References
<a name="Hat08"></a>**[Hat08]** E. V. Hatzikos, G. Tsoumakas, G. Tzanis, N. Bassiliades, and I. Vlahavas. (2008). An empirical study on sea water quality prediction. Knowledge-Based Systems, 21(6), 471-478.

<a name="Tso08"></a>**[Tso08]** G. Tsoumakas, I. Katakis, and I. Vlahavas. (2008). Effective and Efficient Multilabel Classification in Domains with Large Number of Labels. In Proc. ECML/PKDD 2008 Workshop on Mining Multidimensional Data (MMD’08), 53-59.

<a name="Tso11"></a>**[Tso11]** G. Tsoumakas, E. Spyromitros-Xioufis, J. Vilcek, and I. Vlahavas. (2011). Mulan: A java library for multi-label learning. Journal of Machine Learning Research, 12, 2411-2414.

<a name="Moy18"></a>**[Moy18]** J. M. Moyano and E. L. Gibaja and K. J. Cios and S. Ventura. (2018). Review of ensembles of multi-label classifiers: Models, experimental study and prospects. Information Fusion, 44, 33-45.

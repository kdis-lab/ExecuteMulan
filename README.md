# Executing Multi-Label Learning methods from Mulan.

[Mulan](http://mulan.sourceforge.net/) is an open-source Java library for learning from multi-label datasets; including both Multi-Label Classification (MLC) and Multi-Target Regression (MTR) methods. The Mulan library only provides a Java API, but no any GUI or command-line interface to execute its methods.

In this repository, we created an executable .jar file to execute MLC and MTR methods from the Mulan library. The aim of this project was to have a personal way to execute experiments with the different methods as simple as possible.

To execute it, the following command have to be executed in a console:
```sh
java -jar ExecuteMulan.jar [parameters]
```

The jar file allows several parameters to indicate the method to execute, the dataset, and so on. The different parameters are the following:
* With the ```-t``` parameter, we define the .arff filename of the training dataset.
* With the ```-T``` parameter, we define the .arff filename of the test dataset.
* With the ```-x``` parameter, we define the .xml filename of the dataset [(know more about the format of the datasets)](http://www.uco.es/kdis/mllresources/).
* With the ```-a``` parameter, the MLC or MTR method to be executed is indicated. Below is provided a list with all the available methods in this jar.
* With the ```-o``` parameter, we define the filename of the putput file where the results are stored as a .csv file.
* With the ```-i``` parameter, the number of different seeds for random numbers (for those algorithms that need) is indicated; i.e., the number of executions for those algorithms that use random numbers. If this parameter is passed to a method that does not use random numbers, it is omitted. By default, its value is 10.
* The ```-o``` parameter, indicates if, for the macro-averaged measures, the values of the measure for each label is reported or not. It could take the values 0 (false) and 1 (true). By default, its value is 0.

The full list of methods availables in this jar is provided below. It is shown the value of the ```-a``` parameter and the full name of the method:
* Multi-label classification methods:
  * ```AdaBoostMH```: AdaBoost.MH
  * ```BPMLL```: Back-Propagation for MLL
  * ```BR```: 
  * ```CC```: 
  * ```CDE```: 
  * ```CLR```: 
  * ```EBR```: 
  * ```ECC```: 
  * ```EPS```: 
  * ```HOMER```: 
  * ```IBLR```: 
  * ```LP```: 
  * ```LPBR```: 
  * ```MLkNN```: 
  * ```MLS```: 
  * ```PS```: 
  * ```RAkEL```: 
  * ```RFPCT```: 
* Multi-target regression methods:
  * ```ERC```: 
  * ```RC```: 
  * ```RLC```: 
  * ```ST```: 
  * ```SST```: 



### References

<a name="Hal09"></a>**[Hal09]** M. Hall, E. Frank, G. Holmes, B. Pfahringer, P. Reutemann, and I. H. Witten. (2009). The WEKA data mining software: an update. ACM SIGKDD explorations newsletter, 11(1), 10-18.

<a name="Tso11"></a>**[Tso11]** G. Tsoumakas, E. Spyromitros-Xioufis, J. Vilcek, and I. Vlahavas. (2011). Mulan: A java library for multi-label learning. Journal of Machine Learning Research, 12, 2411-2414.

<a name="Ven08"></a>**[Ven08]** S. Ventura, C. Romero, A. Zafra, J. A. Delgado, and C. Hervás. (2008). JCLEC: a Java framework for evolutionary computation. Soft Computing, 12(4), 381-392.
	/**Ã  partir des 64 entrées, retourne un tableau correspondant aux sorties du réseau de neurones*/
	static double[] LoadAndUsePerceptron(int ... codeLettre)
	{
		double[] output = new double[26];
		//On créer un tableau permettant de caster codeLettre en double
		double[] input = new double[64];
		
		//On recopie codeLettre au format double
		for(int i = 0; i<codeLettre.length; i++) {
			input[i] = (double) codeLettre[i];
		}
		
		//On lance le réseau de neurone sur l'input et on stock le résultat dans output
		neuralNetwork.compute(input, output);
		return  output;
	}

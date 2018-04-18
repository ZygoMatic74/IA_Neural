static void buildAndSavePerceptron(File f, Hashtable<String, ArrayList<int[]>>  codeLettres)
	{
		//TODO: définir le neuralNetwork, ses propriétés (nb de neurones, ....)
		
		int neuralInput = 64;
		int neuralHidden = 45;
		int neuralOutput = 26;

		BasicLayer input = new BasicLayer(null,true,neuralInput);
		BasicLayer hidden = new BasicLayer(new ActivationSigmoid(),true,neuralHidden);
		BasicLayer output = new BasicLayer(new ActivationSigmoid(),false,neuralOutput);
		
		neuralNetwork = new BasicNetwork();
		neuralNetwork.addLayer(input);
		neuralNetwork.addLayer(hidden);
		neuralNetwork.addLayer(output);
		neuralNetwork.getStructure().finalizeStructure();
		neuralNetwork.reset();
		
		//TODO insérer les éléments de la tableau de hashage en tant que paramètres d'entrée et de sortie du réseau
		//		neuralNetwork=new ..
		
		//On créer les deux arrayList permettant de stocker les informations au format double
		ArrayList<double[]> training_IDEAL_AR = new ArrayList<double[]>();
		ArrayList<double[]> training_INPUT_AR = new ArrayList<double[]>();
		
		//Pour chaque entrée de la hashtable on définis la lettre réprésentée String
				// et les différente valeurs associées ArrayList<int[]>>
		for(Entry<String, ArrayList<int[]>> entry : codeLettres.entrySet()) {
			
			//On récupére la clé
			String keys = entry.getKey();
			//On récupère l'indice ou l'on doit placer le 1.0 dans le vecteur représentant la lettre
			int indiceKeys = keys.getBytes()[0] - 65;
			
			//On créer le vecteur vectorLettre
			double vectorLettre[] = new double[26];
			for(int i = 0; i<26;i++) {
				if(i==indiceKeys) {
					vectorLettre[i]=1.0;
				}
				else {
					vectorLettre[i]=0.0;
				}
			}
			
			//On récupére l'ensemble des tableaus de byte réprésentant la lettre
			ArrayList<int[]> value = entry.getValue();

			//Ensuite on parcours une à une les représentations
			for(int nbTest = 0; nbTest < value.size(); nbTest ++) {
				//On stock dans currentTest la représentation courante
				int[] currentTest = value.get(nbTest);

				
				//On ajoute le vecteur lettre correspondante à la liste IDEAL
				training_IDEAL_AR.add(vectorLettre);
				
				//Ensuite on cast chaque byte en double dans le tableau temp
				double temp [] = new double[64];
				for(int currentByte = 0; currentByte < currentTest.length; currentByte++) {
					temp[currentByte] = (double) currentTest[currentByte];	
				}
				
				//On ajoute la représentation de la lettre au format double[]
				training_INPUT_AR.add(temp);
			}
		}
		
		//On instancie ensuite les deux tableau de double afin de correspondre au spécification de la fonction BasicMLDataSet
		double training_INPUT[][] = new double[training_INPUT_AR.size()][64];
		double training_IDEAL[][] = new double[training_IDEAL_AR.size()][26];
		
		for(int indice = 0; indice < training_INPUT_AR.size(); indice++) {
			for(int j = 0; j<26;j++) {
				training_IDEAL[indice][j] = training_IDEAL_AR.get(indice)[j];
			}
			for(int j = 0; j<64; j++) {
				training_INPUT[indice][j] = training_INPUT_AR.get(indice)[j];
			}
			
		}
		
		
		//On créer le trainingSet avec les deux tableaux initialisé préalablement
		MLDataSet trainingSet = new BasicMLDataSet (training_INPUT, training_IDEAL) ;
		
		//On initialise la phase d'entraînement du réseau neuralNetwork avec l'ensemble de test trainingSet
		MLTrain train = new ResilientPropagation(neuralNetwork,trainingSet);
		
		//On itère l'entraînement jusqu'a passer en dessous d'une certaine marge d'erreur ici 0.1%
		int epoch=1;
		do{
			train.iteration();
			System.out.println("Test#"+epoch+", Error "+train.getError());
			epoch++;
		}while(train.getError()>0.001);
		
		System.out.println("save the training set");
		// save the trained network into file 
		PersistBasicNetwork pbn = new PersistBasicNetwork();
		try {
			pbn.save(new ObjectOutputStream(new  FileOutputStream(f)), neuralNetwork);
		} 
		catch ( IOException e) { e.printStackTrace(); }

	}

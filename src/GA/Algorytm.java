package GA;

public class Algorytm {

    public static double Ai = -4.5;
    public static double Bi = 4.5;
    static double precyzja = 1000;
    public int liczbaGenow = 2;
    public double mutacjaPrawdopodobienstwo = 0.04;
    public double krzyzowaniePrawdopodobienstwo = 0.6;
    
    public int wielkoscPopulacji;
    public double precision;

    public int generacja;

    public double najlepszeRozwiazanie;
    public int wskaznikEwaulacji = 0;
    
    Czlonek[] populacja;
    
    Czlonek najlepszy = null;
    
    int nrWyspy;

    int ewaulacje;
    
    int MI = 1;
    
    public Algorytm(int _n, int _PopulacjaLength, double _MutacjaPropability, double _krzyzowaniePropability) {
        mutacjaPrawdopodobienstwo = _MutacjaPropability;
        krzyzowaniePrawdopodobienstwo = _krzyzowaniePropability;
        wielkoscPopulacji = _PopulacjaLength;
        liczbaGenow = _n;
        //krok 1 tworzymy populacje bazowa
        populacja = stworzPopulacje();
        MI = Czlonek.Mi(Ai, Bi, precyzja);
    }

    
    private Czlonek[] stworzPopulacje(){
    	
    	Czlonek [] _populacja = new Czlonek[wielkoscPopulacji];
    	for(int i = 0; i < wielkoscPopulacji; i++) {
    		_populacja[i] = new Czlonek(Ai,Bi,liczbaGenow,precyzja);
    		
    	}
    	return _populacja;
    }
    
    
    private Czlonek znajdzNajlepszegoObecnie(Czlonek [] populacja) {
   
    	Czlonek najlepszy = populacja[0];
    	for(int i = 1; i < wielkoscPopulacji; i++) {
    		if(najlepszy.compareTo(populacja[i]) == 1) {
    			najlepszy = populacja[i];
    		}
    	}
    	return najlepszy;
    }
    
    private void zmienPopulacje(Czlonek[] nowa) {
    	for(int i = 1; i < wielkoscPopulacji; i++) {
    		populacja[i] = nowa[i];
    	}
    }
    
    public Czlonek[] run(double env) {
        wskaznikEwaulacji = 1;
        String pomiar = "";
        
        while (wskaznikEwaulacji <= env) {
        	double wartosciFunkcji [] = new double[wielkoscPopulacji];
        	//System.out.println("=====Ewaulacja=========");
        	for(int i = 0; i < wielkoscPopulacji; i++) {
        		populacja[i].wartoscDlaFunkcji = populacja[i].obliczWartoscFunkcji();
        		wartosciFunkcji[i] = populacja[i].wartoscDlaFunkcji;
        		wskaznikEwaulacji++;
        	}
        	pomiar = (wskaznikEwaulacji)+";"+sredniaWartoscPopulacji(wartosciFunkcji);
        	pomiar = pomiar.replace('.',',');
    		zapiszRozwiazania(pomiar,wielkoscPopulacji);
    	
        	
        	//selekcja najlepszych rozwiazan do nowej populacji
        	Turniej turniej = new Turniej(populacja);
        	najlepszy = znajdzNajlepszegoObecnie(populacja);
        	najlepszeRozwiazanie = najlepszy.wartoscDlaFunkcji;
        	
        	populacja = turniej.calyTurniej();
        	populacja[0] = najlepszy;
        	
        	//dokonanie mutacji:
        	populacja = mutujPopulacje(populacja);
//        	
        	//System.out.println("=====MUTACJA=========");
        	for(int i = 0; i < wielkoscPopulacji; i++) {
        		populacja[i].wartoscDlaFunkcji = populacja[i].obliczWartoscFunkcji();
        		//System.out.println(populacja[i].wartoscDlaFunkcji);
        	}
        	
        	//dokonanie krzyzowania:
        	
        	if(liczbaGenow <= 20) {
        		populacja = krzyzuj2Punktowo(populacja);
        	}
        	else {
        		populacja = krzyzuj8Punktowo(populacja);
        		
        	}
        	//System.out.println("=====KRZYZOWANIE=========");
        	for(int i = 0; i < wielkoscPopulacji; i++) {
        		populacja[i].wartoscDlaFunkcji = populacja[i].obliczWartoscFunkcji();
        		//System.out.println(populacja[i].wartoscDlaFunkcji);
        	}
        	
        	pomiar = (wskaznikEwaulacji)+";"+najlepszeRozwiazanie;
          	pomiar = pomiar.replace('.',',');
          	zapiszNajlepszeLokalneRozwiazania(pomiar, wielkoscPopulacji);
          	
          	if(najlepszeRozwiazanie <= Main.globalneNajlepsze) {
          		Main.globalneNajlepsze = najlepszeRozwiazanie;
          	}
          	
          	pomiar = wskaznikEwaulacji+";" + Main.globalneNajlepsze;
          	pomiar = pomiar.replace(".",",");
        	zapiszGlobalnieNajlepszego(pomiar);
          	
        }
      
        return populacja;
    }
    
    private Czlonek[] krzyzuj2Punktowo(Czlonek[] populacja) {
    	Krzyzowanie2Punkt krzyzowanie2Punkt = new Krzyzowanie2Punkt(liczbaGenow*MI);
    	for(int i = 0; i < populacja.length;i++) {
    		krzyzowanie2Punkt.wylosujPunktyKrzyzowania();
    		if(Math.random() < krzyzowaniePrawdopodobienstwo) {
    			int partner = losujPartnera(wielkoscPopulacji,0);
    			char [] genyRodzicaX = populacja[i].dajLiniowo();
    			char [] genyRodzicaY = populacja[partner].dajLiniowo();
    			populacja[i].chromosome = Czlonek.zamienNaGeny(krzyzowanie2Punkt.dajDziecko(genyRodzicaX, genyRodzicaY),liczbaGenow,MI);
    			populacja[partner].chromosome = Czlonek.zamienNaGeny(krzyzowanie2Punkt.dajDziecko(genyRodzicaY, genyRodzicaX),liczbaGenow,MI);
    		}
    		
    	}
    	return populacja;
    }
    private Czlonek[] krzyzuj8Punktowo(Czlonek[] populacja) {
    	Czlonek[] skrzyzowane = Czlonek.kopiaPopulacji(populacja, populacja.length);
    	Krzyzowanie8Punkt krzyzowanie8Punkt = new Krzyzowanie8Punkt(liczbaGenow*MI);
    	for(int i = 0; i < populacja.length;i++) {
    		
    		if(Math.random() < krzyzowaniePrawdopodobienstwo) {
    			int partner = losujPartnera(wielkoscPopulacji,0);
    			char [] genyRodzicaX = skrzyzowane[i].dajLiniowo();
    			char [] genyRodzicaY = skrzyzowane[partner].dajLiniowo();
    			skrzyzowane[i].chromosome = Czlonek.zamienNaGeny(krzyzowanie8Punkt.dajDziecko(genyRodzicaX, genyRodzicaY),liczbaGenow,MI);
    		}
    	}
    	return skrzyzowane;
    }
    
    private Czlonek[] mutujPopulacje(Czlonek[] populacja) {
    	
    	Czlonek[] zmutowana = Czlonek.kopiaPopulacji(populacja, populacja.length);
    	
    	char [] zmutowanyChromosom = new char[liczbaGenow*13];
    	Mutacja mutacja = new Mutacja(mutacjaPrawdopodobienstwo);
    	for(int i = 0; i < wielkoscPopulacji; i++) {
    		
    		//wybor czy dany gen ma zostac zmutowany
    		if(Math.random() < mutacjaPrawdopodobienstwo) {
    			zmutowanyChromosom =  mutacja.czlonekMutacja(zmutowana[i].dajLiniowo());
    			zmutowana[i].chromosome = Czlonek.zamienNaGeny(zmutowanyChromosom, liczbaGenow, MI);
    		}
    	}
    	return zmutowana;
    }
    
    private static double sredniaWartoscPopulacji(double [] populacjaWartosci) {
    	double srednia = 0.0;
    	int wielkoscPopulacji = populacjaWartosci.length;
    	for(int i = 0; i < wielkoscPopulacji;i++) {
    		srednia+=populacjaWartosci[i];
    	}
    	return srednia / wielkoscPopulacji;
    }
    
    private static void zapiszGlobalnieNajlepszego( String pomiar) {
        Zapisywacz zapisywaczWszystkich = new Zapisywacz("globalnieNajlepsze.txt");
        zapisywaczWszystkich.WriteToFile(pomiar);
    }
    
    private static void zapiszNajlepszeLokalneRozwiazania(String pomiar, int populacja) {

        Zapisywacz zapisywaczNajlepszychLokalnie = new Zapisywacz("najlepszeLokalne" + populacja + "." + ".txt");
        zapisywaczNajlepszychLokalnie.WriteToFile(pomiar);
    }

    private static void zapiszRozwiazania(String pomiar, int populacja) {

        Zapisywacz zapisywaczNajlepszychLokalnie = new Zapisywacz("wszystkieRozwiazania" + populacja + "." + ".txt");
        zapisywaczNajlepszychLokalnie.WriteToFile(pomiar);
    }
    private static int losujPartnera(int max, int min) {
		return (int)((Math.random() * (max - min)) + min);
	}

}
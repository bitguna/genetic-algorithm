package GA;

public class Main {
	
	static int maxEwaulacji = 10000;
	static int populacja = 10;
	
	//dla funkcji kwadratowej
	static double globalneNajlepsze = 450000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Algorytm algorytm = new Algorytm(2,1000,0.04,0.6);
		algorytm.run(maxEwaulacji);
		System.out.println("Rozwiazanie "+globalneNajlepsze);
	}

}

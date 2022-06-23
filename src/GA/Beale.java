package GA;

public class Beale {
	public double func(double x1,double x2) {
		double czesc1 = Math.pow((1.5 - x1 + ( x1*x2)), 2);
		double czesc2 = Math.pow((2.25-x1+(x1* Math.pow(x2, 2))),2);
		double czesc3 = Math.pow((2.625 - x1 + (x1*Math.pow(x2, 3)) ), 2);
		return czesc1+czesc2+czesc3;
	}
	
	public double apply(double[]xs) {
		double sum = 0.0;
		for (int i = 1; i < xs.length; i++) {
			sum+=func(xs[i-1],xs[i]);
		}
		return sum;
	}
}

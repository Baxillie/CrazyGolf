package nl.dke12.util;

public class Interpolation {

	public static int numOfSteps = 10; //How many steps to take

	public static float[] setPoints(float p1, float p2, float p3, float p4){

		float[][] ip = new float[4][numOfSteps+1]; 
		float[] finalpoints = new float [numOfSteps+1];

		ip[0]=cosInt(p1,p2);
		ip[1]=cosInt(p1,p3);
		ip[2]=cosInt(p1,p4);

		for(int q = 0; q < ip[0].length; q++){
			finalpoints[q] = (ip[0][q] + ip[1][q] + ip[2][q])/3;
		}
		
		return finalpoints;
	}

	public static float[] cosInt(float points, float points2){

		double[] cosStep = new double[numOfSteps+1];
		float[] interpolated = new float[numOfSteps+1];

		interpolated[numOfSteps] = points2;

		int p = 0;
		for(double m = 0; m < 1.0; m = m + (1.0/numOfSteps)){
			cosStep[p] = (1 - Math.cos(m*Math.PI))/2;
			p++;
		}

		for(int i = 0; i < numOfSteps; i++){
			interpolated[i]=(float)((points*(1-cosStep[i]))+(points2*cosStep[i]));
		}

		return interpolated;
	}
}

package nl.dke12.util;

import java.util.Scanner;

public class testInterpolation {
	
	public static void main(String[] args) {
		
		int numOfSteps = Interpolation.numOfSteps;
		float[] finalpoints = new float[numOfSteps+1];

		Scanner scan = new Scanner(System.in);
		float p1; float p2; float p3; float p4;
		
		System.out.println("Point 1: ");
		p1 = scan.nextFloat();
		System.out.println("Point 2: ");
		p2 = scan.nextFloat();
		System.out.println("Point 3: ");
		p3 = scan.nextFloat();
		System.out.println("Point 4: ");
		p4 = scan.nextFloat();
		
		scan.close();
		
		finalpoints = Interpolation.setPoints(p1, p2, p3, p4);
		
		for(int i = 0; i < numOfSteps+1; i++){
			System.out.println(finalpoints[i]);
		}
		
	}
}

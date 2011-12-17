package sandbox;

import java.util.Arrays;

public class TestaVarargs {

	private static void teste(int... numeros) {
		System.out.println(Arrays.toString(numeros));
		for (int i = 0; i < numeros.length; i++) {
			numeros[i] = 2 * numeros[i];
		}
		System.out.println(Arrays.toString(numeros));
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		teste(1, 3, 5, 6, 7, 8, 9);
	}

}

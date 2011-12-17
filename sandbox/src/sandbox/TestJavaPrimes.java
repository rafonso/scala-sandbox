package sandbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestJavaPrimes {

	private static Long getTime() {
		return System.nanoTime();
	}
	
	static long printResult(String name, int n, int quantity, long t0) {
		long deltaT = getTime() - t0;
		System.out.printf("%10s %,12d %,15d %,20d %n", name, n, quantity, deltaT);
		return deltaT;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] ns = {1, 3, 10, 30, 100, 300, 1000, 3000, 10000, 30000, 100000, 300000, 1000000, 3000000, 10000000, 30000000, 100000000, 300000000};
		List<Long> booleanTimes = new ArrayList<Long>(ns.length);
		List<Long> sieveTimes = new ArrayList<Long>(ns.length);
		
		PrimesBoolean.getPrimesUntil(1);
		SieveJava.sieve_of_eratosthenes(1);
		//Collections.emptyList();
		//Collections.singletonList(2);
		
		System.out.printf("%10s %10s %10s %15s %n", "Name", "n", "Quantity", "time (ms)");
		long t0;
		for (int n : ns) {
			t0 = getTime();
			List<Integer> primesB = PrimesBoolean.getPrimesUntil(n);
			booleanTimes.add(printResult("Boolean", n, primesB.size(), t0));
			

			t0 = getTime();
			List<Integer> primesS = SieveJava.sieve_of_eratosthenes(n);
			sieveTimes.add(printResult("Sieve", n, primesS.size(), t0));
		}
		
		System.out.println('\n');
		System.out.printf("%9s %12s %12s %n", "n", "Boolean", "Sieve");
		for(int i = 0; i < ns.length; i ++) {
			System.out.printf("%9d %12d %12d %n", ns[i], booleanTimes.get(i), sieveTimes.get(i));
		}
		
	}

}

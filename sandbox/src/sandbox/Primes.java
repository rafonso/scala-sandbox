package sandbox;

import java.util.ArrayList;
import java.util.List;

public class Primes {
	
	private static boolean isPrime(int i, Iterable<Integer> priorPrimes) {
		for (Integer prime : priorPrimes) {
			if(Math.sqrt(prime) > i) {
				return true;
			}
			if(i % prime == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int max = Integer.parseInt(args[0]);
		List<Integer> primes = new ArrayList<Integer>();
		primes.add(2);
		long sum = 2;
		
		// Skip odd Numbers
		long t0 = System.currentTimeMillis();
		for(int i = 3; i <= max; i += 2) {
			if(isPrime(i, primes)) {
				primes.add(i);
				sum += i;
//				System.out.println(i);
			}
		}
		long deltaT = System.currentTimeMillis() - t0;
		
//		System.out.println("=============================================================");
		System.out.println("SUM = " + sum);
		System.out.println("Total Time: " + deltaT + " ms");
	}

}

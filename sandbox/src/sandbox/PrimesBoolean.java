package sandbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrimesBoolean {
	
	private static int numberToPosition(int i) {
		return i / 2 - 1;
	}
	
	private static void eliminateMultiples(boolean[] divisible, int prime, int n) {
		for(int i = 2 * prime; i <= n; i += prime) {
			if(i % 2 != 0) {
				divisible[numberToPosition(i)] = true;
			}
		}
	}
	
	public static List<Integer> getPrimesUntil(int n) {
		if(n < 2) {
			return Collections.emptyList();
		}
		if(n == 2) {
			return Collections.singletonList(2);
		}
		
		boolean[] divisible = new boolean[numberToPosition(n) + 1];
		List<Integer> primes = new ArrayList<Integer>();
		primes.add(2);
		
		for(int i = 3; i <= n; i += 2) {
			if(!divisible[numberToPosition(i)]) {
				primes.add(i);
				eliminateMultiples(divisible, i, n);
			}
		}
		
		return primes;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		System.out.println(PrimesBoolean.getPrimesUntil(n).size());
	}

}

package sandbox;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class SieveJava {

	private BitSet sieve;

	public SieveJava(int size) {
		sieve = new BitSet((size + 1) / 2);
	}

	public boolean is_composite(int k) {
		assert k >= 3 && (k % 2) == 1;
		return sieve.get((k - 3) / 2);
	}

	public void set_composite(int k) {
		assert k >= 3 && (k % 2) == 1;
		sieve.set((k - 3) / 2);
	}

	public static List<Integer> sieve_of_eratosthenes(int max) {
		SieveJava sieve = new SieveJava(max + 1); // +1 to include max itself
		for (int i = 3; i * i <= max; i += 2) {
			if (sieve.is_composite(i))
				continue;

			// We increment by 2*i to skip even multiples of i
			for (int multiple_i = i * i; multiple_i <= max; multiple_i += 2 * i)
				sieve.set_composite(multiple_i);
		}

		List<Integer> primes = new ArrayList<Integer>();
		primes.add(2);
		for (int i = 3; i <= max; i += 2)
			if (!sieve.is_composite(i))
				primes.add(i);
		return primes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int max = 1;
		int num_times = 1;

		if (args.length > 0)
			max = Integer.parseInt(args[0]);

		if (args.length > 1)
			num_times = Integer.parseInt(args[1]);

		List<Integer> result = null;
		long start_time = System.currentTimeMillis();
		for (int i = 0; i < num_times; i++)
			result = sieve_of_eratosthenes(max);

		double time_in_ms = (double) (System.currentTimeMillis() - start_time)
				/ num_times;
		double time_per_integer_ns = time_in_ms / max * 1000000;

		System.out.printf("Sieved over integers 1 to %,d in %.0f ms (%.3f ns per integer). There is %,9d primes", max, time_in_ms, time_per_integer_ns, result.size());
		// System.out.println("Sieved over integers 1 to " + max + " in "
		// + time_in_ms + " ms (" + time_per_integer_ns
		// + " ns per integer). There is " + result.size() + " primes");

//		for (Integer i : result)
//			System.out.println(i);
	}

}

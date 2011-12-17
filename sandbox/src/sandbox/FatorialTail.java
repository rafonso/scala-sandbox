package sandbox;

public class FatorialTail {

	private static long fatTail(long n, long fat) {
		if(n == 1) {
			return fat;
		}
		
		return fatTail(n - 1,  n * fat);
	}
	
	private static long fatNotTail(long n) {
		if(n == 1) {
			return 1;
		}
		
		return  n * fatNotTail(n - 1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(fatTail(100, 1));

	}

}

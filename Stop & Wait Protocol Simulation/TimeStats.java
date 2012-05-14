
public class TimeStats {
	
	private long elapsedTime;		//total elapsed time

	public TimeStats() {
		elapsedTime = 0;
	}
	
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void add(long time) {
		elapsedTime += time;
	}
	
	//simulated throughput
	public double throughputSim(int framesToSendNo, long infoTransTime) {
		return ((double)(framesToSendNo * infoTransTime)) / elapsedTime;
	}
	
	//theoretical throughput
	public double throughputTheor(long infoTransTime, long ackTransTime, long timeOut, double frameErrProb, long propDelay, long procTime) {
		double a = (timeOut + infoTransTime) * frameErrProb / (1.0 - frameErrProb);
		double b = infoTransTime + 2 * propDelay + procTime + ackTransTime;
		return (double)infoTransTime / (a + b);
	}
	
	//compares simulated and theoretical throughput in %
	public void throughputEval(double throughputSim, double throughputTheor) {
		System.out.format("%n\tSimulated throughput:\t %05.2f%%%n", throughputSim * 100);
		System.out.format("\tTheoretical throughput:\t %05.2f%%%n", throughputTheor * 100);
	}
	
	//calculate optimal info frame length, given bit error probability
	public int optInfoLength(Input in) {
		return 8192;
	}
}

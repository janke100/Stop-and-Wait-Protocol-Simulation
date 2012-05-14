
public class Channel {
	
	private double 	bitErrProb;				//probability of bit error (0.0 - <1.0)
	private long 	dataRate;				//channel data rate (bits per second)
	private long 	propDelay;				//propagation delay (microseconds)
	
	public Channel(double prob, long rate, long delay) {
		bitErrProb = prob;
		dataRate = rate;
		propDelay = delay;
	}
	
	public double getBitErrProb() {
		return bitErrProb;
	}
	
	public long getDataRate() {
		return dataRate;
	}
	
	public long getPropDelay() {
		return propDelay;
	}
	
	public double getFrameErrorProbability(int frameLength) {
		return 1.0 - Math.pow( (1.0 - bitErrProb), frameLength );
	}
	
	private void corrupt(Frame f) {
		boolean errorOccured = Math.random() <= getFrameErrorProbability(f.getLength()) ? true : false;
		f.setError(errorOccured);
	}
	/////////////////////////////////////check whether channel corrupts ACK frames!!!!!!!!!!!!!
	public Frame transfer(Frame f) {
//		if(f.getType() == Frame.ACK)
//			return f;
		corrupt(f);
		return f;
	}
}

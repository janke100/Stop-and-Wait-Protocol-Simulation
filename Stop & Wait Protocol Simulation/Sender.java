
public class Sender {
	
	private static final byte CONST = 0;	//constant lenthDistrib
//	private static final byte EXP = 1;		//exponential lengthDistrib
	
	private byte 		currentFrameNo;		//current info frame number, mod2 (0 or 1)
	private boolean		timedOut;			//if sender timed-out
	private long 		timeOutPeriod;		//time out period (microseconds)
	private int 		infoFrameLength;	//d+i, info frame length (bits)
	private int 		ackFrameLength;		//d, ack frame length (bits)
	private byte 		lengthDistrib;		//information data length distribution (CONST or EXP)
	private Frame 		frameCopy;			//current frame, a copy saved for retransmission
	private FrameStats 	stats;				//sender's frame statistics
	
	public Sender(int totalFrameNo, long timeOut, int aLength, int iLength, byte lDistrib) {
		currentFrameNo = 1;	//to make sure first frame sent will have number 0
		timedOut = false;
		timeOutPeriod = timeOut;
		infoFrameLength = iLength;
		ackFrameLength = aLength;
		lengthDistrib = lDistrib;
		frameCopy = null;
		stats = new FrameStats(totalFrameNo);
	}
	
	public boolean isTimedOut() {
		return timedOut;
	}
	
	public void timeOut() {
		timedOut = true;
	}
	
	public long getTimeOutPeriod() {
		return timeOutPeriod;
	}
	
	public int getAckFrameLength() {
		return ackFrameLength;
	}
	
	public int getInfoFrameLength() {
		return infoFrameLength;
	}
	
	public byte getLengthDistrib() {
		return lengthDistrib;
	}
	
	public FrameStats getStats() {
		return stats;
	}
	
	public long frameTransTime(int length, long dataRate) {
		return (long)length*1000000 / dataRate;
		//*1000000 because dataRate is in bits per second and result should be in microseconds
		//mustn't divide dataRate by 1000000 because if result is <1 it will be truncated to 0 when converted to long
	}
	
	public Frame takeNewFrame() {		//creates new frame for transmission
		int frameLength;
		
		//constant data length distribution
		if(lengthDistrib == CONST)
			frameLength = infoFrameLength;
		
		//exponential data length distribution
		else {
			double random;
			do {
				random = Math.random();
			} while(random == 0);		//mustn't be 0
			int medianDataLength = infoFrameLength - ackFrameLength; 
			int actualDataLength = (int)(- medianDataLength * Math.log(random));
			frameLength = ackFrameLength + actualDataLength;
		}
		incFrameNo();	//next frame number (mod2)
		stats.nextFrame();	//resets attemptCounter
		frameCopy = new Frame(currentFrameNo, Frame.INFO, frameLength);
		return new Frame(frameCopy);
	}
	
	//mod2 increment
	private void incFrameNo() {
		if(currentFrameNo == 0)
			currentFrameNo = 1;
		else
			currentFrameNo = 0;
	}
	
	//sends (returns) frame with next number
	public Frame send(Frame f) {
		stats.frameSent();
		timedOut = false;
		return f;
	}
	
	public Frame resend() {
		stats.frameSent();
		timedOut = false;
		return frameCopy;
	}
	
	/*
	 * returns whether ack is ok or not (has error or wrong number): true = ack is ok
	 * if ack is ok send next
	 * if ack is bad do immeadiate resend (don't wait for timeout)
	 */////////////////////////////////check whether to immediately resend or wait for timeout!!!!!!!!!!!!!!!!
	public boolean receive(Frame f) {
		stats.frameReceived(f.hasError());
		if(f.hasError() || (f.getNumber() != frameCopy.getNumber()))
			return false;
		else
			return true;
	}
}

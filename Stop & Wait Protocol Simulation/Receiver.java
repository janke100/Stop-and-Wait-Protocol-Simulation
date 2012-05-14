
public class Receiver {
	
	private byte expectedFrameNo;		//expected info frame number, mod2 (0 or 1)
	private byte lastReceivedNo;		//last received frame number, mod2 (0 or 1)
	private long procTime;				//info frame processing time (microseconds)
	private int ackFrameLength;			//d, acknowledgement frame length (bits)
	private FrameStats stats;			//receiver's frame statistics
	
	public Receiver(int totalFrameNo, long pTime, int aLength) {
		expectedFrameNo = 0;
		lastReceivedNo = 1;
		procTime = pTime;
		ackFrameLength = aLength;
		stats = new FrameStats(totalFrameNo);
	}

	public long getProcTime() {
		return procTime;
	}
	
	public int getAckFrameLength() {
		return ackFrameLength;
	}

	public FrameStats getStats() {
		return stats;
	}

	public long frameTransTime(int length, long dataRate) {
		return (long)length*1000000 / dataRate;
		//*1000000 because dataRate is in bits per second and result should be in microseconds
		//mustn't divide dataRate by 1000000 because if result is <1 it will be truncated to 0 when converted to long
	}
	
	public Frame sendAck() {
		stats.frameSent();
		Frame ack = new Frame(lastReceivedNo, Frame.ACK, ackFrameLength);
		return ack;
	}
	
	//mod2 increment
	private void incExpectedNo() {
		if(expectedFrameNo == 0)
			expectedFrameNo = 1;
		else
			expectedFrameNo = 0;
	}
	
	/*
	 * returns whether received frame is corrupted or not: true = corrupted
	 * i.e. whether to send ack or not: true = don't send ack
	 */
	public boolean receive(Frame f) {
		stats.frameReceived(f.hasError());
		if(f.hasError())
			return true;
		else {
			lastReceivedNo = f.getNumber();
			if(f.getNumber() == expectedFrameNo)
				incExpectedNo();
			return false;
		}
	}
}


public class FrameStats {
	public static final int distribLen = 201;
	
	private int 	totalFrameNo;			//total number of frames to transfer
	private long 	totalReceived;			//total number of frames received
	private long	corruptedReceived;		//number of frames with error received
	private long 	totalSent;				//total number of frames sent
	private long 	attemptCounter;			//counting current frame number of transmissions
	private long 	attemptDistrib[];		//number of total transmissions for each frame
	private int 	attemptDistribIndex;	//current index for attemptDistrib
	private long	attemptDistrib2[];		//number of frames sent 1 time, 2 times, 3 times... (index determines attempt count)
	
	public FrameStats(int totalNo) {
		totalFrameNo = totalNo;
		totalReceived = 0;
		corruptedReceived = 0;
		totalSent = 0;
		attemptCounter = 0;
		attemptDistrib = new long[totalFrameNo];
		attemptDistribIndex = -1;
		attemptDistrib2 = new long[distribLen];
		for(int i=0; i<distribLen; i++)
			attemptDistrib2[i] = 0;
	}

	public long[] getAttemptDistrib() {
		return attemptDistrib;
	}

	public long[] getAttemptDistrib2() {
		return attemptDistrib2;
	}
	
	public long getCorruptedReceived() {
		return corruptedReceived;
	}

	public int getTotalFrameNo() {
		return totalFrameNo;
	}

	public long getTotalReceived() {
		return totalReceived;
	}

	public long getTotalSent() {
		return totalSent;
	}
	
	public void nextFrame() {
		if(attemptDistribIndex != -1) {
			attemptDistrib[attemptDistribIndex] = attemptCounter;
			if(attemptCounter < distribLen)
				attemptDistrib2[(int)attemptCounter]++;
		}
		attemptDistribIndex++;
		attemptCounter = 0;
	}
	
	public void frameSent() {
		attemptCounter++;
		totalSent++;
	}
	
	//receiver will only use this method, since receiver only counts corrupted info frames
	public void frameReceived(boolean hasError) {
		totalReceived++;
		if(hasError)
			corruptedReceived++;
	}
}

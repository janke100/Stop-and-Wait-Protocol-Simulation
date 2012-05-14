import java.io.*;

public class Input {
	
	private long 	dataRate;			//channel data rate (bits per second)
	private long 	propDelay;			//propagation delay (microseconds)
	private long 	procTime;			//info frame processing time (microseconds)
	private int 	ackFrameLength;		//d, acknowledgement frame length (bits)
	private int 	infoFrameLength;	//d+i, info frame length (bits)
	private byte 	lengthDistrib;		//information data length distribution (0=CONST or 1=EXP)
	private double 	bitErrProb;			//probability of bit error (0.0 - <1.0)
	private long 	timeOutPeriod;		//time out period (microseconds)
	private int 	totalFrameNo;		//total number of frames to transfer
	
	public long getDataRate() {
		return dataRate;
	}
	
	public long getPropDelay() {
		return propDelay;
	}
	
	public long getProcTime() {
		return procTime;
	}
	
	public long getTimeOutPeriod() {
		return timeOutPeriod;
	}
	
	public double getBitErrProb() {
		return bitErrProb;
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
	
	public int getTotalFrameNo() {
		return totalFrameNo;
	}
	
	public boolean calculateOptLength() throws IOException {
		while(true) {
			System.out.println("\nDo you want to calculate optimal info frame length? ('y' or 'n'): ");
			String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
				if(s.equalsIgnoreCase("y")) {
					return true;
				}
				else if(s.equalsIgnoreCase("n")) {
					return false;
				}
				else
					System.out.println("Invalid input!");
		}
	}
	
	public void read() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("\nDo you want to use default values? ('y' or 'n'): ");
		if( new String("y").equalsIgnoreCase(br.readLine()) ) {
			dataRate = 50000000;
			propDelay = 1000;
			procTime = 20;
			ackFrameLength = 128;
			infoFrameLength = 4096;
			lengthDistrib = 0;
			bitErrProb = 0.0001;
			timeOutPeriod = 5000;
			totalFrameNo = 1000000;
			return;
		}
		
		boolean inputOk = false;
		while(!inputOk) {
			System.out.print("Enter data rate in bits per second: ");
			try{
				long i = Long.parseLong(br.readLine());
				if(i <= 0)
					throw new NumberFormatException();
				else {
					dataRate = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		while(!inputOk) {
			System.out.print("Enter propagation delay in microseconds: ");
			try{
				long i = Long.parseLong(br.readLine());
				if(i < 0)
					throw new NumberFormatException();
				else {
					propDelay = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		while(!inputOk) {
			System.out.print("Enter info frame processing time in microseconds: ");
			try{
				long i = Long.parseLong(br.readLine());
				if(i < 0)
					throw new NumberFormatException();
				else {
					procTime = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}

		inputOk = false;
		while(!inputOk) {
			System.out.print("Enter acknowledgement frame length in bits: ");
			try{
				int i = Integer.parseInt(br.readLine());
				if(i <= 0)
					throw new NumberFormatException();
				else {
					ackFrameLength = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		while(!inputOk) {
			System.out.print("Enter info frame length in bits: ");
			try{
				int i = Integer.parseInt(br.readLine());
				if(i <= 0)
					throw new NumberFormatException();
				else {
					infoFrameLength = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		while(!inputOk) {
			double minErr = 1.0 / infoFrameLength;
			System.out.format("Enter probability of bit error (>= 0.0 and <1.0 and should be less than %f ): ", minErr);
			try{
				double i = Double.parseDouble(br.readLine());
				if(i < 0.0 || i >= 1.0)
					throw new NumberFormatException();
				else {
					bitErrProb = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		long minTimeOut = 2*propDelay + procTime + ackFrameLength*1000000 / dataRate + 1;
		//+1 to account for truncating
		//*1000000 because dataRate is in bits per second and times are in microseconds
		//mustn't divide dataRate by 1000000 because if result is <1 it will be truncated to 0 when converted to long
		while(!inputOk) {
			System.out.print("Enter time-out period in microseconds (must be greater than " + minTimeOut + "): ");
			try{
				long i = Long.parseLong(br.readLine());
				if(i <= minTimeOut)
					throw new NumberFormatException();
				else {
					timeOutPeriod = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		while(!inputOk) {
			System.out.print("Enter data length distribution type ('0' for constant, '1' for exponential): ");
			try{
				byte i = Byte.parseByte(br.readLine());
				if(i < 0 || i > 1)
					throw new NumberFormatException();
				else {
					lengthDistrib = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
		inputOk = false;
		while(!inputOk) {
			System.out.print("Enter total number of frames for transmission: ");
			try{
				int i = Integer.parseInt(br.readLine());
				if(i <= 0)
					throw new NumberFormatException();
				else {
					totalFrameNo = i;
					inputOk = true;
				}
			}catch(NumberFormatException e){
				System.out.println("Invalid input!");
			}
		}
		
/*		inputOk = false;
		while(!inputOk) {
			System.out.print("Enable tracing ('y' or 'n')? ");
			String s = br.readLine();
			if(s.equalsIgnoreCase("y")) {
				Tracing.enable();
				inputOk = true;
			}
			else if(s.equalsIgnoreCase("n")) {
				Tracing.disable();
				inputOk = true;
			}
			else
				System.out.println("Invalid input!");
		} */
	}
}
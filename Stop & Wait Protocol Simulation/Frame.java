
public class Frame {
	
	public static final byte INFO = 0;	//info frame type
	public static final byte ACK = 1;	//ack frame type
	
	private byte	number;			//frame number by mod2 (0 or 1)
	private byte	type;			//frame type (INFO or ACK)
	private int 	length;			//total frame length (bits)
	private boolean hasError;		//is frame corrupted
	
	public Frame(byte no, byte ty, int len) {
		number = no;
		type = ty;
		length = len;
		hasError = false;
	}
	
	public Frame(Frame f) {
		this(f.number, f.type, f.length);
	}
	
	public byte getNumber() {
		return number;
	}
	
	public byte getType() {
		return type;
	}
	
	public int getLength() {
		return length;
	}
	
	public boolean hasError() {
		return hasError;
	}
	
	public void setError(boolean err) {
		hasError = err;
	}
}

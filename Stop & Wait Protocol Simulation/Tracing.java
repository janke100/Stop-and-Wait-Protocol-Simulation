
public class Tracing {
	
	private static boolean isOn = true;	//if Tracing.isOn=true application will print out activities 
	
	public static boolean isOn() {
		return isOn;
	}
	
	public static void enable() {
		isOn = true;
	}
	
	public static void disable() {
		isOn = false;
	}
	
	public static void print(String string) {
		System.out.println(string);
	}
}

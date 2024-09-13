
public class SmartHomeControlHub {
	
	//Simulate some kind of separate/external smart home controller 
	
	boolean light_status = false;
	int fan_status = 0;
	int ac_status = 0;

	public boolean getLightStatus() {
		return light_status;
	}

	public int getFanStatus() {
		return fan_status;
	}
	
	public int getACStatus() {
		return ac_status;
	}
	
	public void setLightStatus(boolean s) {
		light_status = s;
	}
	
	public void setFanStatus(int i) {
		fan_status = i; 
	}
	
	public void setACStatus(int i) {
		ac_status = i; 
	}
}
package be.nvcursus.JUnit;

public class Thermostat {
	//10 is the correct value, not 100, because testclasses use 10 as INTERVAL*3 
	//for Thread.sleep to be sure the thermostat has executed the evaluate method
	private final static int DEFAULT_INTERVAL = 10;
	private Heating heating;
	private Sensor sensor;
	private Thread thread;
	private Temperature targetTemperature;
	private int interval = DEFAULT_INTERVAL;
	private boolean status;
	
	public Thermostat(Heating heating, Sensor sensor) {
		this.heating = heating;
		this.sensor = sensor;
		try {
			this.targetTemperature = new Temperature(0F);
		} catch (InvalidTemperatureException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		thread = new Thread(this::run);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void stop() {
		thread = null;
	}
	
	public void setTargetTemperature(Temperature t) {
		targetTemperature = t;
		evaluate();
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public boolean isHeating() {
		return status;
	}
	
	private void run() {
		while (thread == Thread.currentThread()) {
			evaluate();
			try {
				Thread.sleep(DEFAULT_INTERVAL);
			} catch (InterruptedException ex) {
				//do nothing
			}
		}
	}
	
	private void evaluate() {
		Temperature currentTemperature = sensor.getTemperature();
		status = currentTemperature.getValue() < targetTemperature.getValue();
		heating.setHeating(status);
	}
}

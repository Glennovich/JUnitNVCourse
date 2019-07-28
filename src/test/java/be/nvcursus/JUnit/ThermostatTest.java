package be.nvcursus.JUnit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThermostatTest {
	private final static int INTERVAL = 10;
	private Thermostat thermostat;
	private HeatingStub heatingStub;
	private SensorStub sensorStub;
	
	private class HeatingStub implements Heating {
		public void setHeating(boolean status) {
			//do nothing, we only need a class that seems to implement Heating
		}
	}
	
	private class SensorStub implements Sensor {
		private float temp;
		
		public Temperature getTemperature() {
			try {
				return new Temperature(temp);
			} catch (InvalidTemperatureException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public void setTemp(float temp) {
			this.temp = temp;
		}
	}
	
	@BeforeEach
	public void init() {
		sensorStub = new SensorStub();
		heatingStub = new HeatingStub();
		thermostat = new Thermostat(heatingStub, sensorStub);
		thermostat.setInterval(INTERVAL);
		thermostat.start();
	}
	
	@AfterEach
	public void tearDown() {
		thermostat.stop();
	}
	
	@Test
	public void testThermostat() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(20));
		sensorStub.setTemp(15);
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
	}

}

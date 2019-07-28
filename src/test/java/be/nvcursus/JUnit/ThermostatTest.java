package be.nvcursus.JUnit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
/**
 * ThermostatTest
 * Here, the class is tested using Stubs
 * A stub is an object that replaces another object in a test for the purpose of being
 * able to determine its behaviour in the test. Here we created the SensorStub in order
 * to be able to set the temperature it will return, allowing us to simulate multiple test
 * conditions.
 * @author gdegr_000
 *
 */
@DisplayName("Thermostat should")
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
	@DisplayName("be heating when the sensor temperature is lower than target temperature")
	public void testThermostat() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(20));
		sensorStub.setTemp(15);
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
	}

	@Test
	@DisplayName("stop heating when temperature hits target and start heating when temperature goes below target")
	public void testChangeCurrent() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(20));
		sensorStub.setTemp(20);
		Thread.sleep(INTERVAL*3);
		assertFalse(thermostat.isHeating());
		sensorStub.setTemp(19.0F);
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
	}
	
	@Test
	@DisplayName("stop heating when target temperature is lowered to current temperature or lower and start heating when target is raised")
	public void testChangeTarget() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(25));
		sensorStub.setTemp(20.7F);
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
		thermostat.setTargetTemperature(new Temperature(20.7F));
		Thread.sleep(INTERVAL*3);
		assertFalse(thermostat.isHeating());
		thermostat.setTargetTemperature(new Temperature(20.8F));
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
	}
}

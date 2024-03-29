package be.nvcursus.JUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
/**
 * ThermostatTestWithMocks
 * Here, the class is tested using Mocks
 * A mock is an object that replaces another object for the purpose of testing, just like
 * a stub is, but with the added difference that we can also check whether the communication
 * between the object we're testing and our mock is going as expected.
 * @author gdegr_000
 *
 */
@DisplayName("Thermostat should")
class ThermostatTestWithMocks {
	private final static int INTERVAL = 10;
	private Thermostat thermostat;
	private HeatingMock heatingMock;
	private SensorMock sensorMock;
	
	private class HeatingMock implements Heating {
		private boolean status;
		
		public void setHeating(boolean status) {
			this.status = status;
		}
		
		//extra mock method to verify
		public boolean isHeating() {
			return status;
		}
	}
	
	private class SensorMock implements Sensor {
		private float temp;
		private boolean called;
		
		public Temperature getTemperature() {
			try {
				called = true;
				return new Temperature(temp);
			} catch (InvalidTemperatureException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//stub method to control stub
		public void setTemp(float temp) {
			this.temp = temp;
		}
		
		//extra mock methods to verify
		public boolean isCalled() {
			return called;
		}
		
		public void setCalled(boolean status) {
			called = status;
		}
	}
	
	@BeforeEach
	public void init() {
		sensorMock = new SensorMock();
		heatingMock = new HeatingMock();
		thermostat = new Thermostat(heatingMock, sensorMock);
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
		sensorMock.setTemp(15);
		sensorMock.setCalled(false);
		heatingMock.setHeating(false);
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
		assertTrue(sensorMock.isCalled());
		assertTrue(heatingMock.isHeating());
	}

	@Test
	@DisplayName("stop heating when temperature hits target and start heating when temperature goes below target")
	public void testChangeCurrent() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(20));
		sensorMock.setTemp(20);
		sensorMock.setCalled(false);
		heatingMock.setHeating(false);
		Thread.sleep(INTERVAL*3);
		assertFalse(thermostat.isHeating());
		assertTrue(sensorMock.isCalled());
		assertFalse(heatingMock.isHeating());
		sensorMock.setTemp(19.9F);//borderline value testing
		sensorMock.setCalled(false);//re-initialize our sensorMock so we'll be able to verify whether it was called again
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
		assertTrue(sensorMock.isCalled());
		assertTrue(heatingMock.isHeating());
	}
	
	@Test
	@DisplayName("stop heating when target temperature is lowered to current temperature or lower and start heating when target is raised")
	public void testChangeTarget() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(25));
		sensorMock.setTemp(20.7F);
		sensorMock.setCalled(false);
		heatingMock.setHeating(false);//it would be heating in this case, but here we set the initial situation so we can be sure that it was changed by the thermostat later
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
		assertTrue(sensorMock.isCalled());
		assertTrue(heatingMock.isHeating());
		thermostat.setTargetTemperature(new Temperature(20.7F));
		sensorMock.setCalled(false);//re-initialize our sensorMock so we'll be able to verify whether it was called again
		Thread.sleep(INTERVAL*3);
		assertFalse(thermostat.isHeating());
		assertTrue(sensorMock.isCalled());
		assertFalse(heatingMock.isHeating());
		thermostat.setTargetTemperature(new Temperature(20.8F));
		sensorMock.setCalled(false);//re-initialize our sensorMock so we'll be able to verify whether it was called again
		Thread.sleep(INTERVAL*3);
		assertTrue(thermostat.isHeating());
		assertTrue(sensorMock.isCalled());
		assertTrue(heatingMock.isHeating());
	}
	
	@RepeatedTest(value=10)
	public void testThermostatInterval() throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(20));
		thermostat.setInterval(10);
		heatingMock.setHeating(false);
		sensorMock.setTemp(15);
		sensorMock.setCalled(false);
		assertTimeoutPreemptively(Duration.ofMillis(20), new Executable() {
			public void execute() throws InterruptedException {
				while (!heatingMock.isHeating()) {
					Thread.sleep(1);
				}
			}
		});
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/data.csv")
	@Tag("parameterized")
	public void paramTest(float target, float current, boolean status) throws InterruptedException, InvalidTemperatureException {
		thermostat.setTargetTemperature(new Temperature(target));
		sensorMock.setTemp(current);
		Thread.sleep(INTERVAL*3);
		assertEquals(status,thermostat.isHeating());
	}
}

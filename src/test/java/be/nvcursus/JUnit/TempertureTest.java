package be.nvcursus.JUnit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Class Temperature should")
class TempertureTest {
	
	private Temperature t;

	@BeforeEach
	public void init() throws InvalidTemperatureException {
		t = new Temperature(0.0F);
	}
	
	@Test
	@DisplayName("return the same temperature value it was made with")
	public void testConstructor() {
		assertNotNull(t.getValue());
		assertEquals(0.0F, t.getValue(), () -> "temperature " + t.getValue() + " differs from 0.0F");
	}

	@Test
	@DisplayName("return the same temperature that was set")
	public void testValue() throws InvalidTemperatureException {
		t.setValue(20.5F);
		assertEquals(20.5F, t.getValue(), () -> "temperature " + t.getValue() + " differs from 20.5F");
	}
	
	@Test
	@DisplayName("return boiling false if temperature is not 100")
	public void testNotBoiling() {
		assertFalse(t.isBoiling());
	}
	
	@Test
	@DisplayName("return boiling true if temperature is 100")
	public void testBoiling() throws InvalidTemperatureException {
		t.setValue(100.0F);
		assertTrue(t.isBoiling());
	}
	
	@Test
	@DisplayName("return freezing false if temperature is not 0")
	public void testNotFreezing() throws InvalidTemperatureException {
		t.setValue(50.0F);
		assertFalse(t.isFreezing());
	}
	
	@Test
	@DisplayName("return freezing true if temperature is 0")
	public void testFreezing() {
		assertTrue(t.isFreezing());
	}
	
	@Test
	@DisplayName("throw an exceptoin when setting a temperature lower than -273.15")
	public void testSetValue27320ThrowsException() {
		Throwable error = assertThrows(InvalidTemperatureException.class, () -> t.setValue(-273.20F));
		assertEquals("temperature cannot be lower than -273.15", error.getMessage());
	}
	
	@Test
	@DisplayName("throw no exception when setting a temperature of exactly -273.15")
	public void testSetValue27315ThrowsNothing() {
		assertDoesNotThrow(() -> t.setValue(-273.15F));
	}
	
	@Test
	@DisplayName("throw an exception when trying to make an instance with a temperature lower than -273.15")
	public void constructorThrowsExceptionForLowTemperature() {
		assertThrows(InvalidTemperatureException.class, () -> new Temperature(-273.16F));
	}
}

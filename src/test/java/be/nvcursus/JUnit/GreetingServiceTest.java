package be.nvcursus.JUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GreetingServiceTest {
	private GreetingService greeting;
	private Hello helloMock;
	
	@BeforeEach
	public void beforeTest() {
		helloMock = Mockito.mock(Hello.class);
		greeting = new GreetingService(helloMock);
	}
	
	@Test
	public void testGreet() {
		//prepare mock
		Mockito.when(helloMock.sayHello("World"))
		.thenReturn("Hello World");
		
		//execute test
		String answer = greeting.greet("World");
		Assertions.assertEquals("Greeting message: Hello World", answer);
	}
}

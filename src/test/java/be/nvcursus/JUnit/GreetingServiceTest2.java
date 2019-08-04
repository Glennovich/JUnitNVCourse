package be.nvcursus.JUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

/**
 * Opdracht 13: de Mockito extension van JUnit de mocks laten injecteren ipv dit zelf te doen
 * @author gdegr_000
 *
 */
@ExtendWith(MockitoExtension.class)
public class GreetingServiceTest2 {
	@Mock
	private Hello helloMock;
	
	@InjectMocks
	private GreetingService greeting;
	
	
	@Test
	public void testGreet() {
		//prepare mock
		Mockito.when(helloMock.sayHello("World"))
		.thenReturn("Hello World");
		
		//execute test
		String answer = greeting.greet("World");
		Assertions.assertEquals("Greeting message: Hello World", answer);
	}
	
	@Test
	public void testGreetNull() {
		//prepare mock
		Mockito.when(helloMock.sayHello(null))
		.thenThrow(new NullPointerException());
		
		//execute test
		Assertions.assertThrows(NullPointerException.class, () -> greeting.greet(null));
	}
	
	@Test
	public void testGreetAnswer() {
		//prepare mock
		Mockito.when(helloMock.sayHello("World"))
		.then(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				String arg = invocation.getArgument(0);
				return "Hello " + arg;
			}
		});
		
		//execute test
		String answer = greeting.greet("World");
		Assertions.assertEquals("Greeting message: Hello World", answer);
	}
	
	@Test
	public void testGreetAnswerLambda() {
		//prepare mock
		Mockito.when(helloMock.sayHello("World"))
		.then(invocation -> "Hello " + invocation.getArgument(0));
				
		//execute test
		String answer = greeting.greet("World");
		Assertions.assertEquals("Greeting message: Hello World", answer);
	}
	
	@Test
	public void testGreetMultiple() {
		//prepare mock
		Mockito.when(helloMock.sayHello("World"))
		.thenReturn("Hello World", "Goodbye World");
		
		//execute test
		String answer = greeting.greet("World");
		Assertions.assertEquals("Greeting message: Hello World", answer);
		
		answer = greeting.greet("World");
		Assertions.assertEquals("Greeting message: Goodbye World", answer);
	}
	
	@Test
	public void testGreetMatcher() {
		//prepare mock
		Mockito.when(helloMock.sayHello(ArgumentMatchers.anyString()))
		.then(invocation -> "Hello " + invocation.getArgument(0));
		
		String answer = greeting.greet("Homer");
		Assertions.assertEquals("Greeting message: Hello Homer", answer);
		
	}
	
	@Test
	public void testGreetVoid() {
		//prepare mock
		Mockito.doThrow(NullPointerException.class)
		.when(helloMock).sayHello(null);
		
		//execute test
		Assertions.assertThrows(NullPointerException.class, () -> greeting.greet(null));
	}
}

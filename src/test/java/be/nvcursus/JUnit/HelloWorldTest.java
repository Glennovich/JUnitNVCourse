package be.nvcursus.JUnit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HelloWorldTest {

	@Test
	public final void testSayHello() {
HelloWorld hello = new HelloWorld();
String answer = hello.sayHello();
assertEquals("Hello World", answer);
	}

}

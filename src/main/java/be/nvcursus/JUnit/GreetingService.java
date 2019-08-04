package be.nvcursus.JUnit;

public class GreetingService {
	private Hello hello;
	
	public GreetingService(Hello hello) {
		this.hello = hello;
	}
	
	public String greet(String name) {
		return "Greeting message: " + hello.sayHello(name);
	}
}

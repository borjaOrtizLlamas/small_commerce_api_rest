package com.borja;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * Hello world!
 *
 */
@RestController
public class App 
{

	  private static final String template = "Hello, %s!";
	  private final AtomicLong counter = new AtomicLong();

	  @RequestMapping("/")
	  public Dto greeting(@RequestParam(value="name", defaultValue="World") String name) {
	    return new Dto(counter.incrementAndGet(),
	              String.format(template, name));
	  }
	  
	  
}

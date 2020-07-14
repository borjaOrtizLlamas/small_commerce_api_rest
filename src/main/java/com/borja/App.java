package com.borja;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tfm.dto.Client;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Hello world!
 *
 */
@RestController
@RequestMapping("/client")
public class App {

	private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Autowired
	AccesToMongo acces;

	@PostMapping
	@ResponseBody
	public Boolean  createCliend(@RequestBody Client valueOne) {
		LOGGER.log(Level.INFO, "create clients");
		return acces.insertJson(valueOne);
	}

	@GetMapping
	@ResponseBody
	public List<Client>  getClients() {
		LOGGER.log(Level.INFO, "get  clients");
		return acces.returnClients();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Client getCliend(@PathVariable String id) {
		LOGGER.log(Level.INFO, "get client  --> " + id);
		return acces.returnClient(id);
	}

	@PutMapping
	@ResponseBody
	public Boolean editCliend(@RequestBody Client valueOne) throws Exception {
		LOGGER.log(Level.SEVERE, "UPDATE clients are failling");
		return false;
	}

}

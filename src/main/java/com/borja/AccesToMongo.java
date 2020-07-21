package com.borja;	

import static com.mongodb.client.model.Filters.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.tfm.dto.Client;
import com.tfm.dto.Product;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
@Service
public class AccesToMongo {
	
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");
	char[] password= {'a','d','m','i','n'}; 
	MongoCredential credential;
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection;
	
	public AccesToMongo() {
		credential = MongoCredential.createCredential("admin", "admin",password);//user, database, pw
		mongoClient = new MongoClient(new ServerAddress("mongo.tfm.com", 27017), Arrays.asList(credential));
		database = mongoClient.getDatabase("admin");
		collection = database.getCollection("clients");
	}
	
	public boolean update(Client client) throws Exception {
		if(!existsInBBDD(client.getName())) {
			throw new Exception("client not exits"); 
		}
	    Bson deleteFilter = Filters.eq("name", client.getName());
	    collection.deleteOne(Filters.and(deleteFilter));
		insertJson(client);
		return true; 
	}
	
	public boolean insertJson(Client client) throws Exception {
		if(existsInBBDD(client.getName())) {
			throw new Exception("client exits"); 
		}
		LOGGER.log(Level.INFO, "Adding client");
        Document docu = new Document();
		JSONObject jsonObject = new JSONObject(client); 
		System.out.println("---------------" + jsonObject.toString());
		LOGGER.log(Level.INFO, "---------" + jsonObject.toString());
        docu = docu.parse(client.toString());  
		LOGGER.log(Level.INFO, "Adding document to mogodb: " + docu);
		collection.insertOne(docu);
		LOGGER.log(Level.INFO, "Adding client finished");
		return true; 
	}
	

	public List<Client> returnClients() {
		List<Client> clients = new ArrayList<>(); 
		FindIterable<Document> documents = collection.find();
		for (Document document : documents) {
			Client cli = new Client(); 
			cli.setName(document.getString("name"));
			cli.setPhone(document.getString("phone"));
			List<ArrayList<Document>>  ob = (ArrayList<ArrayList<Document>>) document.get("products"); 
			if(ob != null) { 
				List<Product> abb = new ArrayList<Product>();
				for (List<Document> docus : ob) {
					Product pro = new Product();  
					for(Document doc : docus) {
						pro.setName((String)doc.get("name"));
						pro.setPrice((Double) doc.get("price"));
						pro.setDescription((String)doc.get("description"));
						abb.add(pro);
					}
				}
				cli.setProducts(abb);
			}
			clients.add(cli); 
		}
		return clients;
	}

	public boolean addProductInClient(String id, List<Product> products ) throws Exception {
		Client cli = returnClient(id);
		LOGGER.log(Level.INFO, "el cliente para anadir es este --> "  + cli.toString());

		for(Product product : products) {
			cli.getProducts().add(product);
		}
		LOGGER.log(Level.INFO, "el cliente con los productos añadidos es este --> "  + cli.toString());
		deleteClient(id); 
		LOGGER.log(Level.INFO, "cliente eliminado --> "  + cli.toString());
		insertJson(cli); 
		LOGGER.log(Level.INFO, "insertado");

		return true; 
	}

	
	public Client returnClient(String name) throws Exception {
		if(!existsInBBDD(name)) {
			throw new Exception("client not exits"); 
		}

		Document document = collection.find(eq("name",name)).first(); 
		Client cli = new Client(); 
		cli.setName(document.getString("name"));
		cli.setPhone(document.getString("phone"));
		List<ArrayList<Document>>  ob = (ArrayList<ArrayList<Document>>) document.get("products"); 
		if(ob != null) { 
			List<Product> abb = new ArrayList<Product>();
			for (List<Document> docus : ob) {
				Product pro = new Product();  
				for(Document doc : docus) {
					pro.setName((String)doc.get("name"));
					pro.setPrice((Double)doc.get("price"));
					pro.setDescription((String)doc.get("description"));
					
					abb.add(pro);
				}
			}
			cli.setProducts(abb);
		}
		return cli; 
	}

	public boolean deleteClient(String name) throws Exception {
		if(!existsInBBDD(name)) {
			throw new Exception("the client do not exits"); 
		} 
	    Bson deleteFilter = Filters.eq("name", name);
		LOGGER.log(Level.INFO, "eliminando cliente --> "  + name);

	    collection.deleteOne(Filters.and(deleteFilter));
	    return true; 
	}
	private boolean existsInBBDD(String client) {
		List<Client> allClient = returnClients(); 
		for (Client cli : allClient) {
			if (cli.getName().equals(client)) return true; 
		}
		return false; 
	}
}

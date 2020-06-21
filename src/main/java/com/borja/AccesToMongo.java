package com.borja;	

import static com.mongodb.client.model.Filters.*;
import com.borja.dto.Client;
import com.borja.dto.Product;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;
//@Service
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
	
	
	public boolean insertJson(Client client) {
        LOGGER.log(Level.INFO, "Adding client");
        Document docu = new Document(); 
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
			List<ArrayList<Document>>  ob = (ArrayList<ArrayList<Document>>) document.get("products"); 
			if(ob != null) { 
				List<Product> abb = new ArrayList<Product>();
				for (List<Document> docus : ob) {
					Product pro = new Product();  
					for(Document doc : docus) {
						pro.setName((String)doc.get("name"));
						pro.setPrecio((String)doc.get("precio"));
						pro.setPrecio((String)doc.get("description"));
						abb.add(pro);
					}
				}
				cli.setProducts(abb);
			}
			clients.add(cli); 
		}
		return clients;
	}

	public Client returnClient(String name) {
		Document document = collection.find(eq("name",name)).first(); 
		Client cli = new Client(); 
		cli.setName(document.getString("name"));
		List<ArrayList<Document>>  ob = (ArrayList<ArrayList<Document>>) document.get("products"); 
		if(ob != null) { 
			List<Product> abb = new ArrayList<Product>();
			for (List<Document> docus : ob) {
				Product pro = new Product();  
				for(Document doc : docus) {
					pro.setName((String)doc.get("name"));
					pro.setPrecio((String)doc.get("precio"));
					abb.add(pro);
				}
			}
			cli.setProducts(abb);
		}
		return cli; 
	}

}

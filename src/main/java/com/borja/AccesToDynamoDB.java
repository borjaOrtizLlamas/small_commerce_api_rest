package com.borja;	

import static com.mongodb.client.model.Filters.*;

import com.tfm.dto.Client;
import com.tfm.dto.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.*;

@Service
public class AccesToDynamoDB {
	
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");
    private final String tableName = "nombreTabla";
    private DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(getClient()).build();
	private DynamoDbTable<Client> clientsTable = enhancedClient.table(tableName, TableSchema.fromBean(Client.class));

	public AccesToDynamoDB() {
	}
	
    public boolean createClient(Client client) {
    	try {
    		clientsTable.putItem(client);
	    	return true;
    	} catch (DynamoDbException e) {
    		LOGGER.log(Level.SEVERE, e.getMessage());
    		e.getStackTrace();     		
	        throw e; 
    	}
    }
	
    
	

    private DynamoDbClient getClient() {
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(region)
                .build();

        return ddb;
    }
    
}

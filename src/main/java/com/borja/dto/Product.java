package com.borja.dto;

import java.io.Serializable;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class Product implements Serializable{

	String name;
	String precio;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\", \"precio\":\"" + precio + "\"}";
	}
	
}

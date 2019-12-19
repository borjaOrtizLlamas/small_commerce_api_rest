package com.borja;

public class Dto {
	  private  long id;
	  private  String content;
	  
	  public Dto(){
		  super();
	  }

	public Dto(long id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}

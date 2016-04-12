package edu.ap.jaxrs;

import java.io.*;
import javax.ws.rs.*;
import javax.json.*;

@Path("/quotes")
public class ProductResource {
	
	static final String FILE = "/Users/Robin/Desktop/Quotes.json";
	// get all quotes	
	@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		String htmlString = "<html><body>";
		try {
			JsonReader reader = Json.createReader(new StringReader(getProductsJSON()));
			JsonObject rootObj = reader.readObject();
			JsonArray array = rootObj.getJsonArray("quotes");
			for(int i = 0 ; i < array.size(); i++) {
				JsonObject obj = array.getJsonObject(i);
				htmlString += "<b>qoute : " + obj.getString("quote") +"<b>, geschreven door " + obj.getString("author")+ "</b><br>";
			}
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}
	
	@GET
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public String getProductsJSON() {
		String jsonString = "";
		try {
			InputStream fis = new FileInputStream(FILE);
	        JsonReader reader = Json.createReader(fis);
	        JsonObject obj = reader.readObject();
	        reader.close();
	        fis.close();
	        
	        jsonString = obj.toString();
		} 
		catch (Exception ex) {
			jsonString = ex.getMessage();
		}
		
		return jsonString;
	}
//return quotes from author in html format  /3	
	@GET
	@Path("{author}")
	@Produces({"text/html"})
	public String getProductJSON(@PathParam("author") String author) {
		String htmlString = "<html><body>";
		try {
			InputStream fis = new FileInputStream(FILE);
	        JsonReader reader = Json.createReader(fis);
	        JsonObject jsonObject = reader.readObject();
	        reader.close();
	        fis.close();
	        
	        
	        
	        JsonArray array = jsonObject.getJsonArray("quotes");
	        for(int i = 0; i < array.size(); i++) {
	        	JsonObject obj = array.getJsonObject(i);
	        	if(obj.getString("author").equalsIgnoreCase(author)) {
	        		htmlString += "<b>qoute : " + obj.getString("quote") +"<b>, geschreven door " + obj.getString("author")+ "</b><br>";
	           
	            }
	        }
		} 
		catch (Exception ex) {
			htmlString = ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}
	
	@POST
	@Consumes({"application/json"})
	public String addProduct(String productJSON) {
		String returnCode = "";
		try {
			// read existing products
			InputStream fis = new FileInputStream(FILE);
	        JsonReader jsonReader1 = Json.createReader(fis);
	        JsonObject jsonObject = jsonReader1.readObject();
	        jsonReader1.close();
	        fis.close();
	        
	        JsonReader jsonReader2 = Json.createReader(new StringReader(productJSON));
	        JsonObject newObject = jsonReader2.readObject();
	        jsonReader2.close();
	        
	        JsonArray array = jsonObject.getJsonArray("quotes");
	        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
	        
	        for(int i = 0; i < array.size(); i++){
	        	// add existing products
	        	JsonObject obj = array.getJsonObject(i);
	        	arrBuilder.add(obj);
	        }
	        // add new product
	        arrBuilder.add(newObject);
	        
	        // now wrap it in a JSON object
	        JsonArray newArray = arrBuilder.build();
	        JsonObjectBuilder builder = Json.createObjectBuilder();
	        builder.add("quotes", newArray);
	        JsonObject newJSON = builder.build();

	        // write to file
	        OutputStream os = new FileOutputStream(FILE);
	        JsonWriter writer = Json.createWriter(os);
	        writer.writeObject(newJSON);
	        writer.close();
		} 
		catch (Exception ex) {
			returnCode = ex.getMessage();
		}
		
		return returnCode;
	}
}
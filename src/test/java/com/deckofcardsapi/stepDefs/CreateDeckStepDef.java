package com.deckofcardsapi.stepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;

import com.deckofcardsapi.utilities.ConfigReader;

public class CreateDeckStepDef {
	
	private static String deck_id = "";
	private static int deck_size = 0;
	
	static {
		RestAssured.baseURI = ConfigReader.getProperty("baseUri");
	}
	
	@Given("User creates {int} number of decks of cards shuffled {string}")
	public void iCreateNNumberOfDeckOfCards(int n, String isShuffled) {
		
		if (isShuffled.equals("true")) {
			RestAssured.basePath = "/api/deck/new/shuffle/";
		} else {
			RestAssured.basePath = "/api/deck/new/";
		}
	    
		String response = given()
			.queryParam("deck_count", n)
		
		.when()
			.get()
		
		.then()
			.assertThat().statusCode(200)
			.extract().response().asString();
		
		JsonPath jp = new JsonPath(response);
		
		deck_id = jp.getString("deck_id");
		
		System.out.println(response);
		
	}

	@Then("Decks of Card should have {int} times {int} cards")
	public void deckOfCardShouldHaveNTimesCards(int n, int deckSize) {
		
		if (n < 0) n = 0;
		
		String response = given()
			.basePath("/api/deck/" + deck_id)
		.when()
			.get()
			
		.then()
			.assertThat().statusCode(200)
			.body("remaining", equalTo(n * deckSize))
			.body("success", equalTo(true))
			.extract().response().asString();
		
		JsonPath jp = new JsonPath(response);
		
		deck_id = jp.getString("deck_id");
		
		System.out.println(response);
	   
	}
	
	@Then("Deck of Cards shuffle status must be {string}")
	public void deckOfCardsShuffleStatusMustBeTrue(String isShuffled) {		
		
		String response = given()
				.basePath("/api/deck/" + deck_id)
			.when()
				.get()
				
			.then()
				.assertThat().statusCode(200)
				.body("shuffled", equalTo(Boolean.valueOf(isShuffled)))
				.body("success", equalTo(true))
				.extract().response().asString();
			
			JsonPath jp = new JsonPath(response);
			
			deck_id = jp.getString("deck_id");
			
			System.out.println(response);
	}
	
	@Then("If Number of Decks {int} is over twenty and shuffled {string} success should be false and give error message")
	public void ifNumberOfDecksDeckCountIsOverTwentySuccessShouldBeFalseAndGiveErrorMessage(int n, String isShuffled) {
		
		
		if (isShuffled.equals("true")) {
			RestAssured.basePath = "/api/deck/new/shuffle/";
		} else {
			RestAssured.basePath = "/api/deck/new/";
		}
		
		if (n>20) {
	    
			String response = given()
				.queryParam("deck_count", n)
			
			.when()
				.get()
			
			.then()
				.assertThat().statusCode(200)
				.body("success", equalTo(false))
				.body("error", is(not(emptyString())))
				.extract().response().asString();
			
			JsonPath jp = new JsonPath(response);
			
			deck_id = jp.getString("deck_id");
			
			System.out.println(response);
		}
	   
	}
	
	@Given("User creates one unshuffled deck with jokers")
	public void userCreatesOneUnshuffledDeckWithJokers() {		
	
		RestAssured.basePath = "/api/deck/new/";		
	    
		JsonPath jp = given()			
			.queryParam("jokers_enabled", true)
		.when()
			.get()
		
		.then()
			.assertThat().statusCode(200)
			.extract().response().jsonPath();
		
		deck_id = jp.getString("deck_id");
		deck_size = jp.getInt("remaining");
		
	}

	@Then("Number of cards is {int}")
	public void numberOfCardsIs(int card_count) {
	    Assert.assertEquals(card_count, deck_size);
	}

	@Then("Last two drawn cards are jokers")
	public void firstTwoDrawnCardsAreJokers() {
		
		given()
			.basePath("/api/deck/" + deck_id + "/draw/").queryParam("count", "52")
		.when()
			.get()
		.then()
			.assertThat().statusCode(200);
			
		JsonPath jp = given()
				.basePath("/api/deck/" + deck_id + "/draw/").queryParam("count", "2")				
			
			.when()
				.get()
				
			.then()
				.assertThat().statusCode(200)
				.extract().response().jsonPath();
		
		Assert.assertEquals("JOKER", jp.getString("cards[0].value"));
		Assert.assertEquals("JOKER", jp.getString("cards[1].value"));		
	}
	

}

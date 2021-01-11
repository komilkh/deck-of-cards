package com.deckofcardsapi.stepDefs;

import static io.restassured.RestAssured.given;

import com.deckofcardsapi.utilities.ConfigReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.regex.Pattern;

import org.junit.Assert;


public class DrawCardStepDef {
	
	private static String deck_id = "";
	private static int deck_size = 0;
	private static int draw_count = 0;
	private static JsonPath jPath = null;
	
	static {
		RestAssured.baseURI = ConfigReader.getProperty("baseUri");
	}
	
	@Given("User creates a new deck of cards with random allowed number of decks")
	public void userCreatesANewDeckOfCardsWithRandomAllowedNumberOfDecks() {
		
		int n = (int)(Math.random()*19+1);
		
		String response = given()
			.basePath("/api/deck/new/").queryParam("deck_count", n)
			
		
		.when()
			.get()
		
		.then()
			.assertThat().statusCode(200)
			.extract().response().asString();
		
		JsonPath jp = new JsonPath(response);
		
		deck_id = jp.getString("deck_id");
		deck_size = jp.getInt("remaining");
		
		System.out.println(response);
	}

	@When("User draws random number of cards between one and total number of cards")
	public void userDrawsRandomNumberOfCardsBetweenOneAndTotalNumberOfCards() {
	    
		draw_count = (int)(Math.random()*(deck_size-1)+1);
		
		JsonPath jp = given()
				.basePath("/api/deck/" + deck_id + "/draw/").queryParam("count", draw_count)				
			
			.when()
				.get()
				
			.then()
				.assertThat().statusCode(200)
				.extract().response().jsonPath();
				
		
		jPath = jp;
	}

	@Then("Remaining number of cards are correct")
	public void remainingNumberOfCardsAreCorrect() {
		Assert.assertEquals(deck_size, jPath.getInt("remaining") + draw_count);
	}
	
	@Then("All Drawn cards in the right format")
	public void allDrawnCardsInTheRightFormat() {
		for(int i = 0; i < draw_count; i++) {
			
			String value = jPath.getString("cards[" + i + "].value");
			String suit = jPath.getString("cards[" + i + "].suit");
			String code = jPath.getString("cards[" + i + "].code");
			String image = jPath.getString("cards[" + i + "].image");
			
			Assert.assertTrue(Pattern.matches("([2-9]|10|JACK|QUEEN|KING|ACE)", value));
			Assert.assertTrue(Pattern.matches("(SPADES|CLUBS|HEARTS|DIAMONDS)", suit));
			
			String expectedCode = "";
			if (value.equals("10")) {
				expectedCode = value.substring(1, 2) + suit.substring(0,1);				
			} else {
				expectedCode = value.substring(0, 1) + suit.substring(0,1);				
			}
			
			Assert.assertEquals(expectedCode, code);
			
			if(expectedCode.equals("AD")) expectedCode = "aceDiamonds";
			
			Assert.assertEquals("https://deckofcardsapi.com/static/img/" + expectedCode + ".png", image);			
		}
	}

}

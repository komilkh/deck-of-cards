package com.deckofcardsapi.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
		tags = "@runThis",
		
		features = "src/test/resources/com/deckofcardsapi/features",
		glue = "com/deckofcardsapi/stepDefs",
		plugin = {"summary",
				 
				 "html:target/builtInReport",
				 "json:target/Cucumber.json" 
		
		}, 
		monochrome = true 
		//	,dryRun = true  
		
		,snippets = SnippetType.CAMELCASE
		,stepNotifications = true
		)

public class MainRunner {

}

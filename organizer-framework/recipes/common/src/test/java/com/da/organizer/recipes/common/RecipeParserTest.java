/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.descriptors.UnitSize;
import com.da.organizer.recipes.common.parse.RecipeParser;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author dandrus
 */
public class RecipeParserTest {
    
    final static Logger logger = Logger.getLogger(RecipeParserTest.class);
    
    public RecipeParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    public static final String RECIPE_ONE = 
            "Name: PB&J Sandwich\n"
            + "Description: Super Yummers\n"
            + "Servings: 1\n"
            + "From: childhood\n"
            + "Ingredients:\n"
            + "1/4 c. peanut butter\n"
            + "1/4 c. jelly\n"
            + "2 slices bread\n"
            + "Instructions:\n"
            + "Slather PB on one side\n"
            + "Slather Jelly on other\n"
            + "Slap together\n"
            + "Enjoy.";
    
   
    
    /**
     * Test of fromString method, of class RecipeParser.
     */
    @Test
    public void testFromString() {
        String recipeString = RECIPE_ONE;
        
        List<Recipe> parsedRecipes = RecipeParser.fromString(recipeString);
        assertEquals(parsedRecipes.size(), 1);
        Recipe parsedRecipe = parsedRecipes.get(0);
        assertNotNull(parsedRecipe);
        
        assertEquals(parsedRecipe.getName(), "PB&J Sandwich");
        assertEquals(parsedRecipe.getOrigination(), "childhood");
        assertEquals(parsedRecipe.getNumberOfServings(), 1);
        assertEquals(parsedRecipe.getDescription(), "Super Yummers");
        assertEquals(parsedRecipe.getIngredients().size(), 3);
        assertEquals(parsedRecipe.getInstructions().size(), 4);
        
    }
    
    @Test
    public void testParseServings()
    {
        Recipe recipe = new Recipe();
        
        RecipeParser.parseServingsString("4", recipe);
        assertEquals(recipe.getNumberOfServings(), 4);
        assertEquals(recipe.getServingsDescription(), "4");
        
        RecipeParser.parseServingsString("4 - 6", recipe);
        assertEquals(recipe.getNumberOfServings(), 4);
        assertEquals(recipe.getServingsDescription(), "4 - 6");
        
        RecipeParser.parseServingsString("4-6", recipe);
        assertEquals(recipe.getNumberOfServings(), 4);
        assertEquals(recipe.getServingsDescription(), "4-6");
        
        RecipeParser.parseServingsString("4 appetizers", recipe);
        assertEquals(recipe.getNumberOfServings(), 4);
        assertEquals(recipe.getServingsDescription(), "4 appetizers");
        
        RecipeParser.parseServingsString("4 mains or 6 appetizers", recipe);
        assertEquals(recipe.getNumberOfServings(), 4);
        assertEquals(recipe.getServingsDescription(), "4 mains or 6 appetizers");
    }
    
    @Test 
    public void testWithLeadingSpace()
    {
        String ingString = " pepper";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "pepper");
        if(ingredient.getDescriptors() != null && !ingredient.getDescriptors().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        
        assertNull(ingredient.getRecipeAmount().getAmount());
        assertNull(ingredient.getRecipeAmount().getUnit());
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        
    }
    
    @Test
    public void testIngParser_pb()
    {
        String pb = "2 T crunchy peanut butter, divided";
        RecipeIngredient pbIng = RecipeParser.parseIngredientString(pb);
    }
    
    @Test
    public void testIngParser_multipleNumbers()
    {
        String ingString = "1/3 cup dried currants, soaked in 1/2 cup hot water (optional)";
        // What do I want here?
        /**
         * What do I want here?
         * amount - 1/3 cup
         * ingredient - dried currants
         * optional is a descriptor, and parens contents should then be empty.
         * 
         * actually, I'm going to skip this one for now, and come back to it after
         * I get some of the more basic cases out of the way.
         */
//        
//        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
//        assertEquals(ingredient.getIngredientName(), "dried currants");
//        descriptorCheck(ingredient.getDescriptors(), IngredientDescriptor.optional);
//        assertEquals("(optional)", ingredient.getParensContents());
//        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
//        assertEquals(ingredient.getOriginalString(), ingString);
//        checkAmount((Integer) 1, (Integer)3, 0, ingredient);
    }
    
    @Test
    public void testIngredient_withOr()
    {
        /**
         * This is another complicated one. Kind of like "red or yellow peppers"
         */
//        String ingString = "1 cup sweet or purple basil, torn";
//        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
//        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "basil");
//        
//        checkDescriptors(ingredient.getDescriptors(), IngredientDescriptor.torn);
//        assertEquals(ingredient.getOriginalString(), ingString);    
//        
//        ingString = "2 tablespoons minced fresh thyme or 2 teaspoons dried thyme";
//        ingredient = RecipeParser.parseIngredientString(ingString);
//        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "minced fresh thyme");
//        assertEquals(ingredient.getPrePreparation(), "or 2 teaspoons dried thyme");
//        assertEquals(ingredient.getOriginalString(), ingString);    
    }
}
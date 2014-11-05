/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.parse.RecipeParser;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import com.da.organizer.recipes.common.unit.UnitSize;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
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
    public void testIngParser_onion()
    {
        String onion = "1 large onion, chopped coarsely";
        RecipeIngredient onionIng = RecipeParser.parseIngredientString(onion);
        //testing is so tedious sometimes....especially string parsing....but, how else would I go about this????
        checkAmount(0, 0, (Integer)1, onionIng);
        
        assertEquals(IngredientUnit.whole, onionIng.getRecipeAmount().getUnit());
        assertEquals(onionIng.getRecipeAmount().getUnitSize(), UnitSize.large);
        assertEquals(onionIng.getIngredientName(), "onion");
        assertEquals(onionIng.getPrePreparation(), "chopped coarsely");
    }
    
    @Test 
    public void testWithLeadingSpace()
    {
        String ingString = " pepper";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "pepper");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        
        assertNull(ingredient.getRecipeAmount().getAmount());
        assertNull(ingredient.getRecipeAmount().getUnit());
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
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
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals(ingredient.getIngredientName(), "dried currants");
        assertEquals(ingredient.getPrePreparation(), "soaked in 1/2 cup hot water (optional)");
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        checkAmount((Integer) 1, (Integer)3, 0, ingredient);
    }
    
    private void checkAmount(Integer numerator, Integer denominator, Integer whole, RecipeIngredient ingredient)
    {
        assertEquals(ingredient.getRecipeAmount().getAmount().getNumerator(), numerator);
        assertEquals(ingredient.getRecipeAmount().getAmount().getDenominator(), denominator);
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), whole);
    }
    
    
    @Test
    public void testIngParser_cansTomatos()
    {
        String ingString = "1 28.6-oz can tomatoes, drained";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "tomatoes");
        assertEquals("Ingredient should have pre-prep of 'drained'", ingredient.getPrePreparation(), "drained");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.can);
        assertEquals(ingredient.getRecipeAmount().getPackageSize(), ((Double)28.6));
        assertEquals(ingredient.getRecipeAmount().getPackageUnit(), IngredientUnit.ounce);
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_pepper()
    {
        String ingString = "pepper";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "pepper");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        
        assertNull(ingredient.getRecipeAmount().getAmount());
        assertNull(ingredient.getRecipeAmount().getUnit());
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
            
    }
    
    @Test
    public void testIngredient_saltToTaste()
    {
        String ingString = "salt, to taste";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "salt");
        assertEquals("Ingredient should have pre-prep of 'to taste'", ingredient.getPrePreparation(), "to taste");
        assertNull(ingredient.getRecipeAmount().getAmount());
        assertNull(ingredient.getRecipeAmount().getUnit());
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
            
    }
    
    @Test
    public void testIngredient_boiledEgg()
    {
        String ingString = "12 eggs, boiled";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "eggs");
        assertEquals("Ingredient should have pre-prep of 'boiled'", ingredient.getPrePreparation(), "boiled");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)12));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
            
    }
    
    
    @Test
    public void testIngredient_largeonion()
    {
        String ingString = "1 large onion, chopped coarsely";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "onion");
        assertEquals("Ingredient should have pre-prep of 'chopped coarsely'", ingredient.getPrePreparation(), "chopped coarsely");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertEquals(ingredient.getRecipeAmount().getUnitSize(), UnitSize.large);
    }
    
    
    @Test
    public void testIngredient_packageCreamCheese()
    {
        String ingString = "2 8-oz pkg cream cheese, softened";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "cream cheese");
        assertEquals("Ingredient should have pre-prep of 'softened'", ingredient.getPrePreparation(), "softened");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)2));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.pkg);
        assertEquals(ingredient.getRecipeAmount().getPackageSize(), ((Double)8.0));
        assertEquals(ingredient.getRecipeAmount().getPackageUnit(), IngredientUnit.ounce);
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_smallapples()
    {
        String ingString = "5 small tart apples, peeled, cored & chopped";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "tart apples");
        assertEquals("Ingredient should have pre-prep of 'peeled, cored & chopped'", ingredient.getPrePreparation(), "peeled, cored & chopped");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)5));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertEquals(ingredient.getRecipeAmount().getUnitSize(), UnitSize.small);
    }
    
    @Test
    public void testIngredient_OnePercentMilk()
    {
        String ingString = "1/2 c 1% lowfat milk";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "1% lowfat milk");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        assertEquals(ingredient.getRecipeAmount().getAmount().getNumerator(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getAmount().getDenominator(), ((Integer)2));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_flour()
    {
        String ingString = "2 1/2 c flour";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "flour");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)2));
        assertEquals(ingredient.getRecipeAmount().getAmount().getNumerator(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getAmount().getDenominator(), ((Integer)2));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_flourWithLeadingSpace()
    {
        String ingString = " 2 1/2 c flour";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "flour");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)2));
        assertEquals(ingredient.getRecipeAmount().getAmount().getNumerator(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getAmount().getDenominator(), ((Integer)2));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_lemonJuice()
    {
        String ingString = "Juice of half a lemon";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        
        ingString = "2 4.5-pound pork shoulder";
        ingredient = RecipeParser.parseIngredientString(ingString);
        assertTrue(ingredient.getRecipeAmount().getPackageSize() == 4.5);
        assertTrue(ingredient.getRecipeAmount().getPackageUnit().equals(IngredientUnit.pound));
        checkAmount(0, 0, 2, ingredient); // 2 whole items, no fraction.
        
        ingString = "4 Tbsp. cinnamon";
        ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals(ingredient.getIngredientName(), "cinnamon");
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.tablespoon);
        checkAmount(0, 0, 4, ingredient);
        
        ingString = "1 medium-to-large firm ripe melon";
        ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals(ingredient.getIngredientName(), "medium-to-large firm ripe melon");
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        checkAmount(0, 0, 1, ingredient);
    }
    
    @Test
    public void moreTesting(){
        
        String ingString = "1 2-by-1/2-inch strip orange zest";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals(ingredient.getIngredientName(), "2-by-1/2-inch strip orange zest");
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        checkAmount(0, 0, 1, ingredient);
        
        ingString = "2 cups lightly-packed fresh spring greens";
        ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals(ingredient.getIngredientName(), "lightly-packed fresh spring greens");
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        checkAmount(0, 0, 2, ingredient);
    }
    
    @Test
    public void testIngredient_bunchArugula()
    {
        String ingString = "1 small bunch arugula";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "arugula");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.bunch);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertEquals(ingredient.getRecipeAmount().getUnitSize(), UnitSize.small);
    }
    
    @Test
    public void testIngredient_bunchChickenThighs()
    {
        String ingString = "12 boneless skinless chicken thighs, (about 2 1/2 pounds total)";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "boneless skinless chicken thighs");
        
        assertEquals("Ingredient should have pre-prep of '(about 2 1/2 pounds total)'", ingredient.getPrePreparation(), "(about 2 1/2 pounds total)");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)12));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_chickenBroth()
    {
        String ingString = "3 c low-sodium chicken broth";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "low-sodium chicken broth");
        if(ingredient.getPrePreparation() != null && !ingredient.getPrePreparation().isEmpty())
        {
            fail("Ingredient should have no pre prep or additional notes.");
        }
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)3));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_WholeWheatPitas()
    {
        String ingString = "4 8-inch whole-wheat pitas, toasted";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "whole-wheat pitas");
        assertEquals("Ingredient should have pre-prep of 'toasted'", ingredient.getPrePreparation(), "toasted");
        assertEquals(ingredient.getRecipeAmount().getAmount().getWhole(), ((Integer)4));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.whole);
        assertEquals(ingredient.getRecipeAmount().getPackageSize(), ((Double)8.0));
        assertEquals(ingredient.getRecipeAmount().getPackageUnit(), IngredientUnit.inch);
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    @Test
    public void testIngredient_FlatLeafParsley()
    {
        String ingString = "1/4 c fresh flat-leaf parsley, finely chopped";
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        assertEquals("Ingredient name is incorrect", ingredient.getIngredientName(), "fresh flat-leaf parsley");
        assertEquals("Ingredient should have pre-prep of 'finely chopped'", ingredient.getPrePreparation(), "finely chopped");
        assertEquals(ingredient.getRecipeAmount().getAmount().getNumerator(), ((Integer)1));
        assertEquals(ingredient.getRecipeAmount().getAmount().getDenominator(), ((Integer)4));
        assertEquals(ingredient.getRecipeAmount().getUnit(), IngredientUnit.cup);
        assertNull(ingredient.getRecipeAmount().getPackageSize());
        assertNull(ingredient.getRecipeAmount().getPackageUnit());
        assertNull(ingredient.getRecipeAmount().getUnitSize());
    }
    
    
    @Ignore
    @Test 
    public void testFromFile()
    {
        URL recipeDirectoryUrl = this.getClass().getClassLoader().getResource("recipes");
        File recipeDirectory = FileUtils.toFile(recipeDirectoryUrl);
        String[] extensions = new String[]{"recipes"};
        Collection<File> listOfRecipes = FileUtils.listFiles(recipeDirectory, extensions, false);
        
        for(File recipeFile:listOfRecipes)
        {
            try {
                String recipeString = FileUtils.readFileToString(recipeFile, "UTF-8");
                List<Recipe> recipes = RecipeParser.fromString(recipeString);
            } catch (IOException ex) {
                logger.error("Unable to read file: "+recipeFile, ex);
            } catch (Exception ex2)
            {
                logger.error("Unable to parse recipe", ex2);
                fail("Unable to parse recipe, exception: "+ex2);
            }
        }
    }
}
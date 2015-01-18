/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.descriptors.Form;
import com.da.organizer.recipes.common.descriptors.Optionality;
import com.da.organizer.recipes.common.descriptors.PrepDescriptor;
import com.da.organizer.recipes.common.descriptors.Preparation;
import com.da.organizer.recipes.common.descriptors.Temperature;
import com.da.organizer.recipes.common.descriptors.UnitSize;
import com.da.organizer.recipes.common.parse.RecipeParser;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author diane
 */
public class IngredientParserTest {

    
    
    public void recipeEquals(RecipeIngredient r1, RecipeIngredient r2)
    {
        assertTrue(r1.getIngredientName().equalsIgnoreCase(r2.getIngredientName()));
        assertEquals(r1.getOriginalString(), r2.getOriginalString());
        
        // make sure that if one is null, then the other is null or an empty string. 
        boolean r1isEmptyOrNull = false;
        boolean r2isEmptyOrNull = false;
        if(r1.getParensContents() == null || r1.getParensContents().isEmpty())
        {
            r1isEmptyOrNull = true;
        }
        if(r2.getParensContents() == null || r2.getParensContents().isEmpty())
        {
            r2isEmptyOrNull = true;
        }
        assertEquals(r1isEmptyOrNull, r2isEmptyOrNull);
        
        // otherwise, make sure that they are equal.
        if(!r1isEmptyOrNull)
        {
            assertEquals(r1.getParensContents(), r2.getParensContents());
        }
        assertEquals(r1.getRecipeAmountAsString(), r2.getRecipeAmountAsString());
        
        
        if(r1.getRecipeAmount().getAmount() != null)
        {
            assertNotNull(r2.getRecipeAmount().getAmount());
            assertEquals(r1.getRecipeAmount().getAmount().getNumerator(), r2.getRecipeAmount().getAmount().getNumerator());
            assertEquals(r1.getRecipeAmount().getAmount().getDenominator(), r2.getRecipeAmount().getAmount().getDenominator());
            assertEquals(r1.getRecipeAmount().getAmount().getWhole(), r2.getRecipeAmount().getAmount().getWhole());
        }else
        {
            assertNull(r2.getRecipeAmount().getAmount());
        }
        
        assertEquals(r1.getRecipeAmount().getPackageSize(), r2.getRecipeAmount().getPackageSize());
        assertEquals(r1.getRecipeAmount().getPackageUnit(), r2.getRecipeAmount().getPackageUnit());
        assertEquals(r1.getRecipeAmount().getUnit(), r2.getRecipeAmount().getUnit());
        
        assertEquals(r1.getDescriptors(), r2.getDescriptors());
    }
    
    @Test
    public void stripOfOrangeZest()
    {
        String ingString = "1 strip orange zest, 2-by-1/2-inch";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("orange zest");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.strip);
        expected.setRecipeAmount(amount);
        
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void looselyPackedFreshSpringGreens()
    {
        String ingString = "2 cups lightly-packed fresh spring greens";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("spring greens");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(2, 0, 0));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.fresh);
        expected.getUnitSizeDescriptor().add(UnitSize.looselypacked);
        
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void decimalAmountValue()
    {
        String ingString = "10.7 ounces bread flour";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("bread flour");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(10, 7, 10));
        amount.setUnit(IngredientUnit.ounce);
        expected.setRecipeAmount(amount);
        
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    
    @Test
    public void peeledFreshGinger()
    {
        String ingString = "2 1/4-inch slices peeled fresh ginger";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("ginger");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(2, 0, 0));
        amount.setUnit(IngredientUnit.slice);
        amount.setPackageSize(0.25);
        amount.setPackageUnit(IngredientUnit.inch);
        expected.setRecipeAmount(amount);
        expected.getPreparations().add(Preparation.peeled);
        expected.getFormDescriptors().add(Form.fresh);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void coarselyChoppedLargeOnion()
    {
        String ingString = "1 large onion, coarsely chopped";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("onion");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getUnitSizeDescriptor().add(UnitSize.large);
        expected.getPrepDescriptor().add(PrepDescriptor.coarsely);
        expected.getPreparations().add(Preparation.chopped);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void pepperWithSpaces()
    {
        String ingString = " pepper ";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("pepper");
        IngredientAmount amount = new IngredientAmount();
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void bayLeaves()
    {
        String ingString = "6 bay leaves"; // 'bay leaves' is the ingredient
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("bay leaves");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(6, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void blackberryGarnish()
    {
        String ingString = "Additional fresh blackberries (for garnish)";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("blackberries");
        IngredientAmount amount = new IngredientAmount();
        
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.fresh);
        expected.getOptDescriptor().add(Optionality.additional);
        expected.getOptDescriptor().add(Optionality.garnish);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void blackPepperToTaste()
    {
        String ingString = "Black pepper to taste";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("black pepper");
        IngredientAmount amount = new IngredientAmount();
        expected.setRecipeAmount(amount);
        expected.getOptDescriptor().add(Optionality.totaste);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void chilledCubedButter()
    {
        String ingString = "1/2 cup chilled unsalted butter, cut into 1/2\" cubes";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("unsalted butter");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(0, 1, 2));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);
        expected.getTempDescriptor().add(Temperature.chilled);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void cannedChipotleChiles()
    {
        String ingString = "1 Tbsp canned chipotle chiles in adobo, minced"; 
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("chipotle chiles in adobo");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.tablespoon);
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.canned);
        expected.getPreparations().add(Preparation.minced);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void bottledGroundFreshGinger()
    {
        String ingString = "1 teaspoon bottled ground fresh ginger";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("ground ginger");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.teaspoon);
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.bottled);
        expected.getFormDescriptors().add(Form.fresh);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void mediumTortillas()
    {
        String ingString = "4 8-inch-diameter flour tortillas, warmed";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        // TODO - eventually - be able to parse out this type of size
        expected.setIngredientName("8-inch-diameter flour tortillas");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(4, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getTempDescriptor().add(Temperature.warm);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void oneInchThickHalibut()
    {
        String ingString = "1 1/2 pounds 1-inch-thick halibut fillets";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("1-inch-thick halibut fillets");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 1, 2));
        amount.setUnit(IngredientUnit.pound);
        expected.setRecipeAmount(amount);
        
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void tenderLettuce()
    {
        String ingString = "5 oz. tender lettuces (such as Bibb, butter, and mache; about 6 cups) ";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        // TODO - eventually - be able to parse out this type of size
        expected.setIngredientName("tender lettuces");
        expected.setParensContents("such as Bibb, butter, and mache; about 6 cups");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(5, 0, 0));
        amount.setUnit(IngredientUnit.ounce);
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void thinlySlicedRedOnion()
    {
        String ingString = "1/4 cup very thinly sliced red onion";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        
        expected.setIngredientName("red onion");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(0, 1, 4));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);
        expected.getPreparations().add(Preparation.sliced);
        expected.getPrepDescriptor().add(PrepDescriptor.thinly);
        expected.getPrepDescriptor().add(PrepDescriptor.very);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
        
        ingString = "1/4 cup vertically sliced red onion";
        expected.setOriginalString(ingString);
        expected.getPrepDescriptor().clear();
        expected.getPrepDescriptor().add(PrepDescriptor.vertically);
        ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void lowfatMilk()
    {
        String ingString = "1/2 c 1% lowfat milk";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("1% lowfat milk");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(0, 1, 2));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void tartApples()
    {
        String ingString = "5 small tart apples, peeled, cored & chopped";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("tart apples");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(5, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getUnitSizeDescriptor().add(UnitSize.small);
        expected.getPreparations().add(Preparation.peeled);
        expected.getPreparations().add(Preparation.chopped);
        expected.getPreparations().add(Preparation.cored);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void cloves()
    {
        String ingString = "40 whole cloves";
        
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("cloves");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(40, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
        
        ingString = "40 cloves";
        expected.setOriginalString(ingString);
        ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void softenedCreamCheese()
    {
        String ingString = "2 8-oz pkg cream cheese, softened";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("cream cheese");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(2, 0, 0));
        amount.setUnit(IngredientUnit.pkg);
        amount.setPackageSize(8);
        amount.setPackageUnit(IngredientUnit.ounce);
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.softened);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void looselyPackedParsley()
    {
        String ingString = "1 cup loosely-packed parsley";
        
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("parsley");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);
        expected.getUnitSizeDescriptor().add(UnitSize.looselypacked);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
        
        ingString = "1 cup loosely packed parsley";
        expected.setOriginalString(ingString);
        ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void boiledEggs()
    {
        String ingString = "12 eggs, boiled";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("eggs");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(12, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getTempDescriptor().add(Temperature.boiled);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
        
        ingString = "12 boiled eggs";
        expected.setOriginalString(ingString);
        ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void saltToTaste()
    {
        String ingString = "salt, to taste";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        // TODO - eventually - be able to parse out this type of size
        expected.setIngredientName("salt");
        IngredientAmount amount = new IngredientAmount();
        expected.setRecipeAmount(amount);
        expected.getOptDescriptor().add(Optionality.totaste);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void cannedTomatoes()
    {
        String ingString = "1 28.6-oz can tomatoes, drained";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("tomatoes");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.can);
        amount.setPackageUnit(IngredientUnit.ounce);
        amount.setPackageSize(28.6);
        expected.setRecipeAmount(amount);
        expected.getPreparations().add(Preparation.drained);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void mezcal()
    {
        String ingString = "1/4 cup mezcal (such as Sombra or Del Maguey Vida)";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("mezcal");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(0, 1, 4));
        amount.setUnit(IngredientUnit.cup);
        expected.setParensContents("such as Sombra or Del Maguey Vida");
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void phyllo()
    {
        String ingString = "12 12x9-inch sheets fresh phyllo pastry, or frozen, thawed";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("12x9-inch sheets phyllo pastry");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(12, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.frozen);
        expected.getFormDescriptors().add(Form.fresh);
        expected.getFormDescriptors().add(Form.thawed);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void toastedKaiserRoll()
    {
        String ingString = "toasted Kaiser roll";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("Kaiser roll");
        IngredientAmount amount = new IngredientAmount();
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.toasted);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void choppedAlmonds()
    {
        String ingString = "2 Tbsp. chopped almonds";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("almonds");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(2, 0, 0));
        amount.setUnit(IngredientUnit.tablespoon);
        expected.setRecipeAmount(amount);
        expected.getPreparations().add(Preparation.chopped);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void evoo()
    {
        String ingString = "2 Tbsp. extra-virgin olive oil";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("extra-virgin olive oil");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(2, 0, 0));
        amount.setUnit(IngredientUnit.tablespoon);
        expected.setRecipeAmount(amount);
        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void bottleOfAperol()
    {
        String ingString = "1 750-ml bottle Aperol";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("Aperol");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.bottle);
        amount.setPackageSize(750);
        amount.setPackageUnit(IngredientUnit.milliliter);
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    
    @Test
    public void flour()
    {
        String ingString = "2 1/2 c flour";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("flour");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(2, 1, 2));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void crabWithSizeRange()
    {
        String ingString = "1 2-to-3-pound Dungeness crab";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("2-to-3-pound Dungeness crab");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void orangeZestStrip()
    {
        String ingString = "1 2-by-1/2-inch strip orange zest";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("2-by-1/2-inch orange zest");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.strip);
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void bottledBBQSauce()
    {
        String ingString = "1/2 cup bottled barbecue sauce";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("barbecue sauce");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(0, 1, 2));
        amount.setUnit(IngredientUnit.cup);
        expected.setRecipeAmount(amount);
        expected.getFormDescriptors().add(Form.bottled);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void appleWithColorVariations()
    {
        String ingString = "1 large yellow or green crisp apple, cored and very thinly sliced";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("yellow or green crisp apple");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(1, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getUnitSizeDescriptor().add(UnitSize.large);
        expected.getPreparations().add(Preparation.cored);
        expected.getPreparations().add(Preparation.sliced);
        expected.getPrepDescriptor().add(PrepDescriptor.very);
        expected.getPrepDescriptor().add(PrepDescriptor.thinly);
        

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void freshMintLeavesForGarnish()
    {
        String ingString = "10 fresh mint leaves, for garnish";

        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("mint leaves");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(10, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);
        expected.getOptDescriptor().add(Optionality.garnish);
        expected.getFormDescriptors().add(Form.fresh);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void zestOfOneLemon()
    {
        String ingString = "zest of one lemon";

        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("zest of one lemon");
        IngredientAmount amount = new IngredientAmount();
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
        
        ingString = "zest of lemon";
        expected.setOriginalString(ingString);
        expected.setIngredientName("zest of lemon");
        ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    
    @Test
    public void ingredientIsARecipe()
    {
        String ingString = "1 recipe Caramel Sauce";

        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("caramel sauce");
        IngredientAmount amount = new IngredientAmount();
        amount.setUnit(IngredientUnit.recipe);
        amount.setAmount(new PersistableFraction(1, 0, 0));
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void woodenIcePopSticks()
    {
        String ingString = "8 wooden ice-pop sticks";

        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("wooden ice-pop");
        IngredientAmount amount = new IngredientAmount();
        amount.setUnit(IngredientUnit.stick);
        amount.setAmount(new PersistableFraction(8, 0, 0));
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    @Test
    public void turkishBayLeavesOrCali()
    {
        String ingString = "2 Turkish bay leaves, or 1 California";

        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("Turkish bay leaves");
        IngredientAmount amount = new IngredientAmount();
        amount.setUnit(IngredientUnit.whole);
        amount.setAmount(new PersistableFraction(2, 0, 0));
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void moreTests()
    {
        String ingString = "Dash of freshly ground black pepper";

        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("of ground black pepper");
        IngredientAmount amount = new IngredientAmount();
        amount.setUnit(IngredientUnit.dash);
        expected.setRecipeAmount(amount);
        expected.getPrepDescriptor().add(PrepDescriptor.freshly);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
    
    @Test
    public void testIngredient_bunchChickenThighs()
    {
        String ingString = "12 boneless skinless chicken thighs, (about 2 1/2 pounds total)";
        RecipeIngredient expected = new RecipeIngredient();
        expected.setOriginalString(ingString);
        expected.setIngredientName("boneless skinless chicken thighs");
        expected.setParensContents("about 2 1/2 pounds total");
        IngredientAmount amount = new IngredientAmount();
        amount.setAmount(new PersistableFraction(12, 0, 0));
        amount.setUnit(IngredientUnit.whole);
        expected.setRecipeAmount(amount);

        RecipeIngredient ingredient = RecipeParser.parseIngredientString(ingString);
        recipeEquals(expected, ingredient);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.parse;

import com.da.organizer.recipes.common.IngredientAmount;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import com.da.organizer.recipes.common.exception.IngredientParseException;
import com.da.organizer.recipes.common.exception.UnitNotFoundException;
import com.da.organizer.recipes.common.unit.UnitSize;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author dandrus
 */
public class RecipeParser {

    final static Logger logger = Logger.getLogger(RecipeParser.class);

    /**
     * Parse a list of recipes from a string. Assuming any file reading is done
     * elsewhere, if necessary. Assuming a specific format.
     * -------------------------- 
     * Name: <name> (required) 
     * Description: <description> (optional) 
     * Servings: <num> (optional) 
     * From: <origination> (optional) 
     * Total Time: (in minutes, optional) 
     * Prep Time: (in minutes, optional)
     *
     * Ingredients:
     * <each ingredient on one line>
     * Ingredient Format: [amount] [unit size] [unit] [descriptive name], [extra info]
     *
     * Instructions:
     * <each new line indicates a new instruction>
     *
     *
     * --------------------------
     *
     * @param recipeString
     * @return
     */
    public static List<Recipe> fromString(String recipeString) {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = null;
        String[] lines = recipeString.split("\\r?\\n");
        boolean parsingIngredients = false;
        boolean parsingInstructions = false;
        for (String line : lines) {
            if (line.startsWith("Name:")) {
                if(recipe != null)
                {
                    recipes.add(recipe);
                    logger.info("Parsed Recipe: "+recipe.prettyPrint());
                }
                recipe = new Recipe();
                String name = getValuePastColon(line);
                recipe.setName(name);
            } else if (line.startsWith("Description:")) {
                recipe.setDescription(getValuePastColon(line));
            } else if (line.startsWith("Servings:")) {
                String numServings = getValuePastColon(line);
                try {
                    if (!numServings.isEmpty()) {
                        int servings = Integer.parseInt(numServings);
                        recipe.setNumberOfServings(servings);
                    }
                } catch (NumberFormatException ex) {
                    logger.error("Unable to parse number of servings into number: " + numServings, ex);
                }

            } else if (line.startsWith("From:")) {
                recipe.setOrigination(getValuePastColon(line));
            } else if (line.startsWith("Prep Time:")) {
                String prepTimeString = getValuePastColon(line);

                try {
                    if (!prepTimeString.isEmpty()) {
                        int prepTime = Integer.parseInt(prepTimeString);
                        recipe.setPrepTime(prepTime);
                    }
                } catch (NumberFormatException ex) {
                    logger.error("Unable to parse number of servings into number: " + prepTimeString);
                }
            } else if (line.startsWith("Total Time:")) {

                String totalTimeString = getValuePastColon(line);

                try {
                    if (!totalTimeString.isEmpty()) {
                        int totalTime = Integer.parseInt(getValuePastColon(line));
                        recipe.setTotalTime(totalTime);
                    }
                } catch (NumberFormatException ex) {
                    logger.error("Unable to parse number of servings into number: " + totalTimeString);
                }
            } else if (line.startsWith("Ingredients:")) {
                parsingIngredients = true;
                parsingInstructions = false;
            } else if (line.startsWith("Instructions:")) {
                parsingIngredients = false;
                parsingInstructions = true;
            } else if (parsingIngredients) {
                RecipeIngredient ingredient = parseIngredientString(line);
                if(ingredient != null){
                    recipe.addIngredient(ingredient);
                }
            } else if (parsingInstructions) {
                if(!line.isEmpty())
                {
                    RecipeInstruction instruction = new RecipeInstruction(line);
                    recipe.addInstruction(instruction);
                }
            }
        }

        if(recipe != null)
        {
            recipes.add(recipe);
        }
        return recipes;
    }

    /**
     * This will expect something along the lines of 1/4 tsp vanilla 1 egg 5 T
     * parsley, chopped 1 egg white 1 egg, divided pinch of salt 1/2 package of
     * ranch seasoning
     *
     * so, this has to be rather intelligent... 1) It needs to know how to parse
     * through for units - which may be abbreviated in various ways 2) It need
     * to parse through for amounts, even if it is in words 3) and it should be
     * able to find existing ingredient names
     *
     * Look for things that can help with spell-checking...
     *
     * - parse through the string until you find the last number - everything
     * before that is the amount
     *
     * - then get the next word, till the next space - that's the unit (unless
     * its the last word before a comma, then its the ingredient) - need to add
     * some smarts to my enum - abbreviations to these units and whatnot.
     *
     * - if there's a comma, then everything after that is the ingredient prep.
     * else, everything after the unit is the ingredient name.
     *
     * BUG: Doesn't handle '1 1/2' as the first number. - fixed
     * BUG: Doesn't handle 'Salt' or 'Pepper', you know, 'to taste'
     *
     * [amount (optional)] [ unitSize OR ? (optional)] [unit (optional)] [name (required)][, extra info (optional)]
     * 1 egg, boiled
     * 1 large onion, chopped coarsely
     * 2 16-oz cans tomatoes
     * 1 8-oz pkg cream cheese
     * salt, to taste
     * 5 small apples, peeled, cored & chopped
     * 1/2 c 1% lowfat milk
     * 1 small bunch arugula
     * 
     * @param ingredient
     * @return
     */
    public static RecipeIngredient parseIngredientString(final String ingredientString) {
        if(ingredientString.isEmpty())
        {
            return null;
        }
        
        RecipeIngredient ri = new RecipeIngredient();
        IngredientAmount amt = new IngredientAmount();
        
        
        logger.debug("Parsing '" + ingredientString + "'");
        String toParse = ingredientString;
        int commaIndex = ingredientString.indexOf(",");
        if(commaIndex > 0)
        {
            String prePrep = toParse.substring(commaIndex+1).trim();
            toParse = toParse.substring(0, commaIndex);
            ri.setPrePreparation(prePrep);
        }
        
        String[] split = toParse.split(" ");
        
        Pattern fractionPatter = Pattern.compile("^[0-9/]*$");
        Pattern packagePattern = Pattern.compile("[\\d.]*-\\w*");
        
        String fractionAmount = null;
        
        String restOfString = "";
        for(String part:split)
        {
            boolean nomatch = Boolean.TRUE;
            //get full fraction, if exists, but NO MORE
            Matcher fractionMatcher = fractionPatter.matcher(part);
            Matcher packageMatcher = packagePattern.matcher(part);
            if(fractionMatcher.find())
            {
                if(fractionAmount == null)
                {
                    fractionAmount = part;
                }else{
                    fractionAmount = fractionAmount.concat(" ").concat(part);
                }
            }
            // if the next part is the package size....
            else if(packageMatcher.find())
            {
                String[] packageSplit = part.split("-");
                if(packageSplit.length == 2)
                {
                    String size = packageSplit[0];
                    String unit = packageSplit[1];

                    Double packageSize = null;
                    IngredientUnit packageUnit = null;
                    
                    try{
                        packageSize = Double.valueOf(size);
                        packageUnit = IngredientUnit.getUnit(unit);
                    }catch(NumberFormatException nfe)
                    {
                        logger.warn("this is not a package size: "+part);
                    }catch(UnitNotFoundException unfe)
                    {
                        logger.error("No unit found for package unit: "+unit);
                    }
                    
                    if(packageSize != null && packageUnit != null)
                    {
                        amt.setPackageSize(packageSize);
                        amt.setPackageUnit(packageUnit);
                    }else{
                        restOfString = restOfString.concat(part).concat(" ");
                    }
                    
                    
                }else{
                    logger.error("Unexpected....: "+packageSplit);
                }
            }
            else {
                try{
                    UnitSize unitSize = UnitSize.valueOf(part);
                    amt.setUnitSize(unitSize);
                    nomatch = Boolean.FALSE;
                }catch(IllegalArgumentException iae)
                {
                    logger.warn("no unit size for "+part);
                }
                try{
                    IngredientUnit unit = IngredientUnit.getUnit(part);
                    amt.setUnit(unit);
                    nomatch = Boolean.FALSE;
                }catch(UnitNotFoundException unfe)
                {
                    logger.warn("No unit for "+ part);
                }

                if(nomatch)
                {
                    restOfString = restOfString.concat(part).concat(" ");
                }
            }
            
        }
        if(fractionAmount != null)
        {
            try {
                amt.setAmountFromString(fractionAmount);
            } catch (IngredientParseException ex) {
                logger.warn("Unable to set amount: '"+fractionAmount+"'", ex);
            }
        }

        // IF there is a numerical amount to this amount 
        // && the current ingredient unit is not set, set it to whole
        if( amt.getUnit() == null && amt.getAmount() != null)
        {
            amt.setUnit(IngredientUnit.whole);
        }
        
        ri.setAmount(amt);
        ri.setIngredientName(restOfString.trim());

        StringBuilder b = new StringBuilder();
        b.append("\nOriginal String : '").append(ingredientString).append("'");
        b.append("\nParsed Ingredient: ").append(ri.prettyPrint());
        logger.info(b.toString());
        
        return ri;

    }

    private static String getValuePastColon(String line) {
        int indexOf = line.indexOf(":");
        String subString = line.substring(indexOf + 1);
        return subString.trim();
    }
}

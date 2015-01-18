package com.da.organizer.recipes.common.parse;

import com.da.organizer.recipes.common.IngredientAmount;
import com.da.organizer.recipes.common.PersistableFraction;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.common.descriptors.Descriptor;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import com.da.organizer.recipes.common.exception.IngredientParseException;
import com.da.organizer.recipes.common.exception.UnitNotFoundException;
import static com.da.organizer.recipes.common.parse.RecipeParser.logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author dandrus
 */
public class RecipeParser
{

    final static Logger logger = Logger.getLogger(RecipeParser.class);
    private static final String FRACTION = "FRACTION";

    private static final String PKG_SIZE = "PKG_SIZE";
    private static final String PKG_UNIT = "PKG_UNIT";

    private static final String UNIT = "UNIT";
    private static final String INGREDIENT = "INGREDIENT";

    /**
     * Parse a list of recipes from a string. Assuming any file reading is done
     * elsewhere, if necessary. Assuming a specific format.
     * -------------------------- Name: <name> (required) Description:
     * <description> (optional) Servings: <num> <string> (optional, the first
     * number will be the num servings. ) From: <origination> (optional) Total
     * Time: (in minutes, optional) Prep Time: (in minutes, optional)
     *
     * Ingredients:
     * <each ingredient on one line>
     * Ingredient Format: [amount] [unit size] [unit] [descriptive name], [extra
     * info]
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
    public static List<Recipe> fromString(String recipeString)
    {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = null;
        String[] lines = recipeString.split("\\r?\\n");
        boolean parsingIngredients = false;
        boolean parsingInstructions = false;
        for (String line : lines)
        {
            if (line.startsWith("Name:"))
            {
                if (recipe != null)
                {
                    recipes.add(recipe);
                    logger.info("Parsed Recipe: " + recipe.prettyPrint());
                }
                recipe = new Recipe();
                String name = getValuePastColon(line);
                recipe.setName(name);
            } else if (line.startsWith("Description:"))
            {
                recipe.setDescription(getValuePastColon(line));
            } else if (line.startsWith("Servings:"))
            {
                String numServings = getValuePastColon(line);
                try
                {
                    if (!numServings.isEmpty())
                    {
                        parseServingsString(numServings, recipe);
                    }
                } catch (NumberFormatException ex)
                {
                    logger.error("Unable to parse number of servings into number: " + numServings, ex);
                }

            } else if (line.startsWith("From:"))
            {
                recipe.setOrigination(getValuePastColon(line));
            } else if (line.startsWith("Prep Time:"))
            {
                String prepTimeString = getValuePastColon(line);
                recipe.setPrepTime(prepTimeString);
            } else if (line.startsWith("Total Time:"))
            {
                String totalTimeString = getValuePastColon(line);
                recipe.setTotalTime(totalTimeString);
            } else if (line.startsWith("Ingredients:"))
            {
                parsingIngredients = true;
                parsingInstructions = false;
            } else if (line.startsWith("Instructions:"))
            {
                parsingIngredients = false;
                parsingInstructions = true;
            } else if (parsingIngredients)
            {
                RecipeIngredient ingredient = parseIngredientString(line);
                if (ingredient != null)
                {
                    recipe.addIngredient(ingredient);
                }
            } else if (parsingInstructions)
            {
                if (!line.isEmpty())
                {
                    RecipeInstruction instruction = new RecipeInstruction(line);
                    recipe.addInstruction(instruction);
                }
            }
        }

        if (recipe != null)
        {
            recipes.add(recipe);
        }
        return recipes;
    }

    /**
     * This will set the number of servings and the servings description on the
     * recipe. The description will be the full text of the servings string,
     * while the number of servings will attempt to parse out a numerical value.
     *
     * example string | number of servings | description 4 4 4 4 - 6 4 4 - 6 4-6
     * 4 4-6 4 appetizers 4 4 appetizers 4 mains or 6 sides 4 4 mains or 6 sides
     *
     * @param servingsString
     * @param recipe
     */
    public static void parseServingsString(String servingsString, Recipe recipe)
    {
        // this part is easy
        recipe.setServingsDescription(servingsString);

        // now find the first non-numerical character and take the string before
        // that as the number.
        Pattern numberPattern = Pattern.compile("^[0-9]*$");
        Matcher numberMatcher = numberPattern.matcher(servingsString);
        if (numberMatcher.find())
        {
            String group = numberMatcher.group();
            try
            {
                int servings = Integer.parseInt(group);
                recipe.setNumberOfServings(servings);
            } catch (NumberFormatException ex)
            {
                logger.warn("Unable to parse number of ingredients from " + group, ex);
            }
        }

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
     * BUG: Doesn't handle '1 1/2' as the first number. - fixed BUG: Doesn't
     * handle 'Salt' or 'Pepper', you know, 'to taste'
     *
     * [amount (optional)] [ unitSize OR ? (optional)] [unit (optional)] [name
     * (required)][, extra info (optional)] 1 egg, boiled 1 large onion, chopped
     * coarsely 2 16-oz cans tomatoes 1 8-oz pkg cream cheese salt, to taste 5
     * small apples, peeled, cored & chopped 1/2 c 1% lowfat milk 1 small bunch
     * arugula
     *
     * @param ingredient
     * @return
     */
    public static RecipeIngredient parseIngredientString(final String ingredientString)
    {
        String ingredientStringTrim = ingredientString.trim();
        if (ingredientStringTrim.isEmpty())
        {
            return null;
        }
        
        RecipeIngredient ri = new RecipeIngredient();
        ri.setOriginalString(ingredientString);
        IngredientAmount amt = new IngredientAmount();

        logger.debug("Parsing '" + ingredientStringTrim + "'");
        String toParse = ingredientStringTrim;
        String parensContents = null;

        // find all of the descriptors
        toParse = ParseUtils.getDescriptors(toParse, ri);

        // find parentheses & remove all content including parens.
        Pattern parensPattern = Pattern.compile("\\((.*?)\\)");
        Matcher parensMatcher = parensPattern.matcher(toParse);
        if (parensMatcher.find())
        {
            parensContents = toParse.substring(parensMatcher.start(), parensMatcher.end());
            toParse = parensMatcher.replaceFirst("");
            // we don't need the actual parentheses
            parensContents = parensContents.replace("(", "");
            parensContents = parensContents.replace(")", "");
            ri.setParensContents(parensContents);
        }

        // find everything past a comma, 
        // if it exists and remove from the ingredient string
        if (toParse.contains(","))
        {
            int commaIndex = toParse.indexOf(",");
            toParse = toParse.substring(0, commaIndex);
        }

        // TODO - add a 'substitution' section to the recipe-ingredient
        // but for now, append a " (" + or and everything past it +")"
        // to the end of the preprep string
        // this is a todo - sometimes it's 4 red or green peppers
//        Pattern orPattern = Pattern.compile("\\bor\\b");
//        Matcher orMatcher = orPattern.matcher(toParse);
//        if(orMatcher.find())
//        {
//            substitution = toParse.substring(orMatcher.start());
//            toParse = toParse.substring(0, orMatcher.start());
//        }
        String[] split = toParse.split(" ");

        Map<String, Object> parsedValues = new HashMap<>();
        for (String part : split)
        {
            if (hasFractionOrDecimal(part, parsedValues))
            {
                // found the fraction part
            } else if (hasPackage(part, parsedValues))
            {
                // found the package part
            } else if (hasUnit(part, parsedValues))
            {
                // found the unit
            } else
            {
                String ing = (String) parsedValues.get(INGREDIENT);
                if (ing == null)
                {
                    ing = part;
                } else
                {
                    ing = ing + " " + part;
                }
                parsedValues.put(INGREDIENT, ing);
            }
        } // end of for loop - parsed out all the pieces

        if (parsedValues.containsKey(FRACTION))
        {
            String fractionAmount = (String) parsedValues.get(FRACTION);
            try
            {
                amt.setAmountFromString(fractionAmount);
            } catch (IngredientParseException ex)
            {
                logger.warn("Processing '"+ingredientString+"' - Unable to set amount: '" + fractionAmount + "'", ex);
            }
        }

        if (parsedValues.containsKey(PKG_SIZE))
        {
            Double pkgSize = (Double) parsedValues.get(PKG_SIZE);
            amt.setPackageSize(pkgSize);
        }

        if (parsedValues.containsKey(PKG_UNIT))
        {
            IngredientUnit unit = (IngredientUnit) parsedValues.get(PKG_UNIT);
            amt.setPackageUnit(unit);
        }

        if (parsedValues.containsKey(UNIT))
        {
            IngredientUnit unit = (IngredientUnit) parsedValues.get(UNIT);
            amt.setUnit(unit);
        }

        // IF there is a numerical amount to this amount 
        // && the current ingredient unit is not set, set it to whole
        if (amt.getUnit() == null && amt.getAmount() != null)
        {
            amt.setUnit(IngredientUnit.whole);
        }

        String ing = (String) parsedValues.get(INGREDIENT);
        // get rid of extraneous words and punctuation: or, commas
//        ing = ParseUtils.removeStringIgnoreCase(ing, "or");
        
        // can't quite get rid of 'or' in such a wholesale manner. 
        // I'll end up with 'yellow red peppers' instead of 'yellow or red peppers'
//        ing = ParseUtils.removeStringIgnoreCase(ing, "of");
        ing = ing.replace(",", "");

        logger.info("Ingredient String: " + ing + "\nfrom original string: " + ingredientString);
        ri.setRecipeAmount(amt);
        ri.setIngredientName(ing.trim());

        StringBuilder b = new StringBuilder();
        b.append("\nOriginal String : '").append(ingredientString).append("'");
        b.append("\nParsed Ingredient: ").append(ri.prettyPrint());
        logger.info(b.toString());

        return ri;

    }

    private static boolean hasUnit(String part, Map<String, Object> parsedValues)
    {
        if (!parsedValues.containsKey(UNIT))
        {
            try
            {
                IngredientUnit unit = IngredientUnit.getUnit(part);
                parsedValues.put(UNIT, unit);
                return true;
            } catch (UnitNotFoundException ex)
            {
                logger.debug("No unit found for '"+part+"'");
            }
        }
        return false;
    }

    private static boolean hasFractionOrDecimal(String part, Map<String, Object> parsedValues)
    {
        Pattern fractionPattern = Pattern.compile("^[0-9/\\.]*$");

        String fractionAmount = (String) parsedValues.get(FRACTION);
        Matcher fractionMatcher = fractionPattern.matcher(part);

        if (fractionMatcher.find())
        {
            if (fractionAmount == null)
            {
                fractionAmount = part;
            } else
            {
                fractionAmount = fractionAmount.concat(" ").concat(part);
            }
            parsedValues.put(FRACTION, fractionAmount);
            return true;
        } else
        {
            return false;
        }
    }

    private static boolean hasPackage(String part, Map<String, Object> parsedValues)
    {
        boolean found = false;
        // pattern: [numerical value (decimal OR fraction)]-[unit]
        Pattern packagePattern = Pattern.compile("[0-9/\\.]*-\\w*");
        Matcher packageMatcher = packagePattern.matcher(part);
        if (packageMatcher.find())
        {
            String[] packageSplit = part.split("-");
            if (packageSplit.length == 2)
            {
                String size = packageSplit[0];
                String unit = packageSplit[1];

                Double packageSize = null;
                IngredientUnit packageUnit = null;

                // get the unit
                try
                {
                    packageUnit = IngredientUnit.getUnit(unit);
                } catch (UnitNotFoundException unfe)
                {
                    logger.error("No unit found for package unit: " + unit);
                }
                // try to get the package size as a Double
                try
                {
                    packageSize = Double.valueOf(size);
                } catch (NumberFormatException nfe)
                {
                    logger.warn("this is not a double package size: " + part);
                    // otherwise, try to get the package size as a Fraction
                    try
                    {
                        PersistableFraction fraction = PersistableFraction.fromString(size);
                        packageSize = fraction.asDouble();
                    } catch (IngredientParseException ex)
                    {
                        logger.warn("Unable to set fractional package size: '" + size + "'");
                    }
                }

                if (packageSize != null && packageUnit != null)
                {
                    parsedValues.put(PKG_SIZE, packageSize);
                    parsedValues.put(PKG_UNIT, packageUnit);
                    found = true;
                }
            }
        }
        return found;
    }

    private static String getValuePastColon(String line)
    {
        int indexOf = line.indexOf(":");
        String subString = line.substring(indexOf + 1);
        return subString.trim();
    }
}
/**
 * TODO: * Error #1: 2014-10-29 19:57:38,915 | ERROR | qtp1953167364-58 |
 * RecipeParser | 61 - com.da.organizer.recipes.model - 1.0.0.SNAPSHOT | Unable
 * to parse number of servings into number: 8 servings (serving size: about 1/2
 * cup). java.lang.NumberFormatException: For input string: "8 servings (serving
 * size: about 1/2 cup)." - fixed
 *
 * Error #2 range of amounts: Unable to set amount: '1/4 1/2'
 * com.da.organizer.recipes.common.exception.IngredientParseException: Unable to
 * parse integer amount '1/4' - BAD DATA - fix data
 *
 * Original String : '1/4 c to 1/2 c sugar' - Not supported... * Original String
 * : '1/4 - 3/4 cup Red Chile Powder'
 *
 *
 * Error #3 amount later in the ingredient string... Original String : '3/4 oz
 * fresh pink grapefruit juice (1 1/2 T)' - bad data, expecting a comma
 *
 * Error #4 starting with a space Original String : ' 1/4 c fresh lemon juice,
 * divided' fixed
 *
 * Error #5 Original String : 'Juice of half a lemon' Original String : ' salt'
 *
 * Error #6 Original String : '3/4 cup plus 1 tablespoon lukewarm water
 * (105-115-F)'
 *
 * Error #7 Unable to parse number of servings into number: 6 appetizers
 * java.lang.NumberFormatException: For input string: "6 appetizers" - fixed
 *
 * Error #8 Original String : '1/2 cup plus 2 tablespoons extra virgin olive
 * oil, plus more for brushing' Original String : '1/4 cup plus 2 tablespoons
 * coarsely chopped fresh cilantro leaves, divided' Original String : '1/2 cup
 * plus 2 Tbsp. granulated sugar' Original String : '1/3 cup chopped fresh basil
 * plus 6 whole leaves (for garnish)' Honestly, I'm going to do this via data
 * entry, and just put them on two separate lines
 *
 *
 * Error #9 Original String : '1/4 cup balsamic vinegar reduction or 2
 * tablespoons balsamic vinegar (see Note)' Data entry - either add a comma or
 * put a 'substitute' tage in at the bottom
 *
 * Error #10 - when 'Servings:' has free text instead of a simple number.
 * similar to above, Error #1 Unable to parse number of servings into number:
 * Serving "green stuff" to kids can be a challenge. But a crunchy salad of
 * bite-size vegetables holds plenty of appeal. java.lang.NumberFormatException:
 * For input string: "Serving "green stuff" to kids can be a challenge. But a
 * crunchy salad of bite-size vegetables holds plenty of appeal." fixed
 *
 * Error #11: tricksy!! Original String : '1/3 cup dried currants soaked in 1/2
 * cup hot water (optional)' - data entry, add comma
 *
 * Error #12: Unable to parse number of servings into number: 8 - 10
 * java.lang.NumberFormatException: For input string: "8 - 10" fixed
 *
 *
 * Data Error: Unable to parse number of servings into number: Better Homes &
 * Gardens - May 2008 java.lang.NumberFormatException: For input string: "Better
 * Homes & Gardens - May 2008"
 *
 *
 * Data Error: Original String : '1 1//4 cups heavy whipping cream'
 *
 *
 * Data Error / Error: Original String : '1/2 cup crumbled crisp-fried bacon or
 * 3/4 cup coarsely crumbled chicharron (Mexican crisp-fried pork rind)'
 *
 *
 *
 *
 *
 *
 *
 *
 */

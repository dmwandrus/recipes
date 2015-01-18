/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.unit;


import com.da.organizer.recipes.common.exception.UnitNotFoundException;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Diane
 */
public enum IngredientUnit {
    
    // scientific measurements
    cup("cup", new String[]{"cups", "cup", "c"} ),
    dash("dash", new String[]{"dashes"}),
    drop("drop", new String[]{"drops"}),
    inch("inch", new String[]{"in.", "inches"}),
    pinch("pinch", new String[]{}),
    teaspoon("teaspoon", new String[]{"t", "tsp", "Tsp", "teaspoons"}),
    tablespoon("tablespoon", new String[]{"T", "Tbsp", "tbsp", "Tbs", "tbs", "tablespoons"}),
    
    // liquid measurements
    fluidOunces("fluid ounce", new String[]{"fluid ounces"}),
    gallon("gallon", new String[]{"gal", "gallons"}),
    liter("liter", new String[]{"liters"}),
    milliliter("milliliter", new String[]{"ml", "ml.", "milliliters"}),
    pint("pint", new String[]{"pt", "pints", "pt."}),
    quart("quart", new String[]{"qt", "quarts"}),
    
    // weights
    ounce("ounce", new String[]{"oz", "ounces"}),
    pound("pound", new String[]{"lb", "pounds"}),
    
    // parts of a whole

    //    clove("clove", new String[]{"cloves"}), 
    // There are only a couple things that use clove
    // a whole clove garlic (or whole garlic clove) or a clove, the spice
    // I don't think its important to use it here, and it adds in special logic 
    // to decipher out the clove the spice
    
    half("half", new String[]{}),
    piece("piece", new String[]{"pieces"}),
    slice("slice", new String[]{"slices"}),
//    rack("rack", new String[]{}), // I think this is descriptive of the whole - rack of ribs, and is not a unit on its own
    stick("stick", new String[]{"sticks"}),
    strip("strip", new String[]{"strips"}),
    whole("whole", new String[]{}),

    //organic
    bunch("bunch", new String[]{"bunches"}),
    ear("ear", new String[]{"ears"}),
    head("head", new String[]{"heads"}),
    //leaves("leaves", new String[]{"leaf"}), // this is part of the ingredient name & description
    sprig("sprig", new String[]{"sprigs"}),
    stalk("stalk", new String[]{"stalks"}),
    
    
    // pre-packaged things
    bag("bag", new String[]{"bags"}),
    bottle("bottle", new String[]{"bottles"}),
    box("box", new String[]{"boxes"}), 
    can("can", new String[]{"cans"}), 
    container("container", new String[]{"containers"}),
    envelope("envelope", new String[]{"envelopes"}),
    jar("jar", new String[]{"jars"}), 
    link("link", new String[]{"links"}),
    pkg("package", new String[]{"pkg", "packages"}),
    tube("tube", new String[]{}),
    
    // the point of it all
    recipe("recipe", new String[]{}),
    ;

    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    IngredientUnit(String fullText, String[] altTexts)
    {
        this.fullText = fullText;
        this.altTexts = Arrays.asList(altTexts);

    }
    public String getFullText()
    {
        return fullText;
    }
    public Collection<String> getAltTexts()
    {
        return altTexts;
    }

    
    public static IngredientUnit getUnit(final String text) throws UnitNotFoundException
    {
        // special case : 'T'
        // this is case sensitive
        if(text.equals("T"))
        {
            return IngredientUnit.tablespoon;
        }
        String textCopy = text.trim();
        textCopy = textCopy.toLowerCase();
        IngredientUnit[] values = IngredientUnit.values();
        for(int i=0; i<values.length; i++)
        {
            IngredientUnit unit = values[i];
            if(unit.getFullText().equals(textCopy))
            {
                return unit;
            }
            int indexOf = textCopy.indexOf(".");
            if(indexOf != -1)
            {
                textCopy = textCopy.substring(0,indexOf);
            }
            for(String altText:unit.getAltTexts())
            {
                if(altText.equals(textCopy))
                {
                    return unit;
                }
            }
        }
        throw new UnitNotFoundException("Unable to find unit for: "+text);
    }
}

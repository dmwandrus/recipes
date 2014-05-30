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
    
    cup("cup", new String[]{"cups", "cup", "c"} ),
    teaspoon("teaspoon", new String[]{"t", "tsp", "Tsp"}),
    tablespoon("tablespoon", new String[]{"T", "Tbsp", "tbsp", "Tbs", "tbs"}),
    whole("whole", new String[]{}),
    half("half", new String[]{}),
    ounce("ounce", new String[]{"oz", "ounces"}),
    pound("pound", new String[]{"lb", "pounds"}),
    slice("slice", new String[]{"slices"}),
    pinch("pinch", new String[]{}),
    pkg("package", new String[]{"pkg", "packages"}),
    rack("rack", new String[]{}),
    quart("quart", new String[]{"qt", "quarts"}),
    pint("pint", new String[]{"pt", "pints"}),
    gallon("gallon", new String[]{"gal", "gallons"}),
    
    stick("stick", new String[]{"stick", "sticks"}),
    clove("clove", new String[]{"cloves"}),
    sprig("sprig", new String[]{}),
    inch("inch", new String[]{"in", "inches"}),
    head("head", new String[]{"heads"}),
    leaves("leaves", new String[]{"leaf"}),
    can("can", new String[]{"cans"}), 
    recipe("recipe", new String[]{}),
    
    
    bunch("bunch", new String[]{"bunches"})
    // fluid ounces?
    
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

    
    public static IngredientUnit getUnit(String text) throws UnitNotFoundException
    {
        text = text.trim();
        IngredientUnit[] values = IngredientUnit.values();
        for(int i=0; i<values.length; i++)
        {
            IngredientUnit unit = values[i];
            if(unit.getFullText().equals(text))
            {
                return unit;
            }
            int indexOf = text.indexOf(".");
            if(indexOf != -1)
            {
                text = text.substring(0,indexOf);
            }
            for(String altText:unit.getAltTexts())
            {
                if(altText.equals(text))
                {
                    return unit;
                }
            }
        }
        throw new UnitNotFoundException("Unable to find unit for: "+text);
    }
}

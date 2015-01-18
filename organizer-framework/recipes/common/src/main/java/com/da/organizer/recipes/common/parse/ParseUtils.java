/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.parse;

import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.descriptors.Descriptor;
import com.da.organizer.recipes.common.descriptors.Form;
import com.da.organizer.recipes.common.descriptors.Optionality;
import com.da.organizer.recipes.common.descriptors.PrepDescriptor;
import com.da.organizer.recipes.common.descriptors.Preparation;
import com.da.organizer.recipes.common.descriptors.Temperature;
import com.da.organizer.recipes.common.descriptors.UnitSize;
import com.da.organizer.recipes.common.exception.DescriptorNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author diane
 */
public class ParseUtils {
    
    final static Logger logger = Logger.getLogger(ParseUtils.class);
    
    public static <E extends Descriptor<E>> E getDescriptor(final String text, E  descriptor) throws DescriptorNotFoundException
    {

        String textCopy = text.trim();
        textCopy = textCopy.toLowerCase();
        Collection<E> values = descriptor.getValues();
        for(E d:values)
        {
            if(d.getFullText().equalsIgnoreCase(textCopy))
            {
                return d;
            }

            for(String altText:d.getAltTexts())
            {
                if(altText.equals(textCopy))
                {
                    return d;
                }
            }
        }
        throw new DescriptorNotFoundException("Unable to find descriptor for: "+text + " in descriptor type: "+descriptor.getClass().getName());
    }
    
    /**
     * When this returns, the returned ingredient string will be trimmed 
     * of all descriptor content and the set of descriptors will be full
     * of all appropriate values. 
     * @param ingredient
     * @param descriptors
     * @return 
     */
    public static String getDescriptors(String ingredient, RecipeIngredient ri)
    {
        
        // okay, so I don't like it so much...
        // but its going to work for now. I need to send an INSTANCE of the 
        // Descriptor in for this to work. Though, seems like Java8 has
        // static interface methods, that might work too. But, for now...continue
        // my next goal is to get the parser to work. 
        Set<Optionality> descriptors = new HashSet<>();
        ingredient = getDescriptors(ingredient, Optionality.optional, descriptors);
        ri.setOptDescriptor(descriptors);
        
        Set<Form> forms = new HashSet<>();
        ingredient = getDescriptors(ingredient, Form.canned, forms);
        ri.setFormDescriptors(forms);
        
        Set<PrepDescriptor> prepDesc = new HashSet<>();
        ingredient = getDescriptors(ingredient, PrepDescriptor.coarsely, prepDesc);
        ri.setPrepDescriptor(prepDesc);
        
        Set<Preparation> prep = new HashSet<>();
        ingredient = getDescriptors(ingredient, Preparation.chopped, prep);
        ri.setPreparations(prep);
        
        Set<Temperature> temp = new HashSet<>();
        ingredient = getDescriptors(ingredient, Temperature.boiled, temp);
        ri.setTempDescriptor(temp);
        
        Set<UnitSize> size = new HashSet<>();
        ingredient = getDescriptors(ingredient, UnitSize.generous, size);
        ri.setUnitSizeDescriptor(size);
                
        return ingredient;
    }
    
    private static <E extends Descriptor<E>> String getDescriptors(String ingredient, E descriptorType, Set<E> descriptors )
    {
     
        // do multi-word matches first
        Collection<E> multiwordValues = descriptorType.getMultiWordDescriptors();
        for(E descriptor:multiwordValues)
        {
            if(ParseUtils.containsStringIgnoreCase(ingredient, descriptor.getFullText()))
            {
                descriptors.add(descriptor);
                ingredient = removeStringIgnoreCase(ingredient, descriptor.getFullText());
            }
            Collection<String> altTexts = descriptor.getAltTexts();
            for(String altValue:altTexts)
            {
                if(ParseUtils.containsStringIgnoreCase(ingredient, altValue))
                {
                    descriptors.add(descriptor);
                    ingredient = removeStringIgnoreCase(ingredient, altValue);
                }
            }
        }
        // then do the rest
        Collection<E> values = descriptorType.getValues();
        for(E descriptor:values)
        {
            if(ParseUtils.containsStringIgnoreCase(ingredient, descriptor.getFullText()))
            {
                descriptors.add(descriptor);
                ingredient = removeStringIgnoreCase(ingredient, descriptor.getFullText());
            }
            Collection<String> altTexts = descriptor.getAltTexts();
            for(String altValue:altTexts)
            {
                if(ParseUtils.containsStringIgnoreCase(ingredient, altValue))
                {
                    descriptors.add(descriptor);
                    ingredient = removeStringIgnoreCase(ingredient, altValue);
                }
            }
        }
        
        return ingredient;
    }
    
    
    
    public static boolean containsStringIgnoreCase(String originalString, String toRemove)
    {
        // \b - word boundary in java regex - this is 0-9a-zA-Z_
        // it does not include the dash. so, if a work comes off of a dash, \\b 
        // is going to find it, even if I don't want it to.
        
        // \s - whitespace type of character
        // Pattern.CASE_INSENSITIVE
        // ^ beginning of a line
        // $ end of a line
        // ^\\w - Not a word character (0-9a-zA-Z_)
        String boundary ="\\b";
        Pattern pattern = Pattern.compile(boundary+toRemove+boundary, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(originalString);
        if(matcher.find())
        {
            int startIndex = matcher.start(); // the first character of the match
            int endIndex = matcher.end(); // the character just after the end of the match

            // Now, just making sure that the character BEFORE the match is NOT a dash
            if(startIndex > 0 && (originalString.charAt(startIndex -1) == '-'))
            {
                return false;
            }
            // and the character AFTER the match is not a dash
            if(endIndex < originalString.length() && originalString.charAt(endIndex) == '-')
            {
                return false;
            }
            
            return true;
        }
        
        return false;
    }
    
    
    // the 'toRemove' String must be in word boundary
    public static String removeStringIgnoreCase(String originalString, String toRemove)
    {
        // \b - word boundary in java regex
        // Pattern.CASE_INSENSITIVE
        Pattern pattern = Pattern.compile("\\b"+toRemove+"\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(originalString);
        if(matcher.find())
        {
            String replaced = matcher.replaceFirst("");
            return StringUtils.normalizeSpace(replaced);
        }
        
        return originalString;
    }
    
}

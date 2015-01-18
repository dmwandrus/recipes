/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.descriptors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author diane
 */
public enum PrepDescriptor implements Descriptor<PrepDescriptor> {
    freshly("freshly", new String[]{}),
    vertically("vertically", new String[]{}),
    horizontally("horizontally", new String[]{}),
    finely("finely", new String[]{}),
    coarsely("coarsely", new String[]{}),
    thinly("thinly", new String[]{}),
    very("very", new String[]{})
    
    ;
    
    
    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    PrepDescriptor(String fullText, String[] altTexts)
    {
        this.fullText = fullText;
        this.altTexts = Arrays.asList(altTexts);

    }
    @Override
    public String getFullText()
    {
        return fullText;
    }
    @Override
    public Collection<String> getAltTexts()
    {
        return altTexts;
    }

    @Override
    public Collection<PrepDescriptor> getValues() {
        return Arrays.asList(PrepDescriptor.values());
    }

    @Override
    public Collection<PrepDescriptor> getMultiWordDescriptors() {
        return Collections.EMPTY_SET;
    }
    
}

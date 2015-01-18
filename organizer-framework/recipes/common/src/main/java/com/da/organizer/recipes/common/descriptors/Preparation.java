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
public enum Preparation implements Descriptor<Preparation> {
    torn("torn", new String[]{}),
    trimmed("trimmed", new String[]{}),
    crushed("crushed", new String[]{}),
    cubed("cubed", new String[]{}),
    cored("cored", new String[]{}),
    grated("grated", new String[]{}),
    chopped("chopped", new String[]{}),
    minced("minced", new String[]{}),
    peeled("peeled", new String[]{}),
    shaved("shaved", new String[]{}),
    squeezed("squeezed", new String[]{}),
    mashed("mashed", new String[]{}),
    shredded("shredded", new String[]{}),
    sliced("sliced", new String[]{}),
    diced("diced", new String[]{}),
    drained("drained", new String[]{}),
    crumbled("crumbled", new String[]{}),

    ;
    
    
    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    Preparation(String fullText, String[] altTexts)
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
    public Collection<Preparation> getValues() {
        return Arrays.asList(Preparation.values());
    }

    @Override
    public Collection<Preparation> getMultiWordDescriptors() {
        return Collections.EMPTY_SET;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.descriptors;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author diane
 */
public enum UnitSize implements Descriptor<UnitSize> {
    small("small", new String[]{"sm", "sm."}),
    medium("medium", new String[]{"med", "med."}),
    large("large", new String[]{"lg", "lg."}),
    thick("thick", new String[]{}),
    thin("thin", new String[]{}),
    scant("scant", new String[]{}),
    generous("generous", new String[]{}),
    looselypacked("loosely-packed", new String[]{"loosely packed", "lightly packed", "lightly-packed"}),
    tightlypacked("tightly-packed", new String[]{"tightly packed"}),
    packed("packed", new String[]{}),

    ;
    
    
    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    UnitSize(String fullText, String[] altTexts)
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
    public Collection<UnitSize> getValues() {
        return Arrays.asList(UnitSize.values());
    }

    @Override
    public Collection<UnitSize> getMultiWordDescriptors() {
        Collection<UnitSize> descriptors = new HashSet<UnitSize>();
        descriptors.add(looselypacked);
        descriptors.add(tightlypacked);
                
        return descriptors;
    }
}
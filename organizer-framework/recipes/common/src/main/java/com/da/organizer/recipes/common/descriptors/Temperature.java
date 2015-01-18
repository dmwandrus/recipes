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
public enum Temperature implements Descriptor<Temperature> {
    hot("hot", new String[]{}),
    cooled("cooled", new String[]{}),
    cooked("cooked", new String[]{}),
    cold("cold", new String[]{}),
    chilled("chilled", new String[]{}),
    iced("iced", new String[]{}),
    boiled("boiled", new String[]{}),
    boiling("boiling", new String[]{}),
    refrigerated("refrigerated", new String[]{}),
    roomtemp("room temperature", new String[]{}),
    warm("warm", new String[]{"warmed"}),

    ;
    
    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    Temperature(String fullText, String[] altTexts)
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
    public Collection<Temperature> getValues() {
        return Arrays.asList(Temperature.values());
    }

    @Override
    public Collection<Temperature> getMultiWordDescriptors() {
        Collection<Temperature> mwd = new HashSet<Temperature>();
        mwd.add(roomtemp);
        return mwd;
    }
    
}

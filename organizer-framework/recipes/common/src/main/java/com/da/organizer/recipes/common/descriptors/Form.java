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
public enum Form implements Descriptor<Form> {
    fresh("fresh", new String[]{}),
    frozen("frozen", new String[]{}),
    canned("canned", new String[]{}),
    preserved("preserved", new String[]{}),
    dried("dried", new String[]{}),
    prepared("prepared", new String[]{}),
    ripe("ripe", new String[]{}),
    firm("firm", new String[]{}),
    softened("softened", new String[]{}),
    thawed("thawed", new String[]{}),
    melted("melted", new String[]{}),
    toasted("toasted", new String[]{}),
    cooked("cooked", new String[]{}),
    raw("raw", new String[]{}),
    soaked("soaked", new String[]{}),
    bottled("bottled", new String[]{}),
    ;
    
    
    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    Form(String fullText, String[] altTexts)
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
    public Collection<Form> getValues() {
        return Arrays.asList(Form.values());
    }

    @Override
    public Collection<Form> getMultiWordDescriptors() {
        return Collections.EMPTY_SET;
    }
    
}

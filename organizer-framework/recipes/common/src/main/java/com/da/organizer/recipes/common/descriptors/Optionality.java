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
public enum Optionality implements Descriptor<Optionality> {
    garnish("as garnish", new String[]{"for garnish", "garnish", "garnished"}),
    desired("if desired", new String[]{"as desired"}),
    optional("optional", new String[]{"opt", "opt.", "optionally"}),
    additional("additional", new String[]{"additionally"}),
    extra("extra", new String[]{}),
    needed("as needed", new String[]{"if needed"}),
    assorted("assorted", new String[]{}),
    divided("divided", new String[]{}),
    totaste("to taste", new String[]{})
    ;
    
    
    String fullText;
    //case sensitive alternative texts/abbreviations
    // and abbreviations can be w/ or w/o periods
    Collection<String> altTexts;
    Optionality(String fullText, String[] altTexts)
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
    public Collection<Optionality> getValues() {
        return Arrays.asList(Optionality.values());
    }

    @Override
    public Collection<Optionality> getMultiWordDescriptors() {
        Collection<Optionality> descriptors = new HashSet<Optionality>();
        descriptors.add(Optionality.garnish);
        descriptors.add(Optionality.desired);
        descriptors.add(Optionality.needed);
        descriptors.add(Optionality.totaste);
        return descriptors;
    }
    
}

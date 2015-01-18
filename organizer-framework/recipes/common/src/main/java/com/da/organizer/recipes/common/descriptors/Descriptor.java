/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.descriptors;

import java.util.Collection;

/**
 *
 * @author diane
 */
public interface Descriptor<E> {

    public String getFullText();
    public Collection<E> getMultiWordDescriptors();
    public Collection<String> getAltTexts();
    public Collection<E> getValues();
}

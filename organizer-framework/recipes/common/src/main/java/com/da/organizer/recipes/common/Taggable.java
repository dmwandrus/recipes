/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

/**
 *
 * @author dandrus
 */
public interface Taggable <T extends Persistable> {
    
    public void addTag(Tag tag);
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import java.util.Set;

/**
 *
 * @author dandrus
 */
public interface Taggable <T extends Persistable> {
    
    public void addTag(Taggable<T> taggable, String tag);
    public void addTags(Taggable<T> taggable, Set<String> tags);
    
}

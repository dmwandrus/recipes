/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.dataprovider;

import com.da.organizer.recipes.common.Recipe;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 *
 * @author diane
 */
public class RecipeDataProvider implements IDataProvider<Recipe>{

    private List<Recipe> recipes;
    
    public RecipeDataProvider(List<Recipe> recipesToShow)
    {
        this.recipes = recipesToShow;
    }
    
    @Override
    public Iterator<? extends Recipe> iterator(long first, long count) {
        int startIndex = (int) first;
        int endIndex = startIndex + (int) count;
        return recipes.subList(startIndex, endIndex).iterator();
    }

    @Override
    public long size() {
        return recipes.size();
    }

    @Override
    public IModel<Recipe> model(Recipe recipe) {
        return new RecipeModel(recipe);
    }

    @Override
    public void detach() {
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.dataprovider;

import com.da.organizer.recipes.common.Recipe;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 *
 * @author diane
 */
public class RecipeModel extends LoadableDetachableModel<Recipe>{

    private final Recipe recipe;
    
    public RecipeModel(Recipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    protected Recipe load() {
        return recipe;
    }
    
    
    
}

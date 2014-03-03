/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.service;

import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.Recipe;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dandrus
 */
public interface RecipeService {
    Long addRecipe(Recipe recipe);
    Long addIngredient(Ingredient ingredient);
    
    List<Recipe> retrieveRecipes();
    Recipe retrieveRecipe(Long id);
    
    List<Ingredient> retrieveIngredients();
    Ingredient retrieveIngredient(Long id);
    Collection<String> retrieveIngredientNames();
    Ingredient retrieveIngredientByName(String name);
    
    boolean removeRecipe(Long id);
    boolean removeIngredient(Long id);
}

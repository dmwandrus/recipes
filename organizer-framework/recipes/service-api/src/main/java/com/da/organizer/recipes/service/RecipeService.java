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
 * Basic functions for accessing a recipe & ingredient.
 * Todo: In time, this may be broken up into a Recipe Service and an Ingredient
 * Service, as things grow. Recipe service would be dependent on the ingredient 
 * service, but ingredient service doesn't care about recipes. And then there 
 * could be a nutrition service & a tag service &&& :)
 * 
 * 
 * 
 * @author dandrus
 */
public interface RecipeService {
    /**
     * This persists a new recipe. 
     * It does not persist ingredients in that recipe.
     * @param recipe
     * @return recipe Id
     */
    Long addRecipe(Recipe recipe);
    void updateRecipe(Recipe recipe);
    Long addIngredient(Ingredient ingredient);
    
    List<Recipe> retrieveRecipes();
    Recipe retrieveRecipe(Long id);
    
    List<Ingredient> retrieveIngredients();
    Ingredient retrieveIngredient(Long id);
    Collection<String> retrieveIngredientNames();
    Ingredient retrieveIngredientByName(String name);
    
    boolean removeRecipe(Long id);
    boolean removeIngredient(Long id);
    
    /**
     * This will look for the 'thing' in the name, description, instructions
     * AND ingredients in a recipe and return all recipes that mention 
     * the 'thing'
     * @param thing search term
     * @return List of recipes
     */
    List<Recipe> simpleSearch(String thing);
    
    public void runProcessors();
}

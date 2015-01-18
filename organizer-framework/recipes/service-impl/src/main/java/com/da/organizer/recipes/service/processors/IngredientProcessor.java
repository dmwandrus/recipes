package com.da.organizer.recipes.service.processors;

import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.service.impl.SimpleRecipeService;
import org.apache.log4j.Logger;

/**
 * Okay, so this guy - this will get a recipe, and see if it can find it in the database. 
 * If not, it will see if it can find something like it...?
 * If not again, it will persist the new ingredient. The ingredient will be set to "needs review = true"
 * If it finds a match,
 * 
 * @author diane
 */
public class IngredientProcessor {
    
    private RecipeService recipeService;
    final static Logger logger = Logger.getLogger(IngredientProcessor.class);
    
    public void setRecipeService(SimpleRecipeService service)
    {
        this.recipeService = service;
    }
    
    public void processRecipes(String process)
    {
        for(Recipe recipe:recipeService.retrieveRecipes())
        {
            processRecipe(recipe);
        }
    }
    
    private void processRecipe(Recipe recipe)
    {
        logger.info("Processing "+recipe.getName());
        boolean hasChanged = false;
        for(RecipeIngredient ri:recipe.getIngredients())
        {
            if(ri.getIngredient() == null)
            {
                String ingredientName = ri.getIngredientName();
                Ingredient retrievedIngredient = recipeService.retrieveIngredientByName(ingredientName);
                logger.info("Looking for '"+ingredientName+"'");
                
                if(retrievedIngredient == null)
                {
                    logger.info("No existing ingredient found, saving new ingredient");
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(ingredientName);
                    recipeService.addIngredient(ingredient);
                    retrievedIngredient = ingredient;
                }
                logger.info("Updating recipe: "+retrievedIngredient);
                ri.setIngredient(retrievedIngredient);
                hasChanged = true;

            }
        }
        if(hasChanged)
        {
            recipeService.updateRecipe(recipe);
        }
    }
    
    
}

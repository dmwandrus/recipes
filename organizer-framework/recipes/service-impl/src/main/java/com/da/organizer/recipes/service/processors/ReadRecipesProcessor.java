/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.service.processors;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.parse.RecipeParser;
import com.da.organizer.recipes.service.RecipeService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author diane
 */
public class ReadRecipesProcessor
{

    final static Logger logger = Logger.getLogger(ReadRecipesProcessor.class);
    private RecipeService recipeService;

    public void setRecipeService(RecipeService recipeService)
    {
        this.recipeService = recipeService;
    }

    public void process(String recipeString) throws Exception
    {
        logger.info("Processing Recipe String");

        List<Recipe> recipes = RecipeParser.fromString(recipeString);
        logger.info("...Finished parsing");

        List<Recipe> unableToSave = new ArrayList<>();
        for (Recipe recipe : recipes)
        {
            try
            {
                recipeService.addRecipe(recipe);
            } catch (Exception ex)
            {
                logger.error("...Unable to save Recipe: " + recipe.getName(), ex);
                unableToSave.add(recipe);
            }
        }
        if (!unableToSave.isEmpty())
        {
            StringBuilder b = new StringBuilder();
            b.append("...Unable to save the following recipes: ");
            for (Recipe recipe : unableToSave)
            {
                b.append("\n\t\t" + recipe.getName());
            }
            logger.error(b.toString());
        }

        logger.info("Finished processing");
    }
}

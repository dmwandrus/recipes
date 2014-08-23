/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.views;

import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.previews.RecipePreview;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author dandrus
 */
public class RecipeView extends Panel{

    @Inject 
    @Named("RecipeService")
    private RecipeService myRecipeService;
    
    private Recipe myRecipe;
    
    public RecipeView(String id, Recipe recipe)
    {
        super(id);
        this.myRecipe = recipe;
        
        Label recipeTitle = new Label("recipeTitle", new PropertyModel(myRecipe, "name"));
        Label recipeDescription = new Label("recipeDescription", new PropertyModel(myRecipe, "description"));
        Label recipeOrigin = new Label("recipeOrigin", new PropertyModel(myRecipe, "origination"));
        Label recipeNumServings = new Label("numberOfServings", new PropertyModel(myRecipe, "numberOfServings"));
        Label recipePrepTime = new Label("prepTime", new PropertyModel(myRecipe, "prepTime"));
        Label recipeTotalTime = new Label("totalTime", new PropertyModel(myRecipe, "totalTime"));
        
        
        add(recipeTitle);
        add(recipeDescription);
        add(recipeOrigin);
        add(recipePrepTime);
        add(recipeTotalTime);
        add(recipeNumServings);
        
        add(new ListView<RecipeIngredient>("ingredientsList", myRecipe.getIngredients()){
            @Override
            protected void populateItem(ListItem<RecipeIngredient> item)
            {
                RecipeIngredient ri = (RecipeIngredient) item.getDefaultModel().getObject();
                if(ri.getRecipeAmount() != null)
                {
                    String amount = ri.getRecipeAmount().prettyPrint();
                    item.add(new Label("amountLabel", amount));
                }else{
                    item.add(new Label("amountLabel", ""));
                }
                String ingredientName = ri.getIngredientName();
                item.add(new Label("ingredient", new PropertyModel(ri, "ingredientName")));
                
//                Add link to ingredient
//                Ingredient retrievedIngredient = myRecipeService.retrieveIngredientByName(ingredientName);
//                if(myRecipeService.retrieveIngredientByName(ingredientName))
//                {
//                    
//                }
                
                if(ri.getPrePreparation() != null)
                {
                    String prePrep = ri.getPrePreparation();
                    item.add(new Label("prePrep", new PropertyModel(ri, "prePreparation")));
                }else{
                    item.add(new Label("prePrep", ""));
                }
            }
        });
        
        add(new ListView<RecipeInstruction>("instructionsList", myRecipe.getInstructions()){
            @Override
            protected void populateItem(ListItem<RecipeInstruction> item)
            {
                RecipeInstruction ri = (RecipeInstruction) item.getDefaultModel().getObject();
                
                item.add(new Label("instructionText", ri.getInstructionText()));
            }
        });
        
    }
    
}

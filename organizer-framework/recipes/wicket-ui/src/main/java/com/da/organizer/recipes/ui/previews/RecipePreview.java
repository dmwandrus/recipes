/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.previews;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.ui.RecipesPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author dandrus
 */
public class RecipePreview  extends Panel{
    
    private Recipe myRecipe;
    
    public RecipePreview(String id, Recipe recipe)
    {
        super(id);
        this.myRecipe = recipe;
        
        Label recipeTitle = new Label("recipeTitle", new PropertyModel(myRecipe, "name"));
        Label recipeDescription = new Label("recipeDescription", new PropertyModel(myRecipe, "description"));
        Label recipeOrigin = new Label("recipeOrigin", new PropertyModel(myRecipe, "origination"));
        Label recipeNumServings = new Label("numberOfServings", new PropertyModel(myRecipe, "numberOfServings"));
        Label recipePrepTime = new Label("prepTime", new PropertyModel(myRecipe, "prepTime"));
        Label recipeTotalTime = new Label("totalTime", new PropertyModel(myRecipe, "totalTime"));
        
        
        Link recipePageLink = new Link("recipeLink"){
            @Override
                    public void onClick() {
                        RecipesPage rp = new RecipesPage(myRecipe);
                        setResponsePage(rp);
                }
        };
        recipePageLink.setBody(new PropertyModel(myRecipe, "name"));
        
        add(recipePageLink);
//        add(recipeTitle).add(recipePageLink);
        add(recipeDescription);
        add(recipeOrigin);
        add(recipePrepTime);
        add(recipeTotalTime);
        add(recipeNumServings);
    }
    
    
}

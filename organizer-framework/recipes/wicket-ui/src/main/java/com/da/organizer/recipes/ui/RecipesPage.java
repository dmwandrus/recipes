/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.views.RecipeView;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;


/**
 *
 * @author dandrus
 */
public class RecipesPage extends StandardPage{
    
    final static Logger logger = Logger.getLogger(RecipesPage.class);
    
    @Inject 
    @Named("RecipeService")
    private RecipeService myRecipeService;
    
    private List<Recipe> allRecipes;
    Random random = new Random();
    private Recipe myRecipe;
    
    public RecipesPage()
    {
        super();
        // obviously, this will fail when I actually get a large number 
        // of recipes, and I will need a server side method to 
        // get random recipe or get featured recipe....but I'm just 
        // not there yet. and by 'fail' I mean that it will not perform.
        allRecipes = myRecipeService.retrieveRecipes();
        
        logger.info("Found "+allRecipes.size()+" recipes.");
        if(!allRecipes.isEmpty())
        {
            int randomNumber = random.nextInt(allRecipes.size()-1);
            logger.info("Getting recipe #"+randomNumber+" from list");
            myRecipe = allRecipes.get(randomNumber);
            add(new RecipeView("recipeView", myRecipe));
        }
        else{
            add(new Label("recipeView", "No recipes to display"));
        }
        
        
        
        add(new BookmarkablePageLink("newRecipeLink", AddNewRecipePage.class));
        Link recipePageLink = new Link("editRecipeLink"){
            @Override
                    public void onClick() {
                        EditableRecipePage rp = new EditableRecipePage(myRecipe);
                        setResponsePage(rp);
                }
        };
        add(recipePageLink);
    }
    
    public RecipesPage(Recipe recipe)
    {
        super();
        myRecipe = recipe;
        add(new RecipeView("recipeView", myRecipe));
        add(new BookmarkablePageLink("newRecipeLink", AddNewRecipePage.class));
        
        
        Link recipePageLink = new Link("editRecipeLink"){
            @Override
                    public void onClick() {
                        EditableRecipePage rp = new EditableRecipePage(myRecipe);
                        setResponsePage(rp);
                }
        };
        add(recipePageLink);
        
    }
    
    
    
    
}

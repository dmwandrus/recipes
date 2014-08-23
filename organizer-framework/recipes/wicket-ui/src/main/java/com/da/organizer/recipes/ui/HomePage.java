/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.previews.RecipePreview;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

/**
 *
 * @author dandrus
 */
/**
 * Very simple page providing entry points into various other examples.
 */
public class HomePage extends StandardPage {

    private static final long serialVersionUID = 1L;

    
    
    @Inject 
    @Named("RecipeService")
    private RecipeService myRecipeService;
    
    private List<Recipe> allRecipes;
    
    public HomePage() {
        super();
        
        allRecipes = myRecipeService.retrieveRecipes();
        add(new ListView<Recipe>("allRecipes", allRecipes) {

            @Override
            protected void populateItem(ListItem<Recipe> item) {
                Recipe recipe = (Recipe) item.getDefaultModel().getObject();
                item.add(new RecipePreview("recipePreview", recipe));
            }
        });
                
        
    }
}
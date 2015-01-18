/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.dataprovider.RecipeDataProvider;
import com.da.organizer.recipes.ui.forms.SearchForm;
import com.da.organizer.recipes.ui.previews.RecipePreview;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

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
    
    private List<Recipe> recipesToView;
    
    public HomePage() {
        super();
        recipesToView = myRecipeService.retrieveRecipes();
        init();
    }
    
    public HomePage(List<Recipe> recipes) {
        super();
        recipesToView = recipes;
        init();
        
    }
    private void init()
    {
       
        DataView<Recipe> recipeView = new DataView<Recipe>("pageable", new RecipeDataProvider(recipesToView)) {
            
            @Override
            protected void populateItem(Item<Recipe> item) {
                Recipe recipe = item.getModelObject();
                item.add(new RecipePreview("recipePreview", recipe));
            }
        };

        recipeView.setItemsPerPage(10);
        add(recipeView);

        add(new PagingNavigator("navigator", recipeView));
        
        
    }
}
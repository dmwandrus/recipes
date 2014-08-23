/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.partials;

import com.da.organizer.recipes.ui.HomePage;
import com.da.organizer.recipes.ui.RecipesPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author dandrus
 */
public class HeaderPanel extends Panel 
{
    public HeaderPanel(String id)
    {
        super(id);
        
        
        add(new Label("title", "My Recipes! YUM!!"));
        add(new BookmarkablePageLink("home-link", HomePage.class));
//        add(new Label("home-link", "Home")); 
        add(new Label("ingredients-link", "Ingredients"));
        add(new BookmarkablePageLink("recipes-link", RecipesPage.class));
//        add(new Label("recipes-link", "Recipes"));
        add(new Label("meals-link", "Meals"));
        add(new Label("history-link", "History"));
        add(new Label("blog-link", "Blog"));
    }
    
    
}

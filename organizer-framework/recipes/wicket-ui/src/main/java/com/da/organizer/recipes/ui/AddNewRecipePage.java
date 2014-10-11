/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import com.da.organizer.recipes.ui.forms.AddRecipeForm;

/**
 *
 * @author dandrus
 */
public class AddNewRecipePage extends StandardPage{
    
    public AddNewRecipePage()
    {
        add(new AddRecipeForm("recipeForm"));
        
    }
}

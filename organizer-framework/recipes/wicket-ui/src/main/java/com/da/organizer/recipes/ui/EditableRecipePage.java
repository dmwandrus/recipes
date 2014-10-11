/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.ui.forms.EditableRecipeForm;

/**
 *
 * @author dandrus
 */
public class EditableRecipePage extends StandardPage {
   
    public EditableRecipePage(Recipe recipe)
    {
        add(new EditableRecipeForm("recipeForm", recipe));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.forms;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.parse.RecipeParser;
import com.da.organizer.recipes.service.RecipeService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 *
 * @author dandrus
 */
public class AddRecipeForm extends Panel
{
    final static Logger logger = Logger.getLogger(AddRecipeForm.class);
    
    @Inject 
    @Named("RecipeService")
    private RecipeService myRecipeService;
    
    
    public AddRecipeForm(String id)
    {
        super(id);
        
        final InputModel input = new InputModel();
        setDefaultModel(new CompoundPropertyModel<InputModel>(input));

        // Add a form with an onSumbit implementation that sets a message
        Form<?> form = new Form("form")
        {
            @Override
            protected void onSubmit()
            {
                logger.debug("Got input from form... "+input.text);
                List<Recipe> recipes = RecipeParser.fromString(input.text);
                logger.info("Got recipes: (hopefully just one....)");
                List<Recipe> unableToSave = new ArrayList<>();
                for(Recipe recipe:recipes)
                {
                    try{
                    myRecipeService.addRecipe(recipe);
                    }catch(Exception ex)
                    {
                        logger.error("Unable to save Recipe: "+recipe.getName(), ex);
                        unableToSave.add(recipe);
                    }
                }
                input.text = "";
                if(!unableToSave.isEmpty())
                {
                    StringBuilder b = new StringBuilder();
                    b.append("Unable to save the following recipes: ");
                    for(Recipe recipe:unableToSave)
                    {
                        b.append("\n\t"+recipe.getName());
                    }
                    input.text = b.toString();
                }
                setResponsePage(getPage());
            }
        };
        add(form);

        // add a text area component that uses Input's 'text' property.
        form.add(new TextArea<String>("text"));
        
        
    }
}

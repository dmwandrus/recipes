/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.forms;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.parse.RecipeParser;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.RecipesPage;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.io.IClusterable;

/**
 *
 * @author dandrus
 */
public class RecipeForm extends Panel
{
    final static Logger logger = Logger.getLogger(RecipeForm.class);
    
    @Inject 
    @Named("RecipeService")
    private RecipeService myRecipeService;
    
    
    public RecipeForm(String id)
    {
        super(id);
        
         final Input input = new Input();
        setDefaultModel(new CompoundPropertyModel<Input>(input));

        // Add a form with an onSumbit implementation that sets a message
        Form<?> form = new Form("form")
        {
            @Override
            protected void onSubmit()
            {
                logger.debug("Got input from form... "+input.text);
                List<Recipe> recipes = RecipeParser.fromString(input.text);
                logger.debug("Got recipes: (hopefully just one....)");
                for(Recipe recipe:recipes)
                {
                    System.out.println(recipe.prettyPrint());
                    try{
                    myRecipeService.addRecipe(recipe);
                    }catch(Exception ex)
                    {
                        logger.error("Unable to save Recipe: "+recipe.getName(), ex);
                    }
                }
                input.text = "";
                setResponsePage(getPage());
            }
        };
        add(form);

        // add a text area component that uses Input's 'text' property.
        form.add(new TextArea<String>("text"));
        
        
    }
    
    /** Simple data class that acts as a model for the input fields. */
    private static class Input implements IClusterable
    {
        /** some plain text. */
        public String text = "";

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "text = '" + text + "'";
        }
    }

}

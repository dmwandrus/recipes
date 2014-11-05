/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.forms;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.HomePage;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 *
 * @author diane
 */
public class SearchForm extends Panel {

    final static Logger logger = Logger.getLogger(AddRecipeForm.class);

    @Inject
    @Named("RecipeService")
    private RecipeService myRecipeService;

    public SearchForm(String id) {
        super(id);

        final InputModel input = new InputModel();
        setDefaultModel(new CompoundPropertyModel<InputModel>(input));

        // Add a form with an onSumbit implementation that sets a message
        Form<?> form = new Form("simpleSearchForm") {
            @Override
            protected void onSubmit() {
                List<Recipe> results = myRecipeService.simpleSearch(input.text);
                setResponsePage(new HomePage(results));
            }
            
        };
        add(form);

        // add a text area component that uses Input's 'text' property.
        form.add(new TextField<String>("text"));
    }

}

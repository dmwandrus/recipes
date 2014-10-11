/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.forms;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.ui.RecipesPage;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author dandrus
 */
public class EditableRecipeForm extends Panel {

    final static Logger LOGGER = Logger.getLogger(RecipesPage.class);
    @Inject
    @Named("RecipeService")
    private RecipeService myRecipeService;
    private Recipe myRecipe;
    // Form elements
    private TextField titleField;
    private TextArea descriptionField;
    private TextField originField;
    private TextField numServingsField;
    private TextField prepField;
    private TextField totalField;

    public EditableRecipeForm(String id, Recipe recipe) {
        super(id);
        this.myRecipe = recipe;

        Form<?> form = new Form("form") {
            @Override
            protected void onSubmit() {
                LOGGER.info("ORIGINAL DESCRIPTION: " + myRecipe.getDescription());
                LOGGER.info("NEW DESCRIPTION: " + descriptionField.getValue());

//        myRecipeService.updateRecipe(myRecipe);

                RecipesPage rp = new RecipesPage(myRecipe);
                setResponsePage(rp);
            }
        };

        add(form);

        titleField = new TextField("title", new PropertyModel(myRecipe, "name"));
        descriptionField = new TextArea("description", new PropertyModel(myRecipe, "description"));
        originField = new TextField("origin", new PropertyModel(myRecipe, "origination"));
        numServingsField = new TextField("servings", new PropertyModel(myRecipe, "numberOfServings"));
        prepField = new TextField("prep", new PropertyModel(myRecipe, "prepTime"));
        totalField = new TextField("total", new PropertyModel(myRecipe, "totalTime"));

        form.add(titleField);
        form.add(descriptionField);
        form.add(originField);
        form.add(numServingsField);
        form.add(prepField);
        form.add(totalField);

        form.add(new ListView<RecipeIngredient>("ingredientsList", myRecipe.getIngredients()) {
            @Override
            protected void populateItem(ListItem<RecipeIngredient> item) {

                RecipeIngredient ri = (RecipeIngredient) item.getDefaultModel().getObject();
                String ingredientString = ri.toString();
                TextField ingredientField = new TextField("ingredient", Model.of(ingredientString));
                item.add(ingredientField);
            }
        });

        form.add(new ListView<RecipeInstruction>("instructionsList", myRecipe.getInstructions()) {
            @Override
            protected void populateItem(ListItem<RecipeInstruction> item) {
                RecipeInstruction ri = (RecipeInstruction) item.getDefaultModel().getObject();
                item.add(new TextArea("instruction", Model.of(ri.getInstructionText())));
            }
        });


    }
}

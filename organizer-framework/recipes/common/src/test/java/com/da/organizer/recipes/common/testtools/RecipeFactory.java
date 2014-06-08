/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.testtools;

import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.common.exception.IngredientParseException;

/**
 *
 * @author dandrus
 */
public class RecipeFactory {
    
    
    public static Recipe buildPBJ() throws IngredientParseException
    {
        Recipe recipe = new Recipe();
        recipe.setName("PBJ Sandwich");
        
        recipe.addIngredient(buildPBRi());
        recipe.addIngredient(buildJamRi());
        recipe.addIngredient(buildBreadRi());
        
        recipe.addInstruction(new RecipeInstruction("Slather peanut butter on one slice of bread"));
        recipe.addInstruction(new RecipeInstruction("Slather jam on the other slice of bread"));
        recipe.addInstruction(new RecipeInstruction("Slap two pieces of bread together"));
        recipe.addInstruction(new RecipeInstruction("cut along the diagnol to serve"));
        
        recipe.setOrigination("childhood");
        recipe.setDescription("childhood yum");
        recipe.setNumberOfServings(1);
        
        return recipe;
        
    }
    
    public static Recipe buildPBAndHoney() throws IngredientParseException
    {
        Recipe recipe = new Recipe();
        recipe.setName("PB & Honey Sandwich");
        
        recipe.addIngredient(buildPBRi());
        recipe.addIngredient(buildHoneyRi());
        recipe.addIngredient(buildBreadRi());
        
        recipe.addInstruction(new RecipeInstruction("Slather peanut butter on one slice of bread"));
        recipe.addInstruction(new RecipeInstruction("Slather honey on the other slice of bread"));
        recipe.addInstruction(new RecipeInstruction("Slap two pieces of bread together"));
        recipe.addInstruction(new RecipeInstruction("cut along the diagnol to serve"));
        
        recipe.setOrigination("childhood");
        recipe.setDescription("childhood yum");
        recipe.setNumberOfServings(1);
        
        return recipe;
        
    }
    
    public static RecipeIngredient buildBreadRi() throws IngredientParseException
    {
        RecipeIngredient bread = new RecipeIngredient();
        bread.getRecipeAmount().setAmountFromString("2");
        bread.getRecipeAmount().setUnit(IngredientUnit.slice);
        bread.setIngredientName("White Bread");
        bread.setPrePreparation(" ");
        return bread;
    }
    
    public static RecipeIngredient buildJamRi() throws IngredientParseException
    {
        RecipeIngredient jam = new RecipeIngredient();
        jam.getRecipeAmount().setAmountFromString("2");
        jam.getRecipeAmount().setUnit(IngredientUnit.tablespoon);
        jam.setIngredientName("Thimbleberry Jam");
        jam.setPrePreparation(" ");
        return jam;
    }
    
    public static RecipeIngredient buildHoneyRi() throws IngredientParseException
    {
        RecipeIngredient jam = new RecipeIngredient();
        jam.getRecipeAmount().setAmountFromString("2");
        jam.getRecipeAmount().setUnit(IngredientUnit.tablespoon);
        jam.setIngredientName("honey");
        jam.setPrePreparation(" ");
        return jam;
    }
    
    public static RecipeIngredient buildPBRi() throws IngredientParseException
    {
        RecipeIngredient pb = new RecipeIngredient();
        pb.getRecipeAmount().setAmountFromString("2.0");
        pb.getRecipeAmount().setUnit(IngredientUnit.tablespoon);
        pb.setIngredientName("Creamy Peanut Butter");
        pb.setPrePreparation(" ");
        return pb;
    }
    
    public static Ingredient buildPeanutButter()
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Peanut Butter");
        ingredient.setNotes("Creamy");
        return ingredient;
    }
    
    public static Ingredient buildJelly()
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Jam");
        ingredient.setNotes("Thimbleberry");
        return ingredient;
    }
    
    public static Ingredient buildHoney()
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Honey");
        ingredient.setNotes("Clover");
        return ingredient;
    }
    
    public static Ingredient buildBread()
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Bread");
        ingredient.setNotes("white");
        return ingredient;
    }
    
}

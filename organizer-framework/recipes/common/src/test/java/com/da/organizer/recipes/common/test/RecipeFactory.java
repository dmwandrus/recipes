/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.test;

import com.da.organizer.recipes.common.test.*;
import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.IngredientUnit;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;

/**
 *
 * @author dandrus
 */
public class RecipeFactory {
    
    
    public static Recipe buildPBJ()
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
    
    public static Recipe buildPBAndHoney()
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
    
    public static RecipeIngredient buildBreadRi()
    {
        RecipeIngredient bread = new RecipeIngredient();
        bread.setAmount(2.0);
        bread.setUnit(IngredientUnit.slice);
        bread.setIngredient(buildBread());
        bread.setDescription("cut the crusts off for true decadence");
        bread.setPrePreparation(" ");
        return bread;
    }
    
    public static RecipeIngredient buildJamRi()
    {
        RecipeIngredient jam = new RecipeIngredient();
        jam.setAmount(2.0);
        jam.setUnit(IngredientUnit.tablespoon);
        jam.setIngredient(buildJelly());
        jam.setDescription("I love thimbleberry!");
        jam.setPrePreparation(" ");
        return jam;
    }
    
    public static RecipeIngredient buildHoneyRi()
    {
        RecipeIngredient jam = new RecipeIngredient();
        jam.setAmount(2.0);
        jam.setUnit(IngredientUnit.tablespoon);
        jam.setIngredient(buildHoney());
        jam.setDescription("Tut tut, looks like rain!");
        jam.setPrePreparation(" ");
        return jam;
    }
    
    public static RecipeIngredient buildPBRi()
    {
        RecipeIngredient pb = new RecipeIngredient();
        pb.setAmount(2.0);
        pb.setUnit(IngredientUnit.tablespoon);
        pb.setIngredient(buildPeanutButter());
        pb.setDescription("smooth is best");
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

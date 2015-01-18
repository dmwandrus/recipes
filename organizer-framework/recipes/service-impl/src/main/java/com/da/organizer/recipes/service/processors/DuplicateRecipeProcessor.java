/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.service.processors;

import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeInstruction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author diane
 */
public class DuplicateRecipeProcessor
{
    final static Logger logger = Logger.getLogger(DuplicateRecipeProcessor.class);
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public void process(String opString) throws Exception
    {
        // select recipe_id & name from recipes
        Query findAllRecipeNames = em.createQuery("select r.name, COUNT(r.name) from Recipe r group by r.name");
        List results = findAllRecipeNames.getResultList();
        
        List<String> duplicateRecipeNames = new ArrayList<String>();
        
        // for each recipe name, find where there are more than one name
        Iterator resultIterator = results.iterator();
        while(resultIterator.hasNext())
        {
            Object[] result = (Object[]) resultIterator.next();
            String recipeName = (String) result[0];
            Long recipeCount = (Long) result[1];
            if(recipeCount > 1)
            {
                duplicateRecipeNames.add(recipeName);
            }
        }
        
        TypedQuery<Recipe> findRecipesWithName = em.createQuery("SELECT r FROM Recipe r WHERE r.name = :name", Recipe.class);
        // if multiple names exist - retrieve all of them
        for(String dupe:duplicateRecipeNames)
        {
            logger.info("Multiple recipes exist with name: "+dupe);
            findRecipesWithName.setParameter("name", dupe);
        
            List<Recipe> duplicateRecipes = findRecipesWithName.getResultList();
            List<Recipe> readableList = new ArrayList<>(duplicateRecipes);
            Iterator<Recipe> recipeIterator = readableList.iterator();
            
            while(recipeIterator.hasNext())
            {
                Recipe head = recipeIterator.next();
                String headToString = createEqualsString(head);
                
                logger.info("Investigating Recipe Name: '"+head.getName()+"' with ID: "+head.getId());
                while(recipeIterator.hasNext())
                {
                    Recipe toCompare = recipeIterator.next();
                    String toCompareString = createEqualsString(toCompare);
                    
                    if(headToString.equals(toCompareString))
                    {
                        logger.info("Found duplicate with ID: "+toCompare.getId());
                        em.remove(toCompare);
                        recipeIterator.remove();
                        
                        // TODO - make sure to add description of deleted to the one kept
                        // if its not the same. Also make usre to keep serving data, and
                        // whatnot. 
                    }
                    else
                    {
                        logger.info("HEAD: " + headToString);
                        logger.info("DUPE: " + toCompareString)
                                ;
                    }
                    
                }
            }
            
            
            /*
            gotta think this through, WHAT IF I have 5 recipes with the same name
            and in terms of equals:
            
            [ A , B, C, B, C ]
            So, I need to compare A to all of the others, and then, nothing to do.
            - get first & remove from the list.
            Now, iterate through the list, and compare A to all of them. no matches
            Then my list is 
            [ B, C, B, C ]
            Same thing - take the first (B) and remove from the list.
            iterate thru the list, and compare B to all of them. When you get a match, 
            delete that matched recipe and remove from the list. 
            
            */
        }
        
        // see if they are equals
        
        // if the same, delete the extra
    }
    
    private String createEqualsString(Recipe r)
    {
        StringBuilder b = new StringBuilder();
        b.append("\nRecipe: ").append(r.getName());

        for(RecipeIngredient ingredient:r.getIngredients())
        {
            b.append("\n  ").append(ingredient.prettyPrint());
        }
        int i = 1;
        for(RecipeInstruction instruction:r.getInstructions())
        {
            b.append("\n").append(i).append(") ").append(instruction);
            i++;
        }
        return b.toString();
    }
}

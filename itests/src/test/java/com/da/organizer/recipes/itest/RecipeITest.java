/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.itest;


import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.exception.IngredientParseException;
import com.da.organizer.recipes.common.testtools.RecipeFactory;
import com.da.organizer.recipes.service.RecipeService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import static junit.framework.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.EagerSingleStagedReactorFactory;
import org.osgi.framework.BundleContext;

/**
 *
 * @author dandrus
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(EagerSingleStagedReactorFactory.class)
public class RecipeITest {
    
    static final Logger logger = Logger.getLogger(RecipeITest.class.getName());
    @Inject
    private BundleContext bc;
    @Inject
    private RecipeService recipeService;
    
    @Configuration
    public Option[] config()
    {
        return TestUtils.getRecipesKaraf();
    }

    @Before
    public void before()
    {
    }

    @After
    public void after()
    {
    }

    @Test
    public void sanityTest() throws Exception
    {
        assertTrue(true);
        assertNotNull(bc);
        assertNotNull(recipeService);
        assertTrue(TestUtils.bundlesStarted(bc));
    }
    
    @Test
    public void saveSimpleRecipe()
    {
        
        Recipe recipe = new Recipe();
        recipe.setName("PB&J");
        recipe.setOrigination("home sweet home");
        Long recipeId = recipeService.addRecipe(recipe);
        assertNotNull(recipeId);
        Recipe retrievedRecipe = recipeService.retrieveRecipe(recipeId);
        assertEquals(recipe, retrievedRecipe);
        assertTrue(recipeService.removeRecipe(recipeId));
        retrievedRecipe = recipeService.retrieveRecipe(recipeId);
        assertNull(retrievedRecipe);
    }
    
    @Test
    public void savePBJRecipe()
    {
        try {
            List<Ingredient> retrieveIngredients = recipeService.retrieveIngredients();
            logger.info("\n\n\n\n\n\nundeleted ingredients: "+retrieveIngredients);
            
            Recipe pbj = RecipeFactory.buildPBJ();
            Long recipeId = recipeService.addRecipe(pbj);
            assertNotNull(recipeId);
            Recipe retrievedRecipe = recipeService.retrieveRecipe(recipeId);
            assertEquals(pbj, retrievedRecipe);
            assertTrue(recipeService.removeRecipe(recipeId));
            retrievedRecipe = recipeService.retrieveRecipe(recipeId);
            assertNull(retrievedRecipe);
            for(RecipeIngredient ri:pbj.getIngredients())
            {
                Ingredient retrievedIng = recipeService.retrieveIngredientByName(ri.getIngredientName());
                assertNotNull(retrievedIng);
                recipeService.removeIngredient(retrievedIng.getId());
                retrievedIng = recipeService.retrieveIngredientByName(ri.getIngredientName());
                assertNull(retrievedIng);
            }
        } catch (IngredientParseException ex) {
            Logger.getLogger(RecipeITest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Unable to build recipe");
        }
                
    }
    
}

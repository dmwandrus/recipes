/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.testtools.RecipeFactory;
import javax.persistence.PersistenceException;
import javax.persistence.EntityTransaction;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dandrus
 */
public class RecipeTest {

    Logger LOG = Logger.getLogger(RecipeTest.class);
    private static final String puName = "test_recipe_pu";
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public RecipeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        emf = Persistence.createEntityManagerFactory(puName);
        em = emf.createEntityManager();
        assertNotNull(em);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        em.close();

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public void startTX() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
    }

    public void commitTX() {
        EntityTransaction tx = em.getTransaction();
        tx.commit();
    }
    
    public void rollbackTX()
    {
        try{
        EntityTransaction tx = em.getTransaction();
        if(tx.isActive())
        {
            tx.rollback();
        }
        }catch(Exception e)
        {
            // unable to rollback
        }
    }

    /**
     * Test of getId method, of class Recipe.
     */
    @Test
    public void sanityTest() {
        assertNotNull(emf);
        assertNotNull(em);
    }

    @Test
    public void saveRecipe() {

        try {
            startTX();
            Recipe pbRecipe = RecipeFactory.buildPBJ();
            LOG.info("saveRecipe - PRE-PERSIST: " + pbRecipe.prettyPrint());
            em.persist(pbRecipe);
            LOG.info("Orig ID: "+System.identityHashCode(pbRecipe));
            commitTX();

            // clear out the cache so I'm getting a different object to compare
            // otherwise, I'll just get the exact same object reference that
            // I just persisted, and there's really no point in comparing an
            // object to itself....
            em.clear();
            
            startTX();
            
            Recipe found = em.find(Recipe.class, pbRecipe.getId());
            LOG.info("Found ID: "+System.identityHashCode(found));
            LOG.info("saveRecipe - RETRIEVED: " + found.prettyPrint());
            
            assertEquals(pbRecipe.getName(), found.getName());
            assertEquals(pbRecipe.getDescription(), found.getDescription());
            assertEquals(pbRecipe.getOrigination(), found.getOrigination());
            
            assertEquals(pbRecipe.getIngredients().size(), found.getIngredients().size());
            for(RecipeIngredient origIng:pbRecipe.getIngredients())
            {
                boolean isFound = false;
                for(RecipeIngredient foundIng:found.getIngredients())
                {
                    if(origIng.getIngredientName().equals(foundIng.getIngredientName()))
                    {
                        assertEquals(origIng.getDescriptors(), foundIng.getDescriptors());
                        assertEquals(origIng.getRecipeAmountAsString(), foundIng.getRecipeAmountAsString());
                        isFound = true;
                    }
                }
                assertTrue(isFound);
            }
            
            assertEquals(pbRecipe.getInstructions().size(), found.getInstructions().size());
            
            for(int i=0; i<pbRecipe.getInstructions().size(); i++)
            {
                RecipeInstruction origInstr = pbRecipe.getInstructions().get(i);
                RecipeInstruction foundInstr = found.getInstructions().get(i);
                assertEquals(origInstr.getInstructionText(), foundInstr.getInstructionText());
            }
                
            
            em.remove(found);
            commitTX();

            startTX();
            try {
                Recipe foundAgain = em.find(Recipe.class, pbRecipe.getId());
                assertNull(foundAgain);
            } catch (PersistenceException ex) {
                //expecting entity not found exception.  
            }
            commitTX();

        } catch (Throwable t) {
            LOG.info("THREW UP: ", t);
            
            fail("unable to persist");
        }finally{
            rollbackTX();
        }

    }

    
    @Test
    public void testStringFormat()
    {
        String thing = "pizza";
        LOG.info("Searching for "+thing);
        String searchFormat = "%%%s%%";
        String searchString = String.format(searchFormat, thing);
        LOG.info("Search String: '"+searchString+"'");
        assertEquals(searchString, "%pizza%");
    }
}

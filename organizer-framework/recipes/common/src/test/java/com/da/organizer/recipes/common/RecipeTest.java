/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.exception.IngredientParseException;
import com.da.organizer.recipes.common.testtools.RecipeFactory;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.PersistenceException;
import javax.persistence.EntityTransaction;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
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
//        EntityTransaction tx = em.getTransaction();
//        tx.begin();
    }

    @After
    public void tearDown() {
//        EntityTransaction tx = em.getTransaction();
//        tx.rollback();
    }

    public void startTX() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
    }

    public void commitTX() {
        EntityTransaction tx = em.getTransaction();
        tx.commit();
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

        try{
        startTX();
        Recipe pbRecipe = RecipeFactory.buildPBJ();
        LOG.info("saveRecipe - PRE-PERSIST: " + pbRecipe.prettyPrint());
        Set<Long> ingredientIds = new HashSet<Long>();
        for (RecipeIngredient ri : pbRecipe.getIngredients()) {

            em.persist(ri.getIngredient());
            ingredientIds.add(ri.getIngredient().getId());
        }
        commitTX();

        LOG.info("Ingredients are saved....");
        LOG.info("saveRecipe - PRE-PERSIST: " + pbRecipe.prettyPrint());

        startTX();

        em.persist(pbRecipe);
        commitTX();

        startTX();
        LOG.info("saveRecipe - POST-PERSIST: " + pbRecipe.prettyPrint());
        Recipe found = em.find(Recipe.class, pbRecipe.getId());
        Set<Ingredient> ingredients = new HashSet<Ingredient>();

        LOG.info("saveRecipe - FOUND: " + found.prettyPrint());
        em.remove(pbRecipe);
        for (RecipeIngredient ing : pbRecipe.getIngredients()) {
            em.remove(ing.getIngredient());
        }
        commitTX();

        startTX();
        try {
            Recipe foundAgain = em.find(Recipe.class, pbRecipe.getId());
            assertNull(foundAgain);
        } catch (PersistenceException ex) {
            //expecting entity not found exception.  
        }

        TypedQuery<Ingredient> findAll = em.createNamedQuery("Ingredient.findAll", Ingredient.class);
        List<Ingredient> allIngredients = findAll.getResultList();
        LOG.info("Ingredients in db still: " + allIngredients);

        commitTX();
        }catch(Throwable t)
        {
            LOG.info("THREW UP: ", t);
            fail("unable to persist");
        }

    }

//    @Test
    public void saveRecipeWithoutSavingIngredients() throws IngredientParseException {
        Recipe pbjRecipe = RecipeFactory.buildPBJ();
        LOG.info("saveRecipe2 - PRE-PERSIST: " + pbjRecipe.prettyPrint());
        em.persist(pbjRecipe);
        LOG.info("saveRecipe2 - POST-PERSIST: " + pbjRecipe.prettyPrint());
        Recipe found = em.find(Recipe.class, pbjRecipe.getId());
        LOG.info("saveRecipe2 - FOUND: " + found.prettyPrint());
        em.remove(pbjRecipe);

        // Yes, Ingredients must be saved first.  
        // And this is really how I want it, because I want to ensure
        // a bit of validation on the Ingredient itself before persisting
        // willy nilly.  
    }

    @Test
    public void saveRecipeWithPreExistingIngredients() throws IngredientParseException {

        startTX();

        Recipe pbjRecipe = RecipeFactory.buildPBJ();

        // first save pbj wholly
        for (RecipeIngredient ri : pbjRecipe.getIngredients()) {
            em.persist(ri.getIngredient());
        }
        em.persist(pbjRecipe);
        commitTX();

        startTX();

        Recipe pbhoneyRecipe = RecipeFactory.buildPBAndHoney();
        // then save pbhoney...
        for (RecipeIngredient ri : pbhoneyRecipe.getIngredients()) {
            try {
                TypedQuery<Ingredient> findByName = em.createNamedQuery("Ingredient.findByName", Ingredient.class);
                findByName.setParameter("name", ri.getIngredientName());
                LOG.info("Query for: " + ri.getIngredientName());

                Ingredient result = findByName.getSingleResult();
                LOG.info("Result: " + result);
                if (ri.getIngredientName().equals("Peanut Butter")) {
                    assertNotNull(result);
                }
                if (ri.getIngredientName().equals("Bread")) {
                    assertNotNull(result);
                }
                if (ri.getIngredientName().equals("Honey")) {
                    assertNull(result);
                }
                if (result != null) {
                    LOG.info("found ingredient " + ri.getIngredientName());
//                if(!result.equals(ri.getIngredient()))
//                {
//                    // would need to update the ingredient...
//                    result.setNotes(result.getNotes().concat(" | ").concat(ri.getIngredient().getNotes()));
//                    result.getCategories().addAll(ri.getIngredient().getCategories());
//                    em.persist(ri.getIngredient());
//                }
                    ri.setIngredient(result);
                } else {
                    LOG.info("never found ingredient...." + ri.getIngredientName());
                    em.persist(ri.getIngredient());
                }

            } catch (PersistenceException ex) {
                LOG.error("error, ingredient not found? ", ex);
                if (ri.getIngredientName().equals("Peanut Butter")) {
                    fail("should have found peanut butter");
                }
                if (ri.getIngredientName().equals("Bread")) {
                    fail("should have found bread");
                }
                LOG.info("never found ingredient...." + ri.getIngredientName());
                em.persist(ri.getIngredient());

            }

        }
        commitTX();

        startTX();

        LOG.info("Pre-persist honey: " + pbhoneyRecipe.prettyPrint());

        em.persist(pbhoneyRecipe);

        LOG.info("PBJ (end):  " + pbjRecipe.prettyPrint());
        LOG.info("PBH (end):  " + pbhoneyRecipe.prettyPrint());


        commitTX();

        startTX();

        em.remove(pbhoneyRecipe);
        em.remove(pbjRecipe);

        Set<Ingredient> ingredients = new HashSet<Ingredient>();

        for (RecipeIngredient ri : pbjRecipe.getIngredients()) {
            ingredients.add(ri.getIngredient());
        }
        for (RecipeIngredient ri : pbhoneyRecipe.getIngredients()) {
            ingredients.add(ri.getIngredient());
        }

        for (Ingredient i : ingredients) {
            em.remove(i);

        }
        commitTX();


    }
}

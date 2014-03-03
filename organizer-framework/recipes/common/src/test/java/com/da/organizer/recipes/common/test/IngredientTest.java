/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.test;

import com.da.organizer.recipes.common.Ingredient;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author dandrus
 */
public class IngredientTest {

    Logger LOG = LoggerFactory.getLogger(IngredientTest.class);
    private static final String puName = "test_recipe_pu";
    private static EntityManagerFactory emf;
    private static EntityManager em;

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
    public void saveIngredient() {

        // create & save ingredient
        LOG.info("saveIngredient - start");
        startTX();
        Ingredient i = RecipeFactory.buildPeanutButter();
        assertNull(i.getId());
        LOG.info("saveIngredient -PRE PERSIST = " + i);
        em.persist(i);
        commitTX();

        // retrieve persisted ingredient
        startTX();
        LOG.info("saveIngredient - POST PERSIST = " + i);
        assertNotNull(i.getId());
        Ingredient found = em.find(Ingredient.class, i.getId());
        LOG.info("saveIngredient - POST FOUND = " + found);

        // clean up
        LOG.info("saveIngredient - cleanup");
        em.remove(i);
        commitTX();

        // ensure cleanup went well
        startTX();
        try {
            found = em.find(Ingredient.class, i.getId());
            assertNull(found);
        } catch (PersistenceException ex) {
            // success
        }
        LOG.info("saveIngredient - end");
        commitTX();
    }

    @Test
    public void saveAndRetrieveByName_Ingredient() {

        LOG.info("saveAndRetrieveByName_Ingredient - start");
        // create & save ingredient
        startTX();
        Ingredient i = RecipeFactory.buildPeanutButter();
        LOG.info("saveIngredient -PRE PERSIST = " + i);
        em.persist(i);
        commitTX();
        
        // query for saved ingredient by name
        startTX();
        LOG.info("saveIngredient - POST PERSIST = " + i);
        assertNotNull(i.getId());
        Ingredient foundById = em.find(Ingredient.class, i.getId());
        LOG.info("saveIngredient - POST FOUND By ID = " + foundById);

        TypedQuery<Ingredient> findByName = em.createNamedQuery("Ingredient.findByName", Ingredient.class);
        findByName.setParameter("name", i.getName());
        Ingredient singleResult = findByName.getSingleResult();
        assertNotNull(singleResult);
        assertEquals(foundById, singleResult);
        LOG.info("FOUND: " + singleResult);

        // clean up
        em.remove(i);
        commitTX();
        LOG.info("saveAndRetrieveByName_Ingredient - end");
    }

    @Test
    public void saveAndRetrieveAll_Ingredient() {

        LOG.info("saveAndRetrieveAll_Ingredient - start");
        startTX();
        Ingredient i = RecipeFactory.buildPeanutButter();
        LOG.info("saveIngredient -PRE PERSIST = " + i);
        em.persist(i);

        commitTX();
        startTX();
        LOG.info("saveIngredient - POST PERSIST = " + i);
        assertNotNull(i.getId());
        Ingredient foundById = em.find(Ingredient.class, i.getId());
        LOG.info("saveIngredient - POST FOUND = " + foundById);

        TypedQuery<Ingredient> findAll = em.createNamedQuery("Ingredient.findAll", Ingredient.class);
        List<Ingredient> allIngredients = findAll.getResultList();
        assertNotNull(allIngredients);
        assertFalse(allIngredients.isEmpty());
        assertTrue(allIngredients.contains(foundById));
        LOG.info("Found All: " + allIngredients);

        em.remove(i);
        commitTX();
        LOG.info("saveAndRetrieveAll_Ingredient - end");
    }
}

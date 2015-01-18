/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author diane
 */
public class TagTest {

    Logger LOG = Logger.getLogger(RecipeTest.class);
    private static final String puName = "test_recipe_pu";
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public TagTest() {
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

    @Test
    public void testWithTags() {
        try {
            startTX();
            Ingredient chicken = new Ingredient();
            chicken.setName("boneless skinless chicken breasts");
            Tag poultry = new Tag("poultry");
            List<Tag> poultryTags = new ArrayList<>();
            poultryTags.add(new Tag("protein"));
            poultryTags.add(new Tag("meat"));
            poultry.setTags(poultryTags);
            
            chicken.addTag(poultry);
            
            
            em.persist(chicken);

            Ingredient turkey = new Ingredient();
            turkey.setName("ground turkey");
            turkey.addTag(new Tag("poultry"));
//            turkey.addTag(new Tag("protein"));
            turkey.addTag(new Tag("ground"));
//            turkey.addTag(new Tag("meat"));
            Iterator<Tag> iterator = turkey.getTags().iterator();
            List<Tag> preExistingTags = new ArrayList<>();
            while(iterator.hasNext())
            {
                Tag tag = iterator.next();
                TypedQuery<Tag> selectTag = em.createQuery("select t from Tag t where t.tagText = :tag", Tag.class);
                selectTag.setParameter("tag", tag.getTagText());
                List<Tag> existingTags = selectTag.getResultList();
                if (!existingTags.isEmpty()) {
                    Tag existingTag = existingTags.get(0);
                    iterator.remove();
                    preExistingTags.add(existingTag);
                }
            }
            for(Tag tag:preExistingTags)
            {
                turkey.addTag(tag);
            }
//            for (Tag tag : turkey.getTags()) {
//                TypedQuery<Tag> selectTag = em.createQuery("select t from Tag t where t.tagText = :tag", Tag.class);
//                selectTag.setParameter("tag", tag.getTagText());
//                List<Tag> existingTags = selectTag.getResultList();
//                if (!existingTags.isEmpty()) {
//                    Tag existingTag = existingTags.get(0);
//                    tag.setId(existingTag.getId());
//                }
//
//            }

            em.persist(turkey);

            Query selectAllTags = em.createQuery("select t from Tag t");
        List allTags = selectAllTags.getResultList();
        LOG.info("TAGS: "+allTags);
            commitTX();
        } catch (Exception ex) {
            LOG.error("Exception", ex);
        }

        

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.itest;


import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.service.RecipeService;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static junit.framework.Assert.*;
import org.junit.Ignore;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;

@RunWith(PaxExam.class)
public class RecipesITest {

    static final Logger logger = LoggerFactory.getLogger(RecipesITest.class.getName());
    @Inject
    private BundleContext bc;
    @Inject
    private RecipeService recipeService;
    
 
    @Configuration
    public Option[] config() {
        MavenArtifactUrlReference karafUrl = maven()
            .groupId("com.da.karaf")
            .artifactId("recipes-tier")
            .version("1.0-SNAPSHOT")
            .type("tar.gz");

//        MavenUrlReference customRepo = maven()
//            .groupId("com.da.karaf")
//            .artifactId("karaf-feature")
//            .classifier("features")
//            .type("xml")
//            .versionAsInProject();
        return new Option[] {
//            KarafDistributionOption.debugConfiguration("5005", true),
            karafDistributionConfiguration()
                .frameworkUrl(karafUrl)
                .unpackDirectory(new File("target/exam"))
                .useDeployFolder(false),
            keepRuntimeFolder(),
            
//            KarafDistributionOption.features(customRepo , "scr"),
//            mavenBundle()
//                .groupId("org.ops4j.pax.exam.samples")
//                .artifactId("pax-exam-sample8-ds")
//                .versionAsInProject().start(),
            // Remember that the test executes in another process.  If you want to debug it, you need
                // to tell Pax Exam to launch that process with debugging enabled.  Launching the test class itself with
                // debugging enabled (for example in Eclipse) will not get you the desired results.
//                debugConfiguration("5000", true)
       };
    }
    
//    @Test
//    public void sanityTest()
//    {
//        assertNotNull(bc);
//        assertNotNull(recipeService);
//    }
    
    @Ignore
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
    public void simpleSearchTest()
    {
        List<Recipe> allRecipes = recipeService.retrieveRecipes();
        logger.info("ITESTLOG: Total Number of Recipes: "+allRecipes.size());
        List<String> searchTerms = new ArrayList<>();
        searchTerms.add("pizza");
        searchTerms.add("sauce");
        searchTerms.add("party");
        searchTerms.add("sweet");
        searchTerms.add("salt");
        searchTerms.add("bake");
        searchTerms.add("ice cube");
        searchTerms.add("chill");
        searchTerms.add("saute");
        
        for(String term:searchTerms)
        {
            search(term);
        }
    }
    
    private void search(String term)
    {
        List<Recipe> results = recipeService.simpleSearch(term);
        logger.info("ITESTLOG: Found "+results.size()+" recipes with "+term);
        for(Recipe result:results)
        {
            logger.info("Found "+result.getName());
        }
        
    }
}

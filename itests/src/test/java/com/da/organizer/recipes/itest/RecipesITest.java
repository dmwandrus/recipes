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
            // KarafDistributionOption.debugConfiguration("5005", true),
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
       };
    }
    
//    @Test
//    public void sanityTest()
//    {
//        assertNotNull(bc);
//        assertNotNull(recipeService);
//    }
    
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
}

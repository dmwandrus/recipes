package com.da.organizer.recipes.service.impl;

import com.da.organizer.recipes.common.Ingredient;
import com.da.organizer.recipes.common.Recipe;
import com.da.organizer.recipes.common.RecipeIngredient;
import com.da.organizer.recipes.common.RecipeIngredient_;
import com.da.organizer.recipes.common.RecipeInstruction;
import com.da.organizer.recipes.common.RecipeInstruction_;
import com.da.organizer.recipes.common.Recipe_;
import com.da.organizer.recipes.service.RecipeService;
import com.da.organizer.recipes.service.processors.IngredientProcessor;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dandrus
 */
public class SimpleRecipeService implements RecipeService {

    Logger LOG = LoggerFactory.getLogger(SimpleRecipeService.class);
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long addRecipe(Recipe recipe) {
        LOG.info("SAVE: "+recipe.prettyPrint());
        em.persist(recipe);
        return recipe.getId();
    }

    @Override
    public Long addIngredient(Ingredient ingredient) {
        em.persist(ingredient);
        return ingredient.getId();
    }

    @Override
    public List<Recipe> retrieveRecipes() {
        TypedQuery<Recipe> findAllQuery = em.createNamedQuery("Recipe.findAll", Recipe.class);
        findAllQuery.setMaxResults(50);
        return findAllQuery.getResultList();
    }

    @Override
    public Recipe retrieveRecipe(Long id) {
        try {
            return em.find(Recipe.class, id);
        } catch (PersistenceException ex) {
            return null;
        }
    }

    @Override
    public List<Ingredient> retrieveIngredients() {
        TypedQuery<Ingredient> findAllQuery = em.createNamedQuery("Ingredient.findAll", Ingredient.class);
        return findAllQuery.getResultList();
    }

    @Override
    public Ingredient retrieveIngredient(Long id) {
        return em.find(Ingredient.class, id);
    }

    @Override
    public Collection<String> retrieveIngredientNames() {
        TypedQuery<String> findAllQuery = em.createNamedQuery("Ingredient.findAllNames", String.class);
        return findAllQuery.getResultList();
    }

    @Override
    public boolean removeRecipe(Long id) {
        Recipe found = em.find(Recipe.class, id);
        em.remove(found);
        return true;
    }
    
    @Override
    public boolean removeIngredient(Long id)
    {
        Ingredient found = em.find(Ingredient.class, id);
        em.remove(found);
        return true;
    }

    @Override
    public Ingredient retrieveIngredientByName(String name) {
        Ingredient found = null;
        try {
            TypedQuery<Ingredient> findByName = em.createNamedQuery("Ingredient.findByName", Ingredient.class);
            findByName.setParameter("name", name);
            found = findByName.getSingleResult();
        } catch (PersistenceException ex) {
            LOG.debug("No ingedient found with name: " + name);
        }
        return found;
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        LOG.info("Updating to: "+recipe.prettyPrint());
        // error checking - 
        // * does the incoming recipe have an id? if yes, fine
        //   else, just throw an error
        
        em.merge(recipe);
        runProcessors(); // TODO: this is here for now. I'd prefer camel processors or something, but I"m feeling lazy
    }
    
    @Override
    public List<Recipe> simpleSearch(String thing)
    {
        LOG.info("Searching for "+thing);
        String searchFormat = "%%%s%%";
        String searchString = String.format(searchFormat, thing);
        LOG.info("Search String: '"+searchString+"'");
        // get the criteria builder
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // create the query to find "Recipe" type of objects
        CriteriaQuery<Recipe> queryCriteria = criteriaBuilder.createQuery(Recipe.class);
        // build the root of the query - searching for recipes
        Root<Recipe> recipeRoot = queryCriteria.from(Recipe.class);
        ListJoin<Recipe,RecipeIngredient> ingredients = recipeRoot.join(Recipe_.ingredients);
        ListJoin<Recipe,RecipeInstruction> instructions = recipeRoot.join(Recipe_.instructions);
        // add a where clause - where the name contains the 'thing'
        Predicate nameLike = criteriaBuilder.like(recipeRoot.get(Recipe_.name), searchString);
        Predicate descriptionLike = criteriaBuilder.like(recipeRoot.get(Recipe_.description), searchString);
        Predicate ingredientsLike = criteriaBuilder.like(ingredients.get(RecipeIngredient_.ingredientName), searchString);
        Predicate instructionsLike = criteriaBuilder.like(instructions.get(RecipeInstruction_.instructionText), searchString);
        Predicate part1 = criteriaBuilder.or(nameLike, descriptionLike);
        Predicate part2 = criteriaBuilder.or(ingredientsLike, instructionsLike);
        
        queryCriteria.where(criteriaBuilder.or(part1, part2)).distinct(true);
        
        // the select clause
        queryCriteria.select(recipeRoot);
        LOG.info("Query Criteria: "+queryCriteria);

        // take all of the query criterias and actually create the whole query
        TypedQuery<Recipe> query = em.createQuery(queryCriteria);
        LOG.info("Query: "+query);
        
        // execute the query
        List<Recipe> results = query.getResultList();
        LOG.info("Found "+results.size()+" recipes");
        
        return results;
    }

    @Override
    public void runProcessors() {
        IngredientProcessor processor = new IngredientProcessor();
        processor.setRecipeService(this);
        processor.processRecipes("random string");
    }
    
    
}

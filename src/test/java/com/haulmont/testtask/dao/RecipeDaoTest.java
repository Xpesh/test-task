package com.haulmont.testtask.dao;

import com.haulmont.testtask.entities.Priority;
import com.haulmont.testtask.entities.Recipe;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
class RecipeDaoTest {

    @Test
    void findAll() {
        RecipeDao recipeDao = new RecipeDao(DataBaseManager.getConnection());
        List<Recipe> list = recipeDao.findAll();
        list.forEach(System.out::println);
    }

    @Test
    void findById() {
        RecipeDao recipeDao = new RecipeDao(DataBaseManager.getConnection());
        System.out.println(recipeDao.findById(0));
    }

    @Test
    void save() {
        RecipeDao recipeDao = new RecipeDao(DataBaseManager.getConnection());
        Recipe recipe = new Recipe("описание",0,
                0, LocalDate.now(),
                LocalDate.of(2003,4,5), Priority.NORMAL);
        int before = recipeDao.findAll().size();
        recipeDao.save(recipe);
        Assert.assertEquals(before+1,recipeDao.findAll().size());

    }

    @Test
    void delete() {
        RecipeDao recipeDao = new RecipeDao(DataBaseManager.getConnection());
        int n = recipeDao.findAll().size();
        Recipe recipe = recipeDao.findById(0);
        recipeDao.delete(recipe);
        Assert.assertEquals(n-1,recipeDao.findAll().size());
    }
}
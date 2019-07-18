package com.haulmont.testtask.dao;

import com.haulmont.testtask.entities.Priority;
import com.haulmont.testtask.entities.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDao implements Dao<Recipe> {
    private Connection connection;
    public RecipeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM PUBLIC.RECIPE");
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next())
                recipes.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public Recipe findById(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM PUBLIC.RECIPE WHERE RECIPE_ID=?");
            statement.setLong(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            rs.next();
            return mapRow(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void save(Recipe recipe) {

        try {
            PreparedStatement statement;
            if (recipe.getId()<0) {
                statement = connection.prepareStatement(
                        "INSERT INTO PUBLIC.RECIPE (DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE,VALIDITY,PRIORITY)" +
                                " VALUES(?,?,?,?,?,?)");
            } else {
                statement = connection.prepareStatement(
                        "UPDATE PUBLIC.RECIPE SET DESCRIPTION=?, PATIENT_ID=?, DOCTOR_ID=?, CREATION_DATE=?, " +
                                "VALIDITY=?, PRIORITY=? WHERE RECIPE_ID=?");
                statement.setLong(7, recipe.getId());
            }
            statement.setString(1, recipe.getDescription());
            statement.setLong(2, recipe.getIdPatient());
            statement.setLong(3, recipe.getIdDoctor());
            statement.setDate(4, Date.valueOf(recipe.getCreationDate()));
            statement.setDate(5, Date.valueOf(recipe.getValidity()));
            statement.setString(6, recipe.getPriority().toString());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Recipe recipe) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM PUBLIC.RECIPE WHERE RECIPE_ID=?");
            statement.setLong(1, recipe.getId());
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private Recipe mapRow(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe(
                rs.getString("DESCRIPTION"),
                rs.getLong("PATIENT_ID"),
                rs.getLong("DOCTOR_ID"),
                rs.getDate("CREATION_DATE").toLocalDate(),
                rs.getDate("VALIDITY").toLocalDate(),
                Priority.valueOf(rs.getString("PRIORITY"))


        );
        recipe.setId(rs.getLong("recipe_id"));
        return recipe;
    }
}

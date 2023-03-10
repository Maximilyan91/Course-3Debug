package com.cooking.course3_recipeApp.service.impl;

import com.cooking.course3_recipeApp.exception.ValidationException;
import com.cooking.course3_recipeApp.model.Ingredient;
import com.cooking.course3_recipeApp.model.Recipe;
import com.cooking.course3_recipeApp.service.FileService;
import com.cooking.course3_recipeApp.service.RecipeService;
import com.cooking.course3_recipeApp.service.ValidationService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {


    private static Long id = 0L;
    private final ValidationService validationService;
    private final FileService fileService;
    private Map<Long, Recipe> recipes = new HashMap<>();
    @Value("${path.to.recipes.file}")
    private String recipesFilePath;

    @Value("${path.to.recipes.file}")
    private String recipesFileName;
    @Value("${path.to.recipes.txt.file}")
    private String recipesTxtFileName;

    private Path recipesPath;


    @Override
    public Recipe addRecipe(Recipe recipe) {
        if (!validationService.validate(recipe)) {
            throw new ValidationException(recipe.toString());
        }
        Recipe addedRecipe = recipes.put(id++, recipe);
        fileService.saveToFile(recipes, recipesPath);
        return addedRecipe;

    }

    @Override
    public Optional<Recipe> getRecipe(Long id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @Override
    public Recipe update(Long id, Recipe recipe) {
        if (!validationService.validate(recipe)) {
            throw new ValidationException(recipe.toString());
        }
        Recipe updated = recipes.replace(id, recipe);
        fileService.saveToFile(recipes, recipesPath);
        return updated;
    }


    @Override
    public Recipe delete(Long id) {
        return recipes.remove(id);
    }

    @Override
    public Map<Long, Recipe> getAll() {
        return recipes;
    }

    @Override
    public File readFile() {
        return recipesPath.toFile();
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        fileService.uploadFile(file, recipesPath);
        recipes = fileService.readMapFromFile(recipesPath, new TypeReference<HashMap<Long, Recipe>>() {
        });

    }

    @Override
    public File prepareRecipesTxt() throws IOException {
        return fileService.saveToFile(recipesToString(), Path.of(recipesFilePath, recipesTxtFileName)).toFile();
    }

    @PostConstruct
    private void init() {
        recipesPath = Path.of(recipesFilePath, recipesFileName);
        recipes = fileService.readMapFromFile(recipesPath, new TypeReference<HashMap<Long, Recipe>>() {
        });

    }

    private String recipesToString() {
        StringBuilder sb = new StringBuilder();
        String listEl = " * ";

        for (Recipe recipe : recipes.values()) {
            sb.append("\n").append(recipe.toString()).append("\n");

            sb.append("\nИнгредиенты:\n");

            for (Ingredient ingredient : recipe.getIngredient()) {
                sb.append(listEl).append(ingredient.toString()).append("\n");
            }

            sb.append("\nИнструкция приготовления:\n");
            for (String step : recipe.getStepsCook()) {
                sb.append(listEl).append(step).append("\n");

            }
        }
        return sb.append("\n").toString();
    }


}

package com.cooking.course3_recipeApp.service.impl;

import com.cooking.course3_recipeApp.exception.ValidationException;
import com.cooking.course3_recipeApp.model.Ingredient;
import com.cooking.course3_recipeApp.model.Recipe;
import com.cooking.course3_recipeApp.service.FileService;
import com.cooking.course3_recipeApp.service.IngredientService;
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
public class IngredientServiceImpl implements IngredientService {
    private static Long id = 1L;

    private static Map<Long, Ingredient> ingredients = new HashMap<>();
    private final ValidationService validationService;
    private final FileService fileService;

    @Value("${path.to.ingredients.file}")
    private String ingredientsFilePath;

    @Value("${name.of.ingredients.file}")
    private String ingredientsFileName;

    private Path ingredientPath;
    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        if (!validationService.validate(ingredient)) {
            throw new ValidationException(ingredient.toString());
        }
        Ingredient addedIngredient = ingredients.put(id++, ingredient);
        fileService.saveToFile(ingredients, ingredientPath);
        return addedIngredient;
    }

    @Override
    public Optional<Ingredient> getIngredient(Long id) {
        return Optional.ofNullable(ingredients.get(id));
    }

    @Override
    public Ingredient update(Long id, Ingredient ingredient) {
        if (!validationService.validate(ingredient)) {
            throw new ValidationException(ingredient.toString());
        }
        ingredients.replace(id, ingredient);
        fileService.saveToFile(ingredients, ingredientPath);
        return ingredient;
    }

    @Override
    public Ingredient delete(Long id) {
        Ingredient ingredient = ingredients.remove(id);
        fileService.saveToFile(ingredients, ingredientPath);

        return ingredient;
    }

    @Override
    public Map<Long, Ingredient> getAll() {
        return ingredients;
    }

    @Override
    public File readFile() {
        return ingredientPath.toFile();
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        fileService.uploadFile(file, ingredientPath);
        ingredients = fileService.readMapFromFile(ingredientPath, new TypeReference<HashMap<Long, Ingredient>>() {});

    }

    @PostConstruct
    private void init() {
    ingredientPath = Path.of(ingredientsFilePath, ingredientsFileName);
    ingredients = fileService.readMapFromFile(ingredientPath, new TypeReference<HashMap<Long, Ingredient>>() {});

    }
}

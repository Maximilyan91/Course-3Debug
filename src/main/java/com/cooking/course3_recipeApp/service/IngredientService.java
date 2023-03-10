package com.cooking.course3_recipeApp.service;

import com.cooking.course3_recipeApp.model.Ingredient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface IngredientService {

    Ingredient addIngredient(Ingredient ingredient);

    Optional<Ingredient> getIngredient(Long id);

    Ingredient update(Long id, Ingredient ingredient);


    Ingredient delete(Long id);

    Map<Long, Ingredient> getAll();

    File readFile();

    void uploadFile(MultipartFile file) throws IOException;
}


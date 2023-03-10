package com.cooking.course3_recipeApp.service;

import com.cooking.course3_recipeApp.model.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface RecipeService {

     Recipe addRecipe(Recipe recipe);

     Optional<Recipe> getRecipe(Long id);

     Recipe update(Long id, Recipe recipe);
     Recipe delete(Long id);

     Map<Long, Recipe> getAll();

     File readFile();

     void uploadFile(MultipartFile file) throws IOException;
}

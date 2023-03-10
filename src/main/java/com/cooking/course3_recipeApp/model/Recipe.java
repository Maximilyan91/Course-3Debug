package com.cooking.course3_recipeApp.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private String recipeName;
    private int cookingTime;
    private List<Ingredient> ingredient;
    private List<String> stepsCook;


    @Override
    public String toString() {
        return recipeName + "\n Время приготовления " + cookingTime;
    }
}

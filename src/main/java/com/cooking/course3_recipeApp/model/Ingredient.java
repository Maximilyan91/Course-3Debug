package com.cooking.course3_recipeApp.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private String ingredientName;
    private int ingredientNum;
    private String unitOfMeasure;
    @Override
    public String toString() {
        return ingredientName + " - " + unitOfMeasure + " " + ingredientNum;
    }
}

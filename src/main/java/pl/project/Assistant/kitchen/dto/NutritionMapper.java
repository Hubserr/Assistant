package pl.project.Assistant.kitchen.dto;

import pl.project.Assistant.kitchen.Nutrition;

public class NutritionMapper {

    private NutritionMapper() {
    }

    public static Nutrition toEntity(NutritionDto dto) {
        if (dto == null) {
            return null;
        }
        return new Nutrition(dto.getCalories(), dto.getProtein(), dto.getFat(), dto.getCarbs());
    }

    public static NutritionDto toDto(Nutrition nutrition) {
        if (nutrition == null) {
            return null;
        }
        NutritionDto dto = new NutritionDto();
        dto.setCalories(nutrition.getCalories());
        dto.setProtein(nutrition.getProtein());
        dto.setFat(nutrition.getFat());
        dto.setCarbs(nutrition.getCarbs());
        return dto;
    }
}
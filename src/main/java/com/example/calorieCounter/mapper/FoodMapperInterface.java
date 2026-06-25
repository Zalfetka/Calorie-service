package com.example.calorieCounter.mapper;

import com.example.calorieCounter.dto.FoodImportDto;
import com.example.calorieCounter.entity.Food;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FoodMapperInterface {

    Food toEntity(FoodImportDto dto);
}

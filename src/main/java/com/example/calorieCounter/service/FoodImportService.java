package com.example.calorieCounter.service;

import com.example.calorieCounter.dto.FoodImportDto;
import com.example.calorieCounter.entity.Food;
import com.example.calorieCounter.exeption.ExcelValidationException;
import com.example.calorieCounter.mapper.FoodMapperInterface;
import com.example.calorieCounter.repository.FoodRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodImportService {

    private final FoodRepo foodRepository;
    private final Validator validator;
    private final FoodMapperInterface foodMapper;

    public void importExcel(MultipartFile file) throws IOException {

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            List<Food> foods = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            Set<String> existingNames =
                    foodRepository.findAll()
                            .stream()
                            .map(Food::getNameFood)
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet());

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                FoodImportDto dto =
                        new FoodImportDto(
                                row.getCell(0).getStringCellValue().trim(),
                                getDoubleValue(row.getCell(1)),
                                getDoubleValue(row.getCell(2)),
                                getDoubleValue(row.getCell(3))
                        );

                Set<ConstraintViolation<FoodImportDto>> violations =
                        validator.validate(dto);

                if (!violations.isEmpty()) {

                    for (ConstraintViolation<FoodImportDto> violation : violations) {

                        errors.add(
                                "Строка "
                                        + (i + 1)
                                        + ": "
                                        + violation.getMessage()
                        );
                    }
                    continue;
                }

                String normalizedName =
                        dto.nameFood()
                                .trim()
                                .toLowerCase();

                if (existingNames.contains(normalizedName)) {
                    errors.add(
                            "Строка "
                                    + (i + 1)
                                    + ": продукт '"
                                    + dto.nameFood()
                                    + "' уже существует"
                    );
                    continue;
                }

                Food food = foodMapper.toEntity(dto);
                foods.add(food);
                existingNames.add(normalizedName);
            }

            if (!errors.isEmpty()) {
                throw new ExcelValidationException(
                        String.join("\n", errors)
                );
            }
            foodRepository.saveAll(foods);
        }
    }

    private Double getDoubleValue(Cell cell) {

        if (cell == null) {
            throw new IllegalArgumentException("Пустая ячейка");
        }

        try {
            return switch (cell.getCellType()) {

                case NUMERIC ->
                        cell.getNumericCellValue();

                case STRING ->
                        Double.parseDouble(
                                cell.getStringCellValue()
                                        .replace(",", ".")
                                        .trim()
                        );

                default -> throw new IllegalArgumentException("Некорректный формат числа");
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректное числовое значение");
        }
    }
}

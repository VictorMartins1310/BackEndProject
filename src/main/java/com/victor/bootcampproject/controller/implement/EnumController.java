package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.model.Frequency;
import com.victor.bootcampproject.model.ProductType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class EnumController {
    /**
     * Method to get All Product Types
     * @return List of Product Types (as String)
     */
    @GetMapping("api/types")
    public List<String> getTypeValues() {
        return Arrays.stream(ProductType.values())
                .map(Enum::name)
                .toList();
    }

    /**
     * Method to get All Frequencies of a Task
     * @return List of Frequencies (as String)
     */
    @GetMapping("api/frequencies")
    public List<String> getFrequencyValues() {
        return Arrays.stream(Frequency.values())
                .map(Enum::name)
                .toList();
    }
}

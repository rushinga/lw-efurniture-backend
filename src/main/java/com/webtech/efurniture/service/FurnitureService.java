package com.webtech.efurniture.service;

import com.webtech.efurniture.model.Furniture;
import com.webtech.efurniture.userRepository.FurnitureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FurnitureService {

    private final FurnitureRepository furnitureRepository;

    public FurnitureService(FurnitureRepository furnitureRepository) {
        this.furnitureRepository = furnitureRepository;
    }

    public void saveAll(List<Furniture> furnitureList) {
        furnitureRepository.saveAll(furnitureList);
    }

    public List<Furniture> getAllFurniture() {
        return furnitureRepository.findAll();
    }
}
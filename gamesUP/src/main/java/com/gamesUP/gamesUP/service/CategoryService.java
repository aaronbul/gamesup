package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<Category> getAllCategories() {
        log.info("Récupération de toutes les catégories");
        return categoryRepository.findAll();
    }
    
    public Optional<Category> getCategoryById(Long id) {
        log.info("Récupération de la catégorie avec l'ID: {}", id);
        return categoryRepository.findById(id);
    }
    
    public Optional<Category> getCategoryByType(String type) {
        log.info("Récupération de la catégorie avec le type: {}", type);
        return categoryRepository.findByType(type);
    }
    
    public Category createCategory(Category category) {
        log.info("Création d'une nouvelle catégorie: {}", category.getType());
        
        if (categoryRepository.existsByType(category.getType())) {
            throw new RuntimeException("Une catégorie avec ce type existe déjà");
        }
        
        return categoryRepository.save(category);
    }
    
    public Optional<Category> updateCategory(Long id, Category category) {
        log.info("Mise à jour de la catégorie avec l'ID: {}", id);
        
        return categoryRepository.findById(id).map(existingCategory -> {
            // Vérifier si le type change et s'il existe déjà
            if (!existingCategory.getType().equals(category.getType()) && 
                categoryRepository.existsByType(category.getType())) {
                throw new RuntimeException("Une catégorie avec ce type existe déjà");
            }
            
            existingCategory.setType(category.getType());
            existingCategory.setDescription(category.getDescription());
            
            return categoryRepository.save(existingCategory);
        });
    }
    
    public boolean deleteCategory(Long id) {
        log.info("Suppression de la catégorie avec l'ID: {}", id);
        
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByType(String type) {
        return categoryRepository.existsByType(type);
    }
} 
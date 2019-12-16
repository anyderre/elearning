package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentCategory(Long parentCategory);
    Category findCategoryByNameAndIdIsNot(String name, Long id);
    Category findCategoryByName(String name);
}

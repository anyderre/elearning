package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    SubCategory findSubCategoryByNameAndIdIsNot(String name, Long id);
    SubCategory findSubCategoryByName(String name);
}

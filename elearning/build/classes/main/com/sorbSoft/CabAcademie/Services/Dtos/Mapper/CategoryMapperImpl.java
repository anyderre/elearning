package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-08T01:55:22-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryViewModel mapToViewModel(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryViewModel categoryViewModel = new CategoryViewModel();

        categoryViewModel.setId( category.getId() );
        categoryViewModel.setName( category.getName() );
        categoryViewModel.setDescription( category.getDescription() );

        return categoryViewModel;
    }

    @Override
    public Category mapToEntity(CategoryViewModel vm) {
        if ( vm == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( vm.getId() );
        category.setName( vm.getName() );
        category.setDescription( vm.getDescription() );

        return category;
    }

    @Override
    public Category mapEntityToEntity(Category category) {
        if ( category == null ) {
            return null;
        }

        Category category1 = new Category();

        category1.setName( category.getName() );
        category1.setDescription( category.getDescription() );

        return category1;
    }
}

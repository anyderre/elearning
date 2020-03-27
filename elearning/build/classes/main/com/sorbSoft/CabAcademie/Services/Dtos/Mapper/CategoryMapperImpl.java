package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-03-25T23:25:18-0400",
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
        categoryViewModel.setParentCategory( mapEntityToEntity( category.getParentCategory() ) );
        List<Category> list = category.getChildrenCategory();
        if ( list != null ) {
            categoryViewModel.setChildrenCategory( new ArrayList<Category>( list ) );
        }
        categoryViewModel.setDeleted( category.isDeleted() );

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
        List<Category> list = vm.getChildrenCategory();
        if ( list != null ) {
            category.setChildrenCategory( new ArrayList<Category>( list ) );
        }
        category.setDeleted( vm.isDeleted() );

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
        category1.setParentCategory( mapEntityToEntity( category.getParentCategory() ) );
        List<Category> list = category.getChildrenCategory();
        if ( list != null ) {
            category1.setChildrenCategory( new ArrayList<Category>( list ) );
        }
        category1.setDeleted( category.isDeleted() );

        return category1;
    }
}

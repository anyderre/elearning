package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Type;
import com.sorbSoft.CabAcademie.Repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    public List<Type> fetchAllType(){
        return typeRepository.findAll();
    }

    public Type fetchRole(Long id){
        return typeRepository.findOne(id);
    }

    public Type updateRole(Type type){
        Type currentType= typeRepository.findOne(type.getId());
        currentType.setType(type.getType());
        return typeRepository.save(currentType);
    }
    public Type saveType(Type type){
        return typeRepository.save(type);
    }
    public void deleteType(Long id){
        typeRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}

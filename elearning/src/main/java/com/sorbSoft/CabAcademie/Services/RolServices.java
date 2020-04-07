package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.RolFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.RolInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.RolMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.RolViewModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dany on 07/09/2017.
 */
@Service
@Transactional
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    private RolMapper mapper
            = Mappers.getMapper(RolMapper.class);

    public List<Rol> fetchAllRole(){
        return rolRepository.findAll();
    }

    public List<Rol> findAllRoleFiltered(){
        return rolRepository.findAll().stream().filter(role ->
                !role.getDescription().equals(Roles.ROLE_ADMIN.name()) && !role.getDescription().equals(Roles.ROLE_SUPER_ADMIN.name())).collect(Collectors.toList());
    }

    public Rol fetchRole(Long id){
        return rolRepository.findOne(id);
    }

    public Rol findRoleByName(String role){
        return rolRepository.findByName(role);
    }

    public Pair<String, Rol> updateRol(RolViewModel vm){
        Rol savedRol = rolRepository.findRolByNameAndIdIsNot(vm.getName(), vm.getId());
        if (savedRol != null) {
            return Pair.of("The role name already exist for another definition", null);
        }
        Rol resultRol = mapper.mapToEntity(vm);
        Rol currentRol= rolRepository.findOne(vm.getId());
        currentRol.setDescription(resultRol.getDescription());
        currentRol.setName(resultRol.getName());

        Rol result = rolRepository.save(currentRol);
        if (result == null) {
            return Pair.of("Couldn't update the role", null);
        } else {
            return Pair.of("Rol updated successfully", result);
        }
    }

    public Pair<String, Rol> saveRole(RolViewModel vm){
        if (vm.getId() > 0L) {
            return updateRol(vm);
        } else {
            Rol savedRol = rolRepository.findByName(vm.getName());

            if (savedRol!= null){
                return Pair.of("The role you are trying to save already exist", null);
            }
            Rol resultRol = mapper.mapToEntity(vm);
            // TODO Devide by every word
            resultRol.setDescription("ROLE_"+ String.join("_", resultRol.getName().toUpperCase().split(" ")));
            Rol result = rolRepository.save(resultRol);
            if (result == null){
                return Pair.of("Couldn't save the role", null);
            } else {
                return  Pair.of("Rol saved successfully", result);
            }
        }
    }

    public String deleteRol(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the role";
        }
        Rol role = fetchRole(id);
        if (role == null) {
            return "The role you want to delete doesn't exist";
        }
        role.setDeleted(true);
        return "Rol successfully deleted";
    }

    public List<Rol> getAllFiltered(Long roleId){
        List<Rol> roles = fetchAllRole();
        if (roleId == null)
            return roles;
        if (roleId <= 0L)
            return new ArrayList<>();
        return roles.size() <= 0 ? new ArrayList<>() : roles.stream().filter(o -> !o.getId().equals(roleId)).collect(Collectors.toList());
    }

    public RolViewModel getRoleViewModel(Long roleId){
        RolViewModel vm = RolFactory.getRolViewModel();
        if (roleId != null && roleId > 0) {
            Rol role = this.fetchRole(roleId);
            if (role == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(role);
            }
        }
        return vm;
    }

    public List <RolInfo> getRolInfo(){
        List<Rol> roles = this.fetchAllRole();
        if (roles.isEmpty()) {
            return new ArrayList<>();
        }
        List<RolInfo> info = new ArrayList<>();
        for (Rol role : roles) {
            RolInfo sInfo = mapper.mapEntityToInfo(role);
            info.add(sInfo);
        }
        return info;
    }

   }

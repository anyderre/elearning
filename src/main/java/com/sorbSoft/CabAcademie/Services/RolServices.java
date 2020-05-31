package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.RolFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.RolInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.RolMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
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
        return rolRepository.getOne(id);
    }

    public Rol findRoleByName(String role){
        return rolRepository.findByName(role);
    }

    public Rol findRoleByDescription(String description){
        return rolRepository.findByDescription(description);
    }

    public Result updateRol(RolViewModel vm){
        Result result = new Result();
        Rol current = rolRepository.getOne(vm.getId());
        if (current == null) {
            result.add("The role you want to update does not exist");
            return result;
        }
        Rol savedRol = rolRepository.findRolByNameAndIdIsNot(vm.getName(), vm.getId());
        if (savedRol != null) {
            return result.add("The role name already exist for another definition");
        }

        return save(vm);
    }

    public Result saveRole(RolViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateRol(vm);
        } else {
            Rol savedRol = rolRepository.findByName(vm.getName());

            if (savedRol!= null){
                result.add("The role you are trying to save already exist");
                return result;
            }

            return save(vm);
        }
    }

    public Result deleteRol(Long id){
        Result result = new Result();
        if (id <= 0L) {
            result.add("You should indicate the id of the role");
            return result;
        }
        Rol role = fetchRole(id);
        if (role == null) {
            return result.add("The role you want to delete doesn't exist");
        }
        role.setDeleted(true);
        try {
            rolRepository.save(role);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public List<Rol> getAllFiltered(Long roleId){
        List<Rol> roles = fetchAllRole();
        if (roleId == null)
            return roles;
        if (roleId <= 0L)
            return new ArrayList<>();
        return roles.size() <= 0 ? new ArrayList<>() : roles.stream().filter(o -> !o.getId().equals(roleId)).collect(Collectors.toList());
    }

    private  Result save (RolViewModel vm) {
        Result result = new Result();
        try {
            rolRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
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

    private Rol getEntity(RolViewModel vm){
        Rol resultRol = mapper.mapToEntity(vm);
        resultRol.setDescription("ROLE_"+ String.join("_", resultRol.getName().trim().toUpperCase().split(" ")));
        return resultRol;
    }

    private Result ValidateModel(RolViewModel vm){
        Result result = new Result();

        if (vm.getName().isEmpty()) {
            result.add("You should specify the rol name");
            return result;
        }

        return result;
    }

    public RolViewModel prepareEntity(RolViewModel vm) {
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getName()== null)  {
            vm.setName("");
        }
        if (vm.getDescription() == null)  {
            vm.setDescription("");
        }
        return vm;
    }
}

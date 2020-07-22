package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.UserMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.email.EmailApiService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UserServicesTest {
    private UserServices userServices;
    private UserRepository userRepository;
    private GenericValidator validator;
    private RolRepository rolRepository;
    private UserMapper mapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailApiService emailAPI;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        validator = mock(GenericValidator.class);
        rolRepository = mock(RolRepository.class);
        mapper = mock(UserMapper.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        emailAPI = mock(EmailApiService.class);

        userServices = new UserServices();
        userServices.setUserRepository(userRepository);
        userServices.setValidator(validator);
        userServices.setRolRepository(rolRepository);
        userServices.setMapper(mapper);
        userServices.setbCryptPasswordEncoder(bCryptPasswordEncoder);
        userServices.setEmailAPI(emailAPI);
    }

    @Test
    public void testBatchUploadUsers() throws Exception {
        User admin = new User();
        admin.setSchools(asList(new User()));
        admin.setPassword("password");

        Rol rol = new Rol();
        rol.setDescription(Roles.ROLE_FREELANCER.name());
        rol.setId((long) Roles.ROLE_FREELANCER.ordinal());
        rol.setRole(Roles.ROLE_FREELANCER);

        User newUser = new User();
        newUser.setRole(rol);
        newUser.setSocialUser(false);

        when(userRepository.findByUsername("admin")).thenReturn(admin);
        when(rolRepository.findOne(3L)).thenReturn(rol);
        when(mapper.mapToEntity(any(UserViewModel.class))).thenReturn(newUser);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encryotedPwd");

        MultipartFile file = prepareFileMock("First Name;Last name;Username;Email;Password;Country;Role Id;Timezone\n" +
                                                   "Volodymyr;Bondarchuk2;postuf243;xayog88624@frost2d.net;12345678;Ukraine;3;+3:00\n" +
                                                   "Volodymyr2;Bondarchuk2;postuf243;xayog88624@frost2d.net;12345678;Ukraine;3;+3:00\n");

        userServices.batchSignupUsers(file, "admin");

        verify(userRepository, times(2)).save(eq(newUser));
    }

    private MultipartFile prepareFileMock(String string) throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        when(file.getInputStream()).thenReturn(inputStream);

        return file;
    }
}
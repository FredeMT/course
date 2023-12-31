package com.ead.course.validation;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;


@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private AuthUserClient authUserClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        //Valida o courseDto - substituindo @Valid.
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto, errors);
        //se não tiver erros com a validacao acima executamos nosso validador definido abaixo.
        if(!errors.hasErrors()){
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }
//definimos as validacoes, são duas, primeiro busca em AuthUser o user e verifica se UserType é STUDENT ou não encontrado.
    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        ResponseEntity<UserDto> responseUserInstructor;
        try {
            responseUserInstructor = authUserClient.getOneUserById(userInstructor);
            if(responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)){
                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
            }
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
            }
        }
    }
}

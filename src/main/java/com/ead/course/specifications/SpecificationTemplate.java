package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {
    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @And({
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "userType", spec = Equal.class)
    })
    public interface UserSpec extends  Specification<UserModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}
    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);   //pesquisa registros únicos, sem valores duplicados.
            Root<ModuleModel> module = root;    //Entidades (Root) que fazem parte da consulta: Entidade A (module).
            Root<CourseModel> course = query.from(CourseModel.class);   //Entidades (Root) que fazem parte da consulta: Entidade B (course).
            Expression<Collection<ModuleModel>> courseModules = course.get("modules"); //Buscar a colecction (lista) de entidades A (modules) vinculadas/pertencentes a entidade B (course).
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, courseModules));
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);   //pesquisa registros únicos, sem valores duplicados.
            Root<LessonModel> lesson = root;    //Entidades (Root) que fazem parte da consulta: Entidade A (lesson).
            Root<ModuleModel> module = query.from(ModuleModel.class);   //Entidades (Root) que fazem parte da consulta: Entidade B (module).
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons"); //Buscar a colecction (lista) de entidades A (lessons) vinculadas/pertencentes a entidade B (module).
            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
        };
    }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);   //pesquisa registros únicos, sem valores duplicados.
            Root<UserModel> user = root;    //Entidades (Root) que fazem parte da consulta: Entidade A (user).
            Root<CourseModel> course = query.from(CourseModel.class);   //Entidades (Root) que fazem parte da consulta: Entidade B (course).
            Expression<Collection<UserModel>> courseUsers = course.get("users"); //Buscar a colecction (lista) de entidades A (users) vinculadas/pertencentes a entidade B (course).
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(user, courseUsers));
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);   //pesquisa registros únicos, sem valores duplicados.
            Root<CourseModel> course = root;    //Entidades (Root) que fazem parte da consulta: Entidade A (course).
            Root<UserModel> user = query.from(UserModel.class);   //Entidades (Root) que fazem parte da consulta: Entidade B (user).
            Expression<Collection<CourseModel>> usersCourses = user.get("courses"); //Buscar a colecction (lista) de entidades A (courses) vinculadas/pertencentes a entidade B (user).
            return cb.and(cb.equal(user.get("userId"), userId), cb.isMember(course, usersCourses));
        };
    }

}

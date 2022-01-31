/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.ws.soap;

import com.eltonb.ws.model.StudentRequest;
import com.eltonb.ws.model.CourseData;
import com.eltonb.ws.model.GradeRequest;
import com.eltonb.ws.model.GradeResponse;
import com.eltonb.ws.model.DepartmentData;
import com.eltonb.ws.model.StudentData;
import com.eltonb.ws.model.StudentResponse;
import com.eltonb.ws.db.entities.StudentCoursePK;
import com.eltonb.ws.db.entities.Department;
import com.eltonb.ws.db.entities.Student;
import com.eltonb.ws.db.entities.StudentCourse;
import com.eltonb.ws.utils.Utilities;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

/**
 *
 * @author elton.ballhysa
 */
@WebService
public class StudentRegistry {
    /*
    @PersistenceContext(unitName = "SudentSoapPU-jta")
    private EntityManager newEntityManager;
    */
    
    @WebMethod
    public @WebResult(name="response") StudentResponse readStudent(@WebParam(name = "request") StudentRequest request) {
        EntityManager em = newEntityManager();
        Student student = em.find(Student.class, request.getStudentId());
        StudentData studentData = new StudentData();
        studentData.setId(student.getId());
        studentData.setName(student.getName());
        studentData.setSurname(student.getSurname());

        int totalCredits = student
                                .getStudentCoursesList()
                                .stream()
                                .map(sc -> sc.getCourse())
                                .mapToInt(c -> c.getCredits())
                                .sum();
        double totalPoints = student
                                .getStudentCoursesList()
                                .stream()
                                .mapToDouble(this::earnedPoints)
                                .sum();
        studentData.setTotalCredits(totalCredits);
        studentData.setGpa(totalPoints / totalCredits);
        
        student
            .getStudentCoursesList()
            .stream()
            .map(this::toCourseData)
            .forEach(studentData.getCourses()::add);

        StudentResponse response = new StudentResponse();
        response.setStudent(studentData);
        return response;
    }
    
    private CourseData toCourseData(StudentCourse sc) {
        CourseData courseData = new CourseData();
        courseData.setCode(sc.getCourse().getCode());
        courseData.setTitle(sc.getCourse().getTitle());
        courseData.setCredits(sc.getCourse().getCredits());
        courseData.setGrade(sc.getLetterGrade());
        return courseData;
    }

    private double earnedPoints(StudentCourse sc) {
        int credits = sc.getCourse().getCredits();
        double lg_points = Utilities.creditPoints(sc.getLetterGrade());
        return credits * lg_points;
    }
    
    @WebMethod(operationName = "enterDepartment")
    public @WebResult(name="department") DepartmentData upsertDepartment(@WebParam(name="department")DepartmentData departmentData) throws Exception {
        EntityManager em = newEntityManager();
        em.getTransaction().begin();
        Department department = em.find(Department.class, departmentData.getCode());
        if (department == null) {
            department = new Department(departmentData.getCode());
        }
        department.setName(departmentData.getName());
        em.persist(department);
        em.getTransaction().commit();
        return departmentData;
    }
    
    @WebMethod
    public GradeResponse submitGrade(@WebParam(name = "request") GradeRequest gradeRequest) {
        EntityManager em = newEntityManager();
        StudentCoursePK scpk = new StudentCoursePK();
        scpk.setStudentId(gradeRequest.getStudentId());
        scpk.setCourseCode(gradeRequest.getCourseCode());
        scpk.setSemesterCode(gradeRequest.getSemesterCode());
        StudentCourse sc = em.find(StudentCourse.class, scpk);
        if (sc == null) {
            sc = new StudentCourse(scpk);
        }
        String lg = Utilities.letterGradeOf(gradeRequest.getFinalGrade());
        sc.setFinalGrade(gradeRequest.getFinalGrade());
        sc.setLetterGrade(lg);
        sc.setInstructor(gradeRequest.getInstructorName());
        em.getTransaction().begin();
        em.persist(sc);
        em.getTransaction().commit();
        em.refresh(sc);

        GradeResponse response = new GradeResponse();
        response.setStudentId(sc.getStudent().getId());
        response.setStudentFullName(sc.getStudent().getName() + " " + sc.getStudent().getSurname());
        response.setCourseCode(sc.getCourse().getCode());
        response.setCourseTitle(sc.getCourse().getTitle());
        response.setSemesterName(sc.getSemester().getName());
        response.setFinalGrade(sc.getFinalGrade());
        response.setLetterGrade(sc.getLetterGrade());
        return response;
    }
    
    private EntityManager newEntityManager() {
        return Persistence
                    .createEntityManagerFactory("SudentSoapPU-local")
                    .createEntityManager();
    }
}

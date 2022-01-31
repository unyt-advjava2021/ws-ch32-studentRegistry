/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.ws.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author elton.ballhysa
 */
public class Utilities {

    public static EntityManager entityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("StudentRegistry-workingPU");
        return emf.createEntityManager();
    }
    
    public static double creditPoints(String letterGrade) {
        if ("A".equals(letterGrade))
            return 4.00;
        if ("A-".equals(letterGrade))
            return 3.67;
        if ("B+".equals(letterGrade))
            return 3.33;
        if ("B".equals(letterGrade))
            return 3.00;
        if ("B-".equals(letterGrade))
            return 2.67;
        if ("C+".equals(letterGrade))
            return 2.33;
        if ("C".equals(letterGrade))
            return 2.00;
        if ("C-".equals(letterGrade))
            return 1.67;
        if ("D+".equals(letterGrade))
            return 1.33;
        if ("D".equals(letterGrade))
            return 1.00;
        if ("D-".equals(letterGrade))
            return 0.67;
        
        return 0.0;
    }
    
    public static String letterGradeOf(double overallGrade) {
        if (overallGrade >= 95) return "A";
        if (overallGrade >= 90) return "A-";
        if (overallGrade >= 87) return "B+";
        if (overallGrade >= 83) return "B";
        if (overallGrade >= 80) return "B-";
        if (overallGrade >= 77) return "C+";
        if (overallGrade >= 73) return "C";
        if (overallGrade >= 70) return "C-";
        if (overallGrade >= 67) return "D+";
        if (overallGrade >= 63) return "D";
        if (overallGrade >= 60) return "D-";
        return "F";
    }
}

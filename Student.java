package josh;

import java.util.ArrayList;  

public class Student {  
    private String name; // Student's name  
    private String studentId; // Student's ID  
    private ArrayList<String> enrolledCourses; // List of enrolled courses  
    private ArrayList<Double> grades; // List of grades corresponding to each course  

    // Constructor to initialize a student  
    public Student(String name, String studentId) {  
        this.name = name;  
        this.studentId = studentId;  
        this.enrolledCourses = new ArrayList<>();  
        this.grades = new ArrayList<>();  
    }  

    // Get the student's name  
    public String getName() {  
        return name;  
    }  

    // Set the student's name  
    public void setName(String name) {  
        this.name = name;  
    }  

    // Get the student's ID  
    public String getStudentId() {  
        return studentId;  
    }  

    // Set the student's ID  
    public void setStudentId(String studentId) {  
        this.studentId = studentId;  
    }  

    // Get the list of enrolled courses  
    public ArrayList<String> getEnrolledCourses() {  
        return enrolledCourses;  
    }  

    // Get the list of grades  
    public ArrayList<Double> getGrades() {  
        return grades;  
    }  

    // Enroll in a new course  
    public void enrollCourse(String course) {  
        if (!enrolledCourses.contains(course)) {  
            enrolledCourses.add(course);  
            grades.add(null); // Add a null grade for this course by default  
        } else {  
            System.out.println("Already enrolled in " + course);  
        }  
    }  

    // Set the grade for a specific course  
    public void setGrade(int index, double grade) {  
        if (index >= 0 && index < grades.size()) {  
            grades.set(index, grade);  
        } else {  
            throw new IndexOutOfBoundsException("Invalid course index for setting grade.");  
        }  
    }  

    // Get the grade for a specific course  
    public Double getGrade(int index) {  
        if (index >= 0 && index < grades.size()) {  
            return grades.get(index);  
        } else {  
            throw new IndexOutOfBoundsException("Invalid course index for getting grade.");  
        }  
    }  
} 




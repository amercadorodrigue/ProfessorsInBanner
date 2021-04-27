package org.example;

public class Courses {

    private String subject;
    private String courseNumber;
    private String instructor;
    private int numStudents;
    private String courseTitle;
    private String term;
    private int creditHours;

    public Courses(String subject, String courseNumber, String instructor, int numStudents, String courseTitle, String term, int creditHours) {
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.instructor = instructor;
        this.numStudents = numStudents;
        this.courseTitle = courseTitle;
        this.term = term;
        this.creditHours = creditHours;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    @Override
    public String toString() {
        return "Course{" +
                "subject='" + subject + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", instructor='" + instructor + '\'' +
                ", numStudents=" + numStudents +
                ", courseTitle='" + courseTitle + '\'' +
                ", term='" + term + '\'' +
                ", creditHours=" + creditHours +
                '}';
    }
}

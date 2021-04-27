package org.example;

import static org.junit.Assert.assertTrue;
import java.sql.*;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppTest 
{
    public static final String dbURL = "jdbc:sqlite:BannerDB.db";
    private static Connection connection;

    @BeforeClass
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(dbURL);
    }


    @Test
    public void testGetCourses(){
        List<Courses> courses = BannerUtil.getProfCourses("Tacksoo Im", "Spring 2021");
        Assert.assertEquals(4, courses.size());
    }

    @Test
    public void testGetCourses2(){
        List<Courses> courses = BannerUtil.getProfCourses("Tacksoo Im", "Fall 2020");
        Assert.assertEquals(4, courses.size());
    }

    @Test
    public void getNumStudents(){
        List<Courses> courses = BannerUtil.getProfCourses("Tacksoo Im", "Fall 2020");
        int studentsTotal = 0;
        for (int i = 0; i < courses.size(); i++) {
            studentsTotal += courses.get(i).getNumStudents();
        }
        Assert.assertEquals(87, studentsTotal);
    }

    @Test
    public void getListSemesters(){
        List<String> semesterList = BannerUtil.getListSemester("Fall 2019","Spring 2021");
        List<String> semesters = new ArrayList<>();
        semesters.add("Fall 2019");
        semesters.add("Spring 2020");
        semesters.add("Summer 2020");
        semesters.add("Fall 2020");
        semesters.add("Spring 2021");
        Assert.assertEquals(semesters, semesterList);

        List<String> semesterList2 = BannerUtil.getListSemester("Summer 2020","Spring 2021");
        List<String> semesters2 = new ArrayList<>();
        semesters2.add("Summer 2020");
        semesters2.add("Fall 2020");
        semesters2.add("Spring 2021");
        Assert.assertEquals(semesters2, semesterList2);

        List<String> semesterList3 = BannerUtil.getListSemester("Spring 2010","Fall 2020");
        Assert.assertEquals(33, semesterList3.size());
        Assert.assertEquals(semesterList3.get(3),"Spring 2011");
    }

    @Test
    public void testVerifyTacksooCourse() throws IOException, SQLException {
        List<String> semesterList = BannerUtil.getListSemester("Fall 2010", "Spring 2021");
        Assert.assertEquals(32,semesterList.size());
        List<Courses> courses = new ArrayList<>();
        for (int i = 0; i < semesterList.size(); i++) {
            List<Courses> temp = BannerUtil.getProfCourses("Tacksoo Im", semesterList.get(i));

            for (int j = 0; j < temp.size(); j++) {
                courses.add(temp.get(j));
            }
        }

        FileUtils.writeStringToFile(new File("log.txt"),"Fall2010-Spring2021\n","UTF-8");
        for (int i = 0; i < courses.size(); i++) {
            FileUtils.writeStringToFile(new File("log.txt"),courses.get(i).toString() + "\n", "UTF-8", true);
            addCourseToDB(courses.get(i).getSubject(), courses.get(i).getCourseTitle(), courses.get(i).getCourseNumber(), courses.get(i).getTerm(), courses.get(i).getCreditHours(), courses.get(i).getNumStudents(), 3 );
        }
    }

    @Test
    public void testVerifyPollaciaCourse() throws IOException, SQLException {
        List<String> semesterList = BannerUtil.getListSemester("Fall 2010", "Spring 2021");
        Assert.assertEquals(32,semesterList.size());
        List<Courses> courses = new ArrayList<>();
        for (int i = 0; i < semesterList.size(); i++) {
            List<Courses> temp = BannerUtil.getProfCourses("Lissa Faye Pollacia", semesterList.get(i));

            for (int j = 0; j < temp.size(); j++) {
                courses.add(temp.get(j));
            }
        }

        FileUtils.writeStringToFile(new File("log.txt"),"Fall2010-Spring2021\n","UTF-8");
        for (int i = 0; i < courses.size(); i++) {
            FileUtils.writeStringToFile(new File("log.txt"),courses.get(i).toString() + "\n", "UTF-8", true);
            addCourseToDB(courses.get(i).getSubject(), courses.get(i).getCourseTitle(), courses.get(i).getCourseNumber(), courses.get(i).getTerm(), courses.get(i).getCreditHours(), courses.get(i).getNumStudents(), 1 );
        }
    }

    @Test
    public void testVerifyGunayCourse() throws IOException, SQLException {
        List<String> semesterList = BannerUtil.getListSemester("Fall 2016", "Spring 2021");
        Assert.assertEquals(14,semesterList.size());
        List<Courses> courses = new ArrayList<>();
        for (int i = 0; i < semesterList.size(); i++) {
            List<Courses> temp = BannerUtil.getProfCourses("Cengiz Gunay", semesterList.get(i));

            for (int j = 0; j < temp.size(); j++) {
                courses.add(temp.get(j));
            }
        }

        FileUtils.writeStringToFile(new File("log.txt"),"Fall2010-Spring2021\n","UTF-8");
        for (int i = 0; i < courses.size(); i++) {
            FileUtils.writeStringToFile(new File("log.txt"),courses.get(i).toString() + "\n", "UTF-8", true);
            addCourseToDB(courses.get(i).getSubject(), courses.get(i).getCourseTitle(), courses.get(i).getCourseNumber(), courses.get(i).getTerm(), courses.get(i).getCreditHours(), courses.get(i).getNumStudents(), 2 );
        }
    }


    public void addCourseToDB(String subject, String title, String courseNumber, String semester, int credits, int students,int id) throws SQLException {
        String sql ="insert into COURSES values(null,?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, subject);
        ps.setString(2, title);
        ps.setString(3, courseNumber);
        ps.setString(4, semester);
        ps.setInt(5, credits);
        ps.setInt(6, students);
        ps.setInt(7, id);
        ps.executeUpdate();
        ps.close();
    }

    @Test
    public void testProfessorTeachings() throws SQLException {
        //How many courses Dr Tacksoo taught overall
        String sql1 = "select count(id) from COURSES where professor_id =3;";
        //How many students Dr tacksoo taught
        String sql2 = "select SUM(number_of_students) from COURSES where professor_id=3;";
        //How many course taught in the fall
        String sql3 = "select COUNT(semester_offered) from COURSES where professor_id = 3 AND where semester_offered LIKE \"Fall%\";";
        //How many course taught in the Spring
        String sql4 = "select COUNT(semester_offered) from COURSES where professor_id = 3 AND where semester_offered LIKE \"Spring%\";";
        //How many course taught in the Summer
        String sql5 = "select COUNT(semester_offered) from COURSES where professor_id = 3 AND where semester_offered LIKE \"Summer%\";";
        //How many specific classes has Dr Tacksoo
        String sql6 = "select COUNT(course_number) from COURSES where professor_id = 3;";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql1);
        System.out.println(rs.getInt(1));
    }
}

package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BannerUtil {
    private static WebDriver driver;
    private static String BANNER_URL = "https://ggc.gabest.usg.edu/pls/B400/bwckschd.p_disp_dyn_sched";

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        driver = new ChromeDriver(options);
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }


    public static List<Courses> getProfCourses(String profName, String semester) {
        driver.get("https://banner.ggc.edu");
        //here click on courses schedule link
        WebElement coursesLink = driver.findElement(By.xpath("/html/body/div[3]/div/table/tbody/tr/td/span/p[3]/a"));
        coursesLink.click();

        Select semesterDropDown = new Select(driver.findElement(By.xpath("/html/body/div[3]/form/table/tbody/tr/td/select")));
        //go thru index until finding right semester
        int i = 1;
        while (true) {
            semesterDropDown.selectByIndex(i);
            String currentSemester = semesterDropDown.getFirstSelectedOption().getText();
            if (currentSemester.contains(semester)) {
                System.out.println(currentSemester);
                break;
            }
            i++;
        }
        WebElement submitButton = driver.findElement(By.xpath("/html/body/div[3]/form/input[2]"));
        submitButton.click();

        Select subjectDropDown = new Select(driver.findElement(By.xpath("//*[@id=\"subj_id\"]")));
        subjectDropDown.selectByValue("ITEC");

        WebElement classSearchButton = driver.findElement(By.xpath("/html/body/div[3]/form/input[12]"));
        classSearchButton.click();

        List<Courses> courses = new ArrayList<>();
        WebElement courseTable = driver.findElement(By.xpath("/html/body/div[3]/table[1]"));
        List<WebElement> coursesRows = courseTable.findElements(By.xpath("/html/body/div[3]/table[1]/tbody/tr"));//its in the table fot every table there are 2 trs
        for (int j = 1; j < coursesRows.size(); j += 2) {

            WebElement heading = driver.findElement(By.xpath("/html/body/div[3]/table[1]/tbody/tr[" + j + "]"));
            WebElement body = driver.findElement(By.xpath("/html/body/div[3]/table[1]/tbody/tr[" + (j + 1) + "]"));
            WebElement courseLink = heading.findElement(By.tagName("a"));
            String profXpath = "/html/body/div[3]/table[1]/tbody/tr[" + (j + 1) + "]/td/table/tbody/tr[2]/td[7]";
            WebElement profElement = driver.findElement(By.xpath(profXpath));
            String currentProf = profElement.getText().replaceAll("\\(P\\)", "").trim();
            String courseUrl = courseLink.getAttribute("href");
            String courseHeading = courseLink.getText();
            if (!courseHeading.contains("-")) {
                // somethings wrong
                return null;
            }
            String courseName = courseHeading.substring(0, courseHeading.indexOf("-")).trim();
            String[] tokens = courseHeading.split("-");
            String courseString = tokens[tokens.length - 2];
            String subject = courseString.split(" ")[1];
            String courseNumber = courseString.split(" ")[2];
            courseLink.click();
            String numberOfStudents = driver.findElement(By.xpath("/html/body/div[3]/table[1]/tbody/tr[2]/td/table/tbody/tr[2]/td[2]")).getText();
            //driver.navigate().back();
            WebElement creditsBody = driver.findElement(By.xpath("/html/body/div[3]/table[1]/tbody/tr[2]/td"));
            String credits = creditsBody.getText();
            Pattern p = Pattern.compile("(\\d\\.\\d\\d\\d)");
            Matcher m = p.matcher(credits);
            if (m.find()) {
                credits = m.group(0);
            }
            WebElement goBackToCourses = driver.findElement(By.xpath("/html/body/div[3]/table[2]/tbody/tr/td/a"));
            goBackToCourses.click();

            System.out.println(courseName + " " + courseString + " " + numberOfStudents + " " + credits + " " + currentProf);
            if (currentProf.equals(profName)) {
                Courses course = new Courses(subject, courseNumber, profName, Integer.parseInt(numberOfStudents), courseName, semester, (int) Double.parseDouble(credits));
                courses.add(course);
            }
        }
        return courses;
    }

    public static List<String> getListSemester(String start, String end) {
        List<String> semesterList = new ArrayList<>();
        semesterList.add(start);
        String current = start;
        while (!current.equals(end)) {
            String season = current.split(" ")[0];
            int year = Integer.parseInt(current.split(" ")[1]);
            if (season.equals("Spring")) {
                season = "Summer";
            } else if (season.equals("Summer")) {
                season = "Fall";
            } else if (season.equals("Fall")) {
                season = "Spring";
                year++;
            }
            current = season + " " + year;
            semesterList.add(current);
        }
        return semesterList;
    }
}

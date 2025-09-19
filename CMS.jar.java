import java.io.*;
import java.util.*;

public class CMS {

    // ---------------------------- USERS ----------------------------
    static final int MAX_USERS = 1000;
    static String[] usernames = new String[MAX_USERS];
    static String[] passwords = new String[MAX_USERS];
    static int userCount = 0;

    // Load users from file
    static void loadUsers() {
        userCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null && userCount < MAX_USERS) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    usernames[userCount] = parts[0];
                    passwords[userCount] = parts[1];
                    userCount++;
                }
            }
        } catch (IOException e) {
            // file may not exist on first run
        }
    }

    // Save users to file
    static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt"))) {
            for (int i = 0; i < userCount; i++) {
                pw.println(usernames[i] + "," + passwords[i]);
            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    // Register user
    static void registerUser(Scanner sc) {
        System.out.print("Enter new username: ");
        String uname = sc.next();
        System.out.print("Enter password: ");
        String pword = sc.next();

        usernames[userCount] = uname;
        passwords[userCount] = pword;
        userCount++;

        saveUsers();
        System.out.println("User registered successfully!");
    }

    // Login
    static boolean login(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.next();
        System.out.print("Password: ");
        String pword = sc.next();

        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(uname) && passwords[i].equals(pword)) {
                System.out.println("Login successful!");
                return true;
            }
        }
        System.out.println("Invalid username or password.");
        return false;
    }

    // ---------------------------- COURSES ----------------------------
    static final int MAX_COURSES = 1000;
    static String[] courseIds = new String[MAX_COURSES];
    static String[] courseTitles = new String[MAX_COURSES];
    static int[] creditHours = new int[MAX_COURSES];
    static int courseCount = 0;

    // Load courses
    static void loadCourses() {
        courseCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("courses.txt"))) {
            String line;
            while ((line = br.readLine()) != null && courseCount < MAX_COURSES) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    courseIds[courseCount] = parts[0];
                    courseTitles[courseCount] = parts[1];
                    creditHours[courseCount] = Integer.parseInt(parts[2]);
                    courseCount++;
                }
            }
        } catch (IOException e) {
            // ignore if file not found
        }
    }

    // Save courses
    static void saveCourses() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("courses.txt"))) {
            for (int i = 0; i < courseCount; i++) {
                pw.println(courseIds[i] + "," + courseTitles[i] + "," + creditHours[i]);
            }
        } catch (IOException e) {
            System.out.println("Error saving courses.");
        }
    }

    // Add course
    static void addCourse(Scanner sc) {
        if (courseCount >= MAX_COURSES) {
            System.out.println("Course limit reached!");
            return;
        }
        System.out.print("Enter Course ID: ");
        String id = sc.next();
        // prevent duplicates
        for (int i = 0; i < courseCount; i++) {
            if (courseIds[i].equals(id)) {
                System.out.println("Duplicate Course ID!");
                return;
            }
        }
        sc.nextLine(); // consume newline
        System.out.print("Enter Course Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Credit Hours (1-6): ");
        int hours = sc.nextInt();
        if (hours < 1 || hours > 6) {
            System.out.println("Invalid credit hours!");
            return;
        }

        courseIds[courseCount] = id;
        courseTitles[courseCount] = title;
        creditHours[courseCount] = hours;
        courseCount++;

        saveCourses();
        System.out.println("Course added!");
    }

    // List all courses
    static void listCourses() {
        System.out.println("\n--- All Courses ---");
        for (int i = 0; i < courseCount; i++) {
            System.out.println(courseIds[i] + " | " + courseTitles[i] + " | " + creditHours[i]);
        }
    }

    // Search course
    static void searchCourse(Scanner sc) {
        System.out.print("Enter Course ID or Title keyword: ");
        String keyword = sc.next();
        boolean found = false;
        for (int i = 0; i < courseCount; i++) {
            if (courseIds[i].equals(keyword) || courseTitles[i].contains(keyword)) {
                System.out.println("Found: " + courseIds[i] + " | " + courseTitles[i] + " | " + creditHours[i]);
                found = true;
            }
        }
        if (!found) System.out.println("Course not found!");
    }

    // Delete course
    static void deleteCourse(Scanner sc) {
        System.out.print("Enter Course ID to delete: ");
        String id = sc.next();
        for (int i = 0; i < courseCount; i++) {
            if (courseIds[i].equals(id)) {
                for (int j = i; j < courseCount - 1; j++) {
                    courseIds[j] = courseIds[j+1];
                    courseTitles[j] = courseTitles[j+1];
                    creditHours[j] = creditHours[j+1];
                }
                courseCount--;
                saveCourses();
                System.out.println("Course deleted!");
                return;
            }
        }
        System.out.println("Course not found!");
    }

    // Update course
    static void updateCourse(Scanner sc) {
        System.out.print("Enter Course ID to update: ");
        String id = sc.next();
        sc.nextLine();
        for (int i = 0; i < courseCount; i++) {
            if (courseIds[i].equals(id)) {
                System.out.print("Enter new Course Title: ");
                courseTitles[i] = sc.nextLine();
                System.out.print("Enter new Credit Hours (1-6): ");
                int hours = sc.nextInt();
                if (hours < 1 || hours > 6) {
                    System.out.println("Invalid credit hours!");
                    return;
                }
                creditHours[i] = hours;
                saveCourses();
                System.out.println("Course updated!");
                return;
            }
        }
        System.out.println("Course not found!");
    }

    // ---------------------------- MENUS ----------------------------
    static void courseMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- Course Management Menu ---");
            System.out.println("1. Add Course");
            System.out.println("2. Delete Course");
            System.out.println("3. Search Course");
            System.out.println("4. Update Course");
            System.out.println("5. List All Courses");
            System.out.println("6. Logout/Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1: addCourse(sc); break;
                case 2: deleteCourse(sc); break;
                case 3: searchCourse(sc); break;
                case 4: updateCourse(sc); break;
                case 5: listCourses(); break;
                case 6: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 6);
    }

    // ---------------------------- MAIN ----------------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadUsers();
        loadCourses();

        int choice;
        do {
            System.out.println("\n--- Welcome ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                registerUser(sc);
            } else if (choice == 2) {
                if (login(sc)) {
                    courseMenu(sc);
                }
            } else if (choice == 3) {
                System.out.println("Exiting program...");
            } else {
                System.out.println("Invalid choice!");
            }
        } while (choice != 3);

        sc.close();
    }
}


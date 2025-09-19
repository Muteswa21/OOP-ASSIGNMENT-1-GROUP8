#include <iostream>
#include <fstream>
#include <sstream>
using namespace std;

// ---------------------------- USERS ----------------------------
const int MAX_USERS = 1000;
string usernames[MAX_USERS];
string passwords[MAX_USERS];
int userCount = 0;

// Load users from file
void loadUsers() {
    ifstream file("users.txt");
    string line;
    userCount = 0;
    while (getline(file, line) && userCount < MAX_USERS) {
        stringstream ss(line);
        string uname, pword;
        getline(ss, uname, ',');
        getline(ss, pword, ',');
        usernames[userCount] = uname;
        passwords[userCount] = pword;
        userCount++;
    }
    file.close();
}

// Save users to file
void saveUsers() {
    ofstream file("users.txt");
    for (int i = 0; i < userCount; i++) {
        file << usernames[i] << "," << passwords[i] << "\n";
    }
    file.close();
}

// Register a new user
void registerUser() {
    string uname, pword;
    cout << "Enter new username: ";
    cin >> uname;
    cout << "Enter password: ";
    cin >> pword;

    usernames[userCount] = uname;
    passwords[userCount] = pword;
    userCount++;

    saveUsers();
    cout << "User registered successfully!\n";
}

// Login
bool login() {
    string uname, pword;
    cout << "Username: ";
    cin >> uname;
    cout << "Password: ";
    cin >> pword;

    for (int i = 0; i < userCount; i++) {
        if (usernames[i] == uname && passwords[i] == pword) {
            cout << "Login successful!\n";
            return true;
        }
    }
    cout << "Invalid username or password.\n";
    return false;
}

// ---------------------------- COURSES ----------------------------
const int MAX_COURSES = 1000;
int courseIds[MAX_COURSES];
string courseNames[MAX_COURSES];
string courseLecturers[MAX_COURSES];
int courseCount = 0;

// Load courses from file
void loadCourses() {
    ifstream file("courses.txt");
    string line;
    courseCount = 0;
    while (getline(file, line) && courseCount < MAX_COURSES) {
        stringstream ss(line);
        string idStr, name, lecturer;
        getline(ss, idStr, ',');
        getline(ss, name, ',');
        getline(ss, lecturer, ',');
        courseIds[courseCount] = stoi(idStr);
        courseNames[courseCount] = name;
        courseLecturers[courseCount] = lecturer;
        courseCount++;
    }
    file.close();
}

// Save courses to file
void saveCourses() {
    ofstream file("courses.txt");
    for (int i = 0; i < courseCount; i++) {
        file << courseIds[i] << ","
             << courseNames[i] << ","
             << courseLecturers[i] << "\n";
    }
    file.close();
}

// Add new course
void addCourse() {
    if (courseCount >= MAX_COURSES) {
        cout << "Course limit reached!\n";
        return;
    }
    int id;
    string name, lecturer;
    cout << "Enter Course ID: ";
    cin >> id;
    cin.ignore();
    cout << "Enter Course Name: ";
    getline(cin, name);
    cout << "Enter Lecturer: ";
    getline(cin, lecturer);

    courseIds[courseCount] = id;
    courseNames[courseCount] = name;
    courseLecturers[courseCount] = lecturer;
    courseCount++;

    saveCourses();
    cout << "Course added!\n";
}

// List all courses
void listCourses() {
    cout << "\n--- All Courses ---\n";
    for (int i = 0; i < courseCount; i++) {
        cout << courseIds[i] << " | "
             << courseNames[i] << " | "
             << courseLecturers[i] << endl;
    }
}

// Search for a course
void searchCourse() {
    int id;
    cout << "Enter Course ID to search: ";
    cin >> id;
    for (int i = 0; i < courseCount; i++) {
        if (courseIds[i] == id) {
            cout << "Found: " << courseIds[i] << " | "
                 << courseNames[i] << " | "
                 << courseLecturers[i] << endl;
            return;
        }
    }
    cout << "Course not found!\n";
}

// Delete a course
void deleteCourse() {
    int id;
    cout << "Enter Course ID to delete: ";
    cin >> id;
    bool found = false;
    for (int i = 0; i < courseCount; i++) {
        if (courseIds[i] == id) {
            for (int j = i; j < courseCount - 1; j++) {
                courseIds[j] = courseIds[j+1];
                courseNames[j] = courseNames[j+1];
                courseLecturers[j] = courseLecturers[j+1];
            }
            courseCount--;
            found = true;
            break;
        }
    }
    if (found) {
        saveCourses();
        cout << "Course deleted!\n";
    } else {
        cout << "Course not found!\n";
    }
}

// Update a course
void updateCourse() {
    int id;
    cout << "Enter Course ID to update: ";
    cin >> id;
    cin.ignore();
    for (int i = 0; i < courseCount; i++) {
        if (courseIds[i] == id) {
            cout << "Enter new Course Name: ";
            getline(cin, courseNames[i]);
            cout << "Enter new Lecturer: ";
            getline(cin, courseLecturers[i]);
            saveCourses();
            cout << "Course updated!\n";
            return;
        }
    }
    cout << "Course not found!\n";
}

// ---------------------------- MAIN MENU ----------------------------
void courseMenu() {
    int choice;
    do {
        cout << "\n--- Course Management Menu ---\n";
        cout << "1. Add Course\n";
        cout << "2. Delete Course\n";
        cout << "3. Search Course\n";
        cout << "4. Update Course\n";
        cout << "5. List All Courses\n";
        cout << "6. Logout/Exit\n";
        cout << "Enter choice: ";
        cin >> choice;

        switch (choice) {
            case 1: addCourse(); break;
            case 2: deleteCourse(); break;
            case 3: searchCourse(); break;
            case 4: updateCourse(); break;
            case 5: listCourses(); break;
            case 6: cout << "Logging out...\n"; break;
            default: cout << "Invalid choice!\n";
        }
    } while (choice != 6);
}

// ---------------------------- PROGRAM START ----------------------------
int main() {
    loadUsers();
    loadCourses();

    int choice;
    do {
        cout << "\n--- Welcome ---\n";
        cout << "1. Register\n";
        cout << "2. Login\n";
        cout << "3. Exit\n";
        cout << "Enter choice: ";
        cin >> choice;

        if (choice == 1) {
            registerUser();
        } else if (choice == 2) {
            if (login()) {
                courseMenu();
            }
        } else if (choice == 3) {
            cout << "Exiting program...\n";
        } else {
            cout << "Invalid choice!\n";
        }
    } while (choice != 3);

    return 0;
}

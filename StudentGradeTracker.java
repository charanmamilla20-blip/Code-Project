import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * The main class for the Student Grade Tracker application.
 * It manages the list of students and performs calculations.
 */
public class StudentGradeTracker {

    // Nested class to represent a single student with a name and a numerical score.
    private static class Student {
        private String name;
        private double score;

        /**
         * Constructor for the Student class.
         * @param name The name of the student.
         * @param score The numerical grade/score of the student.
         */
        public Student(String name, double score) {
            this.name = name;
            this.score = score;
        }

        // --- Getters ---
        public String getName() {
            return name;
        }

        public double getScore() {
            return score;
        }

        /**
         * Provides a formatted string representation of the student data.
         */
        @Override
        public String toString() {
            return String.format("%-20s | %.2f", name, score);
        }
    }

    private List<Student> students;
    private Scanner scanner;

    public StudentGradeTracker() {
        this.students = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu and handles user input loop.
     */
    public void run() {
        int choice = -1;
        System.out.println("--- Welcome to the Student Grade Tracker ---");
        while (choice != 4) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add New Student Grade");
            System.out.println("2. Display Summary Report (Scores, Average, High, Low)");
            System.out.println("3. Clear All Data");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        displayReport();
                        break;
                    case 3:
                        clearData();
                        break;
                    case 4:
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the menu choice.");
                scanner.nextLine(); // Clear the invalid input
                choice = -1; // Reset choice to keep loop running
            }
        }
    }

    /**
     * Prompts the user for student name and score, and adds the new Student object to the list.
     */
    private void addStudent() {
        System.out.println("\n--- Add Student Grade ---");
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        double score = -1;
        boolean validScore = false;
        while (!validScore) {
            try {
                System.out.print("Enter student score (0-100): ");
                score = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                if (score >= 0 && score <= 100) {
                    students.add(new Student(name, score));
                    System.out.printf("✅ Successfully added: %s with score %.2f\n", name, score);
                    validScore = true;
                } else {
                    System.out.println("Score must be between 0 and 100.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid numerical score.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    /**
     * Clears all student data from the list.
     */
    private void clearData() {
        if (students.isEmpty()) {
            System.out.println("The list is already empty. Nothing to clear.");
            return;
        }
        students.clear();
        System.out.println("🗑️ All student data has been cleared.");
    }

    /**
     * Calculates the average score of all students.
     * @return The average score, or 0.0 if the list is empty.
     */
    private double calculateAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double totalScore = 0;
        for (Student student : students) {
            totalScore += student.getScore();
        }
        return totalScore / students.size();
    }

    /**
     * Finds the highest score among all students.
     * @return The highest score, or 0.0 if the list is empty.
     */
    private double getHighestScore() {
        return students.stream()
                .mapToDouble(Student::getScore)
                .max()
                .orElse(0.0);
    }

    /**
     * Finds the lowest score among all students.
     * @return The lowest score, or 0.0 if the list is empty.
     */
    private double getLowestScore() {
        return students.stream()
                .mapToDouble(Student::getScore)
                .min()
                .orElse(0.0);
    }

    /**
     * Displays the complete summary report, including individual scores and statistics.
     */
    private void displayReport() {
        if (students.isEmpty()) {
            System.out.println("\n--- REPORT ---");
            System.out.println("No student data available to generate a report.");
            return;
        }

        // --- 1. Individual Scores ---
        System.out.println("\n--- STUDENT SCORE REPORT (Total Students: " + students.size() + ") ---");
        System.out.println("----------------------------------------");
        System.out.printf("%-20s | %s\n", "STUDENT NAME", "SCORE");
        System.out.println("----------------------------------------");
        
        // Sort students by score in descending order for better report readability
        students.sort(Comparator.comparingDouble(Student::getScore).reversed());

        for (Student student : students) {
            System.out.println(student.toString());
        }
        System.out.println("----------------------------------------");

        // --- 2. Summary Statistics ---
        double average = calculateAverage();
        double highest = getHighestScore();
        double lowest = getLowestScore();

        System.out.println("\n--- SUMMARY STATISTICS ---");
        System.out.printf("Average Score: %.2f\n", average);
        System.out.printf("Highest Score: %.2f\n", highest);
        System.out.printf("Lowest Score:  %.2f\n", lowest);
        System.out.println("--------------------------");
    }

    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        StudentGradeTracker tracker = new StudentGradeTracker();
        tracker.run();
    }
}

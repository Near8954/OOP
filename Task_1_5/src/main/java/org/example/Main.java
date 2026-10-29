package org.example;
import java.util.List;
/** Demonstrates the grade book API. */
public final class Main {
    /** Prevents instantiation of the application entry-point class. */
    private Main() {
    }

    public static void main(String[] args) {
        GradeBook book = new GradeBook(false, 2, List.of(
            new Assessment("Algebra", 1, ControlType.EXAM, Grade.EXCELLENT),
            new Assessment("Analysis", 1, ControlType.CREDIT, Grade.PASS),
            new Assessment("Thesis", 8, ControlType.THESIS_DEFENSE, Grade.PENDING)));
        System.out.println("Average: " + book.averageGrade());
        System.out.println("Budget transfer: " + book.canTransferToBudget());
        System.out.println("Honors possible: " + book.canGetHonorsDiploma());
        System.out.println("Increased scholarship: " + book.canGetIncreasedScholarship());
    }
}

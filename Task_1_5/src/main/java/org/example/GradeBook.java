package org.example;

import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

/**
 * An immutable snapshot of a student's electronic grade book.
 *
 * <p>The curriculum control counts are taken from the MMF assignment table. Forecast methods
 * return {@code false} when the records needed for a forecast are incomplete.</p>
 */
public final class GradeBook {
    private final boolean tuitionPaid;
    private final int currentSemester;
    private final List<Assessment> assessments;

    /**
     * Creates an immutable grade-book snapshot for the current semester.
     *
     * <p>Past semester transcript controls must have results. Assignments, tests, and colloquia
     * are tracking items and may remain pending because they do not produce transcript grades.</p>
     *
     * @param tuitionPaid {@code true} if the student pays tuition
     * @param currentSemester current semester from 1 to 8
     * @param assessments controls recorded in the grade book, including planned future controls
     * @throws IllegalArgumentException if the semester is invalid or a past transcript control
     *         has no result
     * @throws NullPointerException if {@code assessments} or one of its elements is {@code null}
     */
    public GradeBook(boolean tuitionPaid, int currentSemester, List<Assessment> assessments) {
        if (currentSemester < 1 || currentSemester > 8) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }
        this.tuitionPaid = tuitionPaid;
        this.currentSemester = currentSemester;
        this.assessments = List.copyOf(Objects.requireNonNull(assessments, "assessments"));
        boolean incompletePastTranscriptControl = this.assessments.stream()
                .anyMatch(assessment -> assessment.semester() < currentSemester
                        && (assessment.type().isGraded() || assessment.type().isPassFail())
                        && !assessment.isCompleted());
        if (incompletePastTranscriptControl) {
            throw new IllegalArgumentException("Past transcript controls must be completed");
        }
    }

    /**
     * Calculates the average of completed numerical transcript grades.
     *
     * @return the average, or an empty value when no numerical grades have been recorded
     */
    public OptionalDouble averageGrade() {
        return assessments.stream()
                .filter(assessment -> assessment.type().isGraded() && assessment.isCompleted())
                .mapToInt(assessment -> assessment.grade().points())
                .average();
    }

    /**
     * Checks whether a fee-paying student meets the stated transfer conditions.
     *
     * <p>The last two completed exam sessions must be fully represented. Exam results must not be
     * satisfactory or failed; satisfactory grades are allowed for differentiated credits. Every
     * ordinary credit up to the last completed semester must be passed.</p>
     *
     * @return {@code true} if the required records are complete and satisfy the conditions
     */
    public boolean canTransferToBudget() {
        if (!tuitionPaid || currentSemester < 3) {
            return false;
        }
        int lastCompletedSemester = currentSemester - 1;
        int firstRequiredSemester = lastCompletedSemester - 1;
        boolean sessionsComplete = IntStream.rangeClosed(firstRequiredSemester, lastCompletedSemester)
                .allMatch(this::hasCompleteExamSession);
        boolean creditsComplete = IntStream.rangeClosed(1, lastCompletedSemester)
                .allMatch(this::hasExpectedCountForCredits);
        if (!sessionsComplete || !creditsComplete) {
            return false;
        }
        boolean sessionsSatisfyRules = assessments.stream()
                .filter(assessment -> assessment.semester() >= firstRequiredSemester
                        && assessment.semester() <= lastCompletedSemester
                        && (assessment.type() == ControlType.EXAM
                        || assessment.type() == ControlType.DIFFERENTIATED_CREDIT))
                .allMatch(assessment -> !assessment.grade().isFailure()
                        && (assessment.type() != ControlType.EXAM
                        || assessment.grade() != Grade.SATISFACTORY));
        boolean creditsPassed = assessments.stream()
                .filter(assessment -> assessment.type() == ControlType.CREDIT
                        && assessment.semester() <= lastCompletedSemester)
                .allMatch(assessment -> assessment.grade() == Grade.PASS);
        return sessionsSatisfyRules && creditsPassed;
    }

    /**
     * Forecasts whether receiving a diploma with honors remains possible.
     *
     * <p>The curriculum must include all graded transcript controls and ordinary credits. No
     * completed transcript grade may be satisfactory or failed, every ordinary credit must be
     * passed, the thesis defense must be excellent, and at least 75 percent of transcript grades
     * must be excellent or still pending.</p>
     *
     * @return {@code true} if a valid diploma with honors remains attainable
     */
    public boolean canGetHonorsDiploma() {
        if (!hasCompleteHonorsCurriculum()) {
            return false;
        }
        boolean anyCompletedViolation = assessments.stream()
                .filter(Assessment::isCompleted)
                .anyMatch(assessment -> assessment.grade().isFailure()
                        || (assessment.type().isGraded()
                        && assessment.grade() == Grade.SATISFACTORY)
                        || (assessment.type() == ControlType.THESIS_DEFENSE
                        && assessment.grade() != Grade.EXCELLENT)
                        || (assessment.type() == ControlType.CREDIT
                        && assessment.grade() != Grade.PASS));
        if (anyCompletedViolation) {
            return false;
        }
        List<Assessment> finalGrades = assessments.stream()
                .filter(assessment -> assessment.type().isGraded())
                .toList();
        long possibleExcellent = finalGrades.stream()
                .filter(assessment -> assessment.grade() == Grade.EXCELLENT
                        || assessment.grade() == Grade.PENDING)
                .count();
        return possibleExcellent * 4 >= finalGrades.size() * 3L;
    }

    /**
     * Checks the assumed criteria for an increased scholarship in the current semester.
     *
     * <p>The assignment does not define scholarship criteria. This implementation assumes that
     * every graded result in the preceding session must be excellent and every ordinary credit
     * from that semester must be passed.</p>
     *
     * @return {@code true} for a state-funded student who satisfies the documented assumption
     */
    public boolean canGetIncreasedScholarship() {
        if (tuitionPaid || currentSemester == 1) {
            return false;
        }
        int previousSemester = currentSemester - 1;
        if (!hasCompleteExamSession(previousSemester)
                || !hasExpectedCountForCredits(previousSemester)) {
            return false;
        }
        boolean allGradesExcellent = assessments.stream()
                .filter(assessment -> assessment.semester() == previousSemester
                        && assessment.type().isGraded())
                .allMatch(assessment -> assessment.grade() == Grade.EXCELLENT);
        boolean allCreditsPassed = assessments.stream()
                .filter(assessment -> assessment.semester() == previousSemester
                        && assessment.type() == ControlType.CREDIT)
                .allMatch(assessment -> assessment.grade() == Grade.PASS);
        return allGradesExcellent && allCreditsPassed;
    }

    /**
     * Checks that a semester contains the curriculum's expected exam and differentiated-credit
     * records, so missing data cannot satisfy an {@code allMatch} condition vacuously.
     *
     * @param semester semester to inspect
     * @return {@code true} if both control types have their expected counts
     */
    private boolean hasCompleteExamSession(int semester) {
        return hasExpectedCount(ControlType.EXAM, semester)
                && hasExpectedCount(ControlType.DIFFERENTIATED_CREDIT, semester);
    }

    /**
     * Checks that a semester contains its expected number of ordinary credits.
     *
     * @param semester semester to inspect
     * @return {@code true} if the ordinary-credit count matches the curriculum
     */
    private boolean hasExpectedCountForCredits(int semester) {
        return hasExpectedCount(ControlType.CREDIT, semester);
    }

    /**
     * Compares the recorded count of a control type with the curriculum table.
     *
     * @param type control type to count
     * @param semester semester to inspect
     * @return {@code true} if the recorded and expected counts match
     */
    private boolean hasExpectedCount(ControlType type, int semester) {
        long recordedCount = assessments.stream()
                .filter(assessment -> assessment.type() == type
                        && assessment.semester() == semester)
                .count();
        return recordedCount == type.expectedCount(semester);
    }

    /**
     * Checks that all graded transcript controls and ordinary credits are present in all semesters.
     *
     * @return {@code true} if the honors forecast has a complete set of relevant controls
     */
    private boolean hasCompleteHonorsCurriculum() {
        return IntStream.rangeClosed(1, 8).allMatch(semester ->
                hasExpectedCount(ControlType.EXAM, semester)
                        && hasExpectedCount(ControlType.DIFFERENTIATED_CREDIT, semester)
                        && hasExpectedCount(ControlType.PRACTICE_DEFENSE, semester)
                        && hasExpectedCount(ControlType.THESIS_DEFENSE, semester)
                        && hasExpectedCountForCredits(semester));
    }
}

package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

/**
 * An immutable snapshot of a student's electronic grade book.
 *
 * <p>Forecasts use the supplied curriculum plan. Missing future transcript results remain
 * possible outcomes; missing records needed for a completed session prevent eligibility.</p>
 */
public final class GradeBook {
    private final boolean tuitionPaid;
    private final int currentSemester;
    private final List<Assessment> assessments;
    private final CurriculumPlan curriculumPlan;

    /**
     * Creates an immutable grade-book snapshot for the current semester.
     *
     * <p>Assignments, tests, colloquia, and incomplete transcript controls may remain pending.
     * Forecast methods require complete results for past semesters where their rules need them.</p>
     *
     * @param tuitionPaid {@code true} if the student pays tuition
     * @param currentSemester current semester from 1 to 8
     * @param assessments controls recorded in the grade book, including planned future controls
     * @throws IllegalArgumentException if the semester is invalid or assessment counts exceed
     *         the curriculum plan
     * @throws NullPointerException if {@code assessments} or one of its elements is {@code null}
     */
    public GradeBook(boolean tuitionPaid, int currentSemester, List<Assessment> assessments) {
        this(tuitionPaid, currentSemester, assessments, CurriculumPlan.standardMmf());
    }

    /**
     * Creates an immutable grade-book snapshot using a configurable curriculum plan.
     *
     * <p>Assignments, tests, colloquia, and incomplete transcript controls may remain pending.
     * Forecast methods require complete results for past semesters where their rules need them.</p>
     *
     * @param tuitionPaid {@code true} if the student pays tuition
     * @param currentSemester current semester from 1 to 8
     * @param assessments controls recorded in the grade book
     * @param curriculumPlan expected control counts for this student's curriculum
     * @throws IllegalArgumentException if the semester is invalid or assessment counts exceed
     *         the plan
     * @throws NullPointerException if an argument or an assessment is {@code null}
     */
    public GradeBook(boolean tuitionPaid, int currentSemester, List<Assessment> assessments,
                     CurriculumPlan curriculumPlan) {
        if (currentSemester < 1 || currentSemester > 8) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }
        this.tuitionPaid = tuitionPaid;
        this.currentSemester = currentSemester;
        this.assessments = List.copyOf(Objects.requireNonNull(assessments, "assessments"));
        this.curriculumPlan = Objects.requireNonNull(curriculumPlan, "curriculumPlan");
        validateAssessmentCounts();
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
     * <p>All previous semester transcript results must be recorded. Current and future controls
     * need not be complete: missing or pending grades count as outcomes that may still become
     * excellent. No completed grade may be satisfactory or failed, every completed ordinary credit
     * must be passed, a completed thesis defense must be excellent, and at least 75 percent of all
     * planned numerical transcript grades must be excellent or still attainable.</p>
     *
     * @return {@code true} if a valid diploma with honors remains attainable
     */
    public boolean canGetHonorsDiploma() {
        if (!hasCompletePastTranscript()) {
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
        int expectedFinalGrades = Arrays.stream(ControlType.values())
                .filter(ControlType::isGraded)
                .mapToInt(curriculumPlan::totalExpectedCount)
                .sum();
        if (expectedFinalGrades == 0) {
            return false;
        }
        long completedNonExcellentGrades = assessments.stream()
                .filter(assessment -> assessment.type().isGraded()
                        && assessment.isCompleted()
                        && assessment.grade() != Grade.EXCELLENT)
                .count();
        long possibleExcellent = expectedFinalGrades - completedNonExcellentGrades;
        return possibleExcellent * 4 >= expectedFinalGrades * 3L;
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
        return hasCompleteControlResults(ControlType.EXAM, semester)
                && hasCompleteControlResults(ControlType.DIFFERENTIATED_CREDIT, semester);
    }

    /**
     * Checks that a semester contains its expected number of ordinary credits.
     *
     * @param semester semester to inspect
     * @return {@code true} if the ordinary-credit count matches the curriculum
     */
    private boolean hasExpectedCountForCredits(int semester) {
        return hasCompleteControlResults(ControlType.CREDIT, semester);
    }

    /**
     * Checks that the recorded count matches the plan and every recorded control is complete.
     *
     * @param type control type to count
     * @param semester semester to inspect
     * @return {@code true} if all expected controls are present and complete
     */
    private boolean hasCompleteControlResults(ControlType type, int semester) {
        List<Assessment> controls = assessments.stream()
                .filter(assessment -> assessment.type() == type
                        && assessment.semester() == semester)
                .toList();
        return controls.size() == curriculumPlan.expectedCount(type, semester)
                && controls.stream().allMatch(Assessment::isCompleted);
    }

    /**
     * Checks that every transcript result in semesters before the current one is present.
     *
     * @return {@code true} if the recorded history is complete
     */
    private boolean hasCompletePastTranscript() {
        return IntStream.rangeClosed(1, currentSemester - 1).allMatch(semester ->
                Arrays.stream(ControlType.values())
                        .filter(type -> type.isGraded() || type.isPassFail())
                        .allMatch(type -> hasCompleteControlResults(type, semester)));
    }

    /**
     * Checks that recorded controls do not exceed the plan.
     *
     * @throws IllegalArgumentException if an assessment count exceeds the plan
     */
    private void validateAssessmentCounts() {
        for (int semester = 1; semester <= 8; semester++) {
            int semesterNumber = semester;
            for (ControlType type : ControlType.values()) {
                long recordedCount = assessments.stream()
                        .filter(assessment -> assessment.type() == type
                                && assessment.semester() == semesterNumber)
                        .count();
                if (recordedCount > curriculumPlan.expectedCount(type, semester)) {
                    throw new IllegalArgumentException(
                            "Assessment count exceeds the curriculum plan for " + type
                                    + " in semester " + semester);
                }
            }
        }
    }
}

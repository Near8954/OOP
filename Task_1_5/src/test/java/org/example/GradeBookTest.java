package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Unit tests for the electronic grade book. */
class GradeBookTest {

    /**
     * Creates the full curriculum with completed past transcript controls and pending future ones.
     *
     * @param currentSemester current stage of study
     * @param overrides grades to substitute, keyed by the generated subject name
     * @return mutable list containing the curriculum records
     */
    private List<Assessment> curriculum(int currentSemester, Map<String, Grade> overrides) {
        List<Assessment> assessments = new ArrayList<>();
        for (int semester = 1; semester <= 8; semester++) {
            for (ControlType type : ControlType.values()) {
                for (int index = 1; index <= type.expectedCount(semester); index++) {
                    String subject = type.name() + "-" + semester + "-" + index;
                    Grade grade = defaultGrade(type, semester, currentSemester);
                    assessments.add(new Assessment(subject, semester, type,
                            overrides.getOrDefault(subject, grade)));
                }
            }
        }
        return assessments;
    }

    /**
     * Returns the default grade for one control at the specified study stage.
     *
     * @param type control type
     * @param semester semester of the control
     * @param currentSemester current semester
     * @return a completed grade for a past transcript control, otherwise pending
     */
    private Grade defaultGrade(ControlType type, int semester, int currentSemester) {
        if (semester >= currentSemester || (!type.isGraded() && !type.isPassFail())) {
            return Grade.PENDING;
        }
        return type.isPassFail() ? Grade.PASS : Grade.EXCELLENT;
    }

    /**
     * Creates a grade book from the full curriculum and selected grade overrides.
     *
     * @param tuitionPaid whether the student pays tuition
     * @param currentSemester current semester
     * @param overrides grades to substitute
     * @return grade book under test
     */
    private GradeBook gradeBook(boolean tuitionPaid, int currentSemester,
                                Map<String, Grade> overrides) {
        return new GradeBook(tuitionPaid, currentSemester, curriculum(currentSemester, overrides));
    }

    /**
     * Creates a map with a single grade override.
     *
     * @param subject generated subject name
     * @param grade replacement grade
     * @return a one-entry map of grade overrides
     */
    private Map<String, Grade> override(String subject, Grade grade) {
        return Map.of(subject, grade);
    }

    /** Tests the average of completed graded controls and the empty case. */
    @Test
    void averageGrade() {
        GradeBook empty = new GradeBook(false, 1, List.of());
        assertTrue(empty.averageGrade().isEmpty());

        GradeBook book = gradeBook(false, 2, Map.of(
                "EXAM-1-1", Grade.GOOD,
                "DIFFERENTIATED_CREDIT-1-1", Grade.GOOD));
        assertEquals(28.0 / 6.0, book.averageGrade().orElseThrow(), 0.001);
    }

    /** Tests transfer eligibility, session completeness, and control-specific grade rules. */
    @Test
    void transferToBudget() {
        GradeBook eligible = gradeBook(true, 3,
                override("DIFFERENTIATED_CREDIT-1-1", Grade.SATISFACTORY));
        assertTrue(eligible.canTransferToBudget());
        assertFalse(gradeBook(false, 3, Map.of()).canTransferToBudget());
        assertFalse(gradeBook(true, 2, Map.of()).canTransferToBudget());
        assertFalse(new GradeBook(true, 3, List.of()).canTransferToBudget());
        assertFalse(gradeBook(true, 3,
                override("EXAM-1-1", Grade.SATISFACTORY)).canTransferToBudget());
        assertFalse(gradeBook(true, 3,
                override("EXAM-2-1", Grade.FAIL)).canTransferToBudget());
        assertFalse(gradeBook(true, 3,
                override("CREDIT-1-1", Grade.NOT_PASSED)).canTransferToBudget());
    }

    /** Tests honors forecasting, the 75 percent threshold, and required thesis result. */
    @Test
    void honorsDiploma() {
        assertTrue(gradeBook(false, 2, Map.of()).canGetHonorsDiploma());
        assertFalse(new GradeBook(false, 2, List.of()).canGetHonorsDiploma());
        assertFalse(gradeBook(false, 2,
                override("EXAM-1-1", Grade.SATISFACTORY)).canGetHonorsDiploma());
        assertFalse(gradeBook(false, 8,
                override("THESIS_DEFENSE-8-1", Grade.GOOD)).canGetHonorsDiploma());

        Map<String, Grade> fourteenGoodGrades = completedGoodGrades(14);
        Map<String, Grade> fifteenGoodGrades = completedGoodGrades(15);
        assertTrue(gradeBook(false, 8, fourteenGoodGrades).canGetHonorsDiploma());
        assertFalse(gradeBook(false, 8, fifteenGoodGrades).canGetHonorsDiploma());
    }

    /**
     * Creates completed grades that reduce the possible excellent count by the requested amount.
     *
     * @param count number of completed good grades
     * @return grade overrides for the first {@code count} transcript controls
     */
    private Map<String, Grade> completedGoodGrades(int count) {
        Map<String, Grade> overrides = new HashMap<>();
        int added = 0;
        for (int semester = 1; semester < 8 && added < count; semester++) {
            for (ControlType type : ControlType.values()) {
                if (!type.isGraded()) {
                    continue;
                }
                for (int index = 1; index <= type.expectedCount(semester) && added < count; index++) {
                    overrides.put(type.name() + "-" + semester + "-" + index, Grade.GOOD);
                    added++;
                }
            }
        }
        assertEquals(count, added);
        return overrides;
    }

    /** Tests the documented previous-session assumption for increased scholarship. */
    @Test
    void increasedScholarship() {
        assertTrue(gradeBook(false, 2, Map.of()).canGetIncreasedScholarship());
        assertFalse(gradeBook(true, 2, Map.of()).canGetIncreasedScholarship());
        assertFalse(gradeBook(false, 1, Map.of()).canGetIncreasedScholarship());
        assertFalse(gradeBook(false, 2,
                override("EXAM-1-1", Grade.GOOD)).canGetIncreasedScholarship());
        assertFalse(new GradeBook(false, 2, List.of()).canGetIncreasedScholarship());
    }

    /** Tests assessment validation and the treatment of pending coursework. */
    @Test
    void validation() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment(" ", 1, ControlType.EXAM, Grade.GOOD));
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment("Course", 0, ControlType.EXAM, Grade.GOOD));
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment("Course", 1, ControlType.CREDIT, Grade.EXCELLENT));
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment("Course", 1, ControlType.EXAM, Grade.PASS));
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment("Course", 1, ControlType.ASSIGNMENT, Grade.GOOD));
        assertThrows(IllegalArgumentException.class,
                () -> new GradeBook(false, 2, curriculum(2,
                        override("EXAM-1-1", Grade.PENDING))));

        GradeBook pastCourseworkMayRemainPending = gradeBook(false, 2, Map.of());
        assertTrue(pastCourseworkMayRemainPending.averageGrade().isPresent());
        assertThrows(IllegalArgumentException.class,
                () -> ControlType.EXAM.expectedCount(9));
    }
}

package org.example;

/**
 * Types of control used in the MMF curriculum and their expected semester counts.
 */
public enum ControlType {
    /** An assignment control item. */
    ASSIGNMENT(2, 2, 3, 2, 2, 2, 2, 0),
    /** A test control item. */
    TEST(3, 3, 2, 1, 2, 2, 0, 0),
    /** A colloquium control item. */
    COLLOQUIUM(1, 1, 0, 0, 0, 0, 0, 0),
    /** An examination. */
    EXAM(3, 3, 2, 5, 3, 2, 1, 0),
    /** A differentiated credit with a numerical grade. */
    DIFFERENTIATED_CREDIT(3, 3, 6, 5, 4, 6, 4, 4),
    /** An ordinary pass/fail credit. */
    CREDIT(3, 2, 0, 0, 0, 0, 1, 1),
    /** A practice report defense. */
    PRACTICE_DEFENSE(0, 0, 0, 0, 0, 0, 1, 2),
    /** The final thesis defense. */
    THESIS_DEFENSE(0, 0, 0, 0, 0, 0, 0, 1);

    private final int[] expectedBySemester;

    /**
     * Creates a control type with its expected count for each of the eight semesters.
     *
     * @param expectedBySemester expected counts in semester order
     */
    ControlType(int... expectedBySemester) {
        this.expectedBySemester = expectedBySemester;
    }

    /**
     * Returns whether this control produces a numerical transcript grade.
     *
     * @return {@code true} for exams, differentiated credits, practice defenses, and thesis defense
     */
    public boolean isGraded() {
        return this == EXAM || this == DIFFERENTIATED_CREDIT
                || this == PRACTICE_DEFENSE || this == THESIS_DEFENSE;
    }

    /**
     * Returns whether this control is recorded as pass or fail.
     *
     * @return {@code true} for ordinary credits
     */
    public boolean isPassFail() {
        return this == CREDIT;
    }

    /**
     * Returns the number of controls of this type in the specified semester.
     *
     * @param semester semester number from 1 to 8
     * @return expected number of controls
     * @throws IllegalArgumentException if the semester is outside 1 to 8
     */
    public int expectedCount(int semester) {
        if (semester < 1 || semester > expectedBySemester.length) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }
        return expectedBySemester[semester - 1];
    }
}

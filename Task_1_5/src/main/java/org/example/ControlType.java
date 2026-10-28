package org.example;

/**
 * Types of control that may appear in a student's curriculum.
 */
public enum ControlType {
    /** An assignment control item. */
    ASSIGNMENT,
    /** A test control item. */
    TEST,
    /** A colloquium control item. */
    COLLOQUIUM,
    /** An examination. */
    EXAM,
    /** A differentiated credit with a numerical grade. */
    DIFFERENTIATED_CREDIT,
    /** An ordinary pass/fail credit. */
    CREDIT,
    /** A practice report defense. */
    PRACTICE_DEFENSE,
    /** The final thesis defense. */
    THESIS_DEFENSE;

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
}

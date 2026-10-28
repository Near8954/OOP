package org.example;

import java.util.Objects;

/**
 * One control item in a student's curriculum.
 */
public final class Assessment {
    private final String subject;
    private final int semester;
    private final ControlType type;
    private final Grade grade;

    /**
     * Creates an assessment and validates that its grade is compatible with its control type.
     *
     * @param subject nonblank subject or control name
     * @param semester semester number from 1 to 8
     * @param type kind of academic control
     * @param grade recorded grade, or {@link Grade#PENDING} if it is not complete
     * @throws IllegalArgumentException if the subject, semester, or grade is invalid
     * @throws NullPointerException if {@code type} or {@code grade} is {@code null}
     */
    public Assessment(String subject, int semester, ControlType type, Grade grade) {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject must not be blank");
        }
        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }
        this.type = Objects.requireNonNull(type, "type");
        this.grade = Objects.requireNonNull(grade, "grade");
        if (type.isGraded() && grade != Grade.PENDING && !grade.hasNumericValue()) {
            throw new IllegalArgumentException("A graded control requires a numerical grade");
        }
        if (type.isPassFail() && grade != Grade.PENDING && !grade.isRegularCreditGrade()) {
            throw new IllegalArgumentException("An ordinary credit requires a pass/fail result");
        }
        if (!type.isGraded() && !type.isPassFail() && grade != Grade.PENDING) {
            throw new IllegalArgumentException("This control type does not have a transcript grade");
        }
        this.subject = subject;
        this.semester = semester;
    }

    /**
     * Returns the name of the subject or control.
     *
     * @return subject or control name
     */
    public String subject() {
        return subject;
    }

    /**
     * Returns the semester in which the control is scheduled.
     *
     * @return semester number from 1 to 8
     */
    public int semester() {
        return semester;
    }

    /**
     * Returns the type of this control.
     *
     * @return control type
     */
    public ControlType type() {
        return type;
    }

    /**
     * Returns the current result for this control.
     *
     * @return recorded grade or {@link Grade#PENDING}
     */
    public Grade grade() {
        return grade;
    }

    /**
     * Checks whether this control has a recorded result.
     *
     * @return {@code true} if the result is not pending
     */
    public boolean isCompleted() {
        return grade != Grade.PENDING;
    }
}

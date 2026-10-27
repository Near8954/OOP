package org.example;

/**
 * A numerical grade, pass/fail result, or pending result.
 */
public enum Grade {
    /** Numerical grade of five points. */
    EXCELLENT(5),
    /** Numerical grade of four points. */
    GOOD(4),
    /** Numerical grade of three points. */
    SATISFACTORY(3),
    /** Numerical failing grade of two points. */
    FAIL(2),
    /** Successful pass/fail result. */
    PASS(0),
    /** Unsuccessful pass/fail result. */
    NOT_PASSED(0),
    /** No result has been recorded yet. */
    PENDING(0);

    private final int points;

    /**
     * Creates a grade with its numeric value.
     *
     * @param points grade value from 2 to 5, or zero when the grade is non-numerical
     */
    Grade(int points) {
        this.points = points;
    }

    /**
     * Returns the numeric value used for average-grade calculations.
     *
     * @return grade value from 2 to 5, or zero for pass/fail and pending results
     */
    public int points() {
        return points;
    }

    /**
     * Checks whether this result represents an unsuccessful assessment.
     *
     * @return {@code true} for {@link #FAIL} and {@link #NOT_PASSED}
     */
    public boolean isFailure() {
        return this == FAIL || this == NOT_PASSED;
    }
}

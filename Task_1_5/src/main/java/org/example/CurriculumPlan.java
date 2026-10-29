package org.example;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Configurable expected control counts for each control type and semester.
 */
public final class CurriculumPlan {
    private static final int SEMESTER_COUNT = 8;
    private final Map<ControlType, List<Integer>> expectedCounts;

    /**
     * Creates a plan from one expected count for every control type in each semester.
     *
     * @param expectedCounts map from control type to eight semester counts in semester order
     * @throws IllegalArgumentException if a type is missing, an unknown type is present,
     *         a list has the wrong length,
     *         or a count is negative
     * @throws NullPointerException if the map, a key, or a list is {@code null}
     */
    public CurriculumPlan(Map<ControlType, List<Integer>> expectedCounts) {
        Objects.requireNonNull(expectedCounts, "expectedCounts");
        EnumMap<ControlType, List<Integer>> copiedCounts = new EnumMap<>(ControlType.class);
        for (ControlType type : ControlType.values()) {
            List<Integer> configuredCounts = expectedCounts.get(type);
            if (configuredCounts == null) {
                throw new IllegalArgumentException("Missing counts for " + type);
            }
            List<Integer> counts = List.copyOf(configuredCounts);
            if (counts.size() != SEMESTER_COUNT || counts.stream().anyMatch(count -> count < 0)) {
                throw new IllegalArgumentException(
                        "Each control type must have eight nonnegative semester counts");
            }
            copiedCounts.put(type, counts);
        }
        if (expectedCounts.size() != ControlType.values().length) {
            throw new IllegalArgumentException("The plan contains an unknown control type");
        }
        this.expectedCounts = Collections.unmodifiableMap(copiedCounts);
    }

    /**
     * Creates the default plan shown in the MMF assignment.
     *
     * <p>Callers can provide a different plan through the public constructor.</p>
     *
     * @return default MMF curriculum plan
     */
    public static CurriculumPlan standardMmf() {
        Map<ControlType, List<Integer>> counts = new EnumMap<>(ControlType.class);
        counts.put(ControlType.ASSIGNMENT, List.of(2, 2, 3, 2, 2, 2, 2, 0));
        counts.put(ControlType.TEST, List.of(3, 3, 2, 1, 2, 2, 0, 0));
        counts.put(ControlType.COLLOQUIUM, List.of(1, 1, 0, 0, 0, 0, 0, 0));
        counts.put(ControlType.EXAM, List.of(3, 3, 2, 5, 3, 2, 1, 0));
        counts.put(ControlType.DIFFERENTIATED_CREDIT, List.of(3, 3, 6, 5, 4, 6, 4, 4));
        counts.put(ControlType.CREDIT, List.of(3, 2, 0, 0, 0, 0, 1, 1));
        counts.put(ControlType.PRACTICE_DEFENSE, List.of(0, 0, 0, 0, 0, 0, 1, 2));
        counts.put(ControlType.THESIS_DEFENSE, List.of(0, 0, 0, 0, 0, 0, 0, 1));
        return new CurriculumPlan(counts);
    }

    /**
     * Returns the expected count for a control type in one semester.
     *
     * @param type control type
     * @param semester semester number from 1 to 8
     * @return expected number of controls
     * @throws IllegalArgumentException if the semester is outside 1 to 8
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public int expectedCount(ControlType type, int semester) {
        Objects.requireNonNull(type, "type");
        if (semester < 1 || semester > SEMESTER_COUNT) {
            throw new IllegalArgumentException("Semester must be between 1 and 8");
        }
        return expectedCounts.get(type).get(semester - 1);
    }

    /**
     * Returns the total number of controls of a type across all semesters.
     *
     * @param type control type
     * @return total number of controls of that type
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public int totalExpectedCount(ControlType type) {
        return expectedCounts.get(Objects.requireNonNull(type, "type")).stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}

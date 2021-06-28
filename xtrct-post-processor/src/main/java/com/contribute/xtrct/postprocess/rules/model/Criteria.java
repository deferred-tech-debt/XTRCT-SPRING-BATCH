package com.contribute.xtrct.postprocess.rules.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Data
public class Criteria {

    private String column;
    private Condition condition;
    private Set<String> values;

    // to be initiated under init()
    private Double maxValue;
    private Double minValue;
    private Predicate<String> patternPredicate;

    // must be execute when column, condition & values are initiated
    public void init() {
        switch (condition) {
            case GE:
            case GT:
                try {
                    maxValue = values.stream().parallel().mapToDouble(Double::valueOf).max().orElse(Double.MAX_VALUE);
                } catch (Exception e) {
                    maxValue = Double.MAX_VALUE;
                }
                break;
            case LE:
            case LT:
                try {
                    minValue = values.stream().mapToDouble(Double::valueOf).min().orElse(Double.MIN_VALUE);
                } catch (Exception e) {
                    minValue = Double.MIN_VALUE;
                }
                break;
            case LIKE:
            case NOT_LIKE:
                patternPredicate = values.stream().parallel().map(Pattern::compile).map(Criteria::patternAsPredicate).reduce(Predicate::or).get();
        }
    }

    // convert the String pattern to predicate with allowing complete match of string
    private static Predicate<String> patternAsPredicate(Pattern pattern) {
        return s -> pattern.matcher(s).matches();
    }

    /**
     * Convert this criteria to predicate on provided header map. if multiple values are provided to match the criteria, they will grouped as OR like SQL IN clause
     * @param headerMap header map -> columns to index
     * @return predicate of this criteria
     */
    public Predicate<List<String>> toPredicate(final Map<String, Integer> headerMap) {
        Predicate<List<String>> recordPredicate = null;
        switch (condition) {
            case EQ:
                recordPredicate = row -> values.contains(row.get(headerMap.get(column)));
                break;
            case NEQ:
                recordPredicate = row -> !values.contains(row.get(headerMap.get(column)));
                break;
            case GE:
                recordPredicate = row -> {
                    try {
                        return Double.valueOf(row.get(headerMap.get(column))) >= maxValue;
                    } catch (Exception e)  {
                        return false;
                    }
                };
                break;
            case GT:
                recordPredicate = row -> {
                    try {
                        return Double.valueOf(row.get(headerMap.get(column))) > maxValue;
                    } catch (Exception e)  {
                        return false;
                    }
                };
                break;
            case LE:
                recordPredicate = row -> {
                    try {
                        return Double.valueOf(row.get(headerMap.get(column))) <= minValue;
                    } catch (Exception e)  {
                        return false;
                    }
                };
                break;
            case LT:
                recordPredicate = row -> {
                    try {
                        return Double.valueOf(row.get(headerMap.get(column))) < minValue;
                    } catch (Exception e)  {
                        return false;
                    }
                };
                break;
            case LIKE:
                recordPredicate = row -> patternPredicate.test(row.get(headerMap.get(column)));
                break;
            case NOT_LIKE:
                recordPredicate = row -> patternPredicate.negate().test(row.get(headerMap.get(column)));
        }
        return recordPredicate;
    }
}

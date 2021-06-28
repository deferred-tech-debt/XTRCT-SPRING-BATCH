package com.chromedata.incentives.extract.postprocess.utils;

import com.chromedata.incentives.extract.postprocess.rules.model.CSVData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostProcessorTestUtils {
    private static final Logger LOG = LogManager.getLogger(PostProcessorTestUtils.class);

    public static CSVData buildCSVData() {
        final List<String>         headers   = Stream.of("IncentiveID", "InstitutionID", "Institution").collect(Collectors.toList());
        final Map<String, Integer> headerMap = headers.stream().collect(Collectors.toMap(Function.identity(), headers::indexOf));
        final List<List<String>> records = Stream.of("12733441,109,OEM",
                                                     "12733442,109,OEM",
                                                     "13595775,89,INFINITI Financial Services (IFS)",
                                                     "13595776,89,INFINITI Financial Services (IFS)",
                                                     "15191068,14,Honda Financial Services",
                                                     "15191069,14,Honda Financial Services")
                                                 .map(s -> Stream.of(s.split(",", -1)).collect(Collectors.toList()))
                                                 .collect(Collectors.toList());
        return new CSVData(headerMap, records);
    }

    public static <T> boolean isEqualCollections(Collection<T> col1, Collection<T> col2, String... skipFields) {
        if (col1 == null || col2 == null || col1.size() != col2.size()) {
            return false;
        }

        boolean           result        = true;
        final Iterator<T> itr1          = col1.iterator();
        final Iterator<T> itr2          = col2.iterator();
        final Set<String> skipFieldsSet = Stream.of(skipFields).collect(Collectors.toSet());

        while (itr1.hasNext() && itr2.hasNext()) {
            result &= isEqualObjects(itr1.next(), itr2.next(), skipFieldsSet);
        }

        return result;
    }

    public static <T> boolean isEqualObjects(final T obj1, final T obj2, final String[] skipFields) {
        return isEqualObjects(obj1, obj2, Stream.of(skipFields).collect(Collectors.toSet()));
    }

    private static <T> boolean isEqualObjects(final T obj1, final T obj2, final Set<String> skipFieldsSet) {
        boolean       result = true;
        final Field[] fields = obj1.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!skipFieldsSet.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    result &= Objects.equals(field.get(obj1), field.get(obj2));
                } catch (IllegalAccessException e) {
                    LOG.error(e);
                    result = false;
                }
            }
        }
        return result;
    }
}

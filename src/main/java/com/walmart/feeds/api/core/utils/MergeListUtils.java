package com.walmart.feeds.api.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeListUtils {

    public static <T extends Object> List<T> getDiffItems(List<T> originalList, List<T> newList) {

        if (originalList == null) {
            return null;
        }

        if (newList == null) {
            return originalList;
        }

        List diff = new ArrayList(Collections.unmodifiableList(originalList));
        diff.removeAll(newList);

        return diff;
    }

}

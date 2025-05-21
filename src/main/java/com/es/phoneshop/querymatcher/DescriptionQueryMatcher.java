package com.es.phoneshop.querymatcher;

import com.es.phoneshop.enums.SearchMethod;
import com.es.phoneshop.model.product.Product;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DescriptionQueryMatcher implements QueryMatcher {

    private static final double VALUE_INCREASED_BY_ALIGNING_PERCENT = 1.1f;

    private boolean caseSensitive;

    private SearchMethod searchMethod;

    public DescriptionQueryMatcher(SearchMethod searchMethod, boolean caseSensitive) {
        this.searchMethod = searchMethod;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public double calculateMatchValue(Product product, List<String> keyWords) {
        if (keyWords == null || keyWords.isEmpty()) {
            return 0;
        }

        String source = product.getDescription();
        if (!caseSensitive) {
            source = source.toUpperCase();
            keyWords = keyWords.stream().map(String::toUpperCase).collect(Collectors.toList());
        }


        List<String> productWords = List.of(Optional.ofNullable(source).orElse("").split(" "));

        Map<String, Double> matchedValue = new HashMap<>();

        List<String> finalKeyWords = keyWords;
        Set<String> keyWordsContained = new HashSet<>();
        productWords.forEach(word -> {
            matchedValue.put(word, 0d);
            finalKeyWords.forEach(keyWord -> {
                double matchValue;
                if (word.contains(keyWord)) {
                    matchValue = (double) keyWord.length() / word.length();
                    keyWordsContained.add(keyWord);
                } else {
                    matchValue = 0;
                }
                if (word.startsWith(keyWord)) {
                    matchValue *= VALUE_INCREASED_BY_ALIGNING_PERCENT;
                }

                if (matchValue > matchedValue.get(word)) {
                    matchedValue.put(word, matchValue);
                }
            });
        });

        double matchValueTotal = 0;
        Iterator<String> keySetIterator = matchedValue.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            matchValueTotal += matchedValue.get(key);
        }

        if (searchMethod == SearchMethod.ALL_WORDS && ! keyWordsContained.containsAll(keyWords))
            matchValueTotal = 0;

        return matchValueTotal;
    }
}

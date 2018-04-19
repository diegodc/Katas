import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * String Calculator Kata
 */
public class StringCalculator {

    private static final String CUSTOM_HEADER_PREFIX = "//";
    private static final String CUSTOM_DELIMITER_PREFIX = "[";
    private static final String NEW_LINE_LITERAL = "\n";
    private static final String DEFAULT_REGEX = ",|\n";
    private static final String EXCEPTION_MESSAGE = "Negatives not allowed: ";
    private static final String CUSTOM_DELIMITER_MATCHER_REGEX = "\\[([^]]+)]";
    private static final String MINUS_SIGN = "-";

    public static int add(String string) {
        return parse(string);
    }

    private static int parse(String string) {
        List<String> tokens = parseTokens(string);
        checkNoNegativesNumbers(tokens);
        return calculateSum(tokens);
    }

    private static List<String> parseTokens(String string) {
        if (string.isEmpty())
            return Collections.emptyList();
        else if (hasCustomHeader(string))
            return parseWithCustomDelimiters(string);
        else
            return parseWithDefaultDelimiters(string);
    }

    private static boolean hasCustomHeader(String string) {
        return string.startsWith(CUSTOM_HEADER_PREFIX);
    }

    private static List<String> parseWithCustomDelimiters(String string) {
        int index = string.indexOf(NEW_LINE_LITERAL);
        String header = string.substring(2, index);
        String tail = string.substring(index + 1);

        return split(tail, parseRegexFromHeader(header));
    }

    private static String parseRegexFromHeader(String header) {
        if (headerHasMultiCharDelimiter(header))
            return parseMultiCharDelimiters(header);
        else
            return parseSingleCharDelimiter(header);
    }

    private static boolean headerHasMultiCharDelimiter(String header) {
        return header.startsWith(CUSTOM_DELIMITER_PREFIX);
    }

    private static String parseMultiCharDelimiters(String header) {
        List<String> delimiters = buildCustomDelimitersList(header);
        return buildCustomDelimitersRegex(delimiters);
    }

    private static String buildCustomDelimitersRegex(List<String> delimiters) {
        Optional<String> optional = delimiters.stream()
                .reduce((s, s1) -> s.concat("|" + s1));

        if (!optional.isPresent())
            throw new RuntimeException("Unable to build delimiters regex: " + delimiters.toString());

        return optional.get();
    }

    private static List<String> buildCustomDelimitersList(String header) {
        List<String> delimiters = new ArrayList<>();

        Matcher m = Pattern.compile(CUSTOM_DELIMITER_MATCHER_REGEX).matcher(header);

        while (m.find()) {
            String token = m.group();
            delimiters.add(extractDelimiter(token));
        }
        return delimiters;
    }

    private static String extractDelimiter(String token) {
        String delimiter = token.substring(1, token.length() - 1);
        return Pattern.quote(delimiter);
    }

    private static String parseSingleCharDelimiter(String header) {
        return Pattern.quote(header.substring(0,1));
    }

    private static List<String> parseWithDefaultDelimiters(String string) {
        return split(string, DEFAULT_REGEX);
    }

    private static List<String> split(String string, String regex) {
        return Arrays.asList(string.split(regex));
    }

    private static void checkNoNegativesNumbers(List<String> tokens) {
        List<String> negatives = filterNegatives(tokens);
        if (!negatives.isEmpty())
            throw new IllegalArgumentException(EXCEPTION_MESSAGE + negatives.toString());
    }

    private static List<String> filterNegatives(List<String> tokens) {
        return tokens.parallelStream()
                .filter((t) -> t.startsWith(MINUS_SIGN))
                .collect(Collectors.toList());
    }

    private static int calculateSum(List<String> tokens) {
        return tokens.parallelStream()
                .mapToInt(Integer::parseInt)
                .filter((n) -> n <= 1000)
                .sum();
    }

}
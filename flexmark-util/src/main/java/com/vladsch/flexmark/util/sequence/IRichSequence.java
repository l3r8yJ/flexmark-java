package com.vladsch.flexmark.util.sequence;

import com.vladsch.flexmark.util.Pair;
import com.vladsch.flexmark.util.mappers.CharMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * A CharSequence that provides a rich set of manipulation methods.
 * <p>
 * NOTE: '\0' changed to '\uFFFD' use {@link com.vladsch.flexmark.util.mappers.NullEncoder#decodeNull} mapper to get original null chars.
 * <p>
 * safe access methods return '\0' for no char response.
 */
@SuppressWarnings("SameParameterValue")
public interface IRichSequence<T extends IRichSequence<T>> extends CharSequence, Comparable<CharSequence> {
    String EOL = "\n";
    String SPACE = " ";
    String EOL_CHARS = "\r\n";

    char EOL_CHAR = EOL_CHARS.charAt(1);
    char EOL_CHAR1 = EOL_CHARS.charAt(0);
    char EOL_CHAR2 = EOL_CHARS.charAt(1);

    char SPC = ' ';
    char NUL = '\0';
    char ENC_NUL = '\uFFFD';
    char NBSP = '\u00A0';
    char LSEP = '\u2028';  // line separator

    String LINE_SEP = Character.toString(LSEP);
    String WHITESPACE_NO_EOL_CHARS = " \t";
    String SPACE_TAB = WHITESPACE_NO_EOL_CHARS;

    String WHITESPACE_CHARS = " \t\r\n";
    String WHITESPACE_NBSP_CHARS = " \t\r\n\u00A0";

    @NotNull T[] emptyArray();
    @NotNull T nullSequence();

    /**
     * @return the last character of the sequence or '\0' if empty
     */
    char lastChar();

    /**
     * @return the first character of the sequence or '\0' if empty
     */
    char firstChar();

    /**
     * return char at index or '\0' if index &lt;0 or &gt;=length()
     *
     * @param index index
     * @return char or '\0'
     */
    char safeCharAt(int index);

    /**
     * Get a portion of this sequence
     *
     * @param startIndex offset from startIndex of this sequence
     * @param endIndex   offset from startIndex of this sequence
     * @return based sequence whose contents reflect the selected portion
     */
    @Override
    @NotNull T subSequence(int startIndex, int endIndex);

    /**
     * Get a portion of this sequence selected by range
     *
     * @param range range to get, coordinates offset form start of this sequence
     * @return based sequence whose contents reflect the selected portion, if range.isNull() then {@link #nullSequence()}
     */
    @NotNull T subSequence(@NotNull Range range);

    /**
     * Get a portion of this sequence before one selected by range
     *
     * @param range range to get, coordinates offset form start of this sequence
     * @return based sequence whose contents reflect the selected portion, if range.isNull() then {@link #nullSequence()}
     */
    @NotNull T subSequenceBefore(@NotNull Range range);

    /**
     * Get a portion of this sequence after one selected by range
     *
     * @param range range to get, coordinates offset form start of this sequence
     * @return based sequence whose contents reflect the selected portion, if range.isNull() then {@link #nullSequence()}
     */
    @NotNull T subSequenceAfter(@NotNull Range range);

    /**
     * Get a portion of this sequence starting from a given offset to endIndex of the sequence
     *
     * @param startIndex offset from startIndex of this sequence
     * @return based sequence whose contents reflect the selected portion
     */
    @NotNull T subSequence(int startIndex);

    /**
     * Convenience method to get characters offset from endIndex of sequence.
     * no exceptions are thrown, instead a \0 is returned for an invalid index positions
     *
     * @param startIndex offset from endIndex of sequence [ 0..length() )
     * @param endIndex   offset from endIndex of sequence [ 0..length() )
     * @return selected portion spanning length() - startIndex to length() - endIndex of this sequence
     */
    @NotNull T endSequence(int startIndex, int endIndex);

    /**
     * Convenience method to get characters offset from endIndex of sequence.
     * no exceptions are thrown, instead a \0 is returned for an invalid index positions
     *
     * @param startIndex offset from endIndex of sequence [ 0..length() )
     * @return selected portion spanning length() - startIndex to length() of this sequence
     */
    @NotNull T endSequence(int startIndex);

    // index from the end of the sequence
    // no exceptions are thrown, instead a \0 is returned for an invalid index
    /**
     * Convenience method to get characters offset from end of sequence.
     * no exceptions are thrown, instead a \0 is returned for an invalid index positions
     *
     * @param index offset from end of sequence
     * @return character located at length() - index in this sequence
     */
    char endCharAt(int index);

    /**
     * Convenience method to get characters offset from start or end of sequence.
     * when offset &gt;=0 then it is offset from the start of the sequence,
     * when &lt;0 then from the end
     * <p>
     * no exceptions are thrown, instead a \0 is returned for an invalid index positions
     *
     * @param startIndex offset into this sequence
     * @param endIndex   offset into this sequence
     * @return selected portion spanning startIndex to endIndex of this sequence. If offset is &lt;0 then it is taken as relative to length()
     */
    @NotNull T midSequence(int startIndex, int endIndex);

    /**
     * Convenience method to get characters offset from start or end of sequence.
     * when offset &gt;= then it is offset from the start of the sequence,
     * when &lt;0 then from the end
     * <p>
     * no exceptions are thrown, instead a \0 is returned for an invalid index positions
     *
     * @param startIndex offset into this sequence
     * @return selected portion spanning startIndex to length() of this sequence. If offset is &lt;0 then it is taken as relative to length()
     */
    @NotNull T midSequence(int startIndex);

    /**
     * Convenience method to get characters offset from start or end of sequence.
     * when index &gt;= then it is offset from the start of the sequence,
     * when &lt;0 then from the end
     * no exceptions are thrown, instead a \0 is returned for an invalid index positions
     *
     * @param index of character to get
     * @return character at index or \0 if index is outside valid range for this sequence
     */
    char midCharAt(int index);

    /**
     * Factory function
     *
     * @param charSequence char sequence from which to construct a rich char sequence
     * @return rich char sequence from given inputs
     */
    @NotNull T sequenceOf(@Nullable CharSequence charSequence);

    /**
     * Factory function
     *
     * @param charSequence char sequence from which to construct a rich char sequence
     * @param startIndex   start index of the sequence to use
     * @return rich char sequence from given inputs
     */
    @NotNull T sequenceOf(@Nullable CharSequence charSequence, int startIndex);

    /**
     * Factory function
     *
     * @param charSequence char sequence from which to construct a rich char sequence
     * @param startIndex   start index of the sequence to use
     * @param endIndex     end index of the sequence to use
     * @return rich char sequence from given inputs
     */
    @NotNull T sequenceOf(@Nullable CharSequence charSequence, int startIndex, int endIndex);

    /**
     * Get a sequence builder for this sequence type
     *
     * @return builder which can build this type of sequence
     */
    @NotNull <B extends SequenceBuilder<B, T>> B getBuilder();

    /**
     * All index methods return the position or -1 if not found of the given character, characters or string.
     * <p>
     * The basic methods have overloads for 1, 2, or 3 characters and CharSequence parameters.
     * If fromIndex is not given then for forward searching methods 0 is taken as the value,
     * for reverse searching methods length() is taken as the value
     * <p>
     * For forward searching methods fromIndex is the minimum start position for search and endIndex
     * is the maximum end position, if not given the length() of string is assumed.
     * <p>
     * For reverse searching methods fromIndex is the maximum start position for search and startIndex
     * is the minimum end position, if not given then 0 is assumed.
     * <p>
     * Variations of arguments are for convenience and speed when 1, 2 or 3 characters are needed to be tested.
     * Methods that take a character set in the form of CharSequence will route their call to character based
     * methods if the CharSequence is only 1, 2 or 3 characters long.
     * <p>
     * indexOf(CharSequence): returns the index of the next occurrence of given text in this sequence
     * <p>
     * indexOfAny(CharSequence): returns the index of the next occurrence of any of the characters in this sequence
     * indexOfAny(char): returns the index of the next occurrence of any of the characters in this sequence
     * indexOfAny(char,char): returns the index of the next occurrence of any of the characters in this sequence
     * indexOfAny(char,char,char): returns the index of the next occurrence of any of the characters in this sequence
     * <p>
     * indexOfAnyNot(CharSequence): returns the index of the next occurrence of any of the characters not in this sequence
     * indexOfAnyNot(char): returns the index of the next occurrence of any of the characters not in this sequence
     * indexOfAnyNot(char,char): returns the index of the next occurrence of any of the characters not in this sequence
     * indexOfAnyNot(char,char,char): returns the index of the next occurrence of any of the characters not in this sequence
     * <p>
     * lastIndexOf(CharSequence): returns the index of the previous occurrence of given text in this sequence, reversed search
     * <p>
     * lastIndexOfAny(CharSequence): returns the index of the previous occurrence of any of the characters in this sequence, reversed search
     * lastIndexOfAny(char): returns the index of the previous occurrence of any of the characters in this sequence, reversed search
     * lastIndexOfAny(char,char): returns the index of the previous occurrence of any of the characters in this sequence, reversed search
     * lastIndexOfAny(char,char,char): returns the index of the previous occurrence of any of the characters in this sequence, reversed search
     * <p>
     * lastIndexOfAnyNot(CharSequence): returns the index of the previous occurrence of any of the characters not in this sequence, reversed search
     * lastIndexOfAnyNot(Char): returns the index of the previous occurrence of any of the characters not in this sequence, reversed search
     * lastIndexOfAnyNot(char,char): returns the index of the previous occurrence of any of the characters not in this sequence, reversed search
     * lastIndexOfAnyNot(char,char,char): returns the index of the previous occurrence of any of the characters not in this sequence, reversed search
     *
     * @param s character sequence whose occurrence to find
     * @return index where found or -1
     */
    int indexOf(@NotNull CharSequence s);
    int indexOf(@NotNull CharSequence s, int fromIndex);
    int indexOf(@NotNull CharSequence s, int fromIndex, int endIndex);

    int indexOf(char c);
    int indexOfAny(char c1, char c2);
    int indexOfAny(char c1, char c2, char c3);
    int indexOfAny(@NotNull CharSequence s);

    int indexOf(char c, int fromIndex);
    int indexOfAny(char c1, char c2, int fromIndex);
    int indexOfAny(char c1, char c2, char c3, int fromIndex);
    int indexOfAny(@NotNull CharSequence s, int fromIndex);

    int indexOf(char c, int fromIndex, int endIndex);
    int indexOfAny(char c1, char c2, int fromIndex, int endIndex);
    int indexOfAny(char c1, char c2, char c3, int fromIndex, int endIndex);
    int indexOfAny(@NotNull CharSequence s, int fromIndex, int endIndex);

    int indexOfNot(char c);
    int indexOfAnyNot(char c1, char c2);
    int indexOfAnyNot(char c1, char c2, char c3);
    int indexOfAnyNot(@NotNull CharSequence s);

    int indexOfNot(char c, int fromIndex);
    int indexOfAnyNot(char c1, char c2, int fromIndex);
    int indexOfAnyNot(char c1, char c2, char c3, int fromIndex);
    int indexOfAnyNot(@NotNull CharSequence s, int fromIndex);

    int indexOfNot(char c, int fromIndex, int endIndex);
    int indexOfAnyNot(char c1, char c2, int fromIndex, int endIndex);
    int indexOfAnyNot(char c1, char c2, char c3, int fromIndex, int endIndex);
    int indexOfAnyNot(@NotNull CharSequence s, int fromIndex, int endIndex);

    int lastIndexOf(@NotNull CharSequence s);
    int lastIndexOf(@NotNull CharSequence s, int fromIndex);
    int lastIndexOf(@NotNull CharSequence s, int startIndex, int fromIndex);

    int lastIndexOf(char c);
    int lastIndexOfAny(char c1, char c2);
    int lastIndexOfAny(char c1, char c2, char c3);
    int lastIndexOfAny(@NotNull CharSequence s);

    int lastIndexOf(char c, int fromIndex);
    int lastIndexOfAny(char c1, char c2, int fromIndex);
    int lastIndexOfAny(char c1, char c2, char c3, int fromIndex);
    int lastIndexOfAny(@NotNull CharSequence s, int fromIndex);

    int lastIndexOf(char c, int startIndex, int fromIndex);
    int lastIndexOfAny(char c1, char c2, int startIndex, int fromIndex);
    int lastIndexOfAny(char c1, char c2, char c3, int startIndex, int fromIndex);
    int lastIndexOfAny(@NotNull CharSequence s, int startIndex, int fromIndex);

    int lastIndexOfNot(char c);
    int lastIndexOfAnyNot(char c1, char c2);
    int lastIndexOfAnyNot(char c1, char c2, char c3);
    int lastIndexOfAnyNot(@NotNull CharSequence s);

    int lastIndexOfNot(char c, int fromIndex);
    int lastIndexOfAnyNot(char c1, char c2, int fromIndex);
    int lastIndexOfAnyNot(char c1, char c2, char c3, int fromIndex);
    int lastIndexOfAnyNot(@NotNull CharSequence s, int fromIndex);

    int lastIndexOfNot(char c, int startIndex, int fromIndex);
    int lastIndexOfAnyNot(char c1, char c2, int startIndex, int fromIndex);
    int lastIndexOfAnyNot(char c1, char c2, char c3, int startIndex, int fromIndex);
    int lastIndexOfAnyNot(@NotNull CharSequence s, int startIndex, int fromIndex);

    /**
     * Count leading/trailing characters of this sequence
     * <p>
     * Parameters can be: 1, 2 or 3 characters or CharSequence. For character arguments counts any contiguous
     * leading/trailing characters in the sequence.
     * <p>
     * For CharSequence counts counts any contiguous
     * leading/trailing characters in the sequence which are contained in the given char sequence.
     * <p>
     * All functions have overloads:
     * with no fromIndex then 0 is taken for leading and length() for trailing methods
     * with fromIndex then this is taken as the start for leading and end for trailing methods
     * with fromIndex and endIndex, counting will start at fromIndex and stop at endIndex
     * <p>
     * countLeading(): count contiguous leading space/tab characters in this sequence
     * countLeading(CharSequence): count contiguous leading characters from set in this sequence
     * countLeading(char): count contiguous leading characters from set in this sequence
     * countLeading(char,char): count contiguous leading characters from set in this sequence
     * countLeading(char,char,char): count contiguous leading characters from set in this sequence
     * <p>
     * countLeadingNot(): count contiguous leading not space/tabs in this sequence
     * countLeadingNot(CharSequence): count contiguous leading characters not from set in this sequence
     * countLeadingNot(char): count contiguous leading characters not from set in this sequence
     * countLeadingNot(char,char): count contiguous leading characters not from set in this sequence
     * countLeadingNot(char,char,char): count contiguous leading characters not from set in this sequence
     * <p>
     * countTrailing(): count contiguous leading space/tab in this sequence
     * countTrailing(CharSequence): count contiguous leading characters from set in this sequence
     * countTrailing(char): count contiguous leading characters from set in this sequence
     * countTrailing(char,char): count contiguous leading characters from set in this sequence
     * countTrailing(char,char,char): count contiguous leading characters from set in this sequence
     * <p>
     * countTrailingNot(): count contiguous leading not space/tabs in this sequence
     * countTrailingNot(CharSequence): count contiguous leading characters not from set in this sequence
     * countTrailingNot(char): count contiguous leading characters not from set in this sequence
     * countTrailingNot(char,char): count contiguous leading characters not from set in this sequence
     * countTrailingNot(char,char,char): count contiguous leading characters not from set in this sequence
     *
     * @param chars set of contiguous characters which should be counted at start of sequence
     * @return number of chars in contiguous span at start of sequence
     */
    int countLeading(@NotNull CharSequence chars);
    int countLeadingNot(@NotNull CharSequence chars);
    int countLeading(@NotNull CharSequence chars, int startIndex);
    int countLeadingNot(@NotNull CharSequence chars, int startIndex);
    int countLeading(@NotNull CharSequence chars, int startIndex, int endIndex);
    int countLeadingNot(@NotNull CharSequence chars, int startIndex, int endIndex);

    int countTrailing(@NotNull CharSequence chars);
    int countTrailingNot(@NotNull CharSequence chars);
    int countTrailing(@NotNull CharSequence chars, int startIndex);
    int countTrailingNot(@NotNull CharSequence chars, int startIndex);
    int countTrailing(@NotNull CharSequence chars, int startIndex, int endIndex);
    int countTrailingNot(@NotNull CharSequence chars, int startIndex, int endIndex);

    int countLeading(char c);
    int countLeadingNot(char c);
    int countLeading(char c, int startIndex);
    int countLeadingNot(char c, int startIndex);
    int countLeading(char c, int startIndex, int endIndex);
    int countLeadingNot(char c, int startIndex, int endIndex);

    int countTrailing(char c);
    int countTrailingNot(char c);
    int countTrailing(char c, int startIndex);
    int countTrailingNot(char c, int startIndex);
    int countTrailing(char c, int startIndex, int endIndex);
    int countTrailingNot(char c, int startIndex, int endIndex);

    int countLeadingSpaceTab();
    int countLeadingNotSpaceTab();
    int countTrailingSpaceTab();
    int countTrailingNotSpaceTab();
    int countOfSpaceTab();
    int countOfNotSpaceTab();

    int countLeadingWhitespace();
    int countLeadingNotWhitespace();
    int countTrailingWhitespace();
    int countTrailingNotWhitespace();
    int countOfWhitespace();
    int countOfNotWhitespace();

    // @formatter:off
    @Deprecated default int countLeading() { return countLeadingSpaceTab(); }
    @Deprecated default int countLeadingNot() { return countLeadingNotSpaceTab(); }
    @Deprecated default int countTrailing() { return countTrailingSpaceTab(); }
    @Deprecated default int countTrailingNot() { return countTrailingNotSpaceTab(); }
    @Deprecated default int countOf() { return countOfSpaceTab(); }
    @Deprecated default int countOfNot() { return countOfNotSpaceTab(); }
    // @formatter:on

    int countOf(char c);
    int countOfNot(char c);
    int countOf(char c, int startIndex);
    int countOfNot(char c, int startIndex);
    int countOf(char c, int startIndex, int endIndex);
    int countOfNot(char c, int startIndex, int endIndex);

    int countOfAny(@NotNull CharSequence chars);
    int countOfAnyNot(@NotNull CharSequence chars);
    int countOfAny(@NotNull CharSequence chars, int startIndex);
    int countOfAnyNot(@NotNull CharSequence chars, int startIndex);
    int countOfAny(@NotNull CharSequence chars, int startIndex, int endIndex);
    int countOfAnyNot(@NotNull CharSequence chars, int startIndex, int endIndex);

    int countLeadingColumns(int startColumn, @NotNull CharSequence chars);

    /**
     * Range of kept sequence when  trim start/end of this sequence is performed, characters to trim are passed in the sequence argument
     * <p>
     * returns range of kept sequence or if nothing matched then Range.NULL is returned, meaning keep all
     * <p>
     * If character set  in the form of character sequence is not passed in the {@link #WHITESPACE_CHARS} are assumed.
     *
     * @param keep  minimum length of string to keep
     * @param chars set of characters to trim from start of line
     * @return range in this sequence to keep or Range.NULL if to keep all
     */
    @NotNull Range trimStartRange(int keep, @NotNull CharSequence chars);
    @NotNull Range trimEndRange(int keep, @NotNull CharSequence chars);
    @NotNull Range trimRange(int keep, @NotNull CharSequence chars);

    @NotNull Range trimStartRange(@NotNull CharSequence chars);
    @NotNull Range trimEndRange(@NotNull CharSequence chars);
    @NotNull Range trimRange(@NotNull CharSequence chars);

    @NotNull Range trimStartRange(int keep);
    @NotNull Range trimEndRange(int keep);
    @NotNull Range trimRange(int keep);

    @NotNull Range trimStartRange();
    @NotNull Range trimEndRange();
    @NotNull Range trimRange();

    /**
     * Trim, Trim start/end of this sequence, characters to trim are passed in the sequence argument
     * <p>
     * returns trimmed sequence or if nothing matched the original sequence
     * <p>
     * If character set  in the form of character sequence is not passed in the {@link #WHITESPACE_CHARS} are assumed.
     *
     * @param keep  minimum length of string to keep
     * @param chars set of characters to trim from start of line
     * @return sequence after it is trimmed
     */
    @NotNull T trimStart(int keep, @NotNull CharSequence chars);
    @NotNull T trimEnd(int keep, @NotNull CharSequence chars);
    @NotNull T trim(int keep, @NotNull CharSequence chars);

    @NotNull T trimStart(int keep);
    @NotNull T trimEnd(int keep);
    @NotNull T trim(int keep);

    @NotNull T trimStart(@NotNull CharSequence chars);
    @NotNull T trimEnd(@NotNull CharSequence chars);
    @NotNull T trim(@NotNull CharSequence chars);

    @NotNull T trimStart();
    @NotNull T trimEnd();
    @NotNull T trim();

    /**
     * Get the characters Trimmed, Trimmed from start/end of this sequence, characters to trim are passed in the sequence argument
     * <p>
     * returns trimmed sequence or if nothing matched the original sequence
     * <p>
     * The pair returning functions return before and after sequence
     *
     * @param keep  minimum length of trimmed characters to keep. ie. leave this many
     * @param chars set of characters to trim from start of line
     * @return part of the sequence that was trimmed from the start
     */
    @NotNull T trimmedStart(int keep, @NotNull CharSequence chars);
    @NotNull T trimmedEnd(int keep, @NotNull CharSequence chars);
    @NotNull Pair<T, T> trimmed(int keep, @NotNull CharSequence chars);

    @NotNull T trimmedStart(int keep);
    @NotNull T trimmedEnd(int keep);
    @NotNull Pair<T, T> trimmed(int keep);

    @NotNull T trimmedStart(@NotNull CharSequence chars);
    @NotNull T trimmedEnd(@NotNull CharSequence chars);
    @NotNull Pair<T, T> trimmed(@NotNull CharSequence chars);

    @NotNull T trimmedStart();
    @NotNull T trimmedEnd();
    @NotNull Pair<T, T> trimmed();

    @NotNull T padStart(int length, char pad);
    @NotNull T padEnd(int length, char pad);
    @NotNull T padStart(int length);
    @NotNull T padEnd(int length);

    boolean isEmpty();
    boolean isBlank();
    boolean isNull();
    boolean isNotNull();

    /**
     * If this sequence is the nullSequence() instance then returns other,
     * otherwise returns this sequence.
     *
     * @param other based sequence to return if this is nullSequence()
     * @return this or other
     */
    @NotNull T ifNull(@NotNull T other);

    /**
     * If this sequence is the nullSequence() instance then returns an empty subSequence from the end of other,
     * otherwise returns this sequence.
     *
     * @param other based sequence from which to take the empty sequence
     * @return this or other.subSequence(other.length(), other.length())
     */
    @NotNull T ifNullEmptyAfter(@NotNull T other);

    /**
     * If this sequence is the nullSequence() instance then returns an empty subSequence from the start of other,
     * otherwise returns this sequence.
     *
     * @param other based sequence from which to take the empty sequence
     * @return this or other.subSequence(0, 0)
     */
    @NotNull T ifNullEmptyBefore(@NotNull T other);

    /**
     * If this sequence is empty return nullSequence() otherwise returns this sequence.
     *
     * @return this or nullSequence()
     */
    @NotNull T nullIfEmpty();

    /**
     * If this sequence is blank return nullSequence() otherwise returns this sequence.
     *
     * @return this or nullSequence()
     */
    @NotNull T nullIfBlank();

    /**
     * If condition is true return nullSequence() otherwise returns this sequence.
     *
     * @param condition when true return NULL else this
     * @return this or nullSequence()
     */
    @NotNull T nullIf(boolean condition);

    /**
     * If predicate returns true for this sequence and one of given sequences return nullSequence() otherwise returns this sequence.
     *
     * @param predicate bi predicate for test, first argument is always this, second is one of the match sequences
     * @param matches   match sequence list
     * @return this or nullSequence()
     */
    @NotNull T nullIf(@NotNull BiPredicate<? super T, ? super CharSequence> predicate, CharSequence... matches);
    @NotNull T nullIfNot(@NotNull BiPredicate<? super T, ? super CharSequence> predicate, CharSequence... matches);

    /**
     * If predicate returns true for one of given sequences return nullSequence() otherwise returns this sequence.
     *
     * @param matches match sequence list
     * @return this or nullSequence()
     */
    @NotNull T nullIf(@NotNull Predicate<? super CharSequence> predicate, CharSequence... matches);
    @NotNull T nullIfNot(@NotNull Predicate<? super CharSequence> predicate, CharSequence... matches);

    /**
     * If this sequence matches one of given sequences return nullSequence() otherwise returns this sequence.
     *
     * @param matches match sequence list
     * @return this or nullSequence()
     */
    @NotNull T nullIf(CharSequence... matches);
    @NotNull T nullIfNot(CharSequence... matches);

    @NotNull T nullIfStartsWith(CharSequence... matches);
    @NotNull T nullIfNotStartsWith(CharSequence... matches);

    @Deprecated
    default @NotNull T nullIfStartsWithNot(CharSequence... matches) {
        return nullIfNotStartsWith(matches);
    }

    @NotNull T nullIfEndsWith(CharSequence... matches);
    @NotNull T nullIfNotEndsWith(CharSequence... matches);
    @NotNull T nullIfStartsWithIgnoreCase(CharSequence... matches);
    @NotNull T nullIfNotStartsWithIgnoreCase(CharSequence... matches);
    @NotNull T nullIfEndsWithIgnoreCase(CharSequence... matches);
    @NotNull T nullIfNotEndsWithIgnoreCase(CharSequence... matches);
    @NotNull T nullIfStartsWith(boolean ignoreCase, CharSequence... matches);
    @NotNull T nullIfNotStartsWith(boolean ignoreCase, CharSequence... matches);
    @NotNull T nullIfEndsWith(boolean ignoreCase, CharSequence... matches);
    @NotNull T nullIfNotEndsWith(boolean ignoreCase, CharSequence... matches);

    @Deprecated
    default @NotNull T nullIfEndsWithNot(CharSequence... matches) {
        return nullIfNotEndsWith(matches);
    }

    /*
       EOL helper methods
     */

    /**
     * Get the length of EOL character at the end of this sequence, if present.
     * <p>
     * \n - 1
     * \r - 1
     * \r\n - 2
     *
     * @return 0 if no EOL, 1, or 2 depending on the EOL suffix of this sequence
     */
    int eolEndLength();

    @Deprecated
    default int eolStartLength() {
        return eolEndLength();
    }

    /**
     * Get the length of EOL character at the given index of this sequence, if present.
     * <p>
     * \n - 1
     * \r - 1
     * \r\n - 2
     *
     * @param eolEnd index where the EOL ends, if any, any value can be passed for this
     *               argument. If > length of this sequence then it is the same as passing length(),
     *               if 0 or less then 0 is returned.
     * @return 0 if no EOL, 1, or 2 depending on the EOL suffix of this sequence
     */
    int eolEndLength(int eolEnd);

    /**
     * Get the length of EOL character at the given index of this sequence, if present.
     * <p>
     * \n - 1
     * \r - 1
     * \r\n - 2
     *
     * @param eolStart index where the EOL starts, if any, any value can be passed for this
     *                 argument. If >= length of this sequence then 0 is returned
     *                 if 0 or less then it is the same as 0
     * @return 0 if no EOL, 1, or 2 depending on the EOL suffix of this sequence
     */
    int eolStartLength(int eolStart);

    @Deprecated
    default int eolLength(int eolStart) {
        return eolStartLength(eolStart);
    }

    /**
     * Return Range of eol at given index
     *
     * @param eolEnd index where the EOL ends, if any, any value can be passed for this
     *               argument. If > length of this sequence then it is the same as passing length(),
     *               if 0 or less then 0 is returned.
     * @return range of eol given by index of its end or Range.NULL if no eol ends at index
     */
    @NotNull Range eolEndRange(int eolEnd);

    /**
     * Return Range of eol at given index
     *
     * @param eolStart index where the EOL starts, if any, any value can be passed for this
     *                 argument. If >= length of this sequence then 0 is returned
     *                 if 0 or less then it is the same as 0
     * @return range of eol given by index of its end or Range.NULL if no eol starts at index
     */
    @NotNull Range eolStartRange(int eolStart);

    /**
     * Trim last eol at the end of this sequence,
     *
     * @return sequence with one EOL trimmed off if it had one
     */
    @NotNull T trimEOL();

    /**
     * Get Trimmed part by {@link #trimEOL()}
     *
     * @return trimmed off EOL if sequence had one or {@link #nullSequence()}
     */
    @NotNull T trimmedEOL();

    /**
     * Find start/end region in this sequence delimited by any characters in argument or the CharSequence
     * <p>
     * For Any and AnyNot methods uses the CharSequence argument as a character set of possible delimiting characters
     *
     * @param s     character sequence delimiting the region
     * @param index from which to start looking for end of region
     * @return index of end of region delimited by s
     */
    int endOfDelimitedBy(@NotNull CharSequence s, int index);
    int endOfDelimitedByAny(@NotNull CharSequence s, int index);
    int endOfDelimitedByAnyNot(@NotNull CharSequence s, int index);

    int startOfDelimitedBy(@NotNull CharSequence s, int index);
    int startOfDelimitedByAny(@NotNull CharSequence s, int index);
    int startOfDelimitedByAnyNot(@NotNull CharSequence s, int index);

    /**
     * Get the offset of the end of line at given index, end of line delimited by \n
     *
     * @param index index where to start searching for end of line
     * @return index of end of line delimited by \n
     */
    int endOfLine(int index);

    /**
     * Get the offset of the end of line at given index, end of line delimited by \n or any of \n \r \r\n for Any methods.
     *
     * @param index index where to start searching for end of line
     * @return index of end of line delimited by \n
     */
    int endOfLineAnyEOL(int index);

    /**
     * Get the offset of the start of line at given index, start of line delimited by \n
     *
     * @param index index where to start searching for end of line
     * @return index of end of line delimited by \n
     */
    int startOfLine(int index);

    /**
     * Get the offset of the start of line at given index, start of line delimited by \n or any of \n \r \r\n for Any methods.
     *
     * @param index index where to start searching for end of line
     * @return index of end of line delimited by \n
     */
    int startOfLineAnyEOL(int index);

    /**
     * Get the line characters at given index, line delimited by \n
     *
     * @param index index at which to get the line
     * @return range in sequence for the line delimited by '\n', containing index
     */
    @NotNull Range lineRangeAt(int index);
    /**
     * Get the line characters at given index, line delimited by \n, \r or \r\n
     *
     * @param index index at which to get the line
     * @return range in sequence for the line delimited by end of line, containing index
     */
    @NotNull Range lineRangeAtAnyEOL(int index);

    /**
     * Get the line characters at given index, line delimited by \n
     *
     * @param index index at which to get the line
     * @return sub-sequence for the line containing index
     */
    @NotNull T lineAt(int index);

    /**
     * Get the line characters at given index, line delimited by \n, \r or \r\n
     *
     * @param index index at which to get the line
     * @return sub-sequence for the line containing index
     */
    @NotNull T lineAtAnyEOL(int index);

    /**
     * Trim leading trailing blank lines in this sequence
     *
     * @return return sequence with trailing blank lines trimmed off
     */
    @NotNull T trimTailBlankLines();
    @NotNull T trimLeadBlankLines();

    /**
     * Get Range of leading blank lines at given index offsets in sequence
     *
     * @param eolChars  characters to consider as EOL, note {@link #eolStartLength(int)} should report length of EOL found if length > 1
     * @param fromIndex minimum index in sequence to check and include in range of blank lines
     *                  can be any value, if less than 0 it is the same as 0,
     *                  if greater than length() it is the same as length()
     * @param endIndex  index in sequence from which to start blank line search, also maximum index to include in blank lines range
     *                  can be any value, if less than 0 it is the same as 0,
     *                  if greater than length() it is the same as length()
     * @return range of blank lines at or before fromIndex and ranging to minimum of startIndex, Range.NULL if none found
     *         if the range in sequence contains only whitespace characters then the whole range will be returned
     *         even if contains no EOL characters
     */
    @NotNull Range leadingBlankLinesRange(@NotNull CharSequence eolChars, int fromIndex, int endIndex);
    /**
     * Get Range of trailing blank lines at given index offsets in sequence
     *
     * @param eolChars   characters to consider as EOL, note {@link #eolStartLength(int)} should report length of EOL found if length > 1
     * @param startIndex index in sequence from which to start blank line search, also maximum index to include in blank lines range
     *                   can be any value, if less than 0 it is the same as 0,
     *                   if greater than length() it is the same as length()
     * @param fromIndex  maximum index in sequence to check and include in range of blank lines
     *                   can be any value, if less than 0 it is the same as 0,
     *                   if greater than length() it is the same as length()
     * @return range of blank lines at or before fromIndex and ranging to minimum of startIndex
     *         if the range in sequence contains only whitespace characters then the whole range will be returned
     *         even if contains no EOL characters
     */
    @NotNull Range trailingBlankLinesRange(CharSequence eolChars, int startIndex, int fromIndex);

    @NotNull Range leadingBlankLinesRange();
    @NotNull Range leadingBlankLinesRange(int startIndex);
    @NotNull Range leadingBlankLinesRange(int fromIndex, int endIndex);
    @NotNull Range trailingBlankLinesRange();
    @NotNull Range trailingBlankLinesRange(int fromIndex);
    @NotNull Range trailingBlankLinesRange(int startIndex, int fromIndex);

    @NotNull List<Range> blankLinesRemovedRanges();
    @NotNull List<Range> blankLinesRemovedRanges(int fromIndex);
    @NotNull List<Range> blankLinesRemovedRanges(int fromIndex, int endIndex);
    @NotNull List<Range> blankLinesRemovedRanges(@NotNull CharSequence eolChars, int fromIndex, int endIndex);

    /**
     * Trim end to end of line containing index
     *
     * @param eolChars characters to consider as EOL, note {@link #eolStartLength(int)} should report length of EOL found if length > 1
     * @param index    index for offset contained by the line
     *                 can be any value, if less than 0 it is the same as 0,
     *                 if greater than length() it is the same as length()
     * @return trimmed version of the sequence to given EOL or the original sequence
     */
    @NotNull T trimToEndOfLine(@NotNull CharSequence eolChars, boolean includeEol, int index);
    @NotNull T trimToEndOfLine(boolean includeEol, int index);
    @NotNull T trimToEndOfLine(boolean includeEol);
    @NotNull T trimToEndOfLine(int index);
    @NotNull T trimToEndOfLine();

    /**
     * Trim start to start of line containing index
     *
     * @param eolChars characters to consider as EOL, note {@link #eolStartLength(int)} should report length of EOL found if length > 1
     * @param index    index for offset contained by the line
     *                 can be any value, if less than 0 it is the same as 0,
     *                 if greater than length() it is the same as length()
     * @return trimmed version of the sequence to given EOL or the original sequence
     */
    @NotNull T trimToStartOfLine(@NotNull CharSequence eolChars, boolean includeEol, int index);
    @NotNull T trimToStartOfLine(boolean includeEol, int index);
    @NotNull T trimToStartOfLine(boolean includeEol);
    @NotNull T trimToStartOfLine(int index);
    @NotNull T trimToStartOfLine();

    /**
     * replace any \r\n and \r by \n
     *
     * @return string with only \n for line separators
     */
    @NotNull String normalizeEOL();

    /**
     * replace any \r\n and \r by \n, append terminating EOL if one is not present
     *
     * @return string with only \n for line separators and terminated by \n
     */
    @NotNull String normalizeEndWithEOL();

    /*
     * comparison helpers
     */

    /**
     * Test the sequence for a match to another CharSequence
     *
     * @param chars characters to match against
     * @return true if match
     */
    boolean matches(@NotNull CharSequence chars);
    boolean matchesIgnoreCase(@NotNull CharSequence chars);
    boolean matches(@NotNull CharSequence chars, boolean ignoreCase);

    /**
     * Test the sequence for a match to another CharSequence, ignoring case differences
     *
     * @param other characters to match against
     * @return true if match
     */
    boolean equalsIgnoreCase(@Nullable Object other);

    /**
     * Test the sequence for a match to another CharSequence
     *
     * @param other      characters to match against
     * @param ignoreCase case ignored when true
     * @return true if match
     */
    boolean equals(@Nullable Object other, boolean ignoreCase);

    /**
     * Test the sequence portion for a match to another CharSequence
     *
     * @param chars characters to match against
     * @return true if characters at the start of this sequence match
     */
    boolean matchChars(@NotNull CharSequence chars);
    boolean matchCharsIgnoreCase(@NotNull CharSequence chars);
    boolean matchChars(@NotNull CharSequence chars, boolean ignoreCase);

    /**
     * Test the sequence portion for a match to another CharSequence
     *
     * @param chars      characters to match against
     * @param startIndex index from which to start the match
     * @return true if characters at the start index of this sequence match
     */
    boolean matchChars(@NotNull CharSequence chars, int startIndex);
    boolean matchCharsIgnoreCase(@NotNull CharSequence chars, int startIndex);
    boolean matchChars(@NotNull CharSequence chars, int startIndex, boolean ignoreCase);

    /**
     * Test the sequence portion for a match to another CharSequence, reverse order
     *
     * @param chars    characters to match against
     * @param endIndex index from which to start the match and proceed to 0
     * @return true if characters at the start index of this sequence match
     */
    boolean matchCharsReversed(@NotNull CharSequence chars, int endIndex);
    boolean matchCharsReversedIgnoreCase(@NotNull CharSequence chars, int endIndex);
    boolean matchCharsReversed(@NotNull CharSequence chars, int endIndex, boolean ignoreCase);

    /**
     * test if this sequence ends with given characters
     *
     * @param suffix characters to test
     * @return true if ends with suffix
     */
    boolean endsWith(@NotNull CharSequence suffix);
    boolean endsWithEOL();              // EOL "\n"
    boolean endsWithAnyEOL();           // EOL_CHARS "\r\n"
    boolean endsWithSpace();            // SPACE " "
    boolean endsWithSpaceTab();         // SPACE_TAB " \t"
    boolean endsWithWhitespace();       // WHITESPACE_CHARS " \t\r\n"

    /**
     * test if this sequence ends with given characters, ignoring case differences
     *
     * @param suffix characters to test
     * @return true if ends with suffix
     */
    boolean endsWithIgnoreCase(@NotNull CharSequence suffix);

    /**
     * test if this sequence ends with given characters
     *
     * @param suffix     characters to test
     * @param ignoreCase case ignored when true
     * @return true if ends with suffix
     */
    boolean endsWith(@NotNull CharSequence suffix, boolean ignoreCase);

    /**
     * test if this sequence starts with given characters
     *
     * @param prefix characters to test
     * @return true if starts with prefix
     */
    boolean startsWith(@NotNull CharSequence prefix);
    boolean startsWithEOL();            // EOL "\n"
    boolean startsWithAnyEOL();         // EOL_CHARS "\r\n"
    boolean startsWithSpace();          // SPACE " "
    boolean startsWithSpaceTab();       // SPACE_TAB " \t"
    boolean startsWithWhitespace();     // WHITESPACE_CHARS " \t\r\n"

    /**
     * test if this sequence starts with given characters, ignoring case differences
     *
     * @param prefix characters to test
     * @return true if starts with prefix
     */
    boolean startsWithIgnoreCase(@NotNull CharSequence prefix);

    /**
     * test if this sequence starts with given characters
     *
     * @param prefix     characters to test
     * @param ignoreCase case ignored when true
     * @return true if starts with prefix
     */
    boolean startsWith(@NotNull CharSequence prefix, boolean ignoreCase);

    /**
     * Remove suffix if present
     *
     * @param suffix characters to remove
     * @return sequence with suffix removed, or same sequence if no suffix was present
     */
    @NotNull T removeSuffix(@NotNull CharSequence suffix);

    /**
     * Remove suffix if present, ignoring case differences
     *
     * @param suffix characters to remove
     * @return sequence with suffix removed, or same sequence if no suffix was present
     */
    @NotNull T removeSuffixIgnoreCase(@NotNull CharSequence suffix);

    /**
     * Remove suffix if present
     *
     * @param suffix     characters to remove
     * @param ignoreCase case ignored when true
     * @return sequence with suffix removed, or same sequence if no suffix was present
     */
    @NotNull T removeSuffix(@NotNull CharSequence suffix, boolean ignoreCase);

    /**
     * Remove prefix if present
     *
     * @param prefix characters to remove
     * @return sequence with prefix removed, or same sequence if no prefix was present
     */
    @NotNull T removePrefix(@NotNull CharSequence prefix);

    /**
     * Remove prefix if present, ignoring case differences
     *
     * @param prefix characters to remove
     * @return sequence with prefix removed, or same sequence if no prefix was present
     */
    @NotNull T removePrefixIgnoreCase(@NotNull CharSequence prefix);

    /**
     * Remove prefix if present
     *
     * @param prefix     characters to remove
     * @param ignoreCase case ignored when true
     * @return sequence with prefix removed, or same sequence if no prefix was present
     */
    @NotNull T removePrefix(@NotNull CharSequence prefix, boolean ignoreCase);

    /**
     * Remove suffix if present but only if this sequence is longer than the suffix
     *
     * @param suffix characters to remove
     * @return sequence with suffix removed, or same sequence if no suffix was present
     */
    @NotNull T removeProperSuffix(@NotNull CharSequence suffix);

    /**
     * Remove suffix if present but only if this sequence is longer than the suffix, ignoring case differences
     *
     * @param suffix characters to remove
     * @return sequence with suffix removed, or same sequence if no suffix was present
     */
    @NotNull T removeProperSuffixIgnoreCase(@NotNull CharSequence suffix);

    /**
     * Remove suffix if present but only if this sequence is longer than the suffix
     *
     * @param suffix     characters to remove
     * @param ignoreCase case ignored when true
     * @return sequence with suffix removed, or same sequence if no suffix was present
     */
    @NotNull T removeProperSuffix(@NotNull CharSequence suffix, boolean ignoreCase);

    /**
     * Remove prefix if present but only if this sequence is longer than the suffix
     *
     * @param prefix characters to remove
     * @return sequence with prefix removed, or same sequence if no prefix was present
     */
    @NotNull T removeProperPrefix(@NotNull CharSequence prefix);

    /**
     * Remove prefix if present but only if this sequence is longer than the suffix, ignoring case differences
     *
     * @param prefix characters to remove
     * @return sequence with prefix removed, or same sequence if no prefix was present
     */
    @NotNull T removeProperPrefixIgnoreCase(@NotNull CharSequence prefix);

    /**
     * Remove prefix if present but only if this sequence is longer than the suffix
     *
     * @param prefix     characters to remove
     * @param ignoreCase case ignored when true
     * @return sequence with prefix removed, or same sequence if no prefix was present
     */
    @NotNull T removeProperPrefix(@NotNull CharSequence prefix, boolean ignoreCase);

    /**
     * Insert char sequence at given index
     *
     * @param chars char sequence to insert
     * @param index index of insertion. if &gt;length of this sequence then same as length, if &lt;0 then same as 0
     * @return resulting sequence
     *         based sequence implementation may throw an IllegalArgumentException
     *         if inserting another based sequence out of order based on offsets
     */
    @NotNull T insert(@NotNull CharSequence chars, int index);

    /**
     * Delete range in sequence
     *
     * @param startIndex start index of deletion
     * @param endIndex   end index, not inclusive, of insertion
     * @return resulting sequence
     */
    @NotNull T delete(int startIndex, int endIndex);

    /**
     * Replace part of the sequence with a char sequence
     *
     * @param startIndex  start index of replaced part
     * @param endIndex    end index of replaced part
     * @param replacement char sequence
     * @return resulting sequence
     */
    @NotNull T replace(int startIndex, int endIndex, @NotNull CharSequence replacement);

    /**
     * Replace all occurrences of one sequence with another
     *
     * @param find    sequence to find
     * @param replace replacement sequence
     * @return array of indices
     */
    @NotNull T replace(@NotNull CharSequence find, @NotNull CharSequence replace);

    /**
     * Map characters of this sequence to: Uppercase, Lowercase or use custom mapping
     *
     * @return lowercase version of sequence
     */
    @NotNull T toLowerCase();
    @NotNull T toUpperCase();
    @NotNull T toMapped(CharMapper mapper);

    /**
     * Map spaces to non break spaces
     *
     * @return mapped sequence with spc changed to NbSp
     */
    @NotNull T toNbSp();

    /**
     * Map non break spaces to spaces
     *
     * @return mapped sequence with NbSp changed to spc
     */
    @NotNull T toSpc();

    @NotNull String toVisibleWhitespaceString();

    int SPLIT_INCLUDE_DELIMS = 1;
    int SPLIT_TRIM_PARTS = 2;
    int SPLIT_SKIP_EMPTY = 4;
    int SPLIT_INCLUDE_DELIM_PARTS = 8;
    int SPLIT_TRIM_SKIP_EMPTY = SPLIT_TRIM_PARTS | SPLIT_SKIP_EMPTY;

    /**
     * Split helpers based on delimiter character sets contained in CharSequence
     *
     * @param delimiter delimiter char or set of chars in CharSequence to split this sequence on
     * @param limit     max number of segments to split
     * @param flags     flags for desired options:
     *                  SPLIT_INCLUDE_DELIMS: include delimiters as part of split item
     *                  SPLIT_TRIM_PARTS: trim the segments, if trimChars is not null or empty then this flag is turned on automatically
     *                  SPLIT_SKIP_EMPTY: skip empty segments (or empty after trimming if enabled)
     *                  SPLIT_INCLUDE_DELIM_PARTS: include delimiters as separate split item of its own
     *                  SPLIT_TRIM_SKIP_EMPTY: same as SPLIT_TRIM_PARTS | SPLIT_SKIP_EMPTY
     * @param trimChars set of characters that should be used for trimming individual split results
     * @return List of split results
     */
    @NotNull T[] split(char delimiter, int limit, int flags, String trimChars);
    @NotNull T[] split(char delimiter);
    @NotNull T[] split(char delimiter, int limit);
    @NotNull T[] split(char delimiter, int limit, int flags);
    @NotNull T[] split(@NotNull CharSequence delimiter);
    @NotNull T[] split(@NotNull CharSequence delimiter, int limit);
    @NotNull T[] split(@NotNull CharSequence delimiter, int limit, int flags);
    @NotNull T[] split(@NotNull CharSequence delimiter, int limit, int flags, @Nullable String trimChars);
    @NotNull List<T> splitList(char delimiter, int limit, int flags, String trimChars);
    @NotNull List<T> splitList(char delimiter);
    @NotNull List<T> splitList(char delimiter, int limit);
    @NotNull List<T> splitList(char delimiter, int limit, int flags);
    @NotNull List<T> splitList(@NotNull CharSequence delimiter);
    @NotNull List<T> splitList(@NotNull CharSequence delimiter, int limit);
    @NotNull List<T> splitList(@NotNull CharSequence delimiter, int limit, int flags);
    @NotNull List<T> splitList(@NotNull CharSequence delimiter, int limit, int flags, @Nullable String trimChars);

    /**
     * Split helpers based on delimiter character sets contained in CharSequence
     *
     * @param delimiter     delimiter char or set of chars in CharSequence to split this sequence on
     * @param limit         max number of segments to split
     * @param includeDelims if true include delimiters as part of split item
     * @param trimChars     set of characters that should be used for trimming individual split results
     * @return List of split results
     */
    @NotNull T[] split(char delimiter, int limit, boolean includeDelims, String trimChars);
    @NotNull T[] split(char delimiter, int limit, boolean includeDelims);
    @NotNull T[] split(@NotNull CharSequence delimiter, int limit, boolean includeDelims);
    @NotNull T[] split(@NotNull CharSequence delimiter, int limit, boolean includeDelims, @Nullable String trimChars);
    @NotNull List<T> splitList(char delimiter, int limit, boolean includeDelims, String trimChars);
    @NotNull List<T> splitList(char delimiter, int limit, boolean includeDelims);
    @NotNull List<T> splitList(@NotNull CharSequence delimiter, int limit, boolean includeDelims);
    @NotNull List<T> splitList(@NotNull CharSequence delimiter, int limit, boolean includeDelims, @Nullable String trimChars);

    // NOTE: these default to including delimiters as part of split item
    @NotNull T[] splitEOL();
    @NotNull T[] splitEOL(boolean includeDelims);
    @NotNull T[] splitEOL(int limit);
    @NotNull T[] splitEOL(int limit, boolean includeDelims);
    @NotNull T[] splitEOL(int limit, boolean includeDelims, @Nullable String trimChars);
    @NotNull List<T> splitListEOL();
    @NotNull List<T> splitListEOL(boolean includeDelims);
    @NotNull List<T> splitListEOL(int limit);
    @NotNull List<T> splitListEOL(int limit, boolean includeDelims);
    @NotNull List<T> splitListEOL(int limit, boolean includeDelims, @Nullable String trimChars);

    /**
     * Get indices of all occurrences of a sequence
     *
     * @param s sequence whose indices to find
     * @return array of indices
     */
    @NotNull int[] indexOfAll(@NotNull CharSequence s);

    /**
     * Prefix this sequence with a char sequence
     *
     * @param prefix char sequence
     * @return resulting sequence
     */
    @NotNull T prefixWith(@Nullable CharSequence prefix);
    /**
     * Prefix this sequence with a char sequence
     *
     * @param suffix char sequence
     * @return resulting sequence
     */
    @NotNull T suffixWith(@Nullable CharSequence suffix);

    /**
     * Prefix this sequence with a char sequence if not already starting with prefix
     *
     * @param prefix char sequence
     * @return resulting sequence
     */
    @NotNull T prefixOnceWith(@Nullable CharSequence prefix);
    /**
     * Suffix this sequence with a char sequence if not already ending with suffix
     *
     * @param suffix char sequence
     * @return resulting sequence
     */
    @NotNull T suffixOnceWith(@Nullable CharSequence suffix);

    @NotNull T appendEOL();
    @NotNull T suffixWithEOL();
    @NotNull T prefixWithEOL();
    @NotNull T prefixOnceWithEOL();
    @NotNull T suffixOnceWithEOL();

    @NotNull T appendSpace();
    @NotNull T suffixWithSpace();
    @NotNull T prefixWithSpace();
    @NotNull T appendSpaces(int count);
    @NotNull T suffixWithSpaces(int count);
    @NotNull T prefixWithSpaces(int count);
    @NotNull T prefixOnceWithSpace();
    @NotNull T suffixOnceWithSpace();
    /**
     * Append helpers
     *
     * @param out        string builder
     * @param startIndex start index
     * @param endIndex   end index
     * @param charMapper mapping to use for output or null if none
     * @return this
     */
    @NotNull T appendTo(@NotNull StringBuilder out, @Nullable CharMapper charMapper, int startIndex, int endIndex);
    @NotNull T appendTo(@NotNull StringBuilder out, @Nullable CharMapper charMapper);
    @NotNull T appendTo(@NotNull StringBuilder out, @Nullable CharMapper charMapper, int startIndex);
    @NotNull T appendTo(@NotNull StringBuilder out, int startIndex, int endIndex);
    @NotNull T appendTo(@NotNull StringBuilder out);
    @NotNull T appendTo(@NotNull StringBuilder out, int startIndex);

    /**
     * Append given ranges of this sequence to string builder
     *
     * @param out        string builder to append to
     * @param charMapper mapping to use for output or null if none
     * @param ranges     ranges to append, null range or one for which range.isNull() is true ranges are skipped
     * @return this
     */
    @NotNull T appendRangesTo(@NotNull StringBuilder out, @Nullable CharMapper charMapper, Range... ranges);
    @NotNull T appendRangesTo(@NotNull StringBuilder out, Range... ranges);
    @NotNull T appendRangesTo(@NotNull StringBuilder out, @Nullable CharMapper charMapper, Iterable<? extends Range> ranges);
    @NotNull T appendRangesTo(@NotNull StringBuilder out, Iterable<? extends Range> ranges);

    /**
     * Build a sequence of ranges in this sequence
     * <p>
     * NOTE: BasedSequence ranges must be non-overlapping and ordered by startOffset or
     * IllegalArgumentException will be thrown
     *
     * @param ranges ranges to extract
     * @return resulting sequence
     */
    @NotNull T extractRanges(Range... ranges);
    @NotNull T extractRanges(Iterable<Range> ranges);

    /**
     * Concatenate this sequence and list of others, returning sequence of result
     *
     * @param sequences list of char sequences to append to this sequence, null sequences are skipped
     * @return appended sequence
     */
    @NotNull T append(CharSequence... sequences);
    @NotNull T append(Iterable<? extends CharSequence> sequences);

    /**
     * Get the line and column information from index into sequence
     *
     * @param index index for which to get line information
     * @return Pair(line, column) where line and column are 0 based,
     *         throws IllegalArgumentException if index &lt; 0 or &gt; length.
     */
    @NotNull Pair<Integer, Integer> lineColumnAtIndex(int index);
    @Deprecated
    default @NotNull Pair<Integer, Integer> getLineColumnAtIndex(int index) {
        return lineColumnAtIndex(index);
    }

    int columnAtIndex(int index);

    @Deprecated
    default int getColumnAtIndex(int index) {
        return columnAtIndex(index);
    }
}
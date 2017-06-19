package de.uni.potsdam.doctool.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.List;

/**
 * the helper to find code position in source code as character array
 */
public class CodeCharacterUtil {

    /**
     * find code position with line number.
     *
     * @param lineLengthCache the list of line numbers from source code, that are scanned.
     * @param lineNumber the line number from source code.
     * @param text the source code as character array
     * @return the code position wrapper instance
     */
    public static Position findPosition(final List<Integer> lineLengthCache, int lineNumber, final char[] text) {
        return findPosition(lineLengthCache, lineNumber, 0, text);
    }

    /**
     *find code position with line number and column number.
     *
     * @param lineLengthCache the list of line numbers from source code, that are scanned.
     * @param lineNumber the line number from source code.
     * @param columnNumber the column number from source code.
     * @param text the source code as character array
     * @return the code position wrapper instance
     */
    public static Position findPosition(final List<Integer> lineLengthCache, int lineNumber, int columnNumber, final char[] text) {
        if (lineNumber == 0) {
            return new Position(columnNumber, false);
        } else if (lineNumber <= lineLengthCache.size()) {
            return new Position(lineLengthCache.get(lineNumber - 1) + columnNumber, false);
        } else {
            return searchFromLastScannedLine(lineLengthCache, lineNumber, columnNumber, text);
        }
    }

    /**
     * keep on finding code position from last line number in cache data.
     *
     * @param lineLengthCache the list of line numbers from source code, that are scanned.
     * @param lineNumber the line number from source code.
     * @param columnNumber the column number from source code.
     * @param text the source code as character array
     * @return the code position wrapper instance
     */
    private static Position searchFromLastScannedLine(final List<Integer> lineLengthCache, int lineNumber, int columnNumber, final char[] text) {
        final Position position;
        int offset = lineLengthCache.get(lineLengthCache.size() - 1);
        boolean afterEndOfLine = false;
        int line = lineLengthCache.size();

        int column = 0;
        for (int i = offset; i < text.length; ++i) {
            final char character = text[i];

            final char nextChar = nextCharacter(text, i);
            if (character == '\n' || character == '\r' && nextChar != '\n') {
                ++line;
                ++offset;
                lineLengthCache.add(offset);
                column = 0;
            } else {
                ++column;
                ++offset;
            }

            if (lineNumber == line && columnNumber == column) {
                if (column == 0 && Character.isWhitespace(nextChar)) {
                    afterEndOfLine = true;
                }
                break;
            }
        }

        position = new Position(offset, afterEndOfLine);
        return position;
    }

    private static char nextCharacter(char[] text, int i) {
        if ((i + 1) < text.length) {
            return text[i + 1];
        }
        return '\0';
    }

    /**
     * the code position wrapper
     */
    public static final class Position {

        /** the code position is after end of line */
        private final boolean afterEndOfLine;
        /** the offset of code position in source code */
        private final int offset;

        private Position(final int offset, final boolean afterEndOfLine) {
            this.offset = offset;
            this.afterEndOfLine = afterEndOfLine;
        }

        /**
         * get psi element in psi file with this code position instance
         *
         * @param psiFile the psi file to find.
         * @return the psi element when the offset of position is exist in psi file
         */
        public PsiElement getElement(final PsiFile psiFile) {
            return psiFile.findElementAt(offset);
        }

        public boolean isAfterEndOfLine() {
            return afterEndOfLine;
        }
    }
}

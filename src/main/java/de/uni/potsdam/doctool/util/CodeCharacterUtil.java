package de.uni.potsdam.doctool.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.List;

/**
 * Created by ngxanh88 on 07.06.17.
 */
public class CodeCharacterUtil {

    public static Position findPosition(final List<Integer> lineLengthCache, int lineNumber, final char[] text) {
        return findPosition(lineLengthCache, lineNumber, 0, text);
    }

    public static Position findPosition(final List<Integer> lineLengthCache, int lineNumber, int columnNumber, final char[] text) {
        if (lineNumber == 0) {
            return Position.at(columnNumber);
        } else if (lineNumber <= lineLengthCache.size()) {
            return Position.at(lineLengthCache.get(lineNumber - 1) + columnNumber);
        } else {
            return searchFromEndOfCachedData(lineLengthCache, lineNumber, columnNumber, text);
        }
    }

    private static Position searchFromEndOfCachedData(final List<Integer> lineLengthCache, int lineNumber, int columnNumber, final char[] text) {
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

        position = Position.at(offset, afterEndOfLine);
        return position;
    }

    private static char nextCharacter(char[] text, int i) {
        if ((i + 1) < text.length) {
            return text[i + 1];
        }
        return '\0';
    }

    public static final class Position {
        private final boolean afterEndOfLine;
        private final int offset;

        private Position(final int offset, final boolean afterEndOfLine) {
            this.offset = offset;
            this.afterEndOfLine = afterEndOfLine;
        }

        private static Position at(final int offset, final boolean afterEndOfLine) {
            return new Position(offset, afterEndOfLine);
        }

        private static Position at(final int offset) {
            return new Position(offset, false);
        }

        public PsiElement getElement(final PsiFile psiFile) {
            return psiFile.findElementAt(offset);
        }

        public boolean isAfterEndOfLine() {
            return afterEndOfLine;
        }
    }
}

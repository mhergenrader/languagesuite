package guicomponents;

// Copyright (c) 2010-2011 Michael Hergenrader
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

/**
 * Allows the user to press a button to add an accent to the current final letter in a text field
 * @author Michael Hergenrader
 */
public class Accent {
    public static final String ACCENT_EXCEPTION_MESSAGE_UPPERCASE = "You cannot accent a non-accentable character.\nIt must be: A, E, I, O, U, C, or N.";
    public static final String ACCENT_EXCEPTION_MESSAGE_LOWERCASE = "You cannot accent a non-accentable character.\nIt must be: a, e, i, o, u, c, or n.";
    public static final String EMPTY_ARGUMENT_MESSAGE = "Nothing entered!";
	
    public static String accentFinalCharacter(String s) throws IllegalArgumentException {
        if(s.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ARGUMENT_MESSAGE);
        }
        if(!Character.isLetter(s.charAt(s.length()-1))) {
            throw new IllegalArgumentException("You cannot accent a non-letter.");
        }

        if(Character.isUpperCase(s.charAt(s.length()-1))) {
            return addAccentUppercase(s);
        }
        else {
            return addAccentLowercase(s);
        }
    }

    // helper method that takes the last letter (if uppercase) and attempts to add an accent to it
	// avoids need for a separate dictionary data structure
    private static String addAccentUppercase(String s) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder(s);
        char charToAdd;

        switch(s.charAt(s.length()-1)) {
            case 'A': // normal a
                charToAdd = 'Á';
                break;
            case 'Á': // acute accent
                charToAdd = 'À';
                break;
            case 'À': // grave accent
                charToAdd = 'Ã';
                break;
            case 'Ã': // tilde
                charToAdd = 'Â';
                break;
            case 'Â': // circumflex
                charToAdd = 'Ä';
                break;
            case 'Ä': // dieresis/umlaut
                charToAdd = 'A';
                break;

            case 'E':
                charToAdd = 'É';
                break;
            case 'É':
                charToAdd = 'È';
                break;
            case 'È':
                charToAdd = 'Ê';
                break;
            case 'Ê':
                charToAdd = 'Ë';
                break;
            case 'Ë':
                charToAdd = 'E';
                break;

            case 'I':
                charToAdd = 'Í';
                break;
            case 'Í':
                charToAdd = 'Ì';
                break;
            case 'Ì':
                charToAdd = 'Î';
                break;
            case 'Î':
                charToAdd = 'Ï';
                break;
            case 'Ï':
                charToAdd = 'I';
                break;

            case 'O':
                charToAdd = 'Ó';
                break;
            case 'Ó':
                charToAdd = 'Ò';
                break;
            case 'Ò':
                charToAdd = 'Õ';
                break;
            case 'Õ':
                charToAdd = 'Ô';
                break;
            case 'Ô':
                charToAdd = 'Ö';
                break;
            case 'Ö':
                charToAdd = 'O';
                break;

            case 'U':
                charToAdd = 'Ú';
                break;
            case 'Ú':
                charToAdd = 'Ù';
                break;
            case 'Ù':
                charToAdd = 'Û';
                break;
            case 'Û':
                charToAdd = 'Ü';
                break;
            case 'Ü':
                charToAdd = 'U';
                break;

            case 'C':
                charToAdd = 'Ç';
                break;
            case 'Ç':
                charToAdd = 'C';
                break;

            case 'N':
                charToAdd = 'Ñ';
                break;
            case 'Ñ':
                charToAdd = 'N';
                break;

            default:
                throw new IllegalArgumentException(ACCENT_EXCEPTION_MESSAGE_UPPERCASE);
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append(charToAdd);
        return sb.toString();
    }

    // helper method that takes the last letter (if uppercase) and attempts to add an accent to it
    private static String addAccentLowercase(String s) throws IllegalArgumentException {        
        StringBuilder sb = new StringBuilder(s);
        char charToAdd;

        switch(s.charAt(s.length()-1)) {
            case 'a': // normal a
                charToAdd = 'á';
                break;
            case 'á': // acute accent
                charToAdd = 'à';
                break;
            case 'à': // grave accent
                charToAdd = 'ã';
                break;
            case 'ã': // tilda
                charToAdd = 'â';
                break;
            case 'â': // circumflex
                charToAdd = 'ä';
                break;
            case 'ä': // dieresis/umlaut
                charToAdd = 'a';
                break;

            case 'e':
                charToAdd = 'é';
                break;
            case 'é':
                charToAdd = 'è';
                break;
            case 'è':
                charToAdd = 'ê';
                break;
            case 'ê':
                charToAdd = 'ë';
                break;
            case 'ë':
                charToAdd = 'e';
                break;

            case 'i':
                charToAdd = 'í';
                break;
            case 'í':
                charToAdd = 'ì';
                break;
            case 'ì':
                charToAdd = 'î';
                break;
            case 'î':
                charToAdd = 'ï';
                break;
            case 'ï':
                charToAdd = 'i';
                break;

            case 'o':
                charToAdd = 'ó';
                break;
            case 'ó':
                charToAdd = 'ò';
                break;
            case 'ò':
                charToAdd = 'õ';
                break;
            case 'õ':
                charToAdd = 'ô';
                break;
            case 'ô':
                charToAdd = 'ö';
                break;
            case 'ö':
                charToAdd = 'o';
                break;

            case 'u':
                charToAdd = 'ú';
                break;
            case 'ú':
                charToAdd = 'ù';
                break;
            case 'ù':
                charToAdd = 'û';
                break;
            case 'û':
                charToAdd = 'ü';
                break;
            case 'ü':
                charToAdd = 'u';
                break;

            case 'c':
                charToAdd = 'ç';
                break;
            case 'ç':
                charToAdd = 'c';
                break;

            case 'n':
                charToAdd = 'ñ';
                break;
            case 'ñ':
                charToAdd = 'n';
                break;

            default:
                throw new IllegalArgumentException(ACCENT_EXCEPTION_MESSAGE_LOWERCASE);
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append(charToAdd);
        return sb.toString();
    }
}

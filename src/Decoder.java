import java.util.Arrays;

/**
 * Implements decoding with fix transmission rate
 */
public class Decoder {
    // transmission rate (bits per dot)
    private static int rate = 0;

    // time units
    private static final int NDOT = 1;
    private static final int NDASH = 3;
    private static final int NSPACE = 7;

    /**
     * Replaces leading and trailing useless 0s
     * @param bitCode
     * @return
     */
    private static String trimBits(String bitCode) {
        String trimmed = bitCode.replaceAll("^0+", "");
        trimmed = trimmed.replaceAll("0+$", "");
        return trimmed;
    }

    /**
     * Calculates transmission rate (bit/dot)
     */
    private static void setRate(String bitCode) {
        String[] ones = bitCode.split("0+");
        int[] durs = new int[ones.length];
        for (int i = 0; i < durs.length; i++) {
            durs[i] = ones[i].length();
        }
        if (durs.length > 1)
            Arrays.sort(durs);

        rate = durs[0];
    }

    /**
     * Decodes a morse signal via its bit code
     * @param sig
     * @param pause
     * @return
     */
    private static String decodeSig(int sig, int pause) {
        String res = "";
        if (sig == NDOT * rate)
            res += ".";
        else if (sig == NDASH * rate)
            res += "-";

        if (pause == NDASH * rate)
            res += " ";
        else if (pause == NSPACE * rate)
            res += "   ";

        return res;
    }

    /**
     * Makes bit code parsing
     */
    private static String decodeBits(String bitCode) {
        if (bitCode.isEmpty() || !bitCode.contains("1"))
                return "";

        String morseCode = "";

        String[] ones = bitCode.split("0+");
        String[] zeros = bitCode.split("1+");

        for (int i = 0; i < ones.length - 1; i++) {
            morseCode += decodeSig(ones[i].length(), zeros[i + 1].length());
        }

        morseCode += decodeSig(ones[ones.length - 1].length(), 0);

        return morseCode;
    }

    /**
     * Decodes a letter + whitespace
     * @param code
     * @param space
     * @return
     */
    private static String decodeLetter(String code, String space) {
        String res = "";
        res += MorseCode.get(code);
        if (space.length() == 3) {
            res += " ";
        }

        return res;
    }

    /**
     * Makes morse code parsing
     */
    private static String decodeMorse(String morseCode) {
        if (morseCode == "")
            return "";

        String msg = "";

        morseCode = morseCode.trim();

        String[] letters = morseCode.split(" +");   // makes an array of letters in morse code
        String[] spaces = morseCode.split("[.-]+"); // makes an array of spaces in code

        // main decoding loop
        for (int i = 0; i < letters.length - 1; i++) {
            msg += decodeLetter(letters[i], spaces[i + 1]);
        }

        msg += decodeLetter(letters[letters.length - 1], "");

        return msg;
    }

    /**
     * Get transmission rate
     */
    private static int getRate() {
        return rate;
    }

    public static void main(String[] args) {
        String bitStream = "1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011";
        String bitCode = trimBits(bitStream);
        setRate(bitCode);
        System.out.println("Rate: " + getRate() + " bit/dot");
        String morseCode = decodeBits(bitCode);
        System.out.println("Morse code: " + morseCode);
        String msg = decodeMorse(morseCode);
        System.out.println("Text: " + msg);
    }
}

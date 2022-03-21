package ch.luca.hydroslide.chestshop.utils;

public class MathUtil {

    /**
     * Check if a String is an Integer
     *
     * @param string to check
     * @return if the string is an Integer
     */
    public static boolean isInt( String string ) {
        if ( string == null ) return false;
        try {
            int amount = Integer.parseInt( string );
            return amount > 0;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }
}

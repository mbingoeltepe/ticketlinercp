package at.ticketline.entity;

public enum Kreditkartentyp {
    VISA, DINERS_CLUB, AMERICAN_EXPRESS, MASTERCARD;

    /**
     * Diese Method wird für Kunde Bearbeiten oder für Kunde anlegen verwendet.
     * 
     * @author MURAT BINGOLTEPE
     */
    public static String[] toStringArray() {
	return new String[] { "VISA", "DINERS CLUB", "AMERICAN EXPRESS", "MASTERCARD" };
    }

    /**
     * Diese Method wird für Kunde Bearbeiten oder für Kunde anlegen verwendet.
     * 
     * @author MURAT BINGOLTEPE
     */
    public static Kreditkartentyp getValueOf(String value) {
	if (value == null) {
	    return null;
	}
	if (value.trim().toUpperCase().equals("VISA")) {
	    return Kreditkartentyp.VISA;
	}
	if (value.trim().toUpperCase().equals("DINERS CLUB")) {
	    return Kreditkartentyp.DINERS_CLUB;
	}
	if (value.trim().toUpperCase().equals("AMERICAN EXPRESS")) {
	    return Kreditkartentyp.AMERICAN_EXPRESS;
	}
	if (value.trim().toUpperCase().equals("MASTERCARD")) {
	    return Kreditkartentyp.MASTERCARD;
	}
	return null;
    }
}

package at.ticketline.entity;

public enum Kundengruppe {
    STANDARD, VIP, GOLD, PREMIUM;

    /**
     * Diese Method wird für Kunde Bearbeiten oder für Kunde anlegen verwendet.
     * 
     * @author MURAT BINGOLTEPE
     */
    public static String[] toStringArray() {
	return new String[] { "STANDARD", "VIP", "GOLD", "PREMIUM" };
    }

    /**
     * Diese Method wird für Kunde Bearbeiten oder für Kunde anlegen verwendet.
     * 
     * @author MURAT BINGOLTEPE
     */
    public static Kundengruppe getValueOf(String value) {
	if (value == null) {
	    return null;
	}
	if (value.trim().toUpperCase().equals("STANDARD")) {
	    return Kundengruppe.STANDARD;
	}
	if (value.trim().toUpperCase().equals("VIP")) {
	    return Kundengruppe.VIP;
	}
	if (value.trim().toUpperCase().equals("GOLD")) {
	    return Kundengruppe.GOLD;
	}
	if (value.trim().toUpperCase().equals("PREMIUM")) {
	    return Kundengruppe.PREMIUM;
	}
	return null;
    }
}

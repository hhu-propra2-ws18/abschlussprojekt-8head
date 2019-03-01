package hhu.ausleihservice.validators;

public class Messages {


	public static String notEmpty = "Benötigtes Feld";

	public static String usernameSize = "Der Benutzername muss zwischen 6 und 32 Zeichen lang sein.";
	public static String duplicateUsername = "Benutzername bereits vergeben.";
	public static String passwordSize = "Passwort muss zwischen 3 und 100 Zeichen lang sein.";
	public static String invalidEmail = "Email ist nicht gültig.";

	public static String latitudeOutOfBounds = "Breitengrad muss zwischen -90 und 90 sein.";
	public static String longitudeOutOfBounds = "Längengrad muss zwischen -180 und 180 sein.";
	public static String sizeLocationDescription = "Beschreibung muss zwischen 6 und 250 Zeichen lang sein.";

	public static String sizeTitle = "Titel muss zwischen 6 und 40 Zeichen lang sein.";
	public static String sizeItemDescription = "Beschreibung muss zwischen 6 und 4000 Zeichen lang sein.";
	public static String negativeValue = "Der Wert muss mindestens 1 betragen";
	public static String invalidPeriod = "Das Anfangsdatum muss vor dem Enddatum liegen!";
	public static String invalidAvailableFrom = "Das Anfangsdatum muss mindestens heute sein";

	public static String itemNotAvailable = "Der Artikel ist im gewählten Zeitraum nicht verfügbar";
	public static String invalidUser = "Der angegebene Nutzer stimmt nicht mit dem Login überein";
	public static String ownItemAusleihe = "Ein eigener Artikel kann nicht ausgeliehen werden";
	public static String ownItemKauf = "Ein eigener Artikel kann nicht gekauft werden";
	public static String notEnoughMoney = "Das Guthaben auf dem ProPay-Konto reicht nicht für die gewünschte Aktion";

	public static String propayUnavailable = "ProPay ist aktuell nicht verfügbar";
	public static String schonVerkauft = "Der Artikel wurde bereits verkauft";
}

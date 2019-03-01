package hhu.ausleihservice.databasemodel;

public enum Status {
	ANGEFRAGT("Die Ausleihe wurde angefragt"),
	BESTAETIGT("Die Ausleihe wurde bestätigt"),
	ABGELEHNT("Die Ausleihe wurde abgelehnt"),
	AUSGELIEHEN("Der Artikel wurde ausgeliehen"),
	ABGESCHLOSSEN("Die Ausleihe ist abgeschlossen"),
	RUECKGABE_ANGEFRAGT("Die Rückgabe wurde angefragt"),
	RUECKGABE_VERPASST("Die Rückgabefrist ist überschritten"),
	VERKAUFT("Der Artikel wurde erfolgreich verkauft");

	private final String beschreibung;

	Status(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getBeschreibung() {
		return beschreibung;
	}
}

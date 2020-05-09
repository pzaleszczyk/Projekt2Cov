package App;


public enum Color {
	RED, GREEN;

	public static Color toColor(String color) {
		if(color != null)
			switch(color) {
			case "RED" : return RED;
			case "GREEN" : return GREEN;
			}
		return null;
	}
}
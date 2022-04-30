package Server.enums;

public enum HttpMethod {

	GET, //
	POST, //package Server.enums;
	PUT, //
	DELETE, //
	HEAD;

	public String getName() {
		return name();
	}

	@Override
	public String toString() {
		return name();
	}

}
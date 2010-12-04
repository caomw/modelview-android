package org.jtb.modelview;

public class ModelLoadException extends Exception {
	private String path;
	private int lineNumber;
	private String line;

	public ModelLoadException() {
		super();
	}

	public ModelLoadException(String msg) {
		super(msg);
	}

	public ModelLoadException(Throwable t) {
		super(t);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	@Override
	public String getMessage() {
		String msg = "";
		if (super.getMessage() != null) {
			msg = super.getMessage() + "\n";
		}
		msg += path + "\nLine Number: " + lineNumber + "\nLine: \""
				+ line + "\"";
		return msg;
	}
}

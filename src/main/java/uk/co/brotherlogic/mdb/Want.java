package uk.co.brotherlogic.mdb;

public class Want implements Comparable<Want> {

	@Override
	public int compareTo(Want arg0) {
		return (author + title).hashCode()
				- (arg0.author + arg0.title).hashCode();
	}

	public int getRecordID() {
		return recordID;
	}

	@Override
	public String toString() {
		if (recordID >= 0)
			return author + " - " + title + " [" + recordID + "]";
		else
			return author + " - " + title;
	}

	String author;
	String title;
	int recordID;

	public Want(String line) {
		this("", "");
		String[] elems = line.trim().split("~");
		if (elems.length >= 2) {
			author = elems[0];
			title = elems[1];
		}
		if (elems.length == 3)
			recordID = Integer.parseInt(elems[2]);
	}

	public Want(String aut, String tit) {
		this(aut, tit, -1);
	}

	public Want(String aut, String tit, int id) {
		author = aut;
		title = tit;
		recordID = -1;
	}

}

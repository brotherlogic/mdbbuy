package uk.co.brotherlogic.mdb;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.co.brotherlogic.mdb.record.GetRecords;
import uk.co.brotherlogic.mdb.record.Record;

public class App {

	private final Double MONEY_PER_WEEK = 20.0;

	List<WantList> lists = new LinkedList<WantList>();

	private void loadLists() {
		for (File f : new File(System.getProperty("user.home") + File.separator
				+ "local" + File.separator + "Dropbox" + File.separator
				+ "wants").listFiles())
			if (f.getName().endsWith(".list")) {
				lists.add(WantList.loadFromFile(f));
			}
	}

	public void run(String command) {
		if (command.equals("pick"))
			if (getMoney() > 0) {
				Want w = pickRecord();
				System.out.println("BUY: " + w);
			}
	}

	private double getMoney() {
		Calendar today = Calendar.getInstance();
		double money = 0.0;
		try {
			for (Integer id : GetRecords.create().getRecordNumbers()) {
				Record r = GetRecords.create().getRecord(id);
				if (r.getDate().get(Calendar.YEAR) == today.get(Calendar.YEAR))
					money += r.getPrice();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		double allocated = today.get(Calendar.DAY_OF_YEAR)
				* (MONEY_PER_WEEK * 52 / 365.0);

		return allocated - money;
	}

	private Want pickRecord() {
		loadLists();
		Collections.sort(lists);
		return lists.get(0).pickWant();
	}

	public static void main(String[] args) {
		App myApp = new App();
		myApp.run("pick");
	}
}

package uk.co.brotherlogic.mdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.co.brotherlogic.mdb.record.GetRecords;
import uk.co.brotherlogic.mdb.record.Record;

public class WantList implements Comparable<WantList>
{
   String name;
   List<Want> wants = new LinkedList<Want>();

   @Override
   public int compareTo(WantList o)
   {
      return -scoreList().compareTo(o.scoreList());
   }

   public Want pickWant()
   {
      Collections.sort(wants);
      for (Want want : wants)
      {
         if (want.getRecordID() < 0)
            return want;
      }
      return null;
   }

   public Double getScore()
   {
      return scoreList();
   }

   private Double scoreList()
   {
      double total = 0.0;
      double count = 0.0;
      for (Want want : wants)
      {
         if (want.getRecordID() >= 0)
         {
            try
            {
               Record r = GetRecords.create().getRecord(want.getRecordID());
               if (r == null)
                  System.err.println(want);
               total += r.getScore(User.getUser("Simon"));
               count += 1;
            }
            catch (SQLException e)
            {
               e.printStackTrace();
            }
         }
      }

      if (count > 0)
         return total / count;
      else
         return 0.0;
   }

   @Override
   public String toString()
   {
      String sString = "";

      for (Want want : wants)
      {
         if (want.getRecordID() >= 0)
         {
            try
            {
               Record r = GetRecords.create().getRecord(want.getRecordID());
               sString += r.getScore(User.getUser("Simon")) + " " + want.author + "-" + want.title
                     + "\n";
            }
            catch (SQLException e)
            {
               e.printStackTrace();
            }
         }
         else
            sString += want.author + "-" + want.title + "\n";
      }

      sString += getScore();

      return sString.trim();
   }

   public static WantList loadFromFile(File f)
   {
      WantList list = new WantList();
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(f));

         list.name = reader.readLine().trim();
         for (String line = reader.readLine(); line != null; line = reader.readLine())
         {
            list.wants.add(new Want(line));
         }
         reader.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      return list;
   }
}

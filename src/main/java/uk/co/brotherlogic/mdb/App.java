package uk.co.brotherlogic.mdb;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class App
{

   private final Double MONEY_PER_WEEK = 20.0;

   List<WantList> lists = new LinkedList<WantList>();

   private void loadLists()
   {
      for (File f : new File(System.getProperty("user.home") + File.separator + "local"
            + File.separator + "Dropbox" + File.separator + "wants").listFiles())
         if (f.getName().endsWith(".list"))
         {
            lists.add(WantList.loadFromFile(f));
         }
   }

   public void run(String command)
   {
      if (command.equals("pick"))
      {

         List<Want> wants = pickRecords(3);
         for (Want w : wants)
            System.out.println("BUY: " + w);
      }
      else if (command.equals("list"))
      {
         loadLists();
         for (WantList list : lists)
            System.out.println(list.getScore() + ":" + list.name);
      }
      else if (command.startsWith("/"))
      {
         File f = new File(command);
         WantList list = WantList.loadFromFile(f);
         System.out.println(list);
      }
   }

   private List<Want> pickRecords(int n)
   {
      loadLists();
      Collections.sort(lists);
      List<Want> wants = new LinkedList<Want>();
      for (int i = 0; i < n - 1; i++)
         wants.add(lists.get(i).pickWant());
      wants.add(lists.get(lists.size() - 1).pickWant());
      return wants;
   }

   public static void main(String[] args)
   {
      App myApp = new App();
      if (args.length > 0)
         myApp.run(args[0]);
   }
}

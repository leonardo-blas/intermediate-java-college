//------------------------------------------------------------------------------
//   @version1.0 07-29-2019
//   @author  FH Computer Science Department
//   File name: MLSListingsApp.java
//   Program purpose: The purpose of this program is to read a file of
//   properties and process them by distinguishing them from SingleFamilyHouse
//   or Condo types. On top of this, we will be implementing a GUI for
//   displaying and searching through the properties acquired through the
//   imported file's properties data. In the GUI we will be able to search by
//   price and by sort by property type, as well as displaying the results.
//   Disclaimer: If this program is working it's written by the author below.
//   Revision history:
//   address      Programmer     Student ID     Description
//   07/29/19     Leonardo Blas  20325202       Initial implementation
//------------------------------------------------------------------------------
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MLSListingsApp
{
   public static void main(String[] args)
   {
      PropertyList propertyList = new PropertyList();
      propertyList.initialize();
      // Code for testing. Left in main for submission for transparency.
      /*System.out.println("*****TESTS:*****");
      System.out.println("\n***propertyList.getAllProperties()***");
      System.out.println(propertyList.getAllProperties());
      System.out.println("\n***propertyList.getSingleFamilyHouse()***");
      System.out.println(propertyList.getSingleFamilyHouse());
      System.out.println("\n***propertyList.getCondo()***");
      System.out.println(propertyList.getCondo());
      System.out.println("\n***propertyList.searchbyPriceRange(0,400000)***");
      System.out.println(propertyList.searchbyPriceRange(0,400000));
      System.out.println("\n***propertyList.searchbyPriceRange(" +
            "800000, 1000000)***");
      System.out.println(propertyList.searchbyPriceRange(
            800000, 1000000));*/
      EventQueue.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            MLSListingsView mlsView = new MLSListingsView();
            mlsView.setProperties(propertyList);
            mlsView.setVisible(true);
         }
      });
   }
}

class MLSListingsView extends JFrame
{
   private static final int viewWidth = 720;
   private static final int viewHeight = 450;
   private static final int listingsRows = 20;
   private static final int listingColumns = 60;
   private String[] messageStrings = {
         "Under 400k",
         "400k - <600k",
         "600k - < 800k",
         "800k - <1M",
         "1M or more"};
   //private propertyList propertyList;
   // UI components
   private JTextArea listings;
   private JComboBox messageList;
   private JButton showAllButton;
   private JButton showSFHButton;
   private JButton showCondoButton;
   private JButton showClearButton;
   private JButton showGoButton;
   private PropertyList propertyList;
   public MLSListingsView()
   {
      // Frame setup.
      setTitle("MLSListings");
      setSize(MLSListingsView.viewWidth, MLSListingsView.viewHeight);
      setLocationRelativeTo(null);
      // 1. Create panels. All of them with default FlowLayout.
      // searchPanel must be adding the components left justified.
      JPanel searchPanel = new JPanel();
      JPanel displayPanel = new JPanel();
      JPanel actionPanel = new JPanel();
      // 2. Create individual UI components for the panels and implement them.
      // 2a. Search Panel.
      searchPanel.setLayout(new FlowLayout(0));
      JLabel label = new JLabel("Search Property:");
      messageList = new JComboBox(messageStrings);
      showGoButton = new JButton("Go");
      searchPanel.add(label);
      searchPanel.add(messageList);
      searchPanel.add(showGoButton);
      // 2b. Display Panel.
      listings = new JTextArea(
            MLSListingsView.listingsRows,
            MLSListingsView.listingColumns);
      // Setting borders for the text area.
      listings.setBorder(BorderFactory.createEtchedBorder());
      listings.setFont((new Font("monospaced", Font.PLAIN, 12)));
      displayPanel.add(listings);
      // 2c. Action Panel.
      showAllButton = new JButton("Show All");
      showSFHButton = new JButton("Show SFH");
      showCondoButton = new JButton("Show Condo");
      showClearButton = new JButton("Clear");
      actionPanel.add(showAllButton);
      actionPanel.add(showSFHButton);
      actionPanel.add(showCondoButton);
      actionPanel.add(showClearButton);
      // 3. Add Panels to the frame.
      add(searchPanel, "North");
      add(displayPanel, BorderLayout.CENTER);
      add(actionPanel, BorderLayout.SOUTH);
      // 4. Provide event handlers for UI components (buttons in the GUI).
      ActionListener bListener = new ButtonListener();
      showAllButton.addActionListener(bListener);
      showClearButton.addActionListener(bListener);
      showSFHButton.addActionListener(bListener);
      showCondoButton.addActionListener(bListener);
      showGoButton.addActionListener(bListener);
      // 5. System.exit when users close the frame.
      addWindowListener(new WindowAdapter()
                        {
                           public void windowClosing(WindowEvent e)
                           {
                              System.exit(0);
                           }
                        }
      );
   }
   public void setProperties(PropertyList propertyList)
   {
      this.propertyList = propertyList;
   }
   class ButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         JButton button = (JButton) e.getSource();
         if (button == showAllButton)
            listings.setText(propertyList.getAllProperties());
         if (button == showClearButton)
            listings.setText("");
         if (button == showSFHButton)
            listings.setText(propertyList.getSingleFamilyHouse());
         if (button == showCondoButton)
            listings.setText(propertyList.getCondo());
         if (button == showGoButton)
         {
            Object selected = messageList.getSelectedItem();
            listings.append("webo\n");
            if (selected == messageStrings[0])
               listings.setText((
                     propertyList.searchbyPriceRange(0, 400000)));
            if (selected == messageStrings[1])
               listings.setText((
                     propertyList.searchbyPriceRange(400000, 600000)));
            if (selected == messageStrings[2])
               listings.setText((
                     propertyList.searchbyPriceRange(600000, 800000)));
            if (selected == messageStrings[3])
               listings.setText((
                     propertyList.searchbyPriceRange(800000, 1000000)));
            if (selected == messageStrings[4])
               listings.setText((
                     propertyList.searchbyPriceRange(
                           1000000, Double.POSITIVE_INFINITY)));
         }
      }
   }
}

class PropertyList
{
   // Members.
   private Property head;
   private static final String DATA_FILE_LOCATION =
         "C:\\Users\\wapen\\Desktop\\Properties Data File.txt";
   private static final int DATA_ROWS = 25;
   private static final String header =
         String.format("%-50s%5s%15s%15s%n",
               "Address", "Price", "Year", "Other Info");
   // Constructors.
   public PropertyList()
   {
      head = null;    // start out with an empty Linked List (no Node)
   }
   // Other functions.
   public void insert(Property property)
   {
      property.setNext(head);  // set next in property object to point to head
      head = property;  // now set head to point to the new property object
   }
   public String getAllProperties()
   {
      Property walker = head; // use a local variable - never touch head
      String returnString = header;
      while (walker != null)
      {
         returnString += walker.toString();
         walker = walker.getNext();
      }
      return returnString;
   }
   public String getSingleFamilyHouse()
   {
      Property walker = head; // use a local variable - never touch head
      String returnString = header;
      while (walker != null)
      {
         if (walker instanceof SingleFamilyHouse)
            returnString += walker.toString();
         walker = walker.getNext();
      }
      return returnString;
   }
   public String getCondo()
   {
      Property walker = head; // use a local variable - never touch head
      String returnString = header;
      while (walker != null)
      {
         if (walker instanceof Condo)
            returnString += walker.toString();
         walker = walker.getNext();
      }
      return returnString;
   }
   public String searchbyPriceRange(double min, double max)
   {
      Property walker = head; // use a local variable - never touch head
      String returnString = header;
      while (walker != null)
      {
         if (min <= walker.getOfferedPrice() &&
               walker.getOfferedPrice() <= max)
            returnString += walker.toString();
         walker = walker.getNext();
      }
      return returnString;  // out of the loop without finding a match.
   }
   public PropertyList initialize()
   {
      ParsePropertiesDataFile.buildPropertyArray(
            this,
            DATA_FILE_LOCATION);
      return this;
   }
}

class Property
{
   // Members.
   private Property next;   // the link to the next Node or next Property
   private String address;
   private double offeredPrice;
   private int yearBuilt;
   // Default member values.
   private static final String DEFAULT_ADDRESS = "No address.";
   private static final double DEFAULT_OFFERED_PRICE = 0;
   private static final int DEFAULT_YEAR_BUILT = 0;
   // Helper functions' values.
   private static final int MIN_ADDRESS_LENGTH = 10;
   private static final int MAX_ADDRESS_LENGTH = 50;
   private static final double MIN_OFFERED_PRICE = 0;
   private static final int MIN_YEAR_BUILT = 0;
   private static final int MAX_YEAR_BUILT = 2019;
   public Property()
   {
      next = null;
      address = DEFAULT_ADDRESS;
      offeredPrice = DEFAULT_OFFERED_PRICE;
      yearBuilt = DEFAULT_YEAR_BUILT;
   }
   // Members' functions.
   public Property(String address, double offeredPrice, int yearBuilt)
   {
      this();
      if (validAddress(address))
         this.address = address;
      if (validOfferedPrice(offeredPrice))
         this.offeredPrice = offeredPrice;
      if (validYearBuilt(yearBuilt))
         this.yearBuilt = yearBuilt;
   }
   public Property getNext()
   {
      return next;
   }
   public void setNext(Property nextProperty)
   {
      next = nextProperty;
   }
   public String getAddress()
   {
      return address;
   }
   private boolean validAddress(String address)
   {
      return MIN_ADDRESS_LENGTH <= address.length()
            && address.length() <= MAX_ADDRESS_LENGTH;
   }
   public void setAddress(String address)
   {
      if (validAddress(address))
         this.address = address;
   }
   public double getOfferedPrice()
   {
      return offeredPrice;
   }
   private boolean validOfferedPrice(double offeredPrice)
   {
      return MIN_OFFERED_PRICE <= offeredPrice;
   }
   public void setOfferedPrice(double offeredPrice)
   {
      if (validOfferedPrice(offeredPrice))
         this.offeredPrice = offeredPrice;
   }
   public int getYearBuilt()
   {
      return yearBuilt;
   }
   private boolean validYearBuilt(int yearBuilt)
   {
      return MIN_YEAR_BUILT <= yearBuilt
            && yearBuilt <= MAX_YEAR_BUILT;
   }
   public void setYearBuilt(int yearBuilt)
   {
      if (validYearBuilt(yearBuilt))
         this.yearBuilt = yearBuilt;
   }
   // Other functions.
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (!(obj instanceof Property))
         return false;
      Property property = (Property) obj;
      return address == property.address
            && yearBuilt == property.yearBuilt;
   }
   @Override
   public String toString()
   {
      String returnString =
            String.format("%-50s$%10s%9d", address, offeredPrice, yearBuilt);
      return returnString;
   }
}

class SingleFamilyHouse extends Property
{
   // Members.
   private int backyardSize;
   // Default member values.
   private static final int DEFAULT_BACKYARD_SIZE = 0;
   // Helper functions' values.
   private static final int MIN_BACKYARD_SIZE = 0;
   // Constructors.
   public SingleFamilyHouse()
   {
      super();
      this.backyardSize = DEFAULT_BACKYARD_SIZE;
   }
   public SingleFamilyHouse(String address,
                            double offeredPrice,
                            int yearBuilt,
                            int backyardSize)
   {
      super(address, offeredPrice, yearBuilt);
      if (validBackyardSize(backyardSize))
         this.backyardSize = backyardSize;
   }
   // Members' functions.
   public int getBackyardSize()
   {
      return backyardSize;
   }
   private boolean validBackyardSize(int backyardSize)
   {
      return MIN_BACKYARD_SIZE <= backyardSize;
   }
   public void setBackyardSize(int backyardSize)
   {
      if (validBackyardSize(backyardSize))
         this.backyardSize = backyardSize;
   }
   // Other functions.
   @Override
   public String toString()
   {
      String returnString = super.toString() +
            String.format("%9s(sqft)%n", backyardSize);
      return returnString;
   }
}

class Condo extends Property
{
   // Members.
   private double HOAfee;
   // Default member values.
   private static final double DEFAULT_HOA_FEE = 100;
   // Helper functions' values.
   private static final double MIN_HOA_FEE = 0;
   // Constructors.
   public Condo()
   {
      super();
      this.HOAfee = DEFAULT_HOA_FEE;
   }
   public Condo(String address,
                double offeredPrice,
                int yearBuilt,
                double HOAfee)
   {
      super(address, offeredPrice, yearBuilt);
      if (validHOAfee(HOAfee))
         this.HOAfee = HOAfee;
   }
   // Members' functions.
   public double getHOAfee()
   {
      return HOAfee;
   }
   private boolean validHOAfee(double HOAfee)
   {
      return MIN_HOA_FEE <= HOAfee;
   }
   public void setHOAfee(double HOAfee)
   {
      if (validHOAfee(HOAfee))
         this.HOAfee = HOAfee;
   }
   // Other functions.
   @Override
   public String toString()
   {
      String returnString = super.toString() +
            String.format("%14s%.2f%n", "HOA fee: ", HOAfee);
      return returnString;
   }
}

class ParsePropertiesDataFile
{
   public static void buildPropertyArray(PropertyList propertyList,
                                         String inputFileLocation)
   {
      Path inputFilePath = Paths.get(inputFileLocation);
      BufferedReader reader = null;
      String line = null;
      int index = 0;
      try
      {
         reader = Files.newBufferedReader(inputFilePath,
               StandardCharsets.US_ASCII);
         while ((line = reader.readLine()) != null)
            addProperty(line, propertyList);
         reader.close();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   public static void addProperty(String line, PropertyList propertyList)
   {
      String propertyType, address, specificData;
      double offeredPrice = 0.0;
      int yearBuilt = 0;
      String singleFamilyHouseType = "SFH";
      String condoType = "CONDO";
      String[] data = line.split(";");
      propertyType = data[0];
      address = data[1];
      offeredPrice = Double.parseDouble(data[2]);
      yearBuilt = Integer.parseInt(data[3]);
      specificData = data[4];
      if (propertyType.toUpperCase().equals(singleFamilyHouseType))
      {
         int backyardSize = Integer.parseInt(specificData);
         propertyList.insert(new SingleFamilyHouse(
               address,
               offeredPrice,
               yearBuilt,
               backyardSize));
      }
      if (propertyType.toUpperCase().equals(condoType))
      {
         double HOAfee = Double.parseDouble(specificData);
         propertyList.insert(new Condo(
               address,
               offeredPrice,
               yearBuilt,
               HOAfee));
      }
   }
}
/*

*****TESTS:*****

***propertyList.getAllProperties()***
Address                                           Price           Year     Other Info
234 Hillview Ave. #376, Milpitas, CA 95023        $  820000.0     2010     HOA fee: 250.00
6711 Michele Way, San Jose, CA 95129              $ 3498000.0     2018     7864(sqft)
4685 Albany Cir #135, San Jose, CA 95129          $  839000.0     1982     HOA fee: 350.00
1018 Harlan Ct, San Jose, CA 95129                $ 2000000.0     1967     9100(sqft)
869 Channing Ave, Palo Alto, CA 94301             $ 2598000.0     1910     5917(sqft)
412 Avenida Palmas, San Jose, CA 95123            $  999900.0     1968     7286(sqft)
6283 Mahan Dr, San Jose, CA 95123                 $  958800.0     1969     1143(sqft)
1256 Copper Peak Ln, San Jose, CA 95120           $ 1150000.0     1986     HOA fee: 270.00
410 Auburn Way #14, San Jose, CA 95129            $  649000.0     1970     HOA fee: 230.00
2150 Monterey Rd #68, San Jose, CA 95112          $  169900.0     1963     1000(sqft)
200 Autumn St, La Honda, CA 94020                 $  692000.0     1930     7400(sqft)
1011 Giacomo Ln #6, San Jose, CA 95131            $ 1004710.0     2018     HOA fee: 250.00
894 Shirley Ave, Sunnyvale, CA 94086              $ 1188000.0     1950     6200(sqft)
8534 Otoole Ct, Gilroy, CA 95020                  $  829500.0     1985     6203(sqft)
8477 Westwood Dr, Gilroy, CA 95020                $  659000.0     1974     6600(sqft)
147 Florence St, Sunnyvale, CA 94086              $ 1198000.0     1942     5989(sqft)
1776 Camino Leonor, San Jose, CA 95131            $  799000.0     2008     HOA fee: 170.00
1022 Banyan Ct, San Jose, CA 95131                $ 1250000.0     2007     3569(sqft)
2654 Ferguson Circle #305, Milpitas, CA 95035     $  732000.0     2010     HOA fee: 250.00
126 Edwards St. #1278, Sunnyvale, CA 94087        $  560000.0     2008     HOA fee: 170.00
3810 Mosher Dr., San Jose, CA 95148               $  950000.0     2000     4000(sqft)
2580 Albert Ave., Sunnyvale, CA 94086             $ 1250000.0     2010     3000(sqft)
787 Adam Street, San Jose, CA 95123               $  750000.0     1980     7000(sqft)
234 Hillview Ave. #245, Milpitas, CA 95023        $  670000.0     2010     HOA fee: 250.00
123 First Street, San Jose, CA 95112              $  450000.0     1956     5000(sqft)


***propertyList.getSingleFamilyHouse()***
Address                                           Price           Year     Other Info
6711 Michele Way, San Jose, CA 95129              $ 3498000.0     2018     7864(sqft)
1018 Harlan Ct, San Jose, CA 95129                $ 2000000.0     1967     9100(sqft)
869 Channing Ave, Palo Alto, CA 94301             $ 2598000.0     1910     5917(sqft)
412 Avenida Palmas, San Jose, CA 95123            $  999900.0     1968     7286(sqft)
6283 Mahan Dr, San Jose, CA 95123                 $  958800.0     1969     1143(sqft)
2150 Monterey Rd #68, San Jose, CA 95112          $  169900.0     1963     1000(sqft)
200 Autumn St, La Honda, CA 94020                 $  692000.0     1930     7400(sqft)
894 Shirley Ave, Sunnyvale, CA 94086              $ 1188000.0     1950     6200(sqft)
8534 Otoole Ct, Gilroy, CA 95020                  $  829500.0     1985     6203(sqft)
8477 Westwood Dr, Gilroy, CA 95020                $  659000.0     1974     6600(sqft)
147 Florence St, Sunnyvale, CA 94086              $ 1198000.0     1942     5989(sqft)
1022 Banyan Ct, San Jose, CA 95131                $ 1250000.0     2007     3569(sqft)
3810 Mosher Dr., San Jose, CA 95148               $  950000.0     2000     4000(sqft)
2580 Albert Ave., Sunnyvale, CA 94086             $ 1250000.0     2010     3000(sqft)
787 Adam Street, San Jose, CA 95123               $  750000.0     1980     7000(sqft)
123 First Street, San Jose, CA 95112              $  450000.0     1956     5000(sqft)


***propertyList.getCondo()***
Address                                           Price           Year     Other Info
234 Hillview Ave. #376, Milpitas, CA 95023        $  820000.0     2010     HOA fee: 250.00
4685 Albany Cir #135, San Jose, CA 95129          $  839000.0     1982     HOA fee: 350.00
1256 Copper Peak Ln, San Jose, CA 95120           $ 1150000.0     1986     HOA fee: 270.00
410 Auburn Way #14, San Jose, CA 95129            $  649000.0     1970     HOA fee: 230.00
1011 Giacomo Ln #6, San Jose, CA 95131            $ 1004710.0     2018     HOA fee: 250.00
1776 Camino Leonor, San Jose, CA 95131            $  799000.0     2008     HOA fee: 170.00
2654 Ferguson Circle #305, Milpitas, CA 95035     $  732000.0     2010     HOA fee: 250.00
126 Edwards St. #1278, Sunnyvale, CA 94087        $  560000.0     2008     HOA fee: 170.00
234 Hillview Ave. #245, Milpitas, CA 95023        $  670000.0     2010     HOA fee: 250.00


***propertyList.searchbyPriceRange(0,400000)***
Address                                           Price           Year     Other Info
2150 Monterey Rd #68, San Jose, CA 95112          $  169900.0     1963     1000(sqft)


***propertyList.searchbyPriceRange(800000, 1000000)***
Address                                           Price           Year     Other Info
234 Hillview Ave. #376, Milpitas, CA 95023        $  820000.0     2010     HOA fee: 250.00
4685 Albany Cir #135, San Jose, CA 95129          $  839000.0     1982     HOA fee: 350.00
412 Avenida Palmas, San Jose, CA 95123            $  999900.0     1968     7286(sqft)
6283 Mahan Dr, San Jose, CA 95123                 $  958800.0     1969     1143(sqft)
8534 Otoole Ct, Gilroy, CA 95020                  $  829500.0     1985     6203(sqft)
3810 Mosher Dr., San Jose, CA 95148               $  950000.0     2000     4000(sqft)

*/
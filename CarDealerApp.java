//------------------------------------------------------------------------------
//   @version1.0 07-04-2019
//   @author  FH Computer Science Department
//   File name:  CarDealerApp.java
//   Program purpose: This program is to process Bank accounts
//   Disclaimer: If this program is working it's written by the author below.
//   Revision history:
//   Date      Programmer     Student ID     Description
//   07/04/19  Leonardo Blas  20325202       Initial implementation
//------------------------------------------------------------------------------

import java.util.Scanner;

public class CarDealerApp
{
   public static void main(String[] args)
   {
      CarDealer carDealer = new CarDealer();
      carDealer.init();
      carDealer.run();
   }
}

class Vehicle
{
   //Arbitrary final values
   private static final int MIN_NAME_LENGTH = 1;
   private static final int MAX_NAME_LENGTH = 30;
   private static final int OLDEST_VEHICLE_YEAR = 1885;
   private static final int NEWEST_VEHICLE_YEAR = 2020;
   private static final double MIN_VEHICLE_PRICE = 0;
   private static final double MAX_VEHICLE_PRICE = 15000000;
   private static final String DEFAULT_MAKE = "Unknown make.";
   private static final String DEFAULT_MODEL = "Unknown model.";
   private String make;
   private String model;
   private int year;
   private double price;
   private Boolean reserved;

   public Vehicle()
   {
      make = DEFAULT_MAKE;
      model = DEFAULT_MODEL;
      year = OLDEST_VEHICLE_YEAR;
      price = MIN_VEHICLE_PRICE;
      reserved = false;
   }

   public Vehicle(String make,
                  String model,
                  int year,
                  double price,
                  Boolean reserved)
   {
      if (!setMake(make))
         this.make = DEFAULT_MAKE;
      if (!setModel(model))
         this.model = DEFAULT_MODEL;
      if (!setYear(year))
         this.year = OLDEST_VEHICLE_YEAR;
      if (!setPrice(price))
         this.price = MIN_VEHICLE_PRICE;
      setReserved(reserved);
   }

   public String getMake()
   {
      return make;
   }

   public String getModel()
   {
      return model;
   }

   public int getYear()
   {
      return year;
   }

   public double getPrice()
   {
      return price;
   }

   public Boolean getReserved()
   {
      return reserved;
   }

   public Boolean setMake(String make)
   {
      int makeLength = make.length();
      if (makeLength >= MIN_NAME_LENGTH && makeLength <= MAX_NAME_LENGTH)
      {
         this.make = make;
         return true;
      }
      return false;
   }

   public Boolean setModel(String model)
   {
      int modelLength = model.length();
      if (modelLength >= MIN_NAME_LENGTH && modelLength <= MAX_NAME_LENGTH)
      {
         this.model = model;
         return true;
      }
      return false;
   }

   public Boolean setYear(int year)
   {
      if (year >= OLDEST_VEHICLE_YEAR && year <= NEWEST_VEHICLE_YEAR)
      {
         this.year = year;
         return true;
      }
      return false;
   }

   public Boolean setPrice(double price)
   {
      if (price >= MIN_VEHICLE_PRICE && price <= MAX_VEHICLE_PRICE)
      {
         this.price = price;
         return true;
      }
      return false;
   }

   public void setReserved(Boolean reserved)
   {
      this.reserved = reserved;
   }

   public String toString()
   {
      String returnString = make + " " + model + ";" + year + ";$" + price;
      return returnString;
   }
}

class CarDealer
{
   //Arbitrary final values
   private static final int MIN_NAME_LENGTH = 1;
   private static final int MAX_NAME_LENGTH = 50;
   private static final String DEFAULT_DEALERSHIP_LOCATION = "Lost City";
   private static final int MIN_LOCAL_INVENTORY = 0;
   private static int maxNumberOfVehicles = 1024;
   private static String dealershipBrand = "Foothill College Car Dealership";
   private Vehicle[] localInventory;
   private int localNumberOfVehicles;
   private String dealershipLocation;

   public CarDealer()
   {
      localInventory = new Vehicle[maxNumberOfVehicles];
      localNumberOfVehicles = MIN_LOCAL_INVENTORY;
      dealershipLocation = DEFAULT_DEALERSHIP_LOCATION;
   }

   public CarDealer(String dealershipLocation)
   {
      if (!setDealershipLocation(dealershipLocation))
         this.dealershipLocation = DEFAULT_DEALERSHIP_LOCATION;
   }

   public static int getMaxNumberOfVehicles()
   {
      return maxNumberOfVehicles;
   }

   public static String getDealershipBrand()
   {
      return dealershipBrand;
   }

   public int getlocalNumberOfVehicles()
   {
      return localNumberOfVehicles;
   }

   public String getDealershipLocation()
   {
      return dealershipLocation;
   }

   public Boolean setlocalNumberOfVehicles(int localNumberOfVehicles)
   {
      if (localNumberOfVehicles >= MIN_LOCAL_INVENTORY
            && localNumberOfVehicles <= maxNumberOfVehicles)
      {
         this.localNumberOfVehicles = localNumberOfVehicles;
         return true;
      }
      return false;
   }

   public Boolean setDealershipLocation(String dealershipLocation)
   {
      int locationLength = dealershipLocation.length();
      if (locationLength >= MIN_NAME_LENGTH && locationLength <= MAX_NAME_LENGTH)
      {
         this.dealershipLocation = dealershipLocation;
         return true;
      }
      return false;
   }

   public void init()
   {
      System.out.println("Welcome to " + dealershipBrand + " at " +
            dealershipLocation + "... Loading vehicles from DM ... " +
            "Please wait ...\n");
      while (true)
      {
         System.out.print("Enter Make Model;Year;Price " +
               "[Ford Taurus;2014;14578.99] or [END;] to quit:");
         Scanner scanner = new Scanner(System.in);
         String inputString = scanner.nextLine();
         if (inputString.equals("END;"))
            break;
         String[] inputStringParts = inputString.split(";");
         String inputStringMake = inputStringParts[0];
         String inputStringModel = inputStringParts[1];
         String inputStringYear = inputStringParts[2];
         String inputStringPrice = inputStringParts[3];
         Vehicle vehicle = new Vehicle(
               inputStringMake,
               inputStringModel,
               Integer.parseInt(inputStringYear),
               Double.parseDouble(inputStringPrice),
               false
         );
         this.localInventory[localNumberOfVehicles] = vehicle;
         this.localNumberOfVehicles++;
      }
   }

   public void menu()
   {
      System.out.println("\n             SMART SEARCH");
      System.out.println("1.View vehicle inventory sorted by year");
      System.out.println("2.View reserved vehicle inventory sorted by year");
      System.out.println("3.Reserve a vehicle");
      System.out.println("4.Search by make and model");
      System.out.println("5.Quit\n");
      System.out.print("             Enter your choice: ");
   }

   public void run()
   {
      while (true)
      {
         menu();
         Scanner scanner = new Scanner(System.in);
         int inputInt = scanner.nextInt();
         switch (inputInt)
         {
            case 1: //view vehicle inventory
               viewInventory();
               continue;
            case 2: //view reserved vehicle inventory
               showReservedVehicles();
               continue;
            case 3: //reserve a vehicle
               reserveVehicle();
               continue;
            case 4: //search by make and model
               searchMakeModel();
               continue;
            case 5: //quit
               quit();
         }
      }
   }

   public void viewInventory()
   {
      System.out.println("         -------------------------------------");
      System.out.println("         |           VEHICLE INVENTORY       |");
      System.out.println("         -------------------------------------");
      System.out.println("MAKE & MODEL                  YEAR      PRICE");
      System.out.println("---------------------------------------------" +
            "-------\n");
      bubbleSortYear(localInventory);
      for (Vehicle vehicle : localInventory)
         if (vehicle != null && !vehicle.getReserved())
            System.out.println(vehicle.toString());
   }

   public void searchMakeModel()
   {
      Boolean found = false;
      Scanner scanner1 = new Scanner(System.in);
      System.out.print("             Enter vehicle make: ");
      String inputMake = scanner1.nextLine();
      Scanner scanner2 = new Scanner(System.in);
      System.out.print("             Enter vehicle model: ");
      String inputModel = scanner2.nextLine();
      System.out.print("\n");
      for (Vehicle vehicle : localInventory)
         if (vehicle != null && vehicle.getMake().equals(inputMake)
               && vehicle.getModel().equals(inputModel)
               && !vehicle.getReserved())
         {
            System.out.println(vehicle.toString());
            found = true;
         }
      if (found == false)
         System.out.println("             No match found");
   }

   public void quit()
   {
      System.out.println("Thanks for using " + dealershipBrand + " at " +
            dealershipLocation);
      System.exit(0);
   }

   public void showReservedVehicles()
   {
      System.out.println("         -------------------------------------");
      System.out.println("         |          RESERVED INVENTORY       |");
      System.out.println("         -------------------------------------");
      System.out.println("MAKE & MODEL                  YEAR      PRICE");
      System.out.println("---------------------------------------------" +
            "-------\n");
      bubbleSortYear(localInventory);
      for (Vehicle vehicle : localInventory)
         if (vehicle != null && vehicle.getReserved())
            System.out.println(vehicle.toString());
   }

   public void bubbleSortYear(Vehicle localInventory[])
   {
      int localInventoryLength = localInventory.length;
      for (int i = 0; i < localInventoryLength - 1; i++)
         for (int j = 0; j < localInventoryLength - i - 1; j++)
            if (localInventory[j] != null && localInventory[j + 1] != null &&
                  localInventory[j].getYear() > localInventory[j + 1].getYear())
            {
               Vehicle temp = localInventory[j];
               localInventory[j] = localInventory[j + 1];
               localInventory[j + 1] = temp;
            }
   }

   public void reserveVehicle()
   {
      Boolean found = false;
      Scanner scanner1 = new Scanner(System.in);
      System.out.print("             Enter vehicle make: ");
      String inputMake = scanner1.nextLine();
      Scanner scanner2 = new Scanner(System.in);
      System.out.print("             Enter vehicle model: ");
      String inputModel = scanner2.nextLine();
      Scanner scanner3 = new Scanner(System.in);
      System.out.print("             Enter vehicle year: ");
      int inputYear = scanner3.nextInt();
      Scanner scanner4 = new Scanner(System.in);
      System.out.print("             Enter vehicle price: ");
      double inputPrice = scanner4.nextDouble();
      System.out.print("\n");
      for (Vehicle vehicle : localInventory)
         if (vehicle != null
               && !vehicle.getReserved()
               && vehicle.getMake().equals(inputMake)
               && vehicle.getModel().equals(inputModel)
               && vehicle.getYear() == inputYear
               && vehicle.getPrice() == inputPrice
               )
         {
            vehicle.setReserved(true);
            System.out.println(vehicle.toString());
            found = true;
         }
      if (found == false)
         System.out.println("             No match found");
   }
}

/*
Welcome to Foothill College Car Dealership at Lost City... Loading vehicles from DM ... Please wait ...

Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Toyota;Camry;2015;22158.95
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Ford;Taurus;2012;9566.99
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Ford;Taurus;2014;17899.00
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Ferrari;FF Coupe;2015;302450.99
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:BMW;325i;2016;37899.88
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Rolls-Royce;Phantom Coupe;2015;471175
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Porsche;911 Convertible;2016;103925.00
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:Cadillac;ATS Sedan;2014;42999.08
Enter Make Model;Year;Price [Ford Taurus;2014;14578.99] or [END;] to quit:END;

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 1
         -------------------------------------
         |           VEHICLE INVENTORY       |
         -------------------------------------
MAKE & MODEL                  YEAR      PRICE
----------------------------------------------------

Ford Taurus;2012;$9566.99
Ford Taurus;2014;$17899.0
Cadillac ATS Sedan;2014;$42999.08
Toyota Camry;2015;$22158.95
Ferrari FF Coupe;2015;$302450.99
Rolls-Royce Phantom Coupe;2015;$471175.0
BMW 325i;2016;$37899.88
Porsche 911 Convertible;2016;$103925.0

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 2
         -------------------------------------
         |          RESERVED INVENTORY       |
         -------------------------------------
MAKE & MODEL                  YEAR      PRICE
----------------------------------------------------


             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 3
             Enter vehicle make: Ford
             Enter vehicle model: Taurus
             Enter vehicle year: 2012
             Enter vehicle price: 9566.99

Ford Taurus;2012;$9566.99

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 3
             Enter vehicle make: Toyota
             Enter vehicle model: Camry
             Enter vehicle year: 2015
             Enter vehicle price: 22158.95

Toyota Camry;2015;$22158.95

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 2
         -------------------------------------
         |          RESERVED INVENTORY       |
         -------------------------------------
MAKE & MODEL                  YEAR      PRICE
----------------------------------------------------

Ford Taurus;2012;$9566.99
Toyota Camry;2015;$22158.95

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 1
         -------------------------------------
         |           VEHICLE INVENTORY       |
         -------------------------------------
MAKE & MODEL                  YEAR      PRICE
----------------------------------------------------

Ford Taurus;2014;$17899.0
Cadillac ATS Sedan;2014;$42999.08
Ferrari FF Coupe;2015;$302450.99
Rolls-Royce Phantom Coupe;2015;$471175.0
BMW 325i;2016;$37899.88
Porsche 911 Convertible;2016;$103925.0

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 4
             Enter vehicle make: Tesla
             Enter vehicle model: Random Model

             No match found

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 4
             Enter vehicle make: Ford
             Enter vehicle model: Taurus

Ford Taurus;2014;$17899.0

             SMART SEARCH
1.View vehicle inventory sorted by year
2.View reserved vehicle inventory sorted by year
3.Reserve a vehicle
4.Search by make and model
5.Quit

             Enter your choice: 5
Thanks for using Foothill College Car Dealership at Lost City

Process finished with exit code 0

 */
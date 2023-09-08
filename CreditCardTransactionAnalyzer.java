//------------------------------------------------------------------------------
//   @version1.0 07-18-2019
//   @author  FH Computer Science Department
//   File name: CreditCardTransactionAnalyzer.java
//   Program purpose: The purpose of this program is to report a customer's
//   credit card's monthly purchase transactions from a text file and store them
//   in an array of transactions. In this program, transactions can be Banking,
//   Department Store, or Grocery transactions. Each transaction could qualify
//   for reward points. The program will also generate an award of Rewardable
//   objects and report all transactions and reward points for a customer.
//   Disclaimer: If this program is working it's written by the author below.
//   Revision history:
//   Date      Programmer     Student ID     Description
//   07/18/19  Leonardo Blas  20325202       Initial implementation
//------------------------------------------------------------------------------

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.Arrays;

public class CreditCardTransactionAnalyzer
{
   public static void main(String[] args)
   {
      Customer customer = new Customer("Leonardo Blas", "1234 5432 4333 5888");
      customer.readTransactions("C:\\Users\\wapen\\Desktop\\16Transactions.txt");
      customer.reportTransactions();
      customer.reportCharges();
      customer.reportRewardSummary();
   }
}

class Customer
{
   private static final int ARRAY_SIZE = 16;
   private String customerName, creditCardNumber;
   private double totalBalance;
   private int rewardPoints;
   private Transaction[] transactions;
   private Rewardable[] rewardables;

   public Customer()
   {
      transactions = new Transaction[ARRAY_SIZE];
      rewardables = new Rewardable[ARRAY_SIZE];
      totalBalance = 0.;
      rewardPoints = 1000;
   }

   public Customer(String customerName, String creditCardNumber)
   {
      this();
      this.customerName = customerName;
      this.creditCardNumber = creditCardNumber;
   }

   public Customer(String customerName, String creditCardNumber,
                   double totalBalance, int rewardPoints, Transaction[] transactions,
                   Rewardable[] rewardables)
   {
      this.customerName = customerName;
      this.creditCardNumber = creditCardNumber;
      this.totalBalance = totalBalance;
      this.rewardPoints = rewardPoints;
      this.transactions = transactions;
      this.rewardables = rewardables;
   }

   public void readTransactions(final String file)
   {
      final int productSize = 16;
      Product[] productList = new Product[productSize];
      ParseTextFile.buildProductList(productList, file);
      int j = 0;
      for (int i = 0; i < productList.length; i++)
      {
         Product product = productList[i];
         if (product.getTransactionType().equals("DS"))
         {
            DepartmentStoreTransaction t = new DepartmentStoreTransaction(
                  product.getId(),
                  product.getDate(),
                  product.getAmount(),
                  product.getSpecificData1(),
                  Integer.parseInt(product.getSpecificData2()));
            transactions[i] = t;
            rewardables[j] = t;
            j++;
         } else if (product.getTransactionType().equals("BK"))
            transactions[i] = new BankingTransaction(
                  product.getId(),
                  product.getDate(),
                  product.getAmount(),
                  product.getSpecificData1(),
                  Double.parseDouble(product.getSpecificData2()));
         else if (product.getTransactionType().equals("GR"))
         {
            GroceryTransaction t = new GroceryTransaction(
                  product.getId(),
                  product.getDate(),
                  product.getAmount(),
                  product.getSpecificData1());
            transactions[i] = t;
            rewardables[j] = t;
            j++;
         }
      }
      Arrays.sort(transactions);
   }

   public void reportTransactions()
   {
      System.out.println("TRANSACTION LISTING \t\t\tREPORT");
      for (Transaction transaction : transactions)
         transaction.list();
   }

   public void reportCharges()
   {
      double sum = 0;
      for (Transaction transaction : transactions)
         if (transaction instanceof BankingTransaction)
            sum += transaction.getAmount();
      System.out.println("Total charges: $" + sum);
   }

   public void reportRewardSummary()
   {
      int deptPoints = 0;
      int groceryPoints = 0;
      for (Rewardable reward : rewardables)
      {
         if (reward == null)
            break;
         deptPoints += (reward instanceof DepartmentStoreTransaction) ?
               reward.earnPoints() : 0;
         groceryPoints += (reward instanceof GroceryTransaction) ?
               reward.earnPoints() : 0;
      }
      System.out.println("Rewards Summary for " + customerName + " " +
            creditCardNumber);
      System.out.println("Previous points balance\t\t" + this.rewardPoints);
      System.out.println("+ Points earned on Department store purchases\t\t" +
            deptPoints);
      System.out.println("+ Points earned on Grocery store purchases\t\t" +
            groceryPoints);
      System.out.println("---------------------------------------------------");
      System.out.println("Total points: " +
            (rewardPoints + deptPoints + groceryPoints));
   }
}

abstract class Transaction implements Comparable<Transaction>
{
   private int transactionID;
   protected String date;
   protected double amount;

   public Transaction(int transactionID, String date, double amount)
   {
      this.transactionID = transactionID;
      this.date = date;
      this.amount = amount;
   }

   public Transaction()
   {
      transactionID = 0;
      date = "<no-date>";
      amount = 0;
   }

   @Override
   public int compareTo(Transaction o)
   {
      if (amount == o.amount)
         return 0;
      return (amount > o.amount) ? 1 : -1;
   }

   abstract void list();

   @Override
   public String toString()
   {
      return "Transaction ID: " + transactionID +
            "\nTransaction Date: " + date +
            "\nTransaction Amount: " + amount;
   }

   @Override
   public boolean equals(Object obj)
   {
      Transaction toCompare = (Transaction) obj;
      return (transactionID == toCompare.getTransactionID())
            && date.equals(toCompare.getDate());
   }

   public int getTransactionID()
   {
      return transactionID;
   }

   public void setTransactionID(int transactionID)
   {
      this.transactionID = transactionID;
   }

   public String getDate()
   {
      return date;
   }

   public void setDate(String date)
   {
      this.date = date;
   }

   public double getAmount()
   {
      return amount;
   }

   public void setAmount(double amount)
   {
      this.amount = amount;
   }
}

class DepartmentStoreTransaction extends Transaction implements Rewardable
{
   private String deptName;
   private int returnPolicy;

   public DepartmentStoreTransaction(int transactionID,
                                     String date,
                                     double transactionAmount,
                                     String deptName,
                                     int returnPolicy)
   {
      super(transactionID, date, transactionAmount);
      this.deptName = deptName;
      this.returnPolicy = returnPolicy;
   }

   @Override
   void list()
   {
      System.out.print(date +
            "\tDepartment Store\t" +
            deptName +
            "\t\t" + "$" + amount);
      System.out.println(" (return in " + returnPolicy + " days)");
   }

   @Override
   public int earnPoints()
   {
      return (int) (3 * amount);
   }
}

class BankingTransaction extends Transaction
{
   private String type;
   private double charge;

   public BankingTransaction(int transactionID,
                             String date,
                             double transactionAmount,
                             String type,
                             double charge)
   {
      super(transactionID, date, transactionAmount);
      this.type = type;
      this.charge = charge;

   }

   @Override
   void list()
   {
      System.out.println(date +
            "\tBanking\t" +
            "\t\t\t" + type +
            "  \t\t" + "$" +
            amount);
   }
}

class GroceryTransaction extends Transaction implements Rewardable
{
   private String storeName;

   public GroceryTransaction(int transactionID,
                             String date,
                             double transactionAmount,
                             String storeName)
   {
      super(transactionID, date, transactionAmount);
      this.storeName = storeName;
   }

   @Override
   void list()
   {
      System.out.println(date +
            "\tGrocery\t" +
            "\t\t\t" + storeName +
            "\t\t" + "$" + amount);
   }

   @Override
   public int earnPoints()
   {
      return (int) (5 * amount);
   }
}

class ParseTextFile
{
   public static void buildProductList(Product[] productList,
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
         {
            addProduct(line, productList, index++);
         }
         reader.close();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public static void addProduct(String line, Product[]
         productList, int index)
   {
      String transactionType, date, specificData1, specificData2;
      int id = 0;
      double amount = 0.0;
      String[] data = line.split("~");
      transactionType = data[0];
      date = data[1];
      id = Integer.parseInt(data[2]);
      amount = Double.parseDouble(data[3]);
      specificData1 = data[4];
      specificData2 = "";
      if (data.length == 6)
         specificData2 = data[5];
      productList[index] = new Product(
            transactionType,
            date,
            id,
            amount,
            specificData1,
            specificData2)
      ;
   }
}

class Product
{
   private String transactionType, date, specificData1, specificData2;
   private int id;
   private double amount;

   public String getTransactionType()
   {
      return transactionType;
   }

   public String getDate()
   {
      return date;
   }

   public String getSpecificData1()
   {
      return specificData1;
   }

   public String getSpecificData2()
   {
      return specificData2;
   }

   public int getId()
   {
      return id;
   }

   public double getAmount()
   {
      return amount;
   }

   public Product(String transactionType,
                  String date,
                  int id,
                  double amount,
                  String specificData1,
                  String specificData2)
   {
      this.transactionType = transactionType;
      this.date = date;
      this.id = id;
      this.amount = amount;
      this.specificData1 = specificData1;
      this.specificData2 = specificData2;
   }

   public String toString()
   {
      String returnString = transactionType + "~" +
            date + "~" +
            id + "~" +
            amount + "~" +
            specificData1;
      if (specificData2 != "")
         returnString += "~" + specificData2;
      return returnString;
   }

   public boolean equals(Object object)
   {
      if (this == object)
         return true;
      if (!(object instanceof Product))
         return false;
      Product product = (Product) object;
      return transactionType == product.transactionType;
   }
}

interface Rewardable
{
   int earnPoints();
}

/*

SAMPLE RUN: (the actual console output is better formatted)

TRANSACTION LISTING 			REPORT
02/12/19	Banking				ATM  		$16.0
04/23/18	Grocery				Safeway		$17.39
04/23/18	Department Store	Macys		$25.67 (return in 60 days)
05/15/19	Grocery				Walmart		$26.83
07/17/20	Department Store	Ross		$35.99 (return in 60 days)
08/18/20	Banking				ATM  		$55.0
04/20/18	Grocery				Lucky		$57.95
09/19/20	Grocery				Safeway		$57.95
04/14/19	Banking				CASH  		$78.0
05/01/18	Banking				CASH  		$100.0
10/20/20	Banking				CASH  		$100.0
06/16/19	Department Store	Kohl's		$199.99 (return in 90 days)
04/01/18	Department Store	Sears		$211.67 (return in 90 days)
03/13/19	Grocery				Safeway		$289.23
03/12/18	Banking				ATM  		$300.0
01/11/19	Department Store	Target		$999.99 (return in 30 days)
Total charges: $649.0
Rewards Summary for Leonardo Blas 1234 5432 4333 5888
Previous points balance		1000
+ Points earned on Department store purchases		4417
+ Points earned on Grocery store purchases		2244
---------------------------------------------------
Total points: 7661

Process finished with exit code 0

 */
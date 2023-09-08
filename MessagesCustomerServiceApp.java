//------------------------------------------------------------------------------
//   @version1.0 08-09-2019
//   @author  FH Computer Science Department
//   File name: CreditCardTransactionAnalyzer.java
//   Program purpose: The purpose of this program is to implement an application
//   to be used by Customer Service representatives at a Wireless Phone Carrier
//   to keep track of customers’ SmartPhone message usages/charges and to
//   provide account disconnect service. The concepts we will be implementing
//   are multiple inheritance, TreeMaps, and WildCards.
//   Disclaimer: If this program is working it's written by the author below.
//   Revision history:
//   Date      Programmer     Student ID     Description
//   08/09/19  Leonardo Blas  20325202       Initial implementation
//------------------------------------------------------------------------------
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class MessagesCustomerServiceApp
{
   public static void main(String[] args)
   {
      String dataFileLocation = "C:\\Users\\wapen\\Desktop\\ItemsData.txt";
      SmartCarrier smartCarrier = new SmartCarrier();
      smartCarrier.init(dataFileLocation);
      smartCarrier.run();
   }
}

class SmartCarrier
{
   // Members.
   private TreeMap<String, ArrayList<Item>> customerMap;
   private String location;
   // Default member values.
   private static final String DEFAULT_LOCATION = "Santa Clara";
   // Helper functions' values.
   // Constructors.
   public SmartCarrier()
   {
      customerMap = new TreeMap<String, ArrayList<Item>>();
      location = DEFAULT_LOCATION;
   }
   public SmartCarrier(TreeMap<String, ArrayList<Item>> customerMap,
                       String location)
   {
      this.customerMap = customerMap;
      this.location = location;
   }
   // Members' functions.
   // Other functions.
   public void init(String inputFileLocation)
   {
      Path inputFilePath = Paths.get(inputFileLocation);
      BufferedReader reader = null;
      String line = null;
      try
      {
         reader = Files.newBufferedReader(inputFilePath,
               StandardCharsets.US_ASCII);
         while ((line = reader.readLine()) != null)
            addItem(line);
         reader.close();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   private void addItem(String line)
   {
      Message<?> message = null;
      String[] data = line.split(",");
      // Item's members.
      int time = Integer.parseInt(data[1]);
      String from = data[2];
      String to = data[3];
      double charge = Double.parseDouble(data[data.length - 1]);
      switch (data[0])
      {
         case "T":
            String content = data[4];
            message = new Message<Text>(new Text(content),
                  time, from, to, charge);
            break;
         case "M":
            double size = Double.parseDouble(data[4]);
            String mediaFormat = data[5];
            message = new Message<Media>(new Media(size, mediaFormat),
                  time, from, to, charge);
            break;
         case "V":
            int duration = Integer.parseInt(data[4]);
            String voiceFormat = data[5];
            message = new Message<Voice>(new Voice(duration, voiceFormat),
                  time, from, to, charge);
            break;
      }
      checkAndPut(message);
   }
   private void checkAndPut(Message<?> message)
   {
      String from = message.getFrom();
      if (customerMap.containsKey(from))
      {
         ArrayList<Item> messageArrayList = customerMap.get(from);
         messageArrayList.add(message);
      } else
      {
         ArrayList<Item> messageArrayList = new ArrayList<>();
         messageArrayList.add(message);
         customerMap.put(from, messageArrayList);
      }
   }
   public void run()
   {
      Scanner scanner = new Scanner(System.in);
      int next = 0;
      do
      {
         switch (next)
         {
            case 1:
               listAllAccounts();
               break;
            case 2:
               deleteFirstMediaMessage();
               break;
            case 3:
               disconnectAccount(scanner);
               break;
            default:
         }
         System.out.println("   FOOTHILL WIRELESS at " + location +
               "\nMESSAGE UTILIZATION AND ACCOUNT ADMIN" +
               "\n1. List all accounts." +
               "\n2. Erase a media message." +
               "\n3. Disconnect account." +
               "\n4. Quit");
      } while ((next = scanner.nextInt()) != 4);
   }
   private void listAllAccounts()
   {
      Set<Map.Entry<String, ArrayList<Item>>> set = customerMap.entrySet();
      Iterator<Map.Entry<String, ArrayList<Item>>> setIterator =
            set.iterator();
      while (setIterator.hasNext())
      {
         Map.Entry<String, ArrayList<Item>> entry = setIterator.next();
         Iterator valueIterator = entry.getValue().iterator();
         double totalSpent = 0.0;
         System.out.println("Account: " + entry.getKey());
         while (valueIterator.hasNext())
         {
            Message message = Message.class.cast(valueIterator.next());
            totalSpent += message.getCharge();
            System.out.println(message.toString());
         }
         System.out.println("Total charges: " + totalSpent);
         System.out.println("-----------------------------------------------");
      }
   }
   private void deleteFirstMediaMessage()
   {
      Set<Map.Entry<String, ArrayList<Item>>> set = customerMap.entrySet();
      Iterator<Map.Entry<String, ArrayList<Item>>> setIterator =
            set.iterator();
      while (setIterator.hasNext())
      {
         Map.Entry<String, ArrayList<Item>> entry = setIterator.next();
         ArrayList<Item> list = entry.getValue();
         eraseHelper(list);
      }
   }
   private void eraseHelper(List<? extends Item> list)
   {
      for (Item item : list)
         if (item instanceof Message<?>)
         {
            Message<?> message = Message.class.cast(item);
            if (message.getMessage() instanceof Media)
            {
               list.remove(item);
               break;
            }
         }
   }
   private void disconnectAccount(Scanner scanner)
   {
      System.out.println("Enter user's phone number to disconnect:");
      String phoneNumber = scanner.next();
      try
      {
         if (customerMap.containsKey(phoneNumber))
         {
            double totalSpent = 0.0;
            ArrayList<Item> list = customerMap.get(phoneNumber);
            for (Item item : list)
            {
               totalSpent += item.getCharge();
            }
            System.out.println("Total charges: " + totalSpent);
            customerMap.remove((phoneNumber));
         }
      } catch (Exception e)
      {
         System.out.println("Account 123-456-789 does not exist!");
      }
   }
}

class Item
{
   // Members.
   private int time;
   private String from;
   private String to;
   private double charge;
   // Default member values.
   private static final int DEFAULT_TIME = 0;
   private static final String DEFAULT_FROM = "Unknown Sender";
   private static final String DEFAULT_TO = "Unknown Recipient";
   private static final double DEFAULT_CHARGE = 0.0;
   // Helper functions' values.
   // Constructors.
   public Item()
   {
      this.time = DEFAULT_TIME;
      this.from = DEFAULT_FROM;
      this.to = DEFAULT_TO;
      this.charge = DEFAULT_CHARGE;
   }
   public Item(int time, String from, String to, double charge)
   {
      this.time = time;
      this.from = from;
      this.to = to;
      this.charge = charge;
   }
   // Members' functions.
   public int getTime()
   {
      return time;
   }
   public void setTime(int time)
   {
      this.time = time;
   }
   public String getFrom()
   {
      return from;
   }
   public void setFrom(String from)
   {
      this.from = from;
   }
   public String getTo()
   {
      return to;
   }
   public void setTo(String to)
   {
      this.to = to;
   }
   public double getCharge()
   {
      return charge;
   }
   public void setCharge(double charge)
   {
      this.charge = charge;
   }
   // Other functions.
   @Override
   public String toString()
   {
      return "Time: " + time
            + ", From: " + from
            + ", To: " + to;
   }
}

class Message<T> extends Item
{
   // Members.
   private T message;
   // Default member values.
   // Helper functions' values.
   // Constructors.
   public Message()
   {
      message = null;
   }
   public Message(T message, int time, String from, String to, double charge)
   {
      super(time, from, to, charge);
      this.message = message;
   }
   // Members' functions.
   public T getMessage()
   {
      return message;
   }
   public void setMessage(T message)
   {
      this.message = message;
   }
   // Other functions.
   @Override
   public String toString() // NEEDS MORE WORK, CHECK LATER
   {
      return String.format(
            "%-50s", message.toString()) +
            String.format("%-85s", super.toString());
   }
}

class Text
{
   private String content;
   public Text() { content = ""; }
   public Text(String text) { content = text; }
   public String getContent()
   {
      return content;
   }
   public void setContent(String content)
   {
      this.content = content;
   }
   public String toString()
   {
      return "\tTEXT: " + content;
   }
}

class Media
{
   private double size;
   private String format;
   public Media()
   {
      size = 0;
      format = "";
   }
   public Media(double size, String format)
   {
      this.size = size;
      this.format = format;
   }
   public double getSize()
   {
      return size;
   }
   public void setSize(double size)
   {
      this.size = size;
   }
   public String getFormat()
   {
      return format;
   }
   public void setFormat(String format)
   {
      this.format = format;
   }
   public String toString()
   {
      return new String("\tMEDIA: Size:" + size + " MB, Format: " + format);
   }
}

class Voice
{
   private int duration;
   private String format;
   public Voice()
   {
      duration = 0;
      format = "";
   }
   public Voice(int duration, String format)
   {
      this.duration = duration;
      this.format = format;
   }
   public int getDuration()
   {
      return duration;
   }
   public void setDuration(int duration)
   {
      this.duration = duration;
   }
   public String getFormat()
   {
      return format;
   }
   public void setFormat(String format)
   {
      this.format = format;
   }
   public String toString()
   {
      return new String("\tVOICE: Duration:" + duration + " (sec), Format: " + format);
   }
}

/*
SAMPLE RUN:

   FOOTHILL WIRELESS at Santa Clara
MESSAGE UTILIZATION AND ACCOUNT ADMIN
1. List all accounts.
2. Erase a media message.
3. Disconnect account.
4. Quit
1
Account: 1-408-111-0222
	TEXT: Are you going to the movie tonight?        Time: 144840960, From: 1-408-111-0222, To: 1-650-111-0000
	VOICE: Duration:120 (sec), Format: MPE           Time: 144840960, From: 1-408-111-0222, To: 1-650-222-5555
	TEXT: Mom said you go home by 11pm               Time: 144840960, From: 1-408-111-0222, To: 1-650-666-9999
	VOICE: Duration:231 (sec), Format: MP4           Time: 144840960, From: 1-408-111-0222, To: 1-650-852-4774
	MEDIA: Size:2.75 MB, Format: GIF                 Time: 144840960, From: 1-408-111-0222, To: 1-650-217-2003
	MEDIA: Size:1.75 MB, Format: GIF                 Time: 144840960, From: 1-408-111-0222, To: 1-650-333-8888
Total charges: 9.2
-----------------------------------------------
Account: 1-408-222-0222
	TEXT: Happy birhday!!!                           Time: 144840960, From: 1-408-222-0222, To: 1-650-333-6666
	VOICE: Duration:670 (sec), Format: MOV           Time: 144840960, From: 1-408-222-0222, To: 1-650-812-0011
Total charges: 7.0
-----------------------------------------------
Account: 1-408-333-0222
	MEDIA: Size:2.5 MB, Format: JPEG                 Time: 144840960, From: 1-408-333-0222, To: 1-650-123-2000
	TEXT: Java is fun to learn right                 Time: 144840960, From: 1-408-333-0222, To: 1-650-213-4444
	MEDIA: Size:5.5 MB, Format: GIF                  Time: 144840960, From: 1-408-333-0222, To: 1-650-444-6666
Total charges: 5.75
-----------------------------------------------
Account: 1-408-444-0222
	MEDIA: Size:3.8 MB, Format: JPG                  Time: 144840960, From: 1-408-444-0222, To: 1-650-567-2003
Total charges: 0.9
-----------------------------------------------
Account: 1-408-555-0222
	TEXT: Can you close the backdoor?                Time: 144840960, From: 1-408-555-0222, To: 1-650-321-3131
	TEXT: I got to go now Sorry                      Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	MEDIA: Size:3.05 MB, Format: PNG                 Time: 144840960, From: 1-408-555-0222, To: 1-650-287-2203
	TEXT: Will come to your house this afternoon     Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	TEXT: Yeah.                                      Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	VOICE: Duration:172 (sec), Format: MOV           Time: 144840960, From: 1-408-555-0222, To: 1-650-000-2828
Total charges: 7.55
-----------------------------------------------
Account: 1-408-666-0333
	TEXT: I'm tired today.                           Time: 144840960, From: 1-408-666-0333, To: 1-650-555-4444
	MEDIA: Size:3.5 MB, Format: GIF                  Time: 144840960, From: 1-408-666-0333, To: 1-650-123-2000
Total charges: 5.7
-----------------------------------------------
   FOOTHILL WIRELESS at Santa Clara
MESSAGE UTILIZATION AND ACCOUNT ADMIN
1. List all accounts.
2. Erase a media message.
3. Disconnect account.
4. Quit
3
Enter user's phone number to disconnect:
1-408-111-0222
Total charges: 9.2
   FOOTHILL WIRELESS at Santa Clara
MESSAGE UTILIZATION AND ACCOUNT ADMIN
1. List all accounts.
2. Erase a media message.
3. Disconnect account.
4. Quit
1
Account: 1-408-222-0222
	TEXT: Happy birhday!!!                           Time: 144840960, From: 1-408-222-0222, To: 1-650-333-6666
	VOICE: Duration:670 (sec), Format: MOV           Time: 144840960, From: 1-408-222-0222, To: 1-650-812-0011
Total charges: 7.0
-----------------------------------------------
Account: 1-408-333-0222
	MEDIA: Size:2.5 MB, Format: JPEG                 Time: 144840960, From: 1-408-333-0222, To: 1-650-123-2000
	TEXT: Java is fun to learn right                 Time: 144840960, From: 1-408-333-0222, To: 1-650-213-4444
	MEDIA: Size:5.5 MB, Format: GIF                  Time: 144840960, From: 1-408-333-0222, To: 1-650-444-6666
Total charges: 5.75
-----------------------------------------------
Account: 1-408-444-0222
	MEDIA: Size:3.8 MB, Format: JPG                  Time: 144840960, From: 1-408-444-0222, To: 1-650-567-2003
Total charges: 0.9
-----------------------------------------------
Account: 1-408-555-0222
	TEXT: Can you close the backdoor?                Time: 144840960, From: 1-408-555-0222, To: 1-650-321-3131
	TEXT: I got to go now Sorry                      Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	MEDIA: Size:3.05 MB, Format: PNG                 Time: 144840960, From: 1-408-555-0222, To: 1-650-287-2203
	TEXT: Will come to your house this afternoon     Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	TEXT: Yeah.                                      Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	VOICE: Duration:172 (sec), Format: MOV           Time: 144840960, From: 1-408-555-0222, To: 1-650-000-2828
Total charges: 7.55
-----------------------------------------------
Account: 1-408-666-0333
	TEXT: I'm tired today.                           Time: 144840960, From: 1-408-666-0333, To: 1-650-555-4444
	MEDIA: Size:3.5 MB, Format: GIF                  Time: 144840960, From: 1-408-666-0333, To: 1-650-123-2000
Total charges: 5.7
-----------------------------------------------
   FOOTHILL WIRELESS at Santa Clara
MESSAGE UTILIZATION AND ACCOUNT ADMIN
1. List all accounts.
2. Erase a media message.
3. Disconnect account.
4. Quit
2
   FOOTHILL WIRELESS at Santa Clara
MESSAGE UTILIZATION AND ACCOUNT ADMIN
1. List all accounts.
2. Erase a media message.
3. Disconnect account.
4. Quit
1
Account: 1-408-222-0222
	TEXT: Happy birhday!!!                           Time: 144840960, From: 1-408-222-0222, To: 1-650-333-6666
	VOICE: Duration:670 (sec), Format: MOV           Time: 144840960, From: 1-408-222-0222, To: 1-650-812-0011
Total charges: 7.0
-----------------------------------------------
Account: 1-408-333-0222
	TEXT: Java is fun to learn right                 Time: 144840960, From: 1-408-333-0222, To: 1-650-213-4444
	MEDIA: Size:5.5 MB, Format: GIF                  Time: 144840960, From: 1-408-333-0222, To: 1-650-444-6666
Total charges: 4.5
-----------------------------------------------
Account: 1-408-444-0222
Total charges: 0.0
-----------------------------------------------
Account: 1-408-555-0222
	TEXT: Can you close the backdoor?                Time: 144840960, From: 1-408-555-0222, To: 1-650-321-3131
	TEXT: I got to go now Sorry                      Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	TEXT: Will come to your house this afternoon     Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	TEXT: Yeah.                                      Time: 144840960, From: 1-408-555-0222, To: 1-650-991-7801
	VOICE: Duration:172 (sec), Format: MOV           Time: 144840960, From: 1-408-555-0222, To: 1-650-000-2828
Total charges: 4.3500000000000005
-----------------------------------------------
Account: 1-408-666-0333
	TEXT: I'm tired today.                           Time: 144840960, From: 1-408-666-0333, To: 1-650-555-4444
Total charges: 0.45
-----------------------------------------------
   FOOTHILL WIRELESS at Santa Clara
MESSAGE UTILIZATION AND ACCOUNT ADMIN
1. List all accounts.
2. Erase a media message.
3. Disconnect account.
4. Quit
4

Process finished with exit code 0

*/

import java.util.List;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;


public class Main {
	public static void main(String[] args) throws Exception
  {
      Schema schema = new Schema(2, "com.kuaipao.db");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
      addNote(schema);
      addCustomerOrder(schema);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
      new DaoGenerator().generateAll(schema, "/Users/ZhanTao/Documents/");
  }
                   
//  private String name;
//  private String desc;
//  private String comment;
//  private String city;
//  private String business;
//  private String location;
//  private String phone;
//  private LocationCoordinate2D locationCoordinate2D;
//  private double distance;
//  private List<CardClass> cardClasses;
//  private List<String> cateList;
//  private int partnerType = 0;//0-tuanGou; 1-zhiJie;
//  private String logoKey;
//  private boolean isFavorited;
//  private boolean isBanned = false;
//
//  private long updateTime;
  private static void addNote(Schema schema)
  {
      Entity note = schema.addEntity("CardMerchant");
      note.addIdProperty();
      note.addLongProperty("merchantID").unique();
      note.addLongProperty("businessID");
      note.addStringProperty("name");
      note.addStringProperty("desc");
      note.addStringProperty("comment");
      note.addStringProperty("city");
      note.addStringProperty("business");
      note.addStringProperty("location");
      note.addStringProperty("phone");
      note.addStringProperty("logoKey");
      
      note.addByteArrayProperty("locationCoordinate2D");
      note.addByteArrayProperty("cardClasses");
      note.addByteArrayProperty("cateList");
      
      note.addDoubleProperty("distance");
      note.addIntProperty("partnerType");
      
      note.addBooleanProperty("isFavorited");
      note.addBooleanProperty("isBanned");
      
      
      note.addDateProperty("updateTime");
      note.addByteArrayProperty("pics");
      note.addByteArrayProperty("facilities");
      
      note.addIntProperty("orderNum");
      note.addIntProperty("restrictType");
      note.addByteArrayProperty("yearCards");
      note.addByteArrayProperty("personalCoaches");
      note.addIntProperty("coachNum");
  }
  
//  private long _ID = -1;
//
//  private long orderID = 0l;
//  private long updateTime = 0l;
//  private String orderCode;
//
//  private String classID;
//  private String className;
//  private long startTime = 0l;
//  private long endTime = 0l;
//  private int classType = 1;// '器械健身': 1, 游泳': 2, '瑜伽': 3, '舞蹈': 4, '单车': 5
//
//
//  private long merchantID = 0l;
//  private String merchantName;
//  private String groupPurchaseName;
//  private String merchantLocation;
//  private int coType = 1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
  private static void addCustomerOrder(Schema schema)
  {
      Entity customer = schema.addEntity("CardOrder");
      customer.addIdProperty();
      customer.addLongProperty("orderID").unique();
      customer.addLongProperty("merchantID");
      
      customer.addIntProperty("classType");
      customer.addIntProperty("coType");
      customer.addIntProperty("orderStatus");
      
      customer.addStringProperty("orderCode");
      customer.addStringProperty("classID");
      customer.addStringProperty("className");
      customer.addStringProperty("merchantName");
      customer.addStringProperty("groupPurchaseName");
      customer.addStringProperty("merchantLocation");
      
      customer.addDateProperty("updateTime");
      customer.addDateProperty("startTime");
      customer.addDateProperty("endTime");
      customer.addStringProperty("notice");

      customer.addIntProperty("inviteMsgID");
      customer.addIntProperty("punchMsgID");
      
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
//      Entity order = schema.addEntity("Order");
//      order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
//      order.addIdProperty();
//      Property orderDate = order.addDateProperty("date").getProperty();
//      Property customerId = order.addLongProperty("customerId").notNull().getProperty();
//      order.addToOne(customer, customerId);
//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
//      ToMany customerToOrders = customer.addToMany(order, customerId);
//      customerToOrders.setName("orders");
//      customerToOrders.orderAsc(orderDate);
  }
                                  
}

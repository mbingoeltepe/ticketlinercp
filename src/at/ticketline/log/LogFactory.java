package at.ticketline.log;

public class LogFactory {
	   public static Logger getLogger(String name)
	   {
	      return new Log4JLogger(name);
	   }
	   
	   @SuppressWarnings("rawtypes")
	   public static Logger getLogger(Class clazz)
	   {
	      return new Log4JLogger(clazz);
	   }
}

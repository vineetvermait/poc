//package in.techutils.awsproxy;
//
//import in.techutils.awsproxy.memory.MemoryManagement;
//
//
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.ws.rs.ApplicationPath;
//import javax.ws.rs.core.Application;
//
//@ApplicationPath("resources")
//public class GenericApplication extends Application {
//    public GenericApplication() {
//        try {
//            MemoryManagement.reinitializeFromDatabase();
//        } catch (Exception e) {
//            System.out.println("Error in DB");
//            e.printStackTrace();
//        }
//    }
//
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> classes = new HashSet<Class<?>>();
//        classes.add(AWSProxyService.class);
//        return classes;
//    }
//}

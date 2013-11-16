package andy.test;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    System.out.println("Hello World!");
  }

  public static Optional<String> getString(String str){
    switch(str) {
      case "Actual":return Optional.of("Actual");
      default:return Optional.absent();
    }

//    if(str != null && str.equals("Actual"))
//      return Optional.of("Actual");
//    else
//      return Optional.absent();
  }
}

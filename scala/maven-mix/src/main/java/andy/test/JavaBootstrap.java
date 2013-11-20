package andy.test;

public class JavaBootstrap {
  public static void main(String[] args) {
    System.out.println(JavaBootstrap.class);
    GreetingInScala scala = new GreetingInScala();
    scala.greet();
  }
}
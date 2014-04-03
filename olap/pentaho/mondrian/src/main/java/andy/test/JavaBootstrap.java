package andy.test;

public class JavaBootstrap {
  public static void main(String[] args) {
    System.out.println(JavaBootstrap.class);
    TestInScala scala = new TestInScala();
    scala.test_connection_execute();
  }
}
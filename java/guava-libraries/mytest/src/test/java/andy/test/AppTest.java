package andy.test;

import com.google.common.base.Optional;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() {
    assertTrue(true);
  }

  public void testJoiner() {
    Joiner joiner = Joiner.on("; ").skipNulls();
    String join_result = joiner.join("Harry", null, "Ron", "Hermione");
    assertEquals("Harry; Ron; Hermione", join_result);
  }

  public void testOptional() {
    assertEquals("Actual", App.getString("Actual").get());
    assertEquals("default", App.getString("").or("default"));
    assertFalse(Objects.equal("default", App.getString("")));
  }
}

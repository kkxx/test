package andy.test;

import java.lang.ClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.reflect.ClassPath;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Hello world!
 */
public class App {
  final static Logger logger = LoggerFactory.getLogger(App.class);
  public static void main(String[] args) {
    logger.info("Entering application.");
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    URL[] urls = ((URLClassLoader)cl).getURLs();
    for(URL url: urls){
      logger.debug(url.getFile());
    }
    logger.info("Hello World!");
  }

  public static Optional<String> getString(String str) {
    switch (str) {
      case "Actual":
        return Optional.of("Actual");
      default:
        return Optional.absent();
    }
  }
}

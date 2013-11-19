package andy.test;

import java.lang.ClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

import ch.qos.logback.classic.Level;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Auto reload configuration when any change happen : part 2
// http://howtodoinjava.com/2012/10/19/auto-reload-configuration-when-any-change-happen-part-2/
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Hello world!
 */
public class App {
  final static Logger logger = LoggerFactory.getLogger(App.class);

  private static PropertiesConfiguration configuration = null;

  static {
    try {
      configuration = new PropertiesConfiguration("test.properties");
      configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
    } catch (ConfigurationException e) {
      logger.error("Ops!", e);
    }
  }

  public static synchronized String getProperty(final String key) {
    return (String) configuration.getProperty(key);
  }

  public static void main(String[] args) {
    long begin = System.currentTimeMillis();
    logger.info("Entering application.");
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    URL[] urls = ((URLClassLoader) cl).getURLs();
    for (URL url : urls) {
      logger.debug(url.getFile());
    }
    logger.info("Hello World!");
    logger.info(App.getProperty("test"));
    long end = System.currentTimeMillis();
    logger.info(String.format("main cost time %d ms", end - begin));

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

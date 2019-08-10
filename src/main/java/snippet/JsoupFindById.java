package snippet;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsoupFindById {

  private static Logger LOGGER = LoggerFactory.getLogger(JsoupFindById.class);

  private static String CHARSET_NAME = "utf8";

  public static void main(String[] args) {

    String originalResourcePath = args[0];
    String otherResourcePath = args[1];


    String targetElementId = "make-everything-ok-button";

    Optional<Element> buttonOpt = findElementById(new File(originalResourcePath), targetElementId);

    buttonOpt.ifPresent(options -> {
      String hrefValue = options.attr("href");
      String cssQuery = "[href = " + hrefValue + "]";

      Optional<Elements> elementsOpt = findElementsByQuery(new File(otherResourcePath), cssQuery);


      elementsOpt.ifPresent(elements -> {
            List<String> parentTags = elements
                .parents()
                .stream()
                .map(parent -> parent.tagName() + ">").collect(Collectors.toList());
            Collections.reverse(parentTags);
            parentTags.forEach(System.out::print);

          }
      );
    });

  }

  private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
    try {
      Document doc = Jsoup.parse(
          htmlFile,
          CHARSET_NAME,
          htmlFile.getAbsolutePath());

      return Optional.of(doc.getElementById(targetElementId));

    } catch (IOException e) {
      LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
      return Optional.empty();
    }
  }

  private static Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery) {
    try {
      Document doc = Jsoup.parse(
          htmlFile,
          CHARSET_NAME,
          htmlFile.getAbsolutePath());

      return Optional.of(doc.select(cssQuery));

    } catch (IOException e) {
      LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
      return Optional.empty();
    }
  }

}

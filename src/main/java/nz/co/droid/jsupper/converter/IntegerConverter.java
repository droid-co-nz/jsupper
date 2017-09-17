package nz.co.droid.jsupper.converter;


import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerConverter implements IConverter<Integer> {

  private static final Logger logger = LoggerFactory.getLogger(IntegerConverter.class);

  @Override
  public Integer convert(Object object) {
    if (object == null || object instanceof Integer) {
      return (Integer) object;
    }

    if (!(object instanceof String)) {
      return null;
    }

    Integer number = null;

    String text = (String) object;
    if (!StringUtil.isBlank(text)) {
      text = text.replaceAll("[^0-9\\.-]", "");
      
      if (text.contains(".")) {
        text = text.substring(0, text.indexOf("."));
      }
      try {
        number = Integer.parseInt(text);
      } catch (Exception e) {
        logger.error("Problem when parsing integer from {}", text, e);
      }
    }

    return number;
  }
}

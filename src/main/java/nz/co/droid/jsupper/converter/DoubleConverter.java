package nz.co.droid.jsupper.converter;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleConverter implements IConverter<Double> {

  private static final Logger logger = LoggerFactory.getLogger(DoubleConverter.class);

  @Override
  public Double convert(Object object) {
    if (object == null || object instanceof Integer) {
      return (Double) object;
    }

    if (!(object instanceof String)) {
      return null;
    }

    Double number = null;

    String text = (String) object;

    if (!StringUtil.isBlank(text)) {
      text = text.replaceAll("[^0-9\\.,-]", "");

      if (text.contains(",")) {
        text = text.replaceAll(",", ".");
      }

      try {
        number = Double.parseDouble(text);
      } catch (Exception e) {
        logger.error("Problem when parsing integer from {}", text);
      }
    }

    return number;
  }
}

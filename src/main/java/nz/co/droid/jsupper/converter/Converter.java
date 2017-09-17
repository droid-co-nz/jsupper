package nz.co.droid.jsupper.converter;

import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Converter {

  private static final Logger logger = LoggerFactory.getLogger(Converter.class);

  private static Map<String, IConverter> converters = new HashMap<String, IConverter>();

  static {
    converters.put(String.class.getName().toLowerCase(), new StringConverter());
    converters.put(Integer.class.getName().toLowerCase(), new IntegerConverter());
    converters.put(Double.class.getName().toLowerCase(), new DoubleConverter());
  }

  @SuppressWarnings("unchecked")
  public static <T> T convert(Class<T> type, Object value) {
    IConverter<T> converter = converters.get(type.getName().toLowerCase());
    if (converter == null) {
      logger.error("The {} is not supported by any registered converters", type.getClass());
      return null;
    }

    T convertedValue = null;

    try {
      convertedValue = (T) converter.convert(value);
    } catch (Exception e) {
      logger.error("Problem when converting {} with converter {}", new Object[] { value, converter, e });
    }

    return convertedValue;
  }
}

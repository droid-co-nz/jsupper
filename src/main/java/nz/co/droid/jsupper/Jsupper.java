package nz.co.droid.jsupper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nz.co.droid.jsupper.action.FormSubmit;
import nz.co.droid.jsupper.converter.Converter;
import nz.co.droid.jsupper.exception.JsupperException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jsupper {

  private static final Logger logger = LoggerFactory.getLogger(Jsupper.class);

  private Document document;

  private Elements elements;

  private Element element;

  private Connection.Response response;

  /**
   * Constructs with jsoup document.
   * 
   * @param document
   *          jsoup document
   */
  public Jsupper(Document document) {
    this.document = document;
  }

  /**
   * Constructs with jsoup elements.
   * 
   * @param elements
   *          jsoup elements
   */
  public Jsupper(Elements elements) {
    this.elements = elements;
  }

  /**
   * Constructs with jsoup element.
   * 
   * @param element
   *          jsoup element
   */
  public Jsupper(Element element) {
    this.element = element;
  }

  /**
   * Constructs with Jsoup response.
   * 
   * @param response
   *          jsoup response
   */
  public Jsupper(Connection.Response response) {
    this.parse(response);
  }

  /**
   * Parse the response with Jsoup.
   * 
   * @param response
   */
  private void parse(Connection.Response response) {
    this.document = Jsoup.parse(response.body(), response.url().toString());
    this.response = response;
  }

  /**
   * Returns Jsoup document
   * 
   * @return jsoup document
   */
  public Document getDocument() {
    return this.document;
  }

  public List<Jsupper> each(String query) {
    List<Jsupper> list = new ArrayList<Jsupper>();
    Elements elements = this.select(query);

    if (elements != null && elements.size() > 0) {
      for (Element element : elements) {
        list.add(new Jsupper(element));
      }
    }
    return list;
  }

  /**
   * Returns list of values converted to given type.
   * 
   * @param type
   *          requested type
   * @param elements
   *          jsoup elements
   * @return list of requested types
   */
  public static <T> List<T> getList(Class<T> type, Elements elements) {
    List<T> collection = new ArrayList<T>();
    T value = null;
    if (elements != null && elements.size() > 0) {
      for (Element element : elements) {
        value = Jsupper.getValue(type, element);
        if (value != null) {
          collection.add(value);
        }
      }
    }
    return collection;
  }

  /**
   * Returns specified type using registered converter (gets first found value). If none of the converters can't handle
   * given type, returns null.
   * 
   * @param type
   *          requested type
   * @param elements
   *          jsoup elements
   * @return requested type or null
   */
  public static <T> T getValue(Class<T> type, Elements elements) {
    if (elements == null || elements.size() == 0) {
      return null;
    }
    return Jsupper.getValue(type, elements.first());
  }

  /**
   * Returns specified type using registered converter. If none of the converters can't handle given type, returns null.
   * 
   * @param type
   *          requested type
   * @param element
   *          jsoup element
   * @return value requested type or null
   */
  public static <T> T getValue(Class<T> type, Element element) {
    T value = null;
    try {
      value = Converter.convert(type, Jsoup.clean(element.ownText(), Whitelist.none()));
    } catch (Exception e) {
      logger.error("Problem when converting {} to {}", new Object[] { element, type, e });
    }
    return value;
  }

  /**
   * Returns first found String.
   * 
   * @param query
   *          selector
   * @return String
   */
  public String getString(String query) {
    String value = Jsupper.getValue(String.class, this.select(query));
    if (StringUtil.isBlank(value)) {
      value = null;
    }
    return value;
  }

  /**
   * Returns list of Strings (removes any blank strings).
   * 
   * @param query
   *          selector
   * @return list of Strings
   */
  public List<String> getStrings(String query) {
    List<String> values = Jsupper.getList(String.class, this.select(query));
    Iterator<String> iterator = values.iterator();
    while (iterator.hasNext()) {
      if (StringUtil.isBlank(iterator.next())) {
        iterator.remove();
      }
    }
    return values;
  }

  /**
   * Return first found integer.
   * 
   * @param query
   *          selector
   * @return integer
   */
  public Integer getInteger(String query) {
    return Jsupper.getValue(Integer.class, this.select(query));
  }

  /**
   * Return selected elements as integers (parsing errors are silently ignored).
   * 
   * @param query
   *          selector
   * @return list of integers
   */
  public List<Integer> getIntegers(String query) {
    return Jsupper.getList(Integer.class, this.select(query));
  }

  /**
   * Return first found double.
   * 
   * @param query
   *          selector
   * @return double
   */
  public Double getDouble(String query) {
    return Jsupper.getValue(Double.class, this.select(query));
  }

  /**
   * Return selected elements as doubles (parsing errors are silently ignored).
   * 
   * @param query
   *          selector
   * @return list of doubles
   */
  public List<Double> getDoubles(String query) {
    return Jsupper.getList(Double.class, this.select(query));
  }

  /**
   * Returns element specified by selector either from Document or Elements.
   * 
   * @param query
   *          selector
   * @return elements
   */
  public Elements select(String query) {
    if (this.element != null) {
      return this.element.select(query);
    } else if (this.elements != null) {
      return this.elements.select(query);
    } else {
      return this.document.select(query);
    }
  }

  /**
   * Submits the form using its first button.
   * 
   * @param query
   *          form element selector
   * @param params
   *          request parameters
   * @throws JsupperException
   *           when something goes wrong
   */
  public void submit(String query, Map<String, String> params) throws JsupperException {
    this.submit(query, params, null);
  }

  /**
   * Submits the form using specified form button.
   * 
   * @param query
   *          form element selector
   * @param params
   *          request parameters
   * @param button
   *          form button used to 'click'
   * @throws JsupperException
   *           when something goes wrong
   */
  public void submit(String query, Map<String, String> params, String button) throws JsupperException {
    Elements elements = this.select(query);

    if (elements == null || elements.size() == 0) {
      throw new JsupperException("Can't find element '" + query + "'");
    }

    FormSubmit formSubmit = new FormSubmit(elements.get(0), this.response);

    try {
      this.parse(formSubmit.submit(params, button));
    } catch (Exception e) {
      throw new JsupperException(e);
    }
  }
}

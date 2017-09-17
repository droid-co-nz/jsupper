package nz.co.droid.jsupper.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nz.co.droid.jsupper.exception.JsupperFormSubmitException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FormSubmit {

  private Element element;

  private Connection.Response previousResponse;

  public FormSubmit(Element element) {
    this(element, null);
  }

  public FormSubmit(Element element, Connection.Response previousResponse) {
    this.element = element;
    this.previousResponse = previousResponse;
  }

  public Connection.Response submit(Map<String, String> params, String button) throws JsupperFormSubmitException {
    this.validate();

    Connection con = this.setupConnection();

    Map<String, String> parameters = new HashMap<String, String>(this.parseFormElements());
    parameters.putAll(this.parseFormButtons(button));
    
    if (params != null) {
      parameters.putAll(params);
    }

    System.out.println(new TreeMap<String, String>(parameters));

    con.data(parameters);
    // TODO this doesn't work as JSOUP doesn't expose the req in previous response ;/
    //con.userAgent(this.previousResponse.header("User-Agent"));
    con.userAgent("Mozilla");
    con.referrer(this.previousResponse.url().toString());
    try {
      return con.execute();
    } catch (Exception e) {
      throw new JsupperFormSubmitException("Problem when submitting form", e);
    }
  }

  private void validate() throws JsupperFormSubmitException {
    if (this.element == null) {
      throw new JsupperFormSubmitException("The html element null");
    }

    if (!"form".equalsIgnoreCase(this.element.tagName())) {
      throw new JsupperFormSubmitException("The html element is not a form but '" + this.element.tagName() + "'");
    }
  }

  private Connection setupConnection() {

    String baseUri = this.element.baseUri();
    String url = this.element.attr("action");
    if (StringUtil.isBlank(url) || baseUri.endsWith(url)) {
      // use the base uri
      url = baseUri;
    } else {
      if (!url.startsWith("http")) {
        if (url.startsWith("/")) {
          String protocol = baseUri.substring(0, baseUri.indexOf("://") + 3);
          String domain = baseUri.substring(baseUri.indexOf("://") + 3);
          domain = domain.substring(0, domain.indexOf("/"));
          url = protocol + domain + url;
        } else {
          url = this.element.baseUri() + url;
        }
      }
    }

    Connection con = HttpConnection.connect(url);

    if ("POST".equalsIgnoreCase(this.element.attr("method"))) {
      con.request().method(Method.POST);
    } else {
      con.request().method(Method.GET);
    }

    if (this.previousResponse != null) {
      for (Entry<String, String> cookie : this.previousResponse.cookies().entrySet()) {
        con.request().cookie(cookie.getKey(), cookie.getValue());
      }
    }

    con.timeout(5000);

    return con;
  }

  private Map<String, String> parseFormButtons(String button) {
    Map<String, String> buttons = new HashMap<String, String>();
    for (Element input : element.select("input")) {
      if ("submit".equalsIgnoreCase(input.attr("type"))) {
        if (StringUtil.isBlank(button) || button.equalsIgnoreCase(input.attr("name"))) {
          buttons.put(input.attr("name"), input.attr("value"));
          break;
        }
      }
    }
    return buttons;
  }

  private Map<String, String> parseFormElements() {
    Map<String, String> elements = new HashMap<String, String>();

    for (Element input : element.select("input")) {
      if ("text".equalsIgnoreCase(input.attr("type")) || "hidden".equalsIgnoreCase(input.attr("type"))) {
        if (!StringUtil.isBlank(input.attr("name"))) {
          elements.put(input.attr("name"), input.attr("value"));
        }
      }
    }

    for (Element select : element.select("select")) {
      Elements options = select.select("option[selected]");
      if (options != null && options.size() == 1) {
        elements.put(select.attr("name"), options.get(0).attr("value"));
      } else {
        options = select.select("option");
        if (options != null && options.size() > 0) {
          elements.put(select.attr("name"), options.get(0).attr("value"));
        }
      }
    }

    return elements;
  }

}

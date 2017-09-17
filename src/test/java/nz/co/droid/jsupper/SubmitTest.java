package nz.co.droid.jsupper;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.junit.Test;

public class SubmitTest {

  @Test
  public void submitGoogle() throws Exception {

    Map<String,String> parameters = new HashMap<String,String>();
    parameters.put("q", "jsoup");
    
    Jsupper jsupper = new Jsupper(Jsoup.connect("http://www.google.com/").userAgent("Mozilla").method(Method.GET).execute());
    jsupper.submit("form[name=f]", parameters, "btnG");

    assertEquals(jsupper.getString("title"), "jsoup - Google Search");
  }

}

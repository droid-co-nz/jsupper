package nz.co.droid.jsupper;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

public class JsupperTest {

  File html;

  @Before
  public void load() {
    URL url = this.getClass().getResource("/index.html");
    html = new File(url.getFile());
  }

  @Test
  public void testDoubles() throws IOException {
    Jsupper jsupper = new Jsupper(Jsoup.parse(html, "utf-8"));

    assertEquals(jsupper.getDoubles("ul.doubles li").size(), 3);
    assertEquals(jsupper.getDouble("ul.doubles li").doubleValue(), 56.78, 0);
    assertEquals(jsupper.getDoubles("ul.doubles  li").get(0).doubleValue(), 56.78, 0);
    assertEquals(jsupper.getDoubles("ul.doubles  li").get(1).doubleValue(), 2, 0);
    assertEquals(jsupper.getDoubles("ul.doubles  li").get(2).doubleValue(), 85.4567, 0);
  }

  @Test
  public void testIntegers() throws IOException {
    Jsupper jsupper = new Jsupper(Jsoup.parse(html, "utf-8"));
    assertEquals(jsupper.getIntegers("ul.integers li").size(), 3);
    assertEquals(jsupper.getInteger("ul.integers li").intValue(), 32);
    assertEquals(jsupper.getIntegers("ul.integers li").get(0).intValue(), 32);
    assertEquals(jsupper.getIntegers("ul.integers li").get(1).intValue(), 5);
    assertEquals(jsupper.getIntegers("ul.integers li").get(2).intValue(), 22);
  }

  @Test
  public void testStrings() throws IOException {
    Jsupper jsupper = new Jsupper(Jsoup.parse(html, "utf-8"));

    assertEquals(jsupper.getStrings("p").size(), 3);
    assertEquals(jsupper.getString("p"), "First");
    assertEquals(jsupper.getStrings("p").get(0), "First");
    assertEquals(jsupper.getStrings("p").get(1), "Second");
  }

}

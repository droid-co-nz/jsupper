jsupper
=======

Jsupper adds some additional features to Jsoup - so here is few examples:

#### Initialise with Document (the same way as Jsoup)

```java
Jsupper jsupper = new Jsupper(Jsoup.parse(html, "utf-8"));
Jsupper jsupper = new Jsupper(Jsoup.connect("http://www.example.com/").method(Method.GET).execute());
```


#### Parse strings/integers/doubles with standard Jsoup query

```java
List<String> doubles = jsupper.getStrings("ul.strings li");
List<Integer> integers = jsupper.getIntegers("ul.integers li");
List<Double> doubles = jsupper.getDoubles("ul.doubles li");
```

You can also get the first element only:

```java
String firstFoundText = jsupper.getString("ul.strings li");
Integer firstFoundInteger = jsupper.getInteger("ul.integers li");
Doubles double = jsupper.getDouble("ul.doubles li");
```

#### And submit form

```java
@Test
public void submitGoogle() throws Exception {

  Map<String,String> parameters = new HashMap<String,String>();
  parameters.put("q", "jsoup");
    
  Jsupper jsupper = new Jsupper(Jsoup.connect("http://www.google.com/").userAgent("Mozilla").method(Method.GET).execute());
  jsupper.submit("form[name=f]", parameters, "btnG");

  assertEquals(jsupper.getString("#search li cite b"), "jsoup");
}
```

package nz.co.droid.jsupper.converter;


public class StringConverter implements IConverter<String> {

  @Override
  public String convert(Object object) {
    return object.toString();
  }
}

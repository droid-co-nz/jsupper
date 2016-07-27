package nz.co.droid.jsupper.converter;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringConverter implements IConverter<String> {

	@Override
	public String convert(Object object) {
		return StringEscapeUtils.unescapeHtml4(object.toString());
	}
}

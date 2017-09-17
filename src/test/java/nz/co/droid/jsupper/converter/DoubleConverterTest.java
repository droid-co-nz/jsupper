package nz.co.droid.jsupper.converter;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DoubleConverterTest {

	@InjectMocks
	DoubleConverter doubleConverter;

	@Test
	public void parseNegativeNumber() {
		assertThat(doubleConverter.convert("-36.8018127")).isEqualTo(-36.8018127);
	}
}

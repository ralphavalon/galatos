package galatos.notification.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NotBlankFieldsValidatorTest {

	private boolean isValid(List<String> target) {
		NotBlankFieldsValidator validator = new NotBlankFieldsValidator();
		validator.initialize(ClassWithField.class.getAnnotation(NotBlankFields.class));
		return validator.isValid(target, null);
	}
	
	@Test
	public void whenNoFields_thenShouldIgnoreAndBeValid() {
		ClassWithField classWithField = new ClassWithField(Arrays.asList());
		assertThat(isValid(classWithField.a)).isTrue();
	}

	@Test
	public void whenNoList_thenShouldIgnoreAndBeValid() {
		ClassWithField classWithField = new ClassWithField(null);
		assertThat(isValid(classWithField.a)).isTrue();
	}

	@Test
	public void whenHasNotBlankFields_thenShouldBeValid() {
		ClassWithField classWithField = new ClassWithField(Arrays.asList("a"));
		assertThat(isValid(classWithField.a)).isTrue();
	}

	@Test
	public void whenHasNullField_thenShouldNotBeValid() {
		List<String> list = new ArrayList<>();
		list.add(null);
		ClassWithField classWithField = new ClassWithField(list);
		assertThat(isValid(classWithField.a)).isFalse();
	}

	@Test
	public void whenHasBlankField_thenShouldNotBeValid() {
		ClassWithField classWithField = new ClassWithField(Arrays.asList("a", " "));
		assertThat(isValid(classWithField.a)).isFalse();
	}

	private class ClassWithField {
		@NotBlankFields
		private List<String> a;

		ClassWithField(List<String> a) {
			this.a = a;
		}
	}

}

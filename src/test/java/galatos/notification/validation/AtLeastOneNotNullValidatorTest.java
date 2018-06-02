package galatos.notification.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AtLeastOneNotNullValidatorTest {
	
	private boolean isValid(Object target) {
		AtLeastOneNotNullValidator validator = new AtLeastOneNotNullValidator();
		validator.initialize(target.getClass().getAnnotation(AtLeastOneNotNull.class));
		return validator.isValid(target, null);
	}

	@Test
	public void whenClassWithFieldsThatMatchOneNotNull_thenShouldBeValid() {
		assertThat(isValid(new ClassWithFieldsThatMatchOneNotNull())).isTrue();
	}
	
	@Test
	public void whenClassWithFieldsThatMatchAllNotNull_thenShouldBeValid() {
		assertThat(isValid(new ClassWithFieldsThatMatchAllNotNull())).isTrue();
	}
	
	@Test
	public void whenClassWithFieldsThatMatchOneNotNullAndOneInvalidField_thenShouldBeValid() {
		assertThat(isValid(new ClassWithFieldsThatMatchOneNotNullAndOneInvalidField())).isTrue();
	}
	
	@Test
	public void whenNoClass_thenShouldNotBeValid() {
		AtLeastOneNotNullValidator validator = new AtLeastOneNotNullValidator();
		validator.initialize(ClassWithoutFields.class.getAnnotation(AtLeastOneNotNull.class));
		assertThat(validator.isValid(null, null)).isFalse();
	}
	
	@Test
	public void whenClassWithoutFields_thenShouldNotBeValid() {
		assertThat(isValid(new ClassWithoutFields())).isFalse();
	}
	
	@Test
	public void whenClassWithFieldsButNoFieldsGiven_thenShouldNotBeValid() {
		assertThat(isValid(new ClassWithFieldsButNoFieldsGiven())).isFalse();
	}
	
	@Test
	public void whenClassWithFieldsThatMatchAllNull_thenShouldNotBeValid() {
		assertThat(isValid(new ClassWithFieldsThatMatchAllNull())).isFalse();
	}

	@AtLeastOneNotNull(fields = "a")
	private class ClassWithoutFields {
	}

	@AtLeastOneNotNull(fields = { "" })
	@SuppressWarnings("unused")
	private class ClassWithFieldsButNoFieldsGiven {
		private Object a = new Object();
		private Object b = new Object();
	}

	@AtLeastOneNotNull(fields = { "a", "b" })
	@SuppressWarnings("unused")
	private class ClassWithFieldsThatMatchOneNotNull {
		private Object a;
		private Object b = new Object();
	}

	@AtLeastOneNotNull(fields = { "", "b" })
	@SuppressWarnings("unused")
	private class ClassWithFieldsThatMatchOneNotNullAndOneInvalidField {
		private Object a;
		private Object b = new Object();
	}

	@AtLeastOneNotNull(fields = { "b", "a" })
	@SuppressWarnings("unused")
	private class ClassWithFieldsThatMatchAllNotNull {
		private Object a = new Object();
		private Object b = new Object();
	}

	@AtLeastOneNotNull(fields = {"a", "b"})
	@SuppressWarnings("unused")
	private class ClassWithFieldsThatMatchAllNull {
		private Object a;
		private Object b;
	}

}

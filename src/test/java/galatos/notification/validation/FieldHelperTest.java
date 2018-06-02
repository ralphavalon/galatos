package galatos.notification.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import galatos.notification.StaticClassTest;

public class FieldHelperTest extends StaticClassTest<FieldHelper> {
	
	private ClassWithField classWithField;
	
	public FieldHelperTest() {
		super(FieldHelper.class);
	}
	
	@Before
	public void setUp() {
		classWithField = new ClassWithField();
	}
	
	Field getFieldInstance(Object instance, String fieldName) {
		List<Field> fields = Arrays.asList(instance.getClass().getDeclaredFields());
		return fields.stream()
				.filter((field) -> field.getName().equals(fieldName))
				.findFirst()
				.orElse(null);
	}
	
	@Test
	public void whenExistingFieldObjectAndParent_thenShouldGetFieldName() {
		assertThat(FieldHelper.getFieldName(classWithField.a, classWithField)).isEqualTo("a");
	}
	
	@Test(expected=NoSuchElementException.class)
	public void whenExistingFieldObjectButDifferentParent_thenShouldThrowError() {
		FieldHelper.getFieldName(classWithField.a, new Object());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void whenExistingDifferentFieldObjectAndDifferentParent_thenShouldThrowError() {
		FieldHelper.getFieldName(new String(), classWithField);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenExistingFieldObjectButNoParent_thenShouldThrowError() {
		FieldHelper.getFieldName(classWithField.a, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenNoExistingFieldObjectButExistingParent_thenShouldThrowError() {
		FieldHelper.getFieldName(null, classWithField);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenNoExistingFieldObjectAndNoParent_thenShouldThrowError() {
		FieldHelper.getFieldName(null, null);
	}
	
	@Test
	public void whenExistingField_thenShouldGetField() {
		Field field = getFieldInstance(classWithField, "a");
		assertThat(FieldHelper.getField(field, classWithField)).isNotNull();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenNotExistingField_thenShouldThrowException() {
		Field field = getFieldInstance(classWithField, "b");
		FieldHelper.getField(field, classWithField);
	}
	
	private class ClassWithField {
		private Object a = new Object();
	}

}

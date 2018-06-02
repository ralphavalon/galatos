package galatos.notification.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import galatos.notification.StaticClassTest;

public class FieldHelperTest extends StaticClassTest<FieldHelper> {
	
	public FieldHelperTest() {
		super(FieldHelper.class);
	}

	private ClassWithField classWithField = new ClassWithField();
	
	Field getFieldInstance(Object instance, String fieldName) {
		List<Field> fields = Arrays.asList(instance.getClass().getDeclaredFields());
		return fields.stream()
				.filter((field) -> field.getName().equals(fieldName))
				.findFirst()
				.orElse(null);
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
	
	@SuppressWarnings("unused")
	private class ClassWithField {
		private Object a = new Object();
	}

}

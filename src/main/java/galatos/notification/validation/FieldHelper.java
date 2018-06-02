package galatos.notification.validation;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

public class FieldHelper {
	
	private FieldHelper() throws InstantiationException {
	    throw new InstantiationException("Instances of this type are forbidden.");
	}
	
	public static Object getField(Field field, Object instance) {
		try {
			return FieldUtils.readField(field, instance, true);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
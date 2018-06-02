package galatos.notification.validation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

public class FieldHelper {
	
	private FieldHelper() throws InstantiationException {
	    throw new InstantiationException("Instances of this type are forbidden.");
	}
	
	public static String getFieldName(Object fieldObject, Object parent) {
		if(fieldObject == null || parent == null) {
			throw new IllegalArgumentException();
		}
			
	    List<Field> allFields = Arrays.asList(parent.getClass().getDeclaredFields());
	    return allFields.stream()
		    	.filter(field -> fieldObject.equals(getField(field, parent)))
		    	.map(field -> field.getName())
		    	.findFirst()
		    	.get();
	}
	
	public static Object getField(Field field, Object instance) {
		try {
			return FieldUtils.readField(field, instance, true);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
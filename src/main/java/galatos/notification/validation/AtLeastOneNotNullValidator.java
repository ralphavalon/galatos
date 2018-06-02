package galatos.notification.validation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {
	
	private Set<String> fields;

	@Override
	public void initialize(AtLeastOneNotNull constraint) {
		this.fields = new HashSet<>(Arrays.asList(constraint.fields()));
	}

	@Override
	public boolean isValid(Object target, ConstraintValidatorContext context) {
		if(target == null) {
			return false;
		}
		
		List<Field> fields = Arrays.asList(target.getClass().getDeclaredFields());
		return fields.stream()
				.filter((field) -> this.fields.contains(field.getName()))
				.anyMatch((field) -> FieldHelper.getField(field, target) != null);
	}
	
}
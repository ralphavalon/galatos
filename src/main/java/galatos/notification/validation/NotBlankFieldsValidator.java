package galatos.notification.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class NotBlankFieldsValidator implements ConstraintValidator<NotBlankFields, List<String>> {

    @Override
    public void initialize(NotBlankFields notBlankFields) {
    }

    @Override
    public boolean isValid(List<String> objects, ConstraintValidatorContext context) {
    	if(objects == null || objects.size() == 0) {
    		return true;
    	}
        return objects.stream().allMatch(value -> StringUtils.isNotBlank(value));
    }

}
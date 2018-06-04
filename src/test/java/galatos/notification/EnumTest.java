package galatos.notification;

import org.junit.Test;

public abstract class EnumTest<T extends Enum<?>> {
	
	private Class<T> enumClass;
	
	public EnumTest(Class<T> enumClass) {
		this.enumClass = enumClass;
	}
	
	@Test
	public void enumCodeCoverage() {
	    try {
	        for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
	            enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
	        }
	    } catch (Throwable e) {
	        throw new RuntimeException(e);
	    }
	}

}

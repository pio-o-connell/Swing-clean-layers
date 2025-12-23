package WareHouse.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Value {
    String value() default "";
}

package WareHouse.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Qualifier {
    String value() default "";
}

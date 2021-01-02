import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

/*Тестируемый класс, должен иметь конструктор по умолчанию,
либо конструктор без параметров, где инициализируются члены необходимые для теста.
Все тестируемые методы должны быть public*/

public class Tester {

    public static void start(Class<?> clazz) {
        try {
            test(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(String className) {
        try {
            test(Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void test(Class<T> clazz) throws Exception {

        var methods = getMethods(clazz);

        T tested = clazz.getConstructor().newInstance();

        methods.forEach((m) -> {
            try {
                m.invoke(tested);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

//    Метод возвращает отсортированный ArrayList из методов с аннотациями @Test
//    а также если имеются методы с @BeforeSuite и @AfterSuite,
//    они будут находиться в начале и в конце списка соответственно
    private static ArrayList<Method> getMethods(Class<?> clazz) {

        var methods = new ArrayList<Method>();
        Method methodBeforeSuite = null;
        Method methodAfterSuite = null;

        for(Method method : clazz.getDeclaredMethods()) {
            if(method.getAnnotation(BeforeSuite.class) != null) {
                if(methodBeforeSuite == null)
                    methodBeforeSuite = method;
                else
                    throw new RuntimeException("Метод с аннотацией @BeforeSuit должен быть один.");
            }
            else if(method.getAnnotation(AfterSuite.class) != null) {
                if(methodAfterSuite == null)
                    methodAfterSuite = method;
                else
                    throw new RuntimeException("Метод с аннотацией @AfterSuit должен быть один.");
            }
            else if(method.getAnnotation(Test.class) != null) {
                methods.add(method);
            }
        }

        methods.sort(Comparator.comparingInt(m -> m.getAnnotation(Test.class).value()));

        if(methodBeforeSuite != null)
            methods.add(0, methodBeforeSuite);

        if(methodAfterSuite != null)
            methods.add(methodAfterSuite);

        return methods;
    }
}
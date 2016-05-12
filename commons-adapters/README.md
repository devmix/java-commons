# commons-adapters

Library for generating adapters (runtime, compile time) for exists classes.

## Code examples

### Simple

Adaptee class:
``` java
public final class Number {
    private Long value;
    public void initialize(final Long value) { this.value = value; }
    public void doPlus(final Long value) { this.value += value; }
    public void doMinus(final Long value) { this.value -= value; }
    public Long getValue() { return value; }
}
```
Adapter class:
``` java
@Adapter
@DelegateMethods({
        @DelegateMethod(from = "then(.*)", to = "do(.*)"),
        @DelegateMethod(from = "get", to = "getValue"),
        @DelegateMethod(from = "set", to = "initialize")
})
public abstract class NumberAdapter {

    private final Number number = new Number();

    @Adaptee
    public Number adaptee() { return number; }

    public abstract NumberAdapter thenPlus(Long value);

    public abstract NumberAdapter thenMinus(Long value);

    public abstract NumberAdapter set(Long value);

    public abstract Long get();
}
```
Usage:
``` java
final AdaptersContext ctx = AdaptersContextBuilders.standard()
            .addPackage(...)
            .build();

final NumberAdapter number = ctx.createByAdaptee(Number.class);

System.out.println(number.set(10L).thenPlus(5L).thenMinus(2L).get());

// print: 13
```

### Generics

Adaptee class:
``` java
public final class Number {
    private Long value;
    public void initialize(final Long value) { this.value = value; }
    public void doPlus(final Long value) { this.value += value; }
    public void doMinus(final Long value) { this.value -= value; }
    public Long getValue() { return value; }
    public <S> S getValueCast() { return (S) value; }
}
```
Adapter interface:
``` java
public interface NumberAdapter<T, A> {
    @Adaptee
    A adaptee();

    NumberAdapter<T, A> thenPlus(T value);

    NumberAdapter<T, A> thenMinus(T value);

    NumberAdapter<T, A> set(T value);

    T get();

    <S> S cast();
}
```
Adapter class:
``` java
@Adapter
@DelegateMethods({
        @DelegateMethod(from = "then(.*)", to = "do(.*)"),
        @DelegateMethod(from = "get", to = "getValue"),
        @DelegateMethod(from = "set", to = "initialize"),
        @DelegateMethod(from = "cast", to = "getValueCast", returnValue = DelegateRule.ReturnValue.RESULT)
})
public abstract class NumberAdapterImpl implements NumberAdapter<Long, Number> {

    private final Number number = new Number();

    @Adaptee
    public Number adaptee() { return number; }
}
```
Usage:
``` java
final AdaptersContext ctx = AdaptersContextBuilders.standard()
            .addPackage(...)
            .build();

final NumberAdapter<Long, Number> number = ctx.createByAdaptee(Number.class);

System.out.println(number.set(10L).thenPlus(5L).thenMinus(2L).get());

// print: 13

final java.lang.Number asNumber = number.cast();

System.out.println(asNumber);

// print: 13
```

### Reusing delegate rules

Annotation:
``` java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@DelegateMethod(from = "cast", to = "getValueCast", returnValue = DelegateRule.ReturnValue.RESULT)
@DelegateMethods({
         @DelegateMethod(from = "then(.*)", to = "do(.*)"),
         @DelegateMethod(from = "get", to = "getValue"),
         @DelegateMethod(from = "set", to = "initialize"),
})
public @interface DelegateNumber {
}
```
Adapter class:
``` java
@Adapter
@DelegateNumber
public abstract class NumberAdapterImpl implements NumberAdapter<Long, Number> {

    private final Number number = new Number();

    @Adaptee
    public Number adaptee() { return number; }
}
```

### Generation of JavaBean by interface

``` java
@Adapter
@BeanProperty
public interface JavaBeanGeneric<I, S> {
    I getInteger();

    void setInteger(I value);

    S getString();

    void setString(S value);
}
```
# Code of your exercise

Put here all the code created for this exercise

Code XML :

```xml
<rule ref="category/java/design.xml/AvoidDeeplyNestedIfStmts">
    <properties>
        <property name="problemDepth" value="3" />
    </properties>
</rule>
```

Code Java de l'exécution de la règle :
````java
public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key) {
    if (map != null) {
        final Object answer = map.get(key);
        if (answer != null) {
            if (answer instanceof Boolean) {
                return (Boolean) answer;
            }
            if (answer instanceof String) {
                return Boolean.valueOf((String) answer);
            }
            if (answer instanceof Number) {
                final Number n = (Number) answer;
                return n.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
            }
        }
    }
    return null;
}
````
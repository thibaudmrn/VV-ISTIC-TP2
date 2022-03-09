# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

Pour éviter les if imbriqués, il faut utiliser la règle suivante.

```xml
<rule ref="category/java/design.xml/AvoidDeeplyNestedIfStmts">
    <properties>
        <property name="problemDepth" value="3" />
    </properties>
</rule>
```

Il est possible de régler la profondeur maximale voulue en modifiant le paramètre ```value```.

Si on exécute cette règle, on peut trouver des if imbriqués comme celui-ci. On remarque qu'il n'est en effet pas nécessaire dans ce cas d'imbriquer les if.
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
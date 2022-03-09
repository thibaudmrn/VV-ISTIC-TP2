# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC indique la "densité de connexion" alors que LCC indique la connectivité globale. Ainsi, pour que TCC soit égale à LCC il faut que toutes les méthodes soient directement connectées les unes aux autres ou que toutes les connexions existantes sont directes.

LCC ne peut être inférieur à TCC car TCC est le nombre de connections directes divisé par le nombre maximal de connexions possibles alors que LCC vaut la somme du nombre de connexions directes et indirectes divisé par le nombre maximal de connexions possibles.

```java
public class Exercice1 {

    private int x;
    private int y;

    public int sum() {
        return x+y;
    }

    public int multiply() {
        return x*y;
    }
    
    public int subtract() {
        return x-y;
    }

}
```
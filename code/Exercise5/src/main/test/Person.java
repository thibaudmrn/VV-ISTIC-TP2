public class Person {
    private int age;
    private String name;

    public String getName() { return name; }

    public boolean isAdult() {
        name = "test";
        return age > 17;
    }

    public boolean getAge() {
        name = "";
        return age > 17;
    }
}
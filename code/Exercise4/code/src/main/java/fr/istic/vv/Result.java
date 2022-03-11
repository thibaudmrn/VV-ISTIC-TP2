package fr.istic.vv;

public class Result {

    private String className;
    private String attributeType;
    private String attributeName;

    public Result(String fileName, String attributeType, String attributeName) {
        this.className = fileName;
        this.attributeType = attributeType;
        this.attributeName = attributeName;
    }

    @Override
    public String toString() {
        return className + ": " + attributeType + " " +attributeName;
    }
}

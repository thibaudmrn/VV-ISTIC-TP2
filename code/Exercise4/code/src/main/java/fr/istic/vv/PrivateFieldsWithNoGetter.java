package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This class visits a compilation unit and prints all private fields with no public getters in a public class.
 */
public class PrivateFieldsWithNoGetter extends VoidVisitorWithDefaults<Void> {

    Set<VariableDeclarator> privateFields;

    public PrivateFieldsWithNoGetter() {
        privateFields = new HashSet<>();
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        privateFields.forEach(pField -> System.out.println(pField.getNameAsString()));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        String methodNameWithoutGet = declaration.getNameAsString().replace("get", "");
        if (declaration.isPublic()) {
            declaration.getBody().ifPresent(body -> body.getStatements().forEach(stmt -> {
                if (stmt.isReturnStmt()) {
                    stmt.asReturnStmt().getExpression().ifPresent(expression ->
                            privateFields.removeIf(pField -> pField.getNameAsString().equals(expression.toString())
                                    && pField.getNameAsString().equalsIgnoreCase(methodNameWithoutGet)));
                }
            }));
        }
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {
        if (field.isPrivate()) {
            privateFields.addAll(field.getVariables());
        }
    }
}
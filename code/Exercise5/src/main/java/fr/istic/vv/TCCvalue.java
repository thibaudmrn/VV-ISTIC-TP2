package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.Pair;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class TCCvalue extends VoidVisitorWithDefaults<Void> {

    int NP;
    Set<VariableDeclarator> privateFields;
    Set<Pair<String,String>> resultConnection;
    Map<String, List<String>> methodEtVar ;

    public TCCvalue() {
        privateFields = new HashSet<>();
        methodEtVar = new HashMap<>();
        resultConnection = new HashSet<>();
    }


    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
        Graphviz.useEngine(new GraphvizCmdLineEngine());
        MutableGraph g = mutGraph("example1");
        for(Pair<String,String> entry : resultConnection){
            g.add(mutNode(entry.a).addLink(mutNode(entry.b)));
        }
        g.use((gr, ctx) -> {graphAttrs().add(Label.of("TCC = " + resultConnection.size()+"/"+NP));}) ;
        try {
            Graphviz.fromGraph(g).width(200).render(Format.PNG).toFile(new File("graphTCC.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean variableEnCommun(List<String> list1, List<String> list2){
        for(String s : list1){
            if (list2.contains(s)) return true ;

        }
        return false ;
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        int nombreMethode = declaration.getMethods().size();
        NP = nombreMethode * (nombreMethode - 1) / 2;

        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        Map<String, List<String>> new_map = new HashMap<>();
        for (Map.Entry<String,List<String>> entry : methodEtVar.entrySet()) {
            new_map.put(entry.getKey(), entry.getValue());
        }

        Iterator iterator = methodEtVar.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,List<String>> mapentry = (Map.Entry) iterator.next();
            new_map.remove(mapentry.getKey());
            for(Map.Entry<String,List<String>> entry : new_map.entrySet()) {
                if(variableEnCommun(mapentry.getValue(),entry.getValue())){
                    resultConnection.add(new Pair(mapentry.getKey(),entry.getKey())) ;

                }
            }
        }
    }


    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }


    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        List<String> varPres = new ArrayList<>() ;
        declaration.getBody().ifPresent(body -> body.getStatements().forEach(stmt -> {
            privateFields.forEach(field -> {
               if (stmt.toString().contains(field.toString())){
                   varPres.add(field.getNameAsString());
               }
            });
        }));
        methodEtVar.put(declaration.getNameAsString(),varPres);
    }


    @Override
    public void visit(FieldDeclaration field, Void arg) {
        privateFields.addAll(field.getVariables());
    }

}

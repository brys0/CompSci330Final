package org.uwgb.compsci330.server.processor;

import com.sun.source.tree.*;
import com.sun.source.util.*;
import org.uwgb.compsci330.server.annotation.FragileSensitiveApi;
import org.uwgb.compsci330.server.annotation.SensitiveApi;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.Set;



@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_25)
public class SensitiveApiProcessor extends AbstractProcessor {

    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        trees = Trees.instance(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element root : roundEnv.getRootElements()) {

            TreePath path = trees.getPath(root);
            if (path == null) continue;

            new TreePathScanner<Void, Void>() {

                private ExecutableElement currentMethod;

                @Override
                public Void visitMethod(MethodTree methodTree, Void p) {

                    TreePath methodPath = getCurrentPath();
                    Element el = trees.getElement(methodPath);

                    if (el instanceof ExecutableElement exec) {
                        currentMethod = exec;
                    }

                    return super.visitMethod(methodTree, p);
                }

                @Override
                public Void visitMethodInvocation(MethodInvocationTree invocation, Void p) {

                    Element called = trees.getElement(getCurrentPath());

                    if (called instanceof ExecutableElement exec) {

                        if (exec.getAnnotation(SensitiveApi.class) != null) {

                            boolean allowed =
                                    currentMethod.getAnnotation(FragileSensitiveApi.class) != null ||
                                            currentMethod.getEnclosingElement()
                                                    .getAnnotation(FragileSensitiveApi.class) != null;

                            if (!allowed) {

                                processingEnv.getMessager().printMessage(
                                        Diagnostic.Kind.ERROR,
                                        "Calling @SensitiveApi requires @FragileSensitiveApi",
                                        currentMethod
                                );
                            }
                        }
                    }

                    return super.visitMethodInvocation(invocation, p);
                }

            }.scan(path, null);
        }

        return false;
    }
}

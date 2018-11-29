package com.zuzuche.easycomponents.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author Roye
 * @date 2018/11/27
 */
@AutoService(Processor.class)
public class UtilInitProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //UtilsManager.getInstance().init(util);
        if (set == null || set.isEmpty()) {
            return false;
        }
        TypeElement typeElement = set.iterator().next();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(typeElement);

        TypeSpec.Builder typeBulider = TypeSpec.classBuilder("UtilAutoIniter")
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("init");
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.returns(TypeName.VOID);
        builder.addException(ClassName.get(Throwable.class));

        //mMessager.printMessage(Diagnostic.Kind.ERROR,className.packageName() + "." + className.simpleName());

        builder.addStatement("Object obj = null");
        for (Element element : elements) {
            //mMessager.printMessage(Diagnostic.Kind.NOTE, element.toString());
            builder.addStatement("obj = Class.forName($S).newInstance()", element.toString());
            builder.addStatement("UtilsManager.getInstance().init((com.zzc.easycomponents.base.IUtil)obj)");
        }

        typeBulider.addMethod(builder.build());
        // 创建Java文件
        JavaFile javaFile = JavaFile.builder("com.zzc.easycomponents.util", typeBulider.build()).build();

        try {
            if (javaFile.toJavaFileObject().delete());
            javaFile.writeTo(mFiler);
        } catch (Throwable e) {
            //e.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(EasyUtil.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
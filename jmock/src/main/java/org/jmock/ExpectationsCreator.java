package org.jmock;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ExpectationsCreator implements Opcodes {

    private static final Logger LOG = Logger.getLogger(ExpectationsCreator.class.getSimpleName());
    
    public static void main(String... argv) throws IOException, Exception {
        
        LOG.info("Expections Old School method injector running");
        LOG.info("Running in " + new java.io.File( "." ).getCanonicalPath());
        // Class klass = org.jmock.Expectations.class;
        String className = "org.jmock.Expectations";
        // String className = "java.lang.Runnable";
        String classPath = className.replace('.', '/') + ".class";

        InputStream stream = ClassLoader.getSystemResourceAsStream(classPath);
        byte[] dump = dump(stream);

        FileOutputStream output = new FileOutputStream("target/classes/"
                + classPath);
        output.write(dump);
        output.close();
    }

    public static byte[] dump(InputStream stream) throws Exception {

        ClassReader classReader = new ClassReader(stream);
        ClassWriter cw = new ClassWriter(classReader, 0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        // 1.5 to allow our generic method overloading
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "org/jmock/Expectations", null,
                "org/jmock/AbstractExpectations", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/AbstractExpectations",
                    "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)Z",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Boolean;>;)Z", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)B",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Byte;>;)B", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)S",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Short;>;)S", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)C",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Character;>;)C", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)I",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Integer;>;)I", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)J",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Long;>;)J", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(LCONST_0);
            mv.visitInsn(LRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)F",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Float;>;)F", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(FCONST_0);
            mv.visitInsn(FRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "with",
                    "(Lorg/hamcrest/Matcher;)D",
                    "(Lorg/hamcrest/Matcher<Ljava/lang/Double;>;)D", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/jmock/Expectations",
                    "addParameterMatcher", "(Lorg/hamcrest/Matcher;)V");
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }

        return cw.toByteArray();
    }
}

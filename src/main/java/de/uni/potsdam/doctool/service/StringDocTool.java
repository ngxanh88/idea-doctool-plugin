package de.uni.potsdam.doctool.service;

import doctool.core.Config;
import doctool.core.DocTool;
import doctool.core.Results;
import doctool.docu.javadoc.JavadocParser;
import doctool.java.antlr.AntlrConvert;
import doctool.java.javacc.JavaParser;
import doctool.java.javacc.ParseException;
import doctool.tree.CompilationUnit;
import doctool.visitor.AlibiTestVisitor;
import doctool.visitor.CompletenessVisitor;
import doctool.visitor.DocumentationParserVisitor;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngxanh88 on 21.06.17.
 */
public class StringDocTool extends DocTool{

    public StringDocTool(Config config) {
        super(config);
    }

    /**
     * check source code as String. need to scan file of Idea, that is not saved.
     *
     * <p> this method use {@link File#createTempFile} to create temporal file with input as String.
     * Then DocTool check this tmp-file in tmp-folder and return the results.</p>
     *
     * @param sourceCode code as String.
     * @return documentation problems of source code.
     */
    public List<Results.Result> checkSourceCodeAsString(String sourceCode) {
        final List<Results.Result> resultList = new ArrayList<>();
        try {
            resultList.addAll(checkInputStream(new ByteArrayInputStream(sourceCode.getBytes())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private List<Results.Result> checkInputStream(InputStream in) throws IOException {
        final File tempFile = File.createTempFile("doctool_plugin_class_tmp", ".java");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        final List<Results.Result> resultList = super.checkFile(tempFile.getPath());

        tempFile.delete();

        return resultList == null ? new ArrayList<>() : resultList;
    }

    /**
     * check source code as String. need to scan file of Idea, that is not saved
     * @param codeCharacter code as String.
     * @param sourcePath file path of this codeCharacter.
     * @return documentation problems of source code.
     */
    public List<Results.Result> checkSourceAsString(String codeCharacter, String sourcePath) {
        long startTime = System.nanoTime(); // parse and analyze time

        CompilationUnit unit = parseSourceAsString (codeCharacter, sourcePath);
        if (unit == null)
            return Results.getInstance().getCurrentResults();

        // actual analyze of the file (unit)
        Results.getInstance().makeNewResult(sourcePath);
        check(unit);

        return Results.getInstance().getCurrentResults();
    }

    // copy from DocTool source code and change.
    private List<Results.Result> check(CompilationUnit unit) {

        // f�hre nur die in der config spezifizierten Aktionen aus

        if (getConfig().isTestEnabled(Config.TestFlag.COMPLETENESS) || getConfig().isTestEnabled(Config.TestFlag.ALIBI)) {

            // first parse documentation
            switch(getConfig().getDocumentationTool()) {
                case javadoc:
                    DocumentationParserVisitor docuParser = new DocumentationParserVisitor(new JavadocParser());
                    unit.accept(docuParser, null);
                    break;
            }

            // then tests
            if (getConfig().isTestEnabled(Config.TestFlag.COMPLETENESS)) {
                CompletenessVisitor completeness = new CompletenessVisitor(getConfig());
                unit.accept(completeness,  new StringBuilder());
            }

            if (getConfig().isTestEnabled(Config.TestFlag.ALIBI)) {
                AlibiTestVisitor alibi = new AlibiTestVisitor(getConfig());
                unit.accept(alibi, null);
            }
            // last result handling
            if (getConfig().isResultHandlingFlag(Config.ResultHandlingFlag.WRITE_LOG_FILE)){}

            if (getConfig().isResultHandlingFlag(Config.ResultHandlingFlag.SYSTEM_OUT))
            {
                System.out.println(Results.getInstance().getCurrentFile());
                for (Results.Result result : Results.getInstance().getCurrentResults())
                    System.out.println(result.toString());
            }
        }

        return Results.getInstance().getCurrentResults();
    }

    // copy from DocTool source code and change.
    private CompilationUnit parseSourceAsString(String codeCharacter, String sourcePath) {
        CompilationUnit unit = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(codeCharacter.getBytes(StandardCharsets.UTF_8));

            // in configuration spezifizierte Programmiersprache bzw. Parser w�hlen

            switch (getConfig().getProgrammingLang())
            {
                case java_antlr:
                    try {
                        unit = new AntlrConvert().parse(inputStream);
                    } catch (RecognitionException re) {
                        System.out.println ("ParseException in File: "+sourcePath+
                                " Line "+re.line+" Column "+re.charPositionInLine+" Token: "+re.token);
                        //re.printStackTrace();
                        inputStream.close();
                        return null;
                    }
                    break;
                case java_javacc:
                    try
                    {
                        unit = new JavaParser().parse(inputStream, "UTF-8");
                    } catch (ParseException pe) {
                        System.out.println ("ParseException in File: "+sourcePath+
                                " Line "+pe.currentToken.beginLine+" Column "+pe.currentToken.beginColumn+" Token: "+pe.currentToken.next);
                        //pe.printStackTrace();
                        //System.out.println( pe.getMessage ());
                        inputStream.close();
                        return null;
                    }
                    break;
                default:
                    inputStream.close();
                    System.out.println("ERROR: Unkown programming language. In: "+sourcePath);
                    return null;
            }

            // die Datei wurde gepasst und kann geschlossen werden
            inputStream.close();

        } catch (IOException e) {
            System.out.println("ERROR: Can't open file: "+sourcePath);
            //e.printStackTrace();
            return null;
        }
        return unit;
    }
}

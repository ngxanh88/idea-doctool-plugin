package de.uni.potsdam.doctool.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import de.uni.potsdam.doctool.PluginContext;
import de.uni.potsdam.doctool.configuration.DocToolConfigState;
import doctool.core.Config;
import doctool.core.DocTool;
import doctool.core.Results;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.uni.potsdam.doctool.configuration.Setting.*;

/**
 * Created by ngxanh88 on 07.06.17.
 */
public class DocToolService {

    private final DocTool docTool;

    private final Project project;

    public DocToolService(@NotNull final Project project) {
        this.project = project;
        this.docTool = new DocTool(createDocToolConfig());

        System.out.println("create new instance doc tool service");
    }

    public void resetConfig() {
        this.docTool.setConfig(createDocToolConfig());
    }

    @NotNull
    public Map<PsiFile, List<DocProblem>> checkPsiFile(@NotNull final PsiFile psiFile) {
        final VirtualFile virtualFile = psiFile.getVirtualFile();

        Results.clear();
        final List<Results.Result> resultList = this.docTool.checkFile(virtualFile.getPath());
//        final List<Results.Result> resultList = this.docTool.checkSourceAsString(psiFile.getText(), virtualFile.getPath());

        final Map<PsiFile, List<DocProblem>> problemMap = new HashMap<>();

        problemMap.put(psiFile, DocProblemConverter.convertToListDocProblem(psiFile, resultList));

        Results.clear();
        return problemMap;
    }

    @NotNull
    public Map<PsiFile, List<DocProblem>> checkProject() {
        return checkVirtualFile(project.getBaseDir());
    }

    @NotNull
    public Map<PsiFile, List<DocProblem>> checkVirtualFiles(@NotNull final VirtualFile[] virtualFiles) {
        final Map<PsiFile, List<DocProblem>> problemMap = new HashMap<>();

        for (VirtualFile f : virtualFiles) {
            problemMap.putAll(checkVirtualFile(f));
        }
        return problemMap;
    }

    @NotNull
    public Map<PsiFile, List<DocProblem>> checkVirtualFile(@NotNull final VirtualFile virtualFile) {
        final Map<PsiFile, List<DocProblem>> problemMap = new HashMap<>();

        if (virtualFile.isDirectory()) {

            for (VirtualFile child : virtualFile.getChildren()) {
                problemMap.putAll(checkVirtualFile(child));
            }

        } else if (StringUtils.equals(virtualFile.getExtension(), "java")) {
            final PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

            if(psiFile != null) {
                Results.clear();
                final List<Results.Result> resultList = this.docTool.checkFile(virtualFile.getPath());
//                final List<Results.Result> resultList = this.docTool.checkSourceAsString(psiFile.getText(), virtualFile.getPath());

                problemMap.put(psiFile, DocProblemConverter.convertToListDocProblem(psiFile, resultList));
                Results.clear();
            }
        }

        return problemMap;
    }

    @NotNull
    private Config createDocToolConfig() {
        final DocToolConfigState configState = PluginContext.getPluginConfigState(project);
        final Config config = new Config();

        if(StringUtils.equals(configState.getConfig(PROGRAMMING_LANGUAGE), ANTLR_LANGUAGE)) {
            config.setProgrammingLang(Config.ProgrammingLang.java_antlr);
        } else {
            config.setProgrammingLang(Config.ProgrammingLang.java_javacc);
        }

        if(StringUtils.equals(configState.getConfig(DOCUMENTATION_LANGUAGE), DE_LANGUAGE)) {
            config.setDocumentationLanguage(Config.DocumentationLang.de);
        } else if(StringUtils.equals(configState.getConfig(DOCUMENTATION_LANGUAGE), EN_LANGUAGE)) {
            config.setDocumentationLanguage(Config.DocumentationLang.en);
        }

        if(StringUtils.equals(configState.getConfig(ALL_TESTS_ON), ON)) {
            config.setEnabledTests(Config.TestFlag.ALL_TESTS_ON);
            config.enableTest(Config.TestFlag.ALIBI);
            config.enableTest(Config.TestFlag.COMPLETENESS);
        } else {

            if(StringUtils.equals(configState.getConfig(ALIBI), ON)) {
                config.enableTest(Config.TestFlag.ALIBI);
            } else {
                config.disableTest(Config.TestFlag.ALIBI);
            }

            if(StringUtils.equals(configState.getConfig(COMPLETENESS), ON)) {
                config.enableTest(Config.TestFlag.COMPLETENESS);
            } else {
                config.disableTest(Config.TestFlag.COMPLETENESS);
            }
        }

        if(StringUtils.equals(configState.getConfig(ALL_ALIBI_SCORE), ON)) {
            config.alibi_setReportAllScores(true);
        }

        if(StringUtils.equals(configState.getConfig(PARSE_ONLY), ON)) {
            config.setResultHandling(EnumSet.noneOf (Config.ResultHandlingFlag.class) );
        }

        if(StringUtils.equals(configState.getConfig(PRIVATE_VISIBLE), ON)) {
            config.getVisibility().add(Config.VisibilityFlag.PRIVATE);
        } else {
            config.getVisibility().remove(Config.VisibilityFlag.PRIVATE);
        }

        if(StringUtils.equals(configState.getConfig(PROTECTED_VISIBLE), ON)) {
            config.getVisibility().add(Config.VisibilityFlag.PROTECTED);
        } else {
            config.getVisibility().remove(Config.VisibilityFlag.PROTECTED);
        }

        config.setStatistic_summary(true);
        config.setStatistic_log(true);

        return config;
    }
}
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
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

import static de.uni.potsdam.doctool.configuration.Setting.*;

/**
 * Make DocTool available to run as a project-level service because the DocTool check per project.
 * This is only one instance of a service is loaded in per project.
 * Registered in {@code plugin.xml} as a {@code projectService}
 */
public class DocToolService {

    /** the DocTool api instance */
    private final StringDocTool docTool;
    /** the current project */
    private final Project project;

    /**
     * Create a new service component bean.
     * @param project the current project.
     */
    public DocToolService(@NotNull final Project project) {
        this.project = project;
        this.docTool = new StringDocTool(createDocToolConfig());

        System.out.println("create new instance doc tool service");
    }

    /**
     * reset all the DocTool Config with the DocTool Settings Storage {@link de.uni.potsdam.doctool.configuration.DocToolConfigState}.
     */
    public void resetConfig() {
        this.docTool.setConfig(createDocToolConfig());
    }

    /**
     * scan and check current psi file with DocTool api
     *
     * @param psiFile the psi file to check muss be {@link NotNull}.
     * @return the map from this psi file and list problem wrapper of psi file
     */
    @NotNull
    public Map<PsiFile, List<DocProblem>> checkPsiFile(@NotNull final PsiFile psiFile) {
        Results.clear();

//        final List<Results.Result> resultList = this.docTool.checkFile(psiFile.getVirtualFile().getPath());

        final List<Results.Result> resultList = this.docTool.checkSourceAsString(psiFile.getText(), psiFile.getVirtualFile().getPath());
//        final List<Results.Result> resultList = this.docTool.checkSourceCodeAsString(psiFile.getText());

        final Map<PsiFile, List<DocProblem>> problemMap = new HashMap<>();

        problemMap.put(psiFile, DocProblemConverter.convertToListDocProblem(psiFile, resultList));

        Results.clear();
        return problemMap;
    }

    /**
     * scan and check total current project.
     *
     * @return the map from psi file of current project and list problem wrapper of psi file
     */
    @NotNull
    public Map<PsiFile, List<DocProblem>> checkProject() {
        return checkVirtualFile(project.getBaseDir());
    }

    /**
     * scan and check list file with DocTool api
     *
     * @param virtualFiles the array of file muss be {@link NotNull}.
     * @return the map from psi file instance of this files and list problem wrapper of psi file
     */
    @NotNull
    public Map<PsiFile, List<DocProblem>> checkVirtualFiles(@NotNull final VirtualFile[] virtualFiles) {
        final Map<PsiFile, List<DocProblem>> problemMap = new HashMap<>();

        for (VirtualFile f : virtualFiles) {
            problemMap.putAll(checkVirtualFile(f));
        }
        return problemMap;
    }

    /**
     * scan and check current file with DocTool api
     * @param virtualFile the virtual file muss be {@link NotNull}.
     * @return the map from psi file instance of this file and list problem wrapper of psi file
     */
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
                problemMap.putAll(checkPsiFile(psiFile));
            }
        }

        return problemMap;
    }

    @NotNull
    private Config createDocToolConfig() {
        final DocToolConfigState configState = PluginContext.getPluginConfigState(project);
        final Config config = new Config();

        if(StringUtils.equals(configState.getConfig(PARSER_LANGUAGE), ANTLR)) {
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

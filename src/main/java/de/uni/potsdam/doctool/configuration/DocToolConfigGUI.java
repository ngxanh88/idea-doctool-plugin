package de.uni.potsdam.doctool.configuration;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.TitledSeparator;
import com.intellij.util.ui.JBUI;
import de.uni.potsdam.doctool.PluginBundle;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static de.uni.potsdam.doctool.configuration.Setting.*;

/**
 * The configuration UI of the idea plugin
 */
public class DocToolConfigGUI {

    private final JPanel rootPanel;

    private final ComboBox progLangComboBox;
    private final ComboBox docLangComboBox;

    private final JCheckBox allTestsCb = new JCheckBox();
    private final JCheckBox alibiCb = new JCheckBox();
    private final JCheckBox completenessCb = new JCheckBox();
    private final JCheckBox visiblePrivateCb = new JCheckBox();
    private final JCheckBox visibleProtectedCb = new JCheckBox();
    private final JCheckBox allAlibiScoreCb = new JCheckBox();
    private final JCheckBox parseOnlyCb = new JCheckBox();

    /**
     * Create a new instance of GUI.
     */
    public DocToolConfigGUI() {
        this.rootPanel = new JPanel(new BorderLayout());

        this.progLangComboBox = buildProgrammingLangComboBox();
        this.docLangComboBox = buildDocumentationLangComboBox();
        initAllCheckBox();

        this.rootPanel.add(buildDocToolConfigPanel(), BorderLayout.NORTH);
    }

    /**
     * get root panel of the configuration UI.
     * @return the instance of root panel.
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * get all new DocTool settings value from the configuration UI.
     *
     * @return the settings Map with setting label ({@link de.uni.potsdam.doctool.configuration.Setting}) as key and new value
     */
    public Map<String, String> getNewSettings() {
        final Map<String, String> configMap = new HashMap<>();

        configMap.put(PROGRAMMING_LANGUAGE,    (String) progLangComboBox.getSelectedItem());
        configMap.put(DOCUMENTATION_LANGUAGE,  (String) docLangComboBox.getSelectedItem());
        configMap.put(ALL_TESTS_ON,            convertBoolean(allTestsCb.isSelected()));
        configMap.put(ALIBI,                   convertBoolean(alibiCb.isSelected()));
        configMap.put(COMPLETENESS,            convertBoolean(completenessCb.isSelected()));
        configMap.put(ALL_ALIBI_SCORE,         convertBoolean(allAlibiScoreCb.isSelected()));
        configMap.put(PARSE_ONLY,              convertBoolean(parseOnlyCb.isSelected()));
        configMap.put(PRIVATE_VISIBLE,         convertBoolean(visiblePrivateCb.isSelected()));
        configMap.put(PROTECTED_VISIBLE,       convertBoolean(visibleProtectedCb.isSelected()));

        return configMap;
    }

    /**
     * check settings of the configuration UI changed or not
     *
     * @param configState the DocTool Settings Storage.
     * @return true when all values from the configuration UI are different from the DocTool Settings Storage.
     */
    public boolean hasNoChangedSettings(@NotNull final DocToolConfigState configState) {
        return StringUtils.equals(configState.getConfig(PROGRAMMING_LANGUAGE),      (String) progLangComboBox.getSelectedItem())
                && StringUtils.equals(configState.getConfig(DOCUMENTATION_LANGUAGE),(String) docLangComboBox.getSelectedItem())

                && StringUtils.equals(configState.getConfig(ALL_TESTS_ON),          convertBoolean(allTestsCb.isSelected()))
                && StringUtils.equals(configState.getConfig(ALIBI),                 convertBoolean(alibiCb.isSelected()))
                && StringUtils.equals(configState.getConfig(COMPLETENESS),          convertBoolean(completenessCb.isSelected()))
                && StringUtils.equals(configState.getConfig(ALL_ALIBI_SCORE),       convertBoolean(allAlibiScoreCb.isSelected()))
                && StringUtils.equals(configState.getConfig(PARSE_ONLY),            convertBoolean(parseOnlyCb.isSelected()))
                && StringUtils.equals(configState.getConfig(PRIVATE_VISIBLE),       convertBoolean(visiblePrivateCb.isSelected()))
                && StringUtils.equals(configState.getConfig(PROTECTED_VISIBLE),     convertBoolean(visibleProtectedCb.isSelected()));
    }

    /**
     * set setting values into the configuration UI with the DocTool Settings Storage.
     *
     * @param configState the DocTool Settings Storage.
     */
    public void setConfigSettings(@NotNull final DocToolConfigState configState) {
        progLangComboBox.setSelectedItem(configState.getConfig(PROGRAMMING_LANGUAGE));
        docLangComboBox.setSelectedItem(configState.getConfig(DOCUMENTATION_LANGUAGE));

        allTestsCb.setSelected(StringUtils.equals(configState.getConfig(ALL_TESTS_ON), ON));
        alibiCb.setSelected(StringUtils.equals(configState.getConfig(ALIBI), ON));
        completenessCb.setSelected(StringUtils.equals(configState.getConfig(COMPLETENESS), ON));
        visiblePrivateCb.setSelected(StringUtils.equals(configState.getConfig(PRIVATE_VISIBLE), ON));
        visibleProtectedCb.setSelected(StringUtils.equals(configState.getConfig(PROTECTED_VISIBLE), ON));
        allAlibiScoreCb.setSelected(StringUtils.equals(configState.getConfig(ALL_ALIBI_SCORE), ON));
        parseOnlyCb.setSelected(StringUtils.equals(configState.getConfig(PARSE_ONLY), ON));
    }

    private JPanel buildDocToolConfigPanel() {

        final JPanel docToolConfigPanel = new JPanel(new GridBagLayout());

        final GridBagLayout gbl = new GridBagLayout();
        docToolConfigPanel.setLayout( gbl );

        addComponent(docToolConfigPanel, gbl, 0, 0, 2, 1, 0.0, 0.0, new TitledSeparator(PluginBundle.message("doctool.config.gui.title")));

        addComponent(docToolConfigPanel, gbl, 0, 1, 1, 1, 0.0, 0.0, new JLabel(PluginBundle.message("doctool.config.gui.programming-language-label")));
        addComponent(docToolConfigPanel, gbl, 1, 1, 1, 1, 0.0, 0.0, progLangComboBox);

        addComponent(docToolConfigPanel, gbl, 0, 2, 1, 1, 0.0, 0.0, new JLabel(PluginBundle.message("doctool.config.gui.documentation-language-label")));
        addComponent(docToolConfigPanel, gbl, 1, 2, 1, 1, 0.0, 0.0, docLangComboBox);

        addComponent(docToolConfigPanel, gbl, 0, 3, 2, 1, 1.0, 0.0, allTestsCb);
        addComponent(docToolConfigPanel, gbl, 0, 4, 2, 1, 1.0, 0.0, alibiCb);
        addComponent(docToolConfigPanel, gbl, 0, 5, 2, 1, 1.0, 0.0, completenessCb);
        addComponent(docToolConfigPanel, gbl, 0, 6, 2, 1, 1.0, 0.0, visiblePrivateCb);
        addComponent(docToolConfigPanel, gbl, 0, 7, 2, 1, 1.0, 0.0, visibleProtectedCb);
        addComponent(docToolConfigPanel, gbl, 0, 8, 2, 1, 1.0, 0.0, allAlibiScoreCb);
        addComponent(docToolConfigPanel, gbl, 0, 9, 2, 1, 1.0, 0.0, parseOnlyCb);

        return docToolConfigPanel;
    }

    private void initAllCheckBox() {
        allTestsCb.setText(PluginBundle.message("doctool.config.gui.all-test-checkbox-text"));
        allTestsCb.setToolTipText(PluginBundle.message("doctool.config.gui.all-test-checkbox-tooltip"));

        alibiCb.setText(PluginBundle.message("doctool.config.gui.alibi-checkbox-text"));
        alibiCb.setToolTipText(PluginBundle.message("doctool.config.gui.alibi-checkbox-tooltip"));

        completenessCb.setText(PluginBundle.message("doctool.config.gui.completeness-checkbox-text"));
        completenessCb.setToolTipText(PluginBundle.message("doctool.config.gui.completeness-checkbox-tooltip"));

        visiblePrivateCb.setText(PluginBundle.message("doctool.config.gui.visible-private-checkbox-text"));
        visiblePrivateCb.setToolTipText(PluginBundle.message("doctool.config.gui.visible-private-checkbox-tooltip"));

        visibleProtectedCb.setText(PluginBundle.message("doctool.config.gui.visible-protected-checkbox-text"));
        visibleProtectedCb.setToolTipText(PluginBundle.message("doctool.config.gui.visible-protected-checkbox-tooltip"));

        allAlibiScoreCb.setText(PluginBundle.message("doctool.config.gui.all-alibi-score-checkbox-text"));
        allAlibiScoreCb.setToolTipText(PluginBundle.message("doctool.config.gui.all-alibi-score-checkbox-tooltip"));

        parseOnlyCb.setText(PluginBundle.message("doctool.config.gui.parse-only-checkbox-text"));
        parseOnlyCb.setToolTipText(PluginBundle.message("doctool.config.gui.parse-only-checkbox-tooltip"));

        final ItemListener checkboxListener = e -> {
            if(e.getItem() == alibiCb || e.getItem() == completenessCb) {
                if(alibiCb.isSelected() && completenessCb.isSelected() && !allTestsCb.isSelected()) {
                    allTestsCb.setSelected(true);
                } else if((!alibiCb.isSelected() || !completenessCb.isSelected()) && allTestsCb.isSelected()) {
                    allTestsCb.setSelected(false);
                }
            } else if(e.getItem() == allTestsCb) {
                if(allTestsCb.isSelected()) {
                    if(!alibiCb.isSelected()) {
                        alibiCb.setSelected(true);}
                    if(!completenessCb.isSelected()) {
                        completenessCb.setSelected(true);}
                } else {
                    if(alibiCb.isSelected() && completenessCb.isSelected()) {
                        alibiCb.setSelected(false);
                        completenessCb.setSelected(false);
                    }
                }
            }
        };

        alibiCb.addItemListener(checkboxListener);
        completenessCb.addItemListener(checkboxListener);
        allTestsCb.addItemListener(checkboxListener);
    }

    private ComboBox buildProgrammingLangComboBox() {
        final String[] progLangs = (String[]) Arrays.asList(JAVA_LANGUAGE, ANTLR_LANGUAGE).toArray();
        return new ComboBox<>(progLangs);
    }

    private ComboBox buildDocumentationLangComboBox() {
        final String[] proLangs = (String[]) Arrays.asList(EN_LANGUAGE, DE_LANGUAGE).toArray();
        return new ComboBox<>(proLangs);
    }

    private void addComponent(Container cont, GridBagLayout gbl, int x, int y, int width, int height,
                              double weightx, double weighty, Component componentToAdd) {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightx;
        constraints.weighty = weighty;

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = JBUI.insets(4);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.ipadx = 0;
        constraints.ipady = 0;

        gbl.setConstraints(componentToAdd, constraints);
        cont.add(componentToAdd);
    }
}

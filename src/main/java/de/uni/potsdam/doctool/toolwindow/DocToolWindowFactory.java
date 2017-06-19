package de.uni.potsdam.doctool.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.uni.potsdam.doctool.PluginBundle;
import org.jetbrains.annotations.NotNull;

/**
 * Factory to create DocTool tool window. This is registered as {@code toolWindow} in {@code plugin.xml}.
 */
public class DocToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        final Content toolContent = ContentFactory.SERVICE.getInstance().createContent(
                new DocToolWindowPanel(project),
                PluginBundle.message("doctool.toolwindow.title"),
                false);

        toolWindow.getContentManager().addContent(toolContent);

        toolWindow.setTitle(PluginBundle.message("doctool.toolwindow.title"));
        toolWindow.setType(ToolWindowType.DOCKED, null);
        toolWindow.setIcon(PluginBundle.icon("/toolwindows/documentation.png"));
    }
}

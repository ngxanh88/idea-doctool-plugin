package de.uni.potsdam.doctool.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * the manager for the persistent DocTool plug-in configuration. Registered in {@code plugin.xml}.
 */
@State(
    name = "DocToolConfigState",
    storages = {
            @Storage(StoragePathMacros.WORKSPACE_FILE)
    }
)
public class DocToolConfigState implements PersistentStateComponent<DocToolConfigState.DocToolSetting>{

    /** the DocTool Settings Storage. */
    private final Map<String, String> storage = new HashMap<>();

    /**
     * add new Settings into the DocTool Settings Storage.
     *
     * @param configMap the new settings map of the DocTool.
     */
    public void addConfig(@NotNull final Map<String, String> configMap) {
        storage.putAll(configMap);
    }

    /**
     * add new Setting into the DocTool Settings Storage.
     *
     * @param key the setting label key {@link de.uni.potsdam.doctool.configuration.Setting}
     * @param value the new setting value.
     */
    public void addConfig(@NotNull final String key, @NotNull final String value) {
        storage.put(key, value);
    }

    /**
     * get setting of the DocTool with key label.
     *
     * @param key the setting label key {@link de.uni.potsdam.doctool.configuration.Setting}
     * @return the value of this setting label.
     */
    public String getConfig(@NotNull final String key) {
        return storage.get(key);
    }

    /**
     * check a Settings Storage has all default settings of the DocTool.
     * <p> the default setting labels are defined in {@link de.uni.potsdam.doctool.configuration.Setting}</p>
     *
     * @return true when all default labels are defined.
     */
    public boolean hasConfig() {
        return Setting.hasDocToolSetting(storage);
    }

    @Nullable
    @Override
    public DocToolSetting getState() {
        return new DocToolSetting(storage);
    }

    @Override
    public void loadState(final DocToolSetting state) {
        storage.clear();
        if (state != null) {
            Map<String, String> loadedConfigMap = state.getConfigurationMap();
            storage.putAll(loadedConfigMap);
        }
    }

    /**
     * the Wrapper of the persistent state serialisation.
     */
    public static class DocToolSetting {

        /** the configuration map for serialisation purposes */
        public Map<String, String> configuration;

        public DocToolSetting() {
            this.configuration = new HashMap<>();
        }

        public DocToolSetting(final Map<String, String> configuration) {
            this.configuration = new HashMap<>(configuration);
        }

        public Map<String, String> getConfigurationMap() {
            if (configuration == null) {
                return new HashMap<String, String>();
            }
            return configuration;
        }
    }
}

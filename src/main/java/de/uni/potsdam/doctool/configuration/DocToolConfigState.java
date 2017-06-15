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
 * Created by ngxanh88 on 13.06.17.
 */
@State(
    name = "DocToolConfigState",
    storages = {
            @Storage(StoragePathMacros.WORKSPACE_FILE)
    }
)
public class DocToolConfigState implements PersistentStateComponent<DocToolConfigState.DocToolSetting>{

    private final Map<String, String> storage = new HashMap<>();

    public void addConfig(@NotNull final Map<String, String> configMap) {
        storage.putAll(configMap);
    }

    public void addConfig(@NotNull final String key, @NotNull final String value) {
        storage.put(key, value);
    }

    public String getConfig(@NotNull final String key) {
        return storage.get(key);
    }

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

    public static class DocToolSetting {

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

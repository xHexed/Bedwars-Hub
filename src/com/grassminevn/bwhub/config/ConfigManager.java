/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConfigManager {
    private static final Random rand = new Random();
    private File configFile;
    private MultiKeyMap<String, Object> configs = new MultiKeyMap<>();

    public ConfigManager(final String pluginName, final String configName) {
        setPath("plugins/" + pluginName + "/" + configName, true);
    }

    public ConfigManager(final String pluginName, final String configName, final boolean createNewFile) {
        setPath("plugins/" + pluginName + "/" + configName, createNewFile);
    }

    public void addConfig(final String name, final String value) {
        configs.put(name, value);
    }

    public void addConfig(final String name, final boolean value) {
        configs.put(name, value);
    }

    public void addComment(final String comment) {
        addConfig("# " + comment, "");
    }

    public String getConfigString(final String name) {
        final Object obj = getConfigObj(name);
        if (obj instanceof String) {
            return (String)obj;
        }
        return null;
    }

    public Boolean getConfigBoolean(final String name) {
        final String str;
        final Object obj = getConfigObj(name);
        if (obj instanceof Boolean) {
            return (Boolean)obj;
        }
        if (obj instanceof String && ((str = String.valueOf(obj)).equalsIgnoreCase("true") || str.equalsIgnoreCase("false"))) {
            return Boolean.valueOf(str);
        }
        return null;
    }

    public void addEmptyLine() {
        addConfig("empty" + rand.nextInt(), null);
    }

    private Object getConfigObj(final String name) {
        return configs.getFirst(name);
    }

    public MultiKeyMap<String, Object> getInside(final int insideLvl) {
        final MultiKeyMap<String, Object> list = new MultiKeyMap<>();
        for (final MultiKey.MultiKeyEntry<String, Object> MultiKeyEntry2 : configs.entrySet()) {
            final String name = MultiKeyEntry2.getKey();
            Object value = MultiKeyEntry2.getValue();
            if (String.valueOf(name).startsWith("# ") || name.split("\\.").length - 1 != insideLvl) continue;
            final StringBuilder str = new StringBuilder();
            int i = 1;
            final int max = name.split("\\.").length;
            for (String s : name.split("\\.")) {
                while (s.startsWith("\t")) {
                    s = s.substring(1);
                }
                if (i >= max) {
                    str.append(s);
                    continue;
                }
                str.append(s).append(".");
                ++i;
            }
            if (value instanceof String) {
                String v = (String)value;
                while (v.startsWith("\t")) {
                    v = v.substring(1);
                }
                value = v;
            }
            list.put(str.toString(), value);
        }
        return list;
    }

    public void save() {
        try {
            savee();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void savee() throws IOException {
        if (configFile.exists()) {
            configFile.delete();
        }
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
        final Collection<String> doneInsideConfigs = new ArrayList<>();
        for (final MultiKey.MultiKeyEntry<String, Object> MultiKeyEntry2 : configs.entrySet()) {
            final String name = MultiKeyEntry2.getKey();
            final Object value = MultiKeyEntry2.getValue();
            if (name.startsWith("empty") && value == null) {
                bw.write("");
                bw.newLine();
                continue;
            }
            if (!name.startsWith("# ")) {
                if (name.split("\\.").length >= 2) {
                    String insideConfig = "";
                    for (int i = 0; i < name.split("\\.").length - 1; ++i) {
                        insideConfig = i <= name.split("\\.").length - 2 ? insideConfig + name.split("\\.")[i] : insideConfig + name.split("\\.")[i] + ".";
                    }
                    if (doneInsideConfigs.contains(insideConfig)) continue;
                    bw.write(insideConfig + " {");
                    bw.newLine();
                    for (final MultiKey.MultiKeyEntry<String, Object> MultiKeyEntry1 : configs.entrySet()) {
                        final String name1 = MultiKeyEntry1.getKey();
                        final Object value1 = MultiKeyEntry1.getValue();
                        if (String.valueOf(name1).startsWith("# ") || name1.split("\\.").length < 2) continue;
                        String insideConfig1 = "";
                        for (int i = 0; i < name1.split("\\.").length - 1; ++i) {
                            insideConfig1 = i <= name1.split("\\.").length - 2 ? insideConfig1 + name1.split("\\.")[i] : insideConfig1 + name1.split("\\.")[i] + ".";
                        }
                        if (!insideConfig.equals(insideConfig1)) continue;
                        final StringBuilder s = new StringBuilder();
                        for (int i = 0; i < name1.split("\\.").length - 1; ++i) {
                            s.append("\t");
                        }
                        if (value1 != null) {
                            bw.write(s + name1.replace(insideConfig, "").substring(1) + ": " + value1);
                        } else {
                            bw.write(s + name1.replace(insideConfig, "").substring(1));
                        }
                        bw.newLine();
                    }
                    doneInsideConfigs.add(insideConfig);
                    bw.write("}");
                    bw.newLine();
                    continue;
                }
                if (value != null) {
                    bw.write(name + ": " + value);
                } else {
                    bw.write(name);
                }
                bw.newLine();
                continue;
            }
            bw.write(name);
            bw.newLine();
        }
        bw.close();
    }

    public void load() {
        load(true);
    }

    private void load(final boolean utf8) {
        BufferedReader br = null;
        try {
            br = !utf8 ? new BufferedReader(new InputStreamReader(new FileInputStream(configFile))) : new BufferedReader(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
        }
        catch (final Exception e) {
            System.err.println("[MBedwars] There was an issue with loading a config with utf8.");
            if (!utf8) {
                e.printStackTrace();
            }
            load(false);
        }
        String line;
        String currentLooking = "";
        try {
            clear();
            while ((line = Objects.requireNonNull(br).readLine()) != null) {
                String[] strs;
                boolean b = false;
                if (line.endsWith("{")) {
                    String z = line.substring(0, line.length() - 1);
                    while (z.startsWith("\t")) {
                        z = z.substring(1);
                    }
                    while (z.endsWith(" ")) {
                        z = z.substring(0, z.length() - 1);
                    }
                    currentLooking = currentLooking.isEmpty() ? currentLooking + z : currentLooking + "." + z;
                    b = true;
                } else if (line.startsWith("}")) {
                    strs = currentLooking.split("\\.");
                    currentLooking = currentLooking.substring(0, currentLooking.length() - strs[strs.length - 1].length());
                    b = true;
                }
                if (b) continue;
                strs = line.split(":");
                String name = strs[0];
                String value = "";
                if (strs.length >= 2) {
                    for (int i = 1; i < strs.length; ++i) {
                        value = i > 1 ? value + ":" + strs[i] : value + strs[i];
                    }
                } else {
                    value = null;
                }
                if (value != null) {
                    while (value.startsWith("\t") || value.startsWith(" ")) {
                        value = value.substring(1);
                    }
                }
                while (name.startsWith("\t") || name.startsWith(" ")) {
                    name = name.substring(1);
                }
                if (currentLooking.isEmpty()) {
                    configs.put(name, value);
                    continue;
                }
                configs.put(currentLooking + "." + name, value);
            }
            br.close();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        configs = new MultiKeyMap<>();
    }

    public boolean exists() {
        return configFile.exists();
    }

    private void setPath(final String path, final boolean createNewFile) {
        configFile = new File(path);
        final File dir = configFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (createNewFile && !configFile.exists()) {
            try {
                configFile.createNewFile();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class MultiKey {

        static class MultiKeyEntry<K, V>
        implements Map.Entry<K, V> {
            private final K o1;
            private V o2;

            MultiKeyEntry(final K key, final V value) {
                o1 = key;
                o2 = value;
            }

            @Override
            public K getKey() {
                return o1;
            }

            @Override
            public V getValue() {
                return o2;
            }

            @Override
            public V setValue(final V value) {
                o2 = value;
                return value;
            }
        }

    }

    public static class MultiKeyMap<K, V> {
        private final Collection<K> l1 = new ArrayList<>();
        private final List<V> l2 = new ArrayList<>();

        private void put(final K o1, final V o2) {
            l1.add(o1);
            l2.add(o2);
        }

        private V getFirst(final K o1) {
            if (get(o1).size() >= 1) {
                return get(o1).get(0);
            }
            return null;
        }

        private List<V> get(final K o1) {
            final List<V> list = new ArrayList<>();
            int i = 0;
            for (final K o : l1) {
                if (o1.equals(o)) {
                    list.add(l2.get(i));
                }
                ++i;
            }
            return list;
        }

        public ArrayList<MultiKey.MultiKeyEntry<K, V>> entrySet() {
            final ArrayList<MultiKey.MultiKeyEntry<K, V>> list = new ArrayList<>();
            int i = 0;
            for (final K o : l1) {
                list.add(new MultiKey.MultiKeyEntry<>(o, l2.get(i)));
                ++i;
            }
            return list;
        }
    }

}


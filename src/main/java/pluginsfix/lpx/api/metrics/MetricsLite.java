package pluginsfix.lpx.api.metrics;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
public class MetricsLite {
    private static final int B_STATS_VERSION = 2;
    private static final int SERVICE_ID = 9156;
    private final Plugin plugin;
    private final MetricsBase metricsBase;
    public MetricsLite(Plugin plugin) {
        this.plugin = plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration bStatsConfig = loadBStatsConfig(configFile);
        saveYaml(bStatsConfig, configFile, plugin);
        boolean enabled = bStatsConfig.getBoolean("enabled", true);
        String serverUuid = bStatsConfig.getString("serverUuid");
        if (serverUuid == null || serverUuid.isBlank()) {
            serverUuid = UUID.randomUUID().toString();
            bStatsConfig.set("serverUuid", serverUuid);
            saveYaml(bStatsConfig, configFile, plugin);
        }
        boolean logFailedRequests = bStatsConfig.getBoolean("logFailedRequests", false);
        boolean logSentData = bStatsConfig.getBoolean("logSentData", false);
        boolean logResponseStatusText = bStatsConfig.getBoolean("logResponseStatusText", false);
        if (!enabled) {
            this.metricsBase = null;
            return;
        }
        this.metricsBase = new MetricsBase(
            "bukkit",
            serverUuid,
            SERVICE_ID,
            true,
            this::appendPlatformData,
            this::appendServiceData,
            task -> Bukkit.getScheduler().runTaskAsynchronously(plugin, task),
            plugin::isEnabled,
            (message, throwable) -> {
                if (logFailedRequests) {
                    plugin.getLogger().log(Level.WARNING, message, throwable);
                }
            },
            message -> {
                if (logSentData || logResponseStatusText) {
                    plugin.getLogger().info(message);
                }
            },
            logFailedRequests,
            logSentData,
            logResponseStatusText
        );
    }
    public void shutdown() {
        if (metricsBase != null) {
            metricsBase.shutdown();
        }
    }
    public void addCustomChart(CustomChart customChart) {
        if (metricsBase != null) {
            metricsBase.addCustomChart(customChart);
        }
    }
    private void appendPlatformData(JsonObjectBuilder builder) {
        builder
            .appendField("playerAmount", getPlayerAmount())
            .appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0)
            .appendField("bukkitVersion", Bukkit.getVersion())
            .appendField("bukkitName", Bukkit.getName())
            .appendField("javaVersion", System.getProperty("java.version"))
            .appendField("osName", System.getProperty("os.name"))
            .appendField("osArch", System.getProperty("os.arch"))
            .appendField("osVersion", System.getProperty("os.version"))
            .appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }
    private void appendServiceData(JsonObjectBuilder builder) {
        builder
            .appendField("pluginVersion", plugin.getDescription().getVersion())
            .appendField("pluginName", plugin.getDescription().getName());
    }
    private int getPlayerAmount() {
        return Bukkit.getOnlinePlayers().size();
    }
    private static YamlConfiguration loadBStatsConfig(File configFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("enabled")) {
            config.set("enabled", true);
            config.set("serverUuid", UUID.randomUUID().toString());
            config.set("logFailedRequests", false);
            config.set("logSentData", false);
            config.set("logResponseStatusText", false);
        }
        return config;
    }
    private static void saveYaml(YamlConfiguration config, File file, Plugin plugin) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().log(Level.WARNING, "Unable to save bStats config.", exception);
        }
    }
    private static Exception a(Exception exception) {
        return exception;
    }
    public static class MetricsBase {
        private static final String REPORT_URL = "https://bstats.org/api/v2/data/bukkit";
        private final String platform;
        private final String serverUuid;
        private final int serviceId;
        private final boolean enabled;
        private final Consumer<JsonObjectBuilder> appendPlatformDataConsumer;
        private final Consumer<JsonObjectBuilder> appendServiceDataConsumer;
        private final Consumer<Runnable> submitTaskConsumer;
        private final Supplier<Boolean> checkServiceEnabledSupplier;
        private final BiConsumer<String, Throwable> errorLogger;
        private final Consumer<String> infoLogger;
        private final boolean logErrors;
        private final boolean logSentData;
        private final boolean logResponseStatusText;
        private final List<CustomChart> customCharts;
        private ScheduledExecutorService scheduler;
        public MetricsBase(
            String platform,
            String serverUuid,
            int serviceId,
            boolean enabled,
            Consumer<JsonObjectBuilder> appendPlatformDataConsumer,
            Consumer<JsonObjectBuilder> appendServiceDataConsumer,
            Consumer<Runnable> submitTaskConsumer,
            Supplier<Boolean> checkServiceEnabledSupplier,
            BiConsumer<String, Throwable> errorLogger,
            Consumer<String> infoLogger,
            boolean logErrors,
            boolean logSentData,
            boolean logResponseStatusText
        ) {
            this.platform = platform;
            this.serverUuid = serverUuid;
            this.serviceId = serviceId;
            this.enabled = enabled;
            this.appendPlatformDataConsumer = appendPlatformDataConsumer;
            this.appendServiceDataConsumer = appendServiceDataConsumer;
            this.submitTaskConsumer = submitTaskConsumer;
            this.checkServiceEnabledSupplier = checkServiceEnabledSupplier;
            this.errorLogger = errorLogger;
            this.infoLogger = infoLogger;
            this.logErrors = logErrors;
            this.logSentData = logSentData;
            this.logResponseStatusText = logResponseStatusText;
            this.customCharts = new CopyOnWriteArrayList<>();
            checkRelocation();
            if (enabled) {
                startSubmitting();
            }
        }
        private static byte[] compress(String data) throws IOException {
            if (data == null) {
                return null;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                gzipOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            }
            return outputStream.toByteArray();
        }
        public void addCustomChart(CustomChart customChart) {
            customCharts.add(customChart);
        }
        public void shutdown() {
            if (scheduler != null) {
                scheduler.shutdownNow();
                scheduler = null;
            }
        }
        private void startSubmitting() {
            if (scheduler != null) {
                return;
            }
            scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread thread = new Thread(r, "lpx-bstats-submit");
                thread.setDaemon(true);
                return thread;
            });
            long initialDelayMinutes = 5L;
            long periodMinutes = 30L;
            scheduler.scheduleAtFixedRate(
                () -> submitTaskConsumer.accept(this::submitData),
                initialDelayMinutes,
                periodMinutes,
                TimeUnit.MINUTES
            );
        }
        private void submitData() {
            if (!enabled || !checkServiceEnabledSupplier.get()) {
                return;
            }
            JsonObjectBuilder rootBuilder = new JsonObjectBuilder()
                .appendField("platform", platform)
                .appendField("serverUUID", serverUuid)
                .appendField("metricsVersion", B_STATS_VERSION);
            appendPlatformDataConsumer.accept(rootBuilder);
            JsonObjectBuilder serviceBuilder = new JsonObjectBuilder()
                .appendField("id", serviceId);
            appendServiceDataConsumer.accept(serviceBuilder);
            List<JsonObjectBuilder.JsonObject> chartData = new ArrayList<>();
            for (CustomChart customChart : customCharts) {
                JsonObjectBuilder.JsonObject chart = customChart.getRequestJsonObject(errorLogger, logErrors);
                if (chart != null) {
                    chartData.add(chart);
                }
            }
            if (!chartData.isEmpty()) {
                serviceBuilder.appendField("customCharts", chartData.toArray(new JsonObjectBuilder.JsonObject[0]));
            }
            rootBuilder.appendField("service", serviceBuilder.build());
            try {
                sendData(rootBuilder.build());
            } catch (Exception exception) {
                if (logErrors) {
                    errorLogger.accept("Could not submit bStats metrics data", exception);
                }
            }
        }
        private void sendData(JsonObjectBuilder.JsonObject data) throws Exception {
            if (logSentData) {
                infoLogger.accept("Sending bStats metrics data: " + data);
            }
            byte[] compressedData = compress(data.toString());
            HttpURLConnection connection = (HttpURLConnection) new URL(REPORT_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Connection", "close");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "MetricsLite/1");
            connection.setDoOutput(true);
            connection.getOutputStream().write(compressedData);
            String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (logResponseStatusText) {
                infoLogger.accept("bStats response: " + response);
            }
            connection.getInputStream().close();
            connection.disconnect();
        }
        private void checkRelocation() {
        }
        private static Throwable a(Throwable throwable) {
            return throwable;
        }
    }
    public static abstract class CustomChart {
        private final String chartId;
        private static boolean legacyState;
        protected CustomChart(String chartId) {
            if (chartId == null || chartId.isBlank()) {
                throw new IllegalArgumentException("ChartId cannot be null or empty.");
            }
            this.chartId = chartId;
        }
        public JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> errorLogger, boolean logErrors) {
            JsonObjectBuilder.JsonObject chart;
            try {
                chart = getChartData();
                if (chart == null) {
                    return null;
                }
            } catch (Throwable throwable) {
                if (logErrors) {
                    errorLogger.accept("Failed to get data for chart " + chartId, throwable);
                }
                return null;
            }
            return new JsonObjectBuilder()
                .appendField("chartId", chartId)
                .appendField("data", chart)
                .build();
        }
        protected abstract JsonObjectBuilder.JsonObject getChartData() throws Exception;
        public static void ZH(boolean value) {
            legacyState = value;
        }
        public static boolean Y9() {
            return legacyState;
        }
        public static boolean Yl() {
            return !legacyState;
        }
        private static Throwable a(Throwable throwable) {
            return throwable;
        }
    }
    public static class JsonObjectBuilder {
        private final Map<String, Object> fields = new LinkedHashMap<>();
        public JsonObjectBuilder() {
        }
        private static String escape(String value) {
            StringBuilder escaped = new StringBuilder(value.length() + 8);
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '\\':
                        escaped.append("\\\\");
                        break;
                    case '"':
                        escaped.append("\\\"");
                        break;
                    case '\b':
                        escaped.append("\\b");
                        break;
                    case '\f':
                        escaped.append("\\f");
                        break;
                    case '\n':
                        escaped.append("\\n");
                        break;
                    case '\r':
                        escaped.append("\\r");
                        break;
                    case '\t':
                        escaped.append("\\t");
                        break;
                    default:
                        if (c < 0x20) {
                            escaped.append(String.format("\\u%04x", (int) c));
                        } else {
                            escaped.append(c);
                        }
                        break;
                }
            }
            return escaped.toString();
        }
        public JsonObjectBuilder appendNull(String key) {
            fields.put(key, null);
            return this;
        }
        public JsonObjectBuilder appendField(String key, String value) {
            fields.put(key, value);
            return this;
        }
        public JsonObjectBuilder appendField(String key, int value) {
            fields.put(key, value);
            return this;
        }
        public JsonObjectBuilder appendField(String key, JsonObject value) {
            fields.put(key, value);
            return this;
        }
        public JsonObjectBuilder appendField(String key, String[] value) {
            fields.put(key, value);
            return this;
        }
        public JsonObjectBuilder appendField(String key, int[] value) {
            fields.put(key, value);
            return this;
        }
        public JsonObjectBuilder appendField(String key, JsonObject[] value) {
            fields.put(key, value);
            return this;
        }
        private void appendFieldUnescaped(String key, String value) {
            fields.put(key, value);
        }
        public JsonObject build() {
            return new JsonObject(toJson(fields));
        }
        private static String toJson(Object value) {
            if (value == null) {
                return "null";
            }
            if (value instanceof JsonObject jsonObject) {
                return jsonObject.toString();
            }
            if (value instanceof String text) {
                return "\"" + escape(text) + "\"";
            }
            if (value instanceof Number || value instanceof Boolean) {
                return String.valueOf(value);
            }
            if (value instanceof Map<?, ?> map) {
                StringBuilder builder = new StringBuilder("{");
                boolean first = true;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (!first) {
                        builder.append(',');
                    }
                    first = false;
                    builder.append("\"").append(escape(String.valueOf(entry.getKey()))).append("\":");
                    builder.append(toJson(entry.getValue()));
                }
                return builder.append('}').toString();
            }
            if (value instanceof Iterable<?> iterable) {
                StringBuilder builder = new StringBuilder("[");
                boolean first = true;
                for (Object entry : iterable) {
                    if (!first) {
                        builder.append(',');
                    }
                    first = false;
                    builder.append(toJson(entry));
                }
                return builder.append(']').toString();
            }
            if (value.getClass().isArray()) {
                int length = Array.getLength(value);
                StringBuilder builder = new StringBuilder("[");
                for (int i = 0; i < length; i++) {
                    if (i > 0) {
                        builder.append(',');
                    }
                    builder.append(toJson(Array.get(value, i)));
                }
                return builder.append(']').toString();
            }
            return "\"" + escape(String.valueOf(value)) + "\"";
        }
        private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
            return illegalArgumentException;
        }
        public static class JsonObject {
            private final String value;
            private JsonObject(String value) {
                this.value = value;
            }
            @Override
            public String toString() {
                return value;
            }
        }
    }
    public static class SingleLineChart extends CustomChart {
        private final Callable<Integer> callable;
        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            int value = callable.call();
            if (value == 0) {
                return null;
            }
            return new JsonObjectBuilder().appendField("value", value).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
    public static class DrilldownPie extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;
        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        public JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Map<String, Map<String, Integer>> values = callable.call();
            if (values == null || values.isEmpty()) {
                return null;
            }
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            int added = 0;
            for (Map.Entry<String, Map<String, Integer>> entry : values.entrySet()) {
                Map<String, Integer> inner = entry.getValue();
                if (inner == null || inner.isEmpty()) {
                    continue;
                }
                JsonObjectBuilder innerBuilder = new JsonObjectBuilder();
                int innerAdded = 0;
                for (Map.Entry<String, Integer> innerEntry : inner.entrySet()) {
                    if (innerEntry.getValue() == null || innerEntry.getValue() <= 0) {
                        continue;
                    }
                    innerBuilder.appendField(innerEntry.getKey(), innerEntry.getValue());
                    innerAdded++;
                }
                if (innerAdded > 0) {
                    valuesBuilder.appendField(entry.getKey(), innerBuilder.build());
                    added++;
                }
            }
            if (added == 0) {
                return null;
            }
            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
    public static class AdvancedBarChart extends CustomChart {
        private final Callable<Map<String, int[]>> callable;
        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Map<String, int[]> values = callable.call();
            if (values == null || values.isEmpty()) {
                return null;
            }
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            int added = 0;
            for (Map.Entry<String, int[]> entry : values.entrySet()) {
                int[] array = entry.getValue();
                if (array == null || array.length == 0) {
                    continue;
                }
                valuesBuilder.appendField(entry.getKey(), array);
                added++;
            }
            if (added == 0) {
                return null;
            }
            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
    public static class SimpleBarChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;
        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Map<String, Integer> values = callable.call();
            if (values == null || values.isEmpty()) {
                return null;
            }
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            int added = 0;
            for (Map.Entry<String, Integer> entry : values.entrySet()) {
                Integer value = entry.getValue();
                if (value == null || value <= 0) {
                    continue;
                }
                valuesBuilder.appendField(entry.getKey(), new int[]{value});
                added++;
            }
            if (added == 0) {
                return null;
            }
            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
    public static class AdvancedPie extends CustomChart {
        private final Callable<Map<String, Integer>> callable;
        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Map<String, Integer> values = callable.call();
            if (values == null || values.isEmpty()) {
                return null;
            }
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            int added = 0;
            for (Map.Entry<String, Integer> entry : values.entrySet()) {
                Integer value = entry.getValue();
                if (value == null || value <= 0) {
                    continue;
                }
                valuesBuilder.appendField(entry.getKey(), value);
                added++;
            }
            if (added == 0) {
                return null;
            }
            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
    public static class MultiLineChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;
        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Map<String, Integer> values = callable.call();
            if (values == null || values.isEmpty()) {
                return null;
            }
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            int added = 0;
            for (Map.Entry<String, Integer> entry : values.entrySet()) {
                Integer value = entry.getValue();
                if (value == null || value <= 0) {
                    continue;
                }
                valuesBuilder.appendField(entry.getKey(), value);
                added++;
            }
            if (added == 0) {
                return null;
            }
            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
    public static class SimplePie extends CustomChart {
        private final Callable<String> callable;
        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = Objects.requireNonNull(callable, "callable");
        }
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            String value = callable.call();
            if (value == null || value.isBlank()) {
                return null;
            }
            return new JsonObjectBuilder().appendField("value", value).build();
        }
        private static Exception a(Exception exception) {
            return exception;
        }
    }
}

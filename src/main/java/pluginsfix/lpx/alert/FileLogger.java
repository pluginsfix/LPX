package pluginsfix.lpx.alert;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.core.LPXCore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bukkit.plugin.Plugin;
public class FileLogger {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final java.text.SimpleDateFormat FILE_SUFFIX = new java.text.SimpleDateFormat("yyyy-MM-dd");
    private static final java.text.SimpleDateFormat LINE_TIME = new java.text.SimpleDateFormat("HH:mm:ss");
    private final LPXCore core;
    private final Plugin plugin;
    private final ExecutorService executor;
    private LocalDate currentDate;
    private File currentFile;
    private PrintWriter writer;
    private boolean ready;
    public FileLogger(long ignored, LPXCore core) {
        this.core = core;
        this.plugin = core.getPlugin();
        this.executor = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder().setNameFormat("lpx-file-logger-%d").build()
        );
        this.currentDate = LocalDate.now();
        this.ready = false;
    }
    public void XY(Object[] ignored) {
        XR(new Object[0]);
        XN(new Object[0]);
    }
    public void i0(Object... args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        Check<?> check = (Check<?>) args[2];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        if (!core.sZ(new Object[0]).Bw(new Object[0]).YI(new Object[0])) {
            return;
        }
        if (executor.isShutdown() || executor.isTerminated()) {
            return;
        }
        try {
            executor.execute(() -> iG(0L, player, check, infos));
        } catch (RejectedExecutionException ignoredException) {
        }
    }
    public void l1(Object[] args) {
        @SuppressWarnings("unchecked")
        Supplier<String> supplier = (Supplier<String>) args[0];
        if (executor.isShutdown() || executor.isTerminated()) {
            return;
        }
        try {
            executor.execute(() -> ll(new Object[]{0L, supplier}));
        } catch (RejectedExecutionException ignoredException) {
        }
    }
    public void Xu(Object[] ignored) {
        if (executor.isShutdown() || executor.isTerminated()) {
            closeWriter(true);
            return;
        }
        try {
            executor.execute(() -> closeWriter(true));
        } catch (RejectedExecutionException ignoredException) {
            closeWriter(true);
        }
    }
    private void iG(Object... args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        Check<?> check = (Check<?>) args[2];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        ll(new Object[]{0L, (Supplier<String>) () -> formatAlertEntry(player, check, infos)});
    }
    private void ll(Object[] args) {
        @SuppressWarnings("unchecked")
        Supplier<String> supplier = (Supplier<String>) args[1];
        ensureWriter();
        if (!ready || writer == null || currentFile == null) {
            return;
        }
        writer.println("[" + LINE_TIME.format(new Date()) + "] " + supplier.get());
    }
    private void X2(Object[] ignored) {
        ensureWriter();
    }
    private void XN(Object[] ignored) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }
    private void XR(Object[] ignored) {
        closeWriter(true);
    }
    private void wh(Object[] args) {
        File source = (File) args[1];
        if (source == null || !source.exists() || !source.isFile()) {
            return;
        }
        String name = source.getName();
        if (name.endsWith(".zip")) {
            return;
        }
        File zipTarget = new File(source.getParentFile(), name.replace(".log", "") + ".zip");
        if (zipTarget.exists()) {
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(zipTarget);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(source)) {
            zos.putNextEntry(new ZipEntry(name));
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, read);
            }
            zos.closeEntry();
        } catch (IOException ioException) {
            plugin.getLogger().log(Level.SEVERE, "Failed to zip alert log " + source.getName(), ioException);
        }
    }
    private String formatAlertEntry(ProtocolPlayer player, Check<?> check, FlagInfo[] infos) {
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < infos.length; i++) {
            if (i > 0) {
                details.append(", ");
            }
            FlagInfo info = infos[i];
            details.append(info.getName()).append('=').append(info.getValue());
        }
        return player.getUsername()
            + " "
            + check.getType().getCategory().getFormattedName()
            + " "
            + check.getType().getId().name()
            + " "
            + check.getOptions().getVl()
            + "/"
            + check.getOptions().getCheckOptions().getMaxVL()
            + " ["
            + details
            + "]";
    }
    private void ensureWriter() {
        LocalDate today = LocalDate.now();
        if (currentFile != null && writer != null && ready && today.equals(currentDate)) {
            return;
        }
        File previous = currentFile;
        closeWriter(false);
        currentDate = today;
        File logsDir = new File(plugin.getDataFolder(), "alerts");
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            plugin.getLogger().warning("Unable to create alerts log directory: " + logsDir.getAbsolutePath());
            ready = false;
            return;
        }
        currentFile = new File(logsDir, "alerts-" + FILE_SUFFIX.format(new Date()) + ".log");
        try {
            writer = new PrintWriter(new FileWriter(currentFile, true), true);
            ready = true;
        } catch (IOException ioException) {
            ready = false;
            writer = null;
            plugin.getLogger().log(Level.SEVERE, "Unable to open alerts log file: " + currentFile.getAbsolutePath(), ioException);
            return;
        }
        if (previous != null && previous.exists() && !previous.equals(currentFile)) {
            wh(new Object[]{0L, previous});
        }
        cleanupOldLogs(logsDir);
    }
    private void cleanupOldLogs(File logsDir) {
        int retentionDays = Math.max(1, core.sZ(new Object[0]).Bw(new Object[0]).YD(new Object[0]));
        File[] files = logsDir.listFiles();
        if (files == null) {
            return;
        }
        LocalDate cutoff = LocalDate.now().minusDays(retentionDays);
        for (File file : files) {
            String name = file.getName();
            if (!name.startsWith("alerts-")) {
                continue;
            }
            if (name.endsWith(".log")) {
                LocalDate fileDate = parseLogDate(name, ".log");
                if (fileDate != null && fileDate.isBefore(cutoff)) {
                    wh(new Object[]{0L, file});
                    if (!file.delete()) {
                        plugin.getLogger().warning("Unable to delete old log file: " + file.getAbsolutePath());
                    }
                }
                continue;
            }
            if (name.endsWith(".zip")) {
                LocalDate fileDate = parseLogDate(name, ".zip");
                if (fileDate != null && fileDate.isBefore(cutoff) && !file.delete()) {
                    plugin.getLogger().warning("Unable to delete old archive: " + file.getAbsolutePath());
                }
            }
        }
    }
    private LocalDate parseLogDate(String fileName, String suffix) {
        String datePart = fileName.substring("alerts-".length(), fileName.length() - suffix.length());
        try {
            return LocalDate.parse(datePart, DATE_FORMAT);
        } catch (RuntimeException ignored) {
            return null;
        }
    }
    private void closeWriter(boolean flush) {
        if (writer == null) {
            ready = false;
            return;
        }
        try {
            if (flush) {
                writer.flush();
            }
            writer.close();
        } catch (Exception exception) {
            plugin.getLogger().log(Level.WARNING, "Unable to close alerts writer cleanly", exception);
        } finally {
            writer = null;
            ready = false;
        }
    }
}

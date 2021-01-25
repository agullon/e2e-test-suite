package io.managed.services.test.framework;

import io.managed.services.test.Environment;
import io.managed.services.test.TestUtils;
import io.managed.services.test.k8s.KubeClusterConnectionFactory;
import io.managed.services.test.k8s.KubeClusterResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogCollector {
    private static final Logger LOGGER = LogManager.getLogger(LogCollector.class);

    /**
     * Calls storing cluster info for every connected cluster
     *
     * @param extensionContext
     * @param throwable
     * @throws Throwable
     */
    public static void saveKubernetesState(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        Path logPath = TestUtils.getLogPath(Environment.LOG_DIR.resolve("failedTest").toString(), extensionContext);
        Files.createDirectories(logPath);
        LOGGER.info("Storing cluster info into {}", logPath.toString());
        try {
            saveClusterState(logPath);
        } catch (IOException ex) {
            LOGGER.warn("Cannot save logs in {}", logPath.toString());
        }
        throw throwable;
    }

    private static void saveClusterState(Path logpath) throws IOException {
        for (KubeClusterResource cluster : KubeClusterConnectionFactory.getCurrentConnectedClusters()) {
            Files.writeString(logpath.resolve("describe_nodes.log"), cluster.cmdKubeClient().exec(false, false, "describe", "nodes").out());
            Files.writeString(logpath.resolve("events.log"), cluster.cmdKubeClient().exec(false, false, "get", "events", "--all-namespaces").out());
            Files.writeString(logpath.resolve("pvs.txt"), cluster.cmdKubeClient().exec(false, false, "describe", "pv").out());
            Files.writeString(logpath.resolve("storageclass.yml"), cluster.cmdKubeClient().exec(false, false, "get", "storageclass", "-o", "yaml").out());
            Files.writeString(logpath.resolve("routes.yml"), cluster.cmdKubeClient().exec(false, false, "get", "routes", "--all-namespaces", "-o", "yaml").out());
        }
    }
}

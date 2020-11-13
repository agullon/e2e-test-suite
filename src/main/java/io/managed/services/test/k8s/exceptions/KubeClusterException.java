package io.managed.services.test.k8s.exceptions;


import io.managed.services.test.executor.ExecResult;

public class KubeClusterException extends RuntimeException {
    public final ExecResult result;

    public KubeClusterException(ExecResult result, String s) {
        super(s);
        this.result = result;
    }

    public KubeClusterException(Throwable cause) {
        super(cause);
        this.result = null;
    }

    public static class NotFound extends KubeClusterException {

        public NotFound(ExecResult result, String s) {
            super(result, s);
        }
    }

    public static class AlreadyExists extends KubeClusterException {

        public AlreadyExists(ExecResult result, String s) {
            super(result, s);
        }
    }

    public static class InvalidResource extends KubeClusterException {

        public InvalidResource(ExecResult result, String s) {
            super(result, s);
        }
    }
}

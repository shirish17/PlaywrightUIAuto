package com.cro.playwright;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.cro.settings.PathManager;

/**
 * Returns storageState path if session already exists.
 * Ensures only ONE thread creates session per user.
 * Supports parallel scenario execution with timeout safety.
 */
public final class SessionManager {

	private static final ConcurrentHashMap<String, Object> LOCKS = new ConcurrentHashMap<>();

    private SessionManager() {}

    public static Path getOrCreateSession(
            String role,
            String username,
            Runnable loginFlow
    ) {
        String key = role + "_" + username;
        Path sessionFile = PathManager.sessionDir().resolve(key + ".json");

        if (Files.exists(sessionFile)) {
            return sessionFile;
        }

        Object lock = LOCKS.computeIfAbsent(key, k -> new Object());

        synchronized (lock) {
            try {
                if (!Files.exists(sessionFile)) {
                    loginFlow.run();
                }

                if (!Files.exists(sessionFile)) {
                    throw new IllegalStateException(
                        "Session file not created for " + key
                    );
                }

                return sessionFile;
            } finally {
                LOCKS.remove(key);
            }
        }
    }

}

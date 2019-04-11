package org.apache.hadoop.fs.s3a;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.util.NativeCodeLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * OpenSSL secure random using JNI.
 * This implementation is thread-safe.
 * <p>
 *
 * If using an Intel chipset with RDRAND, the high-performance hardware
 * random number generator will be used and it's much faster than
 * {@link java.security.SecureRandom}. If RDRAND is unavailable, default
 * OpenSSL secure random generator will be used. It's still faster
 * and can generate strong random bytes.
 * <p>
 * See https://wiki.openssl.org/index.php/Random_Numbers
 * See http://en.wikipedia.org/wiki/RdRand
 */
@InterfaceAudience.Private
public class OpensslSecureRandom extends SecureRandom {
  private static final long serialVersionUID = -7828193502768789584L;
  private static final Logger LOG =
      LoggerFactory.getLogger(org.apache.hadoop.crypto.random.OpensslSecureRandom.class.getName());

  /** If native SecureRandom unavailable, use java SecureRandom */
  private java.security.SecureRandom fallback = null;
  private static boolean nativeEnabled = false;
  static {
    if (NativeCodeLoader.isNativeCodeLoaded() &&
        NativeCodeLoader.buildSupportsOpenssl()) {
      try {
        initSR();
        nativeEnabled = true;
      } catch (Throwable t) {
        LOG.error("Failed to load Openssl SecureRandom", t);
      }
    }
  }

  public static boolean isNativeCodeLoaded() {
    return nativeEnabled;
  }

  public OpensslSecureRandom() {
    if (!nativeEnabled) {
      throw new RuntimeException("Build does not support openssl, " +
          "falling back to Java SecureRandom.");
    }
  }

  /**
   * Generates a user-specified number of random bytes.
   * It's thread-safe.
   *
   * @param bytes the array to be filled in with random bytes.
   */
  @Override
  public void nextBytes(byte[] bytes) {
    if (!nativeEnabled || !nextRandBytes(bytes)) {
      fallback.nextBytes(bytes);
    }
  }

  @Override
  public void setSeed(long seed) {
    // Self-seeding.
  }

  private native static void initSR();
  private native boolean nextRandBytes(byte[] bytes);
}

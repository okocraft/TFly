package net.okocraft.tfly.data;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;

/**
 * A class for holding the player's data.
 * <p>
 * Note that this class can be safely accessed by any thread, but strict thread-safety is <b>NOT</b> guaranteed.
 * <p>
 * For example, setter methods are synchronized to be processed in the order in which they are called,
 * but getter methods may not immediately reflect changes made by their setter methods.
 * <p>
 * See the individual methods for more information.
 */
public class TFlyData {

    private final Object lock = new Object();

    private Status status = Status.STOPPED;
    private long remainingTime = 0;
    private boolean paused = false;

    private boolean dirty = false;

    public TFlyData() {
    }

    public TFlyData(long remainingTime, boolean paused) {
        this.remainingTime = remainingTime;
        this.paused = paused;
    }

    /**
     * Gets the current {@link Status}.
     *
     * @return the current {@link Status}
     */
    public @NotNull Status status() {
        Status status;
        synchronized (lock) {
            status = this.status;
        }
        return status;
    }

    /**
     * Sets the {@link Status}.
     * <p>
     * This method is internally synchronized, so multiple threads can call this at the same time.
     *
     * @param newStatus the new {@link Status} to set
     * @return the previous {@link Status}
     */
    @CanIgnoreReturnValue
    public @NotNull Status status(@NotNull Status newStatus) {
        Objects.requireNonNull(newStatus);
        Status oldStatus;
        synchronized (lock) {
            oldStatus = this.status;
            this.status = newStatus;
        }
        return oldStatus;
    }

    @CanIgnoreReturnValue
    public boolean statusIf(@NotNull Predicate<Status> predicate, @NotNull Status newStatus) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(newStatus);
        boolean modified;
        synchronized (lock) {
            if (predicate.test(this.status)) {
                this.status = newStatus;
                modified = true;
            } else {
                modified = false;
            }
        }
        return modified;
    }

    public boolean paused() {
        boolean paused;

        synchronized (lock) {
            paused = this.paused;
        }

        return paused;
    }

    public void paused(boolean paused) {
        synchronized (lock) {
            this.paused = paused;
            this.dirty = true;
        }
    }

    /**
     * Gets the current remaining time.
     *
     * @return the current remaining time
     */
    public long remainingTime() {
        long remainingTime;
        synchronized (lock) {
            remainingTime = this.remainingTime;
        }
        return remainingTime;
    }

    /**
     * Decrements the remaining time and gets the result.
     *
     * @return the remaining time after decreasing by 1
     */
    public long decrementTime() {
        long newRemainingTime;
        synchronized (lock) {
            this.remainingTime--;
            newRemainingTime = this.remainingTime;
            this.dirty = true;
        }
        return newRemainingTime;
    }

    /**
     * Modifies the remaining time and gets the result.
     *
     * @param operator the {@link LongUnaryOperator} to calculate the remaining time
     * @return the remaining time that calculated by {@link LongUnaryOperator}
     */
    public long remainingTime(@NotNull LongUnaryOperator operator) {
        Objects.requireNonNull(operator);
        long newRemainingTime;
        synchronized (lock) {
            this.remainingTime = operator.applyAsLong(this.remainingTime);
            newRemainingTime = this.remainingTime;
            this.dirty = true;
        }
        return newRemainingTime;
    }

    @NotNull TFlyDataStorage.TFlyDataRecord asRecord() {
        TFlyDataStorage.TFlyDataRecord result;
        synchronized (lock) {
            result = new TFlyDataStorage.TFlyDataRecord(this.remainingTime, this.paused);
            this.dirty = false;
        }
        return result;
    }

    @Nullable TFlyDataStorage.TFlyDataRecord asRecordIfDirty() {
        TFlyDataStorage.TFlyDataRecord result;
        synchronized (lock) {
            result = this.dirty ? new TFlyDataStorage.TFlyDataRecord(this.remainingTime, this.paused) : null;
            this.dirty = false;
        }
        return result;
    }

    public enum Status {
        STARTING,
        RUNNING,
        STOPPING,
        STOPPED,
    }
}

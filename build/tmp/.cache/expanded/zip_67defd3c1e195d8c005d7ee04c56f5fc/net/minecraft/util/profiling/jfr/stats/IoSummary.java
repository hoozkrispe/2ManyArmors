package net.minecraft.util.profiling.jfr.stats;

import com.mojang.datafixers.util.Pair;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public final class IoSummary<T> {
    private final IoSummary.CountAndSize totalCountAndSize;
    private final List<Pair<T, IoSummary.CountAndSize>> largestSizeContributors;
    private final Duration recordingDuration;

    public IoSummary(Duration pRecordingDuration, List<Pair<T, IoSummary.CountAndSize>> pEntries) {
        this.recordingDuration = pRecordingDuration;
        this.totalCountAndSize = pEntries.stream().map(Pair::getSecond).reduce(new IoSummary.CountAndSize(0L, 0L), IoSummary.CountAndSize::add);
        this.largestSizeContributors = pEntries.stream().sorted(Comparator.comparing(Pair::getSecond, IoSummary.CountAndSize.SIZE_THEN_COUNT)).limit(10L).toList();
    }

    public double getCountsPerSecond() {
        return (double)this.totalCountAndSize.totalCount / (double)this.recordingDuration.getSeconds();
    }

    public double getSizePerSecond() {
        return (double)this.totalCountAndSize.totalSize / (double)this.recordingDuration.getSeconds();
    }

    public long getTotalCount() {
        return this.totalCountAndSize.totalCount;
    }

    public long getTotalSize() {
        return this.totalCountAndSize.totalSize;
    }

    public List<Pair<T, IoSummary.CountAndSize>> largestSizeContributors() {
        return this.largestSizeContributors;
    }

    public static record CountAndSize(long totalCount, long totalSize) {
        static final Comparator<IoSummary.CountAndSize> SIZE_THEN_COUNT = Comparator.comparing(IoSummary.CountAndSize::totalSize)
            .thenComparing(IoSummary.CountAndSize::totalCount)
            .reversed();

        IoSummary.CountAndSize add(IoSummary.CountAndSize pCountAndSize) {
            return new IoSummary.CountAndSize(this.totalCount + pCountAndSize.totalCount, this.totalSize + pCountAndSize.totalSize);
        }

        public float averageSize() {
            return (float)this.totalSize / (float)this.totalCount;
        }
    }
}
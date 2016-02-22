package com.open_source.joker.concentration.util.log;

/**
 * 文件名：com.open_source.joker.concentration.util.log
 * 描述：
 * 时间：16/2/22
 * 作者: joker
 */
public class LogConfig {

    //发送url
    protected static final String API_ERROR_LOG = "";

    protected static final int INFO = 3;
    protected static final int ERROR = 4;

    /**
     * 1MB = 1024 * 1024
     */
    protected static final long maxSize = 4 * 1024;
    /**
     * 0:console, 1:file,
     */
    protected static final int[] outputTargetList = {0, 1};
    /**
     * 根据maxSize & backupIndex 将日志文件备份成 均等的 backupIndex 份，
     * 分别是log.(backupIndex - 1) －> log.(backupIndex - 2) -> ... -> log.(backupIndex - backupIndex)
     * -> log.(backupIndex - 1)
     * backupIndex == 1 表示不备份,
     *
     * @WARNING backupIndex 不能等于0
     */
    protected static final int backupIndex = 4;
    protected static final String NOVA_LOG_TAG = "NOVA_LOG";
}

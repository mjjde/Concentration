package com.open_source.joker.concentration.util.log;

import android.content.SharedPreferences;

import com.open_source.joker.concentration.app.CONApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：
 * 时间：16/2/22
 * 作者: joker
 */
public class FileAppender extends Appender {
    private static final String TAG = FileAppender.class.getSimpleName();
    protected static final String LOG_NAME = "logbase";
    protected static final File LOG_DIR_PATH = new File(CONApplication.instance().getFilesDir(),
            LOG_NAME);
    private static FileAppender instance;
    private ExecutorService mPool = Executors.newSingleThreadExecutor();
    private long mCount = 0L;
    private static final long mPerSize = LogConfig.maxSize / LogConfig.backupIndex;
    private BufferedWriter mBufferedWriter = null;
    private static int mBackupIndex = LogConfig.backupIndex - 1;
    protected static final String PREFERENCE_NOVA_LOG = "log_latest_modified";
    private SharedPreferences mySharedPreferences = CONApplication.instance().getSharedPreferences(
            PREFERENCE_NOVA_LOG, CONApplication.instance().MODE_PRIVATE);
    private SharedPreferences.Editor editor = mySharedPreferences.edit();
    private static final int mBufferSize = 1024;
    private List<String> mErrorLogList = new ArrayList<String>();


    private FileAppender() {
        if (!LOG_DIR_PATH.exists()) {
            if (LOG_DIR_PATH.mkdirs()) {
                // There is no need to media scan
                try {
                    new File(LOG_DIR_PATH, ".nomedia").createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                android.util.Log.e(TAG, LOG_DIR_PATH + " create fail.");
            }

        } else {
        }
    }

    public static FileAppender getInstance() {
        if (instance == null) {
            instance = new FileAppender();
        }
        return instance;
    }


    @Override
    public void i(int priority, String TAG, String message) {
        write(formatMessage(priority, TAG, message));
    }

    @Override
    public void e(int priority, String TAG, String message) {
        write(formatMessage(priority, TAG, message));
    }

    @Override
    public void open() {
        File latestModifiedFile = getLatestModifiedFile(mySharedPreferences.getString(
                PREFERENCE_NOVA_LOG, ""));

        File file = null;
        boolean append = false;
        if (latestModifiedFile == null) {
            file = new File(LOG_DIR_PATH.getAbsolutePath(), LOG_NAME + "." + mBackupIndex);
            append = false;
        } else {
            file = latestModifiedFile;
            mBackupIndex = Integer.parseInt(String.valueOf(latestModifiedFile.getName().charAt(
                    latestModifiedFile.getName().length() - 1)));

            mCount = latestModifiedFile.length();

            append = true;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, append);
            mBufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"),
                    mBufferSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (mBufferedWriter != null) {
            try {
                mBufferedWriter.flush();
                mBufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (editor != null) {
            editor.putString(PREFERENCE_NOVA_LOG, LOG_NAME + "." + mBackupIndex);
            editor.commit();
        }


        if (mPool != null) {
            mPool.shutdown();
        }
    }

    public void flush() {
        if (mBufferedWriter != null) {
            try {
                mBufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String formatMessage(int priority, String tag, String msg) {
        if (msg == null) {
            return "";
        } else {
            JSONObject jsonObj = new JSONObject();
            String logContent = tag + File.separator + msg;
            try {
                jsonObj.put("time", System.currentTimeMillis());

                if (priority == LogConfig.INFO) {
                    jsonObj.put("level", "normal");
                } else if (priority == LogConfig.ERROR) {
                    jsonObj.put("level", "error");
                } else {
                    jsonObj.put("level", "?");
                }

                jsonObj.put("log", logContent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /**
             * post error log
             */
            if (priority == LogConfig.ERROR) {
                if (!mErrorLogList.contains(logContent)) {
                    postLog(jsonObj.toString(), LogConfig.API_ERROR_LOG);
                    if (mErrorLogList.size() > 10) {
                        mErrorLogList.remove(mErrorLogList.size() - 1);
                        mErrorLogList.add(logContent);
                    } else {
                        mErrorLogList.add(logContent);
                    }
                } else {
                    // TODO:如果打印次数过多，可以作一些WARNING提示
                }
            }

            return jsonObj.toString() + "," + '\n';
        }

    }


    private void write(String message) {
        final String msg = message;
        mPool.execute(new Thread() {
            @Override
            public void run() {
                mCount += msg.length();

                try {
                    if (mCount < mPerSize) {
                        mBufferedWriter.write(msg);
                        /**
                         * flushing this writer on below condition:
                         * App crash, close App, read log, create new log,
                         * buffer size over 8192.
                         */
                        // mBufferedWriter.flush();
                    } else {
                        if (mBufferedWriter != null) {
                            mBufferedWriter.flush();
                            mBufferedWriter.close();
                        }

                        if (mBackupIndex > 0) {
                            mBackupIndex--;
                        } else {
                            mBackupIndex = LogConfig.backupIndex - 1;
                        }

                        if (editor != null) {
                            editor.putString(PREFERENCE_NOVA_LOG, LOG_NAME + "." + mBackupIndex);
                            editor.commit();
                        }

                        try {
                            String logFile = LOG_DIR_PATH.getAbsolutePath() + File.separator
                                    + LOG_NAME + "." + mBackupIndex;
                            FileOutputStream fileOutputStream = new FileOutputStream(logFile, false);
                            mBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                                    fileOutputStream, "UTF-8"), mBufferSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mCount = 0;
                        write(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * post to service
     *
     * @param log
     * @param url
     */
    public void postLog(String log, String url) {

    }

    private File getLatestModifiedFile(String fileName) {
        File latestModifiedFile = null;
        File[] files = LOG_DIR_PATH.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (!file.isFile() || file.isHidden()) {
                continue;
            }

            /**
             * get latest modified file directly according file name.
             */
            if (!fileName.equalsIgnoreCase("") && (fileName.equalsIgnoreCase(file.getName()))) {
                latestModifiedFile = file;
                break;
            }

            // 给latest modified 赋值，值为第一个非隐藏的文件
            if (latestModifiedFile == null) {
                latestModifiedFile = file;
            }

            /**
             * 在文件比较小的情况，连续打印会出现 两个文件时间戳一样的情况：
             * 最近修改文件获取方法：
             */
            if (latestModifiedFile.length() > file.length()) {
                latestModifiedFile = file;
            } else if (latestModifiedFile.lastModified() < file.lastModified()) {
                latestModifiedFile = file;
            } else {
                // No way
            }
        }
        return latestModifiedFile;
    }
}

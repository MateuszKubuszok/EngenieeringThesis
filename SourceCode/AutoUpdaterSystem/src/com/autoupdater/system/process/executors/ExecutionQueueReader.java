package com.autoupdater.system.process.executors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Vector;

/**
 * Enqueues execution of Process, from each of them obtains Output and Error
 * stream and read them sequentially till both of them are closed as long as
 * there are enqueued process'.
 * 
 * <p>
 * Should be created by ProcessExecutors.
 * </p>
 * 
 * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
 */
public class ExecutionQueueReader {
    private final ProcessQueue processQueue;
    private BufferedReader reader;

    /**
     * Creates instance of ExecutionQueueReader
     * 
     * <p>
     * Should be called only by ProcessExecutors.
     * </p>
     * 
     * @see com.autoupdater.system.process.executors.AbstractProcessExecutor
     * 
     * @param processQueue
     *            list of ProcessBuilders, serves as a queue
     */
    ExecutionQueueReader(ProcessQueue processQueue) {
        this.processQueue = processQueue != null ? processQueue : new ProcessQueue();
    }

    /**
     * Returns next line from input from enqueued programs.
     * 
     * @return read line from input
     * @throws InvalidCommandException
     *             thrown if attempt to run of any of commands happen to fail
     *             (e.g. program doesn't exists)
     */
    public String getNextOutput() throws InvalidCommandException {
        String line;

        while (true) {
            if ((line = readNextLine()) != null)
                return line;

            if (processQueue.isEmpty())
                return null;
            loadNextReader();
        }
    }

    /**
     * Reads all enqueued programs' results till last output stream is closed.
     * 
     * <p>
     * WARNING!: If any of process doesn't finish method will stuck as infinite
     * loop.
     * </p>
     * 
     * @throws InvalidCommandException
     *             thrown if attempt to run of any of commands happen to fail
     *             (e.g. program doesn't exists)
     */
    public void rewind() throws InvalidCommandException {
        while (getNextOutput() != null)
            ;
    }

    /**
     * Reads next line. If it is first read it initializes reader.
     * 
     * @return next reader line, or null if stream ended
     * @throws InvalidCommandException
     *             thrown if attempt to run of any of commands happen to fail
     *             (e.g. program doesn't exists)
     */
    private String readNextLine() throws InvalidCommandException {
        if (reader == null) {
            if (!processQueue.isEmpty())
                loadNextReader();
            else
                return null;
        }

        String line = null;
        try {
            while ((line = reader.readLine()) != null && line.isEmpty())
                ;
        } catch (IOException e) {
        }
        return line;
    }

    /**
     * If reader is null or all of its input streams reached end/were closed
     * tries to obtain streams from next program.
     * 
     * @throws InvalidCommandException
     *             thrown if attempt to run of any of commands happen to fail
     *             (e.g. program doesn't exists)
     */
    private void loadNextReader() throws InvalidCommandException {
        if (!processQueue.isEmpty()) {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }

            try {
                Process process = processQueue.getNextProcess();
                Vector<InputStream> vector = new Vector<InputStream>();
                vector.add(process.getInputStream());
                vector.add(new ByteArrayInputStream(new byte[] { '\n' }));
                vector.add(process.getErrorStream());
                vector.add(new ByteArrayInputStream(new byte[] { '\n' }));
                reader = new BufferedReader(new InputStreamReader(new SequenceInputStream(
                        vector.elements())));
            } catch (IOException e) {
                throw new InvalidCommandException(e.getMessage());
            }
        }
    }
}
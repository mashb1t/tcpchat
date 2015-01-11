/*
 * The MIT License
 *
 * Copyright 2015 Manuel Schmid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package logging;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This class can create loggers
 *
 * @author Manuel Schmid
 */
public class CustomLogger {

    /**
     * Creates a logger and adds handler
     *
     * @param logName Name of the logger, element of enum LogName
     * @param logPath Path to logfile, element of enum LogPath
     * @return Logger
     */
    public static Logger create(LogName logName, LogPath logPath) {

        // Basic declarations
        Logger logger = Logger.getLogger(logName + "." + logPath);
        FileHandler fh = null;

        checkDir();
        resetLogger();

        // Setting up format for filename
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss"); //just to make our log file nicer :)
        try {
            fh = new FileHandler(logPath.getPath());
        } catch (IOException | SecurityException e) {
            // TODO handle
            e.printStackTrace();
        }

        // Set formatter for logger to get rid of ugly standard format
        fh.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat logTime = new SimpleDateFormat(
                        "MM-dd-yyyy HH:mm:ss");
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(record.getMillis());
                String recordLevel = record.getLevel().toString();

                // Giving LogLevels the same margin for better overview
                while (recordLevel.length() < 10) {
                    recordLevel += " ";
                }

                // Building output string
                String returnString = recordLevel
                        + logTime.format(cal.getTime())
                        + " || "
                        + record.getSourceClassName().substring(
                                record.getSourceClassName().lastIndexOf(".") + 1,
                                record.getSourceClassName().length())
                        + "."
                        + record.getSourceMethodName()
                        + "() : "
                        + record.getMessage() + System.getProperty("line.separator");
                return returnString;
            }
        });

        logger.addHandler(fh);
        return logger;
    }

    /**
     * Returns a Logger by name and purpose
     *
     * @param logName name of the logger, defined in enum LogName
     * @param logPath purpose of logger, defined in enum LogPath
     * @return
     */
    public static Logger getLogger(LogName logName, LogPath logPath) {
        Logger logger = Logger.getLogger(logName + "." + logPath);
        return logger;
    }

//   /**
//    * Creates an array of loggers
//    * @param className name of the class calling this method
//    * @return Logger[3]
//    */
//   public static Logger[] createBasicLoggers(String className){
//       Logger[] loggers = new Logger[3];
//       loggers[0] = logging.CustomLogger.create(className, LogPath.CONNECTION);
//       loggers[1] = logging.CustomLogger.create(className, LogPath.EXCEPTION);
//       loggers[2] = logging.CustomLogger.create(className, LogPath.GENERAL);
//       return loggers;
//   }
    /**
     * Checks if the log dir exists if not, create it
     */
    private static void checkDir() {

        File f = new File(LogPath.LOGDIR.getPath());
        if (!f.exists() || !f.isDirectory()) {
            f.mkdir();
        }
    }

    private static void resetLogger() {
        //LogManager.getLogManager().reset();
    }
}
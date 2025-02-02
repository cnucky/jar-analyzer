/*
 * MIT License
 *
 * Copyright (c) 2023-2024 4ra1n (Jar Analyzer Team)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.n1ar4.jar.analyzer.cli;

import com.beust.jcommander.JCommander;
import me.n1ar4.jar.analyzer.core.CoreRunner;
import me.n1ar4.jar.analyzer.starter.Const;
import me.n1ar4.jar.analyzer.utils.DirUtil;
import me.n1ar4.log.LogManager;
import me.n1ar4.log.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    private static final Logger logger = LogManager.getLogger();

    public static void run(JCommander commander, BuildCmd buildCmd) {
        String cmd = commander.getParsedCommand();
        if (cmd == null || cmd.trim().isEmpty()) {
            commander.usage();
            System.exit(-1);
        }
        switch (cmd) {
            case BuildCmd.CMD:
                logger.info("use build command");
                if (buildCmd.getJar() == null || buildCmd.getJar().isEmpty()) {
                    logger.error("need --jar file");
                    commander.usage();
                    System.exit(-1);
                }
                String jarPath = buildCmd.getJar();
                Path jarPathPath = Paths.get(jarPath);
                if (!Files.exists(jarPathPath)) {
                    logger.error("jar file not exist");
                    commander.usage();
                    System.exit(-1);
                }
                if (buildCmd.delCache()) {
                    logger.info("delete cache files");
                    try {
                        DirUtil.removeDir(new File(Const.tempDir));
                    } catch (Exception ignored) {
                        logger.warn("delete cache files fail");
                    }
                }
                if (buildCmd.delExist()) {
                    logger.info("delete old db");
                    try {
                        Files.delete(Paths.get(Const.dbFile));
                    } catch (Exception ignored) {
                        logger.warn("delete old db fail");
                    }
                }
                CoreRunner.run(jarPathPath, null, false);
                logger.info("write file to: {}", Const.dbFile);
                System.exit(0);
            case StartCmd.CMD:
                logger.info("run jar-analyzer gui");
                break;
            default:
                throw new RuntimeException("invalid params");
        }
    }
}

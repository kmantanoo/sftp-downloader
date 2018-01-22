package com.tutorial.sftp_downloader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpDownloader implements AutoCloseable {
    private static final String home = System.getProperty("user.home");
    private Session ss;
    private ChannelSftp sftp;

    public SftpDownloader(String user, String host, int port) throws JSchException {
        JSch jsch = new JSch();
        Path ssh = Paths.get(home, ".ssh");
        String prvkey = ssh.resolve("id_rsa").toString();
        String knownHosts = ssh.resolve("known_hosts").toString();

        jsch.addIdentity(prvkey);
        jsch.setKnownHosts(knownHosts);
        ss = jsch.getSession(user, host, port);
        ss.connect();

        if (ss.isConnected()) {
            sftp = (ChannelSftp) ss.openChannel("sftp");
            sftp.connect();
        }
    }

    public File getFile(String pathToFile, String pathToStore) throws SftpException {
        Paths.get(pathToStore).toFile().getParentFile().mkdirs();

        if (sftp.isConnected()) {
            sftp.get(pathToFile, pathToStore);
        }

        return Paths.get(pathToStore).toFile();
    }

    public void close() {
        if (sftp != null && !sftp.isClosed())
            sftp.disconnect();
        if (ss != null)
            ss.disconnect();
    }

}

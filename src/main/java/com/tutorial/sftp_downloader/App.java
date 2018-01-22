package com.tutorial.sftp_downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        try (SftpDownloader dl = new SftpDownloader("diract", "192.168.1.102", 22)) {
            File received = dl.getFile("/home/diract/script.js", "/home/diract/Server/");
            
            BufferedReader br = new BufferedReader(new FileReader(received));
            String line = null;
            
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println(sb.toString());
        } catch (JSchException e) {
            System.err.println(e.getMessage());
        } catch (SftpException e) {
            if (e.getCause() != null)
                System.err.println(e.getCause().getMessage());
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

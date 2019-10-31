package com.example.demo.xieyu.chapter02;

import java.io.*;
import java.net.Socket;

/**
 * @Author: zhuwei
 * @Date: 2019/10/25 8:32
 * @Description: 包装Socket传送类
 */
public class SocketWrapper {

        private Socket socket;

        private InputStream inputStream;

        private BufferedReader inputReader;

        private BufferedWriter outputWriter;

        public SocketWrapper(Socket socket) throws IOException {
            this.socket = socket;
            this.inputStream = socket.getInputStream();
            this.inputReader = new BufferedReader(
                    new InputStreamReader(inputStream, "GBK"));
            this.outputWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "GBK"));
        }

        public String readLine() throws IOException {
            return inputReader.readLine();
        }

        public void writeLine(String line) throws IOException {
            outputWriter.write(line+'\n');
            outputWriter.flush();//由于是Buffer,所以要flush
        }

        public void close() {
            try {
                this.socket.close();
            } catch (Exception e) {

            }
        }

    }
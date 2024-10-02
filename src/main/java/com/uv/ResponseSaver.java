package com.uv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class ResponseSaver {

    public static void saveResponse(String response, String mimeType, JFrame parentFrame) {
        String extension;

        if (mimeType.contains("html")) {
            extension = ".html";
        } else {
            extension = ".txt";
        }

        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(parentFrame);

        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                Path file = Path.of(fileChooser.getSelectedFile().getAbsolutePath() + extension);
                Files.writeString(file, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

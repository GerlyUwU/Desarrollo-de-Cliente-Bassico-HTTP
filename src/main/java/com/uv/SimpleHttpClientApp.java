package com.uv;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleHttpClientApp {
    private JFrame frame;
    private JTextField urlField;
    private JComboBox<String> methodComboBox;
    private JRadioButton rawButton;
    private JRadioButton htmlButton;
    private JTextArea responseBodyArea;
    private JTextArea responseHeadersArea;
    private JLabel statusLabel;
    private JLabel mimeLabel;
    private HttpRequestHandler requestHandler;

    public SimpleHttpClientApp() {
        requestHandler = new HttpRequestHandler();
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SimpleHttpClientApp window = new SimpleHttpClientApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        
        frame = new JFrame();
        frame.setTitle("HTTP Client");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

       
        JPanel topPanel = new JPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new FlowLayout());

        JLabel urlLabel = new JLabel("URL:");
        topPanel.add(urlLabel);

        urlField = new JTextField();
        urlField.setColumns(30);
        topPanel.add(urlField);

        methodComboBox = new JComboBox<>(new String[]{"GET", "POST", "PUT", "DELETE"});
        topPanel.add(methodComboBox);

        
        JTabbedPane tabbedPane = new JTabbedPane();
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        responseBodyArea = new JTextArea();
        JScrollPane scrollPane1 = new JScrollPane(responseBodyArea);
        tabbedPane.addTab("Cuerpo", scrollPane1);

        responseHeadersArea = new JTextArea();
        JScrollPane scrollPane2 = new JScrollPane(responseHeadersArea);
        tabbedPane.addTab("Cabeceras", scrollPane2);

        
        JPanel bottomPanel = new JPanel();
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        rawButton = new JRadioButton("Texto (Raw)");
        rawButton.setSelected(true);
        htmlButton = new JRadioButton("HTML");

        ButtonGroup group = new ButtonGroup();
        group.add(rawButton);
        group.add(htmlButton);

        bottomPanel.add(rawButton);
        bottomPanel.add(htmlButton);

        JButton queryButton = new JButton("Consultar");
        bottomPanel.add(queryButton);

        JButton saveButton = new JButton("Guardar respuesta");
        bottomPanel.add(saveButton);

        statusLabel = new JLabel("Estado: ");
        bottomPanel.add(statusLabel);

        mimeLabel = new JLabel("MIME: ");
        bottomPanel.add(mimeLabel);

        
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeRequest();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveResponse();
            }
        });
    }

    private void executeRequest() {
        String url = urlField.getText();
        String method = (String) methodComboBox.getSelectedItem();

        requestHandler.executeRequest(url, method, rawButton.isSelected(), new HttpRequestHandler.Callback() {
            @Override
            public void onResponse(String status, String mimeType, String headers, String body) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Estado: " + status);
                    mimeLabel.setText("MIME: " + mimeType);
                    responseHeadersArea.setText(headers);

                    if (rawButton.isSelected()) {
                        responseBodyArea.setText(body);
                    } else {
                        responseBodyArea.setText(renderHTML(body));
                    }
                });
            }
        });
    }

    private String renderHTML(String html) {
        return "<html><body>" + html + "</body></html>";
    }

    private void saveResponse() {
        String response = responseBodyArea.getText();
        String mimeType = mimeLabel.getText().replace("MIME: ", "");

        ResponseSaver.saveResponse(response, mimeType, frame);
    }
}
